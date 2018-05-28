package com.alibaba.tsmock.log;

import java.io.File;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

public class Log4jInitor {
	private static Properties props = null;
	public static void setup(Map<String, String> logProps) {
		if (props == null) {
			props = new Properties();
			props.setProperty("log4j.rootLogger", "DEBUG, file, stdout");
			props.setProperty("log4j.logger.RocketmqClient", "ERROR");
			props.setProperty("log4j.logger.RocketmqCommon", "ERROR");
			props.setProperty("log4j.logger.RocketmqRemoting", "ERROR");
			props.setProperty("log4j.logger.io.undertow", "ERROR");
			props.setProperty("log4j.logger.org.jboss.logging", "ERROR");
			props.setProperty("log4j.logger.org.xnio", "ERROR");
			props.setProperty("log4j.logger.org.apache.http", "ERROR");
			props.setProperty("log4j.appender.file", "org.apache.log4j.RollingFileAppender");
			props.setProperty("log4j.appender.file.File", "log" + File.separator + "tsmock.log");
			props.setProperty("log4j.appender.file.MaxFileSize", "10MB");
			props.setProperty("log4j.appender.file.MaxBackupIndex", "10");
			props.setProperty("log4j.appender.file.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.file.layout.ConversionPattern",
					"%d{yyyy-MM-dd HH:mm:ss} [%p] [%t] [%c{10}:%L] %m%n");
			props.setProperty("log4j.appender.stdout", "org.apache.log4j.ConsoleAppender");
			props.setProperty("log4j.appender.stdout.Target", "System.out");
			props.setProperty("log4j.appender.stdout.threshold", "INFO");
			props.setProperty("log4j.appender.stdout.layout", "org.apache.log4j.PatternLayout");
			props.setProperty("log4j.appender.stdout.layout.ConversionPattern", "[%d{yyyy-MM-dd HH:mm:ss}] %m%n");

			for (String propName : logProps.keySet()) {
				props.setProperty(propName, logProps.get(propName));
			}
			PropertyConfigurator.configure(props);
		}
	}
}