package com.flytxt.imageprocessor.config;
/**
 * 
 * @author shiju.john
 *
 */
public class LoggerFactory {

	public static Logger getLogger(Class<?> logClass) {
		return new Logger(logClass);
	}
 
}
