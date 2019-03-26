package com.springBoot.utils.configs;

import com.alibaba.fastjson.parser.ParserConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.lang.reflect.Method;
import java.time.Duration;

/**
 * @author chenkuan
 * @version v1.0
 * @desc redis配置类 缓存配置-使用Lettuce客户端，自动注入配置的方式
 * @date 2019/3/26 026 10:34
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

	private static final Logger logger = LoggerFactory.getLogger(RedisConfig.class);

	/**
	 * 重写Redis序列化方式，使用Json方式:
	 * 当我们的数据存储到Redis的时候，我们的键（key）和值（value）都是通过Spring提供的Serializer序列化到数据库的。RedisTemplate默认使用的是JdkSerializationRedisSerializer，StringRedisTemplate默认使用的是StringRedisSerializer。
	 * Spring Data JPA为我们提供了下面的Serializer：
	 * GenericToStringSerializer、Jackson2JsonRedisSerializer、JacksonJsonRedisSerializer、JdkSerializationRedisSerializer、OxmSerializer、StringRedisSerializer。
	 * 在此我们将自己配置RedisTemplate并定义Serializer。
	 * <p>
	 * 存入redis时，默认使用的是JdkSerializationRedisSerializer，使得存入的数据全部序列化了，所需自定义一个RedisTemplate，使用其他序列化方式
	 * 当redis依赖包导入的时候，默认的cache即可自动变成redis模式；如果只是导入cache的依赖，则默认的是simpleCacheManager；
	 * 使用redis缓存时，RedisCacheManager生成RedisCache后生成缓存时默认使用JdkSerializationRedisSerializer序列化（cache存储的时候）
	 * 当ioc容器内没有自定义的缓存管理器的时候---默认使用自带的；
	 * 当通过@Bean在ioc容器中注入了以下管理器，则会使用自定义的管理器；
	 */
	@Bean
	@Primary // 当有多个管理器的时候，必须使用该注解在一个管理器上注释：表示该管理器为默认的管理器
	public CacheManager cacheManager(LettuceConnectionFactory factory) {
		// 以锁写入的方式创RedisCacheWriter对象
		RedisCacheWriter cacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(factory);

		// 序列化方式1 - JdkSerializationRedisSerializer
		// 设置CacheManager的值序列化方式为JdkSerializationRedisSerializer, 但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，
		// JdkSerializationRedisSerializer序列化value, 所以以下(4行) 注释代码为默认实现
		// ClassLoader loader = this.getClass().getClassLoader();
		// JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
		// RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
		// RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);
		// 序列化方式1-- - 另一种实现方式
		// RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig(); // 该语句相当于序列化方式1

		// 序列化方式2 - FastJsonRedisSerializer
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class); // JSONObject
		RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer);
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

		// 序列化方式3 - Jackson2JsonRedisSerializer
		// Jackson2JsonRedisSerializer serializer=new Jackson2JsonRedisSerializer(Object.class);
		// RedisSerializationContext.SerializationPair<Object> pair = RedisSerializationContext.SerializationPair.fromSerializer(serializer);
		// RedisCacheConfiguration defaultCacheConfig=RedisCacheConfiguration.defaultCacheConfig().serializeValuesWith(pair);

		// 设置过期时间
		// cacheConfig = cacheConfig.entryTtl(Duration.ofSeconds(100));

		// 初始化RedisCacheManager
		RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter, cacheConfig);

		// 设置白名单---非常重要
		// 使用fastjson的时候：序列化时将class信息写入，反解析的时候，fastjson默认情况下会开启autoType的检查，相当于一个白名单检查，
		// 如果序列化信息中的类路径不在autoType中，反解析就会报com.alibaba.fastjson.JSONException: autoType is not support的异常
		// 可参考 https://blog.csdn.net/u012240455/article/details/80538540
		ParserConfig.getGlobalInstance().addAccept("com.springBoot.entity.");

		return cacheManager;
	}

	/**
	 * 设置 redis 数据默认过期时间
	 * 设置@cacheable 序列化方式
	 */
	@Bean
	public RedisCacheConfiguration redisCacheConfiguration() {
		FastJsonRedisSerializer<Object> fastJsonRedisSerializer = new FastJsonRedisSerializer<>(Object.class);
		RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig();
		cacheConfig = cacheConfig.serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(fastJsonRedisSerializer)).entryTtl(Duration.ofDays(30));
		return cacheConfig;
	}

	/**
	 * 获取缓存操作助手对象 redisTemplate
	 */
	@Bean(name = "redisTemplate")
	@SuppressWarnings("unchecked")
	@ConditionalOnMissingBean(name = "redisTemplate")
	public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory factory) {
		// 创建Redis缓存操作助手RedisTemplate对象
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(factory);

		// 以下代码为将RedisTemplate的Key、Value序列化方式由JdkSerializationRedisSerializer更换为FastJsonRedisSerializer
		FastJsonRedisSerializer fastJsonRedisSerializer = new FastJsonRedisSerializer(Object.class);
		// key采用String的序列化方式
		template.setKeySerializer(new StringRedisSerializer());
		// hash的key也采用String的序列化方式
		template.setHashKeySerializer(new StringRedisSerializer());
		// value序列化方式采用fastJson
		template.setValueSerializer(fastJsonRedisSerializer);
		// hash的value序列化方式采用fastJson
		template.setHashValueSerializer(fastJsonRedisSerializer);

		template.afterPropertiesSet();
		return template;
	}

	/**
	 * 自定义缓存key的生成策略。默认的生成策略是看不懂的(乱码内容) 通过Spring 的依赖注入特性进行自定义的配置注入
	 * 并且此类是一个配置类可以更多程度的自定义配置
	 */
	@Bean
	@Override
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	/**
	 * 异常处理，当Redis发生异常时，打印日志，但是程序正常走
	 */
	@Bean
	@Override
	public CacheErrorHandler errorHandler() {
		logger.info("初始化 -> [{}]", "Redis CacheErrorHandler");
		CacheErrorHandler cacheErrorHandler = new CacheErrorHandler() {
			@Override
			public void handleCacheGetError(RuntimeException e, Cache cache, Object key) {
				logger.error("Redis occur handleCacheGetError：key -> [{}]", key, e);
			}

			@Override
			public void handleCachePutError(RuntimeException e, Cache cache, Object key, Object value) {
				logger.error("Redis occur handleCachePutError：key -> [{}]；value -> [{}]", key, value, e);
			}

			@Override
			public void handleCacheEvictError(RuntimeException e, Cache cache, Object key) {
				logger.error("Redis occur handleCacheEvictError：key -> [{}]", key, e);
			}

			@Override
			public void handleCacheClearError(RuntimeException e, Cache cache) {
				logger.error("Redis occur handleCacheClearError：", e);
			}
		};
		return cacheErrorHandler;
	}
}
