package com.springBoot.utils.config.log;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.color.ANSIConstants;
import ch.qos.logback.core.pattern.color.ForegroundCompositeConverterBase;

/**
 * @author chenkuan
 * @version v1.0
 * @desc logback日志颜色配置
 * @date 2019/3/2 002 10:48
 */
public class LogbackColorful extends ForegroundCompositeConverterBase<ILoggingEvent> {

	@Override
	protected String getForegroundColorCode(ILoggingEvent event) {
		Level level = event.getLevel();
		switch (level.toInt()) {
			// INFO等级为黑色
			case Level.INFO_INT:
				return ANSIConstants.BLACK_FG;
			// WARN等级为蓝色
			case Level.WARN_INT:
				return ANSIConstants.BLUE_FG;
			// ERROR等级为红色
			case Level.ERROR_INT:
				return ANSIConstants.RED_FG;
			// DEBUG等级为黑色
			case Level.DEBUG_INT:
				return ANSIConstants.BLACK_FG;
			// 其他为默认颜色
			default:
				return ANSIConstants.DEFAULT_FG;
		}
	}
}
