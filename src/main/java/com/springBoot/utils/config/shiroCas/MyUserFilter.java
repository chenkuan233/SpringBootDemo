package com.springBoot.utils.config.shiroCas;

import com.springBoot.entity.User;
import com.springBoot.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.UnknownSessionException;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.UserFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author chenkuan
 * @version v1.0
 * @desc 在访问controller前判断是否登录，如果是ajax请求则返回json，不进行重定向
 * @date 2019/4/28 028 11:15
 */
@Slf4j
public class MyUserFilter extends UserFilter {

	/**
	 * 表示是否允许访问；mappedValue就是[urls]配置中拦截器参数部分
	 * 如果允许访问返回true，否则false；
	 * 本filter实现单点登录，可控制同一账号登陆的设备数并踢出最早登陆的设备
	 *
	 * @param request
	 * @param response
	 * @param mappedValue
	 * @return boolean
	 */
	@Override
	protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
		if (this.isLoginRequest(request, response)) {
			return true;
		}
		//判断是否需要shiro验证
		if (!FilterDependsOnBean.isOpenShiro)
			return true;
		//判断是否登陆，没有登录返回false不允许访问
		Subject subject = this.getSubject(request, response);
		if (subject.getPrincipal() == null)
			return false;
		//没有登录且没有记住我，返回false
		if (!subject.isAuthenticated() && !subject.isRemembered())
			return false;

		//获取session
		Session session = subject.getSession();
		Serializable sessionId = session.getId();

		//当前用户
		User user = (User) subject.getPrincipal();
		String username = user.getUserName();

		//获取redis、sessionDao、sessionManager、单点登录设置
		RedisUtil redisUtil = FilterDependsOnBean.redisUtil;
		SessionDAO sessionDAO = FilterDependsOnBean.sessionDAO;
		int singleLogon = FilterDependsOnBean.singleLogon;

		//读取缓存用户 没有就存入
		boolean sessionListIsChange = false; //记录sessionList是否改变来决定是否更新redis缓存
		List<Serializable> sessionList;
		try {
			sessionList = (List<Serializable>) redisUtil.get(username);
		} catch (Exception e) {
			log.error("redis出错，单点登录失效，继续向下执行", e);
			return true;
		}
		if (sessionList == null) sessionList = new ArrayList<Serializable>();

		//将该用户sessionId放入队列,并放入redis缓存
		if (!sessionList.contains(sessionId)) {
			sessionList.add(sessionId);
			sessionListIsChange = true;
		}

		//获取当前所有在线的session
		Collection<Session> sessionCollection = sessionDAO.getActiveSessions();

		//判断是否开启单点登陆
		if (singleLogon > 0) {
			//开启单点登录
			//如果队列里的sessionId数超出最大会话数，开始踢人，踢出最早登陆的
			while (sessionList.size() > singleLogon) {
				log.info("---" + username + "的sessionId长度：" + sessionList.size() + ", 超出限制, 开始踢人---");
				Serializable outSessionId = sessionList.get(0); //获取最早的sessionId
				//获取sessionId的session对象
				Session outSession;
				try {
					outSession = sessionDAO.readSession(outSessionId);
				} catch (UnknownSessionException e) {
					//未知sessionId，直接移除
					sessionList.remove(outSessionId);
					sessionListIsChange = true;
					continue;
				}
				//判断该session是否在有效列表
				if (!sessionCollection.contains(outSession)) {
					//session已失效，从redis缓存中删除
					sessionList.remove(outSessionId);
					sessionListIsChange = true;
					continue;
				}
				//删除session
				sessionDAO.delete(outSession);
				sessionList.remove(outSessionId);
				sessionListIsChange = true;
				log.info("---已踢出sessionId: " + outSessionId + "---");
			}
		} else {
			//不开启单点登陆
			//维护sessionList为有效sessionId,移除失效的sessionId
			Iterator<Serializable> iter = sessionList.iterator();
			while (iter.hasNext()) {
				Serializable serializable = iter.next();
				//获取sessionId的session对象
				Session outSession;
				try {
					outSession = sessionDAO.readSession(serializable);
				} catch (UnknownSessionException e) {
					//未知sessionId，直接移除
					iter.remove();
					sessionListIsChange = true;
					continue;
				}
				//判断该session是否在有效列表
				if (!sessionCollection.contains(outSession)) {
					//session已失效
					iter.remove();
					sessionListIsChange = true;
				}
			}
		}

		//更新redis缓存
		if (sessionListIsChange) {
			if (!redisUtil.set(username, sessionList)) {
				log.warn(sessionId + "--sessionList更新失败");
			}
		}

		return true;
	}

	/**
	 * 表示当访问拒绝时是否已经处理了
	 * 如果返回true表示需要继续处理；如果返回false表示该拦截器实例已经处理了，将直接返回即可
	 * 本filter判断是否ajax请求来决定重定向方法
	 *
	 * @param request
	 * @param response
	 * @return false
	 * @throws Exception
	 */
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletRequest httpServletRequest = (HttpServletRequest) request;
		HttpServletResponse httpServletResponse = (HttpServletResponse) response;

		//判断是否ajax请求决定采用重定向或返回403状态码
		if (isAjax(httpServletRequest)) {
			httpServletResponse.setCharacterEncoding("UTF-8");
			// servletResponse.setContentType("application/json");
			httpServletResponse.setStatus(403);
			return false;
		} else {
			//return super.onAccessDenied(request, response);
			this.saveRequestAndRedirectToLogin(request, response);
			return false;
		}
	}

	// 判断request请求是否是Ajax
	private boolean isAjax(HttpServletRequest request) {
		String header = request.getHeader("X-Requested-With");
		return "XMLHttpRequest".equalsIgnoreCase(header);
	}
}
