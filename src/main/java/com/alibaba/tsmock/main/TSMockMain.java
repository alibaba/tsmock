/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: MockServerMain.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.main
 * @Description: Main entry
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:19
 * @version: v1.0
 */

package com.alibaba.tsmock.main;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.tsmock.constants.Config;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.tsmock.concurrent.HttpMockServerThread;
import com.alibaba.tsmock.concurrent.MqMockServerThread;
import com.alibaba.tsmock.config.EnumConfigType;
import com.alibaba.tsmock.config.MockServerConfig;

/** The Class TSMockMain
 *
 * @ClassName: TSMockMain
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:19 */
public class TSMockMain {
	public static String httpOptionStr = null;
	public static String mqOptionStr = null;
	public static String logOptionStr = null;
	public static boolean daemonOptionBool = false;

	/** pass the name of the file like: java -jar tsmock.jar -h httpmock.json -m
	 * mqmock.json
	 *
	 * @param args
	 *            the arguments
	 * @throws InterruptedException
	 *             the interrupted exception */
	public static void main(String[] args) throws InterruptedException {

		final Options options = new Options();
		final Option httpOption = Option.builder("h").longOpt("http").hasArg().optionalArg(true)
		        .desc("HTTP mock configuration file").build();
		final Option mqOption = Option.builder("m").longOpt("mq").hasArg().optionalArg(true)
		        .desc("MetaQ mock configuration file").build();
		final Option logOption = Option.builder("l").longOpt("log").required().hasArg().optionalArg(false)
		        .desc("Log path").build();

		options.addOption(httpOption);
		options.addOption(mqOption);
		options.addOption(logOption);


		final CommandLineParser parser = new DefaultParser();
		final HelpFormatter formatter = new HelpFormatter();
		try {
			final CommandLine cmd = parser.parse(options, args);
			final Option[] optionArray = cmd.getOptions();
			if (optionArray.length == 0) {
				System.out.println("Please specify at least one option");
				formatter.printHelp("tsmock", options);
				System.exit(1);
			}
			httpOptionStr = cmd.getOptionValue("h");
			mqOptionStr = cmd.getOptionValue("m");
			logOptionStr = cmd.getOptionValue("l");
		} catch (final ParseException pe) {
			System.out.println("Command option error:" + pe.getMessage());
			formatter.printHelp("tsmock", options);
			System.exit(1);
		}

		Map<String, String> logProps = new HashMap<String, String>();
		logProps.put("log4j.appender.file.File", logOptionStr + File.separator + Config.TSMOCK_LOG);

		if (start(httpOptionStr, mqOptionStr,logProps, false) == false) {
			System.exit(1);
		}
	}

	public static boolean start(String httpMockConfigFile, String mqMockConfigFile, Map<String,String> logProps,boolean daemon) throws InterruptedException {

		if (!StringUtils.isEmpty(httpMockConfigFile)) {
			if (!MockServerConfig.init(EnumConfigType.HTTP, httpMockConfigFile,logProps)) {
				System.err.println("Failed to init http");
				return false;
			}
		}
		if (!StringUtils.isEmpty(mqMockConfigFile)) {
			if (!MockServerConfig.init(EnumConfigType.MQ, mqMockConfigFile,logProps)) {
				System.err.println("Failed to init mq");
				return false;
			}
		}

		if (start(daemon) == false) {
			System.err.println("Failed to start mock core");
			return false;
		}
		return true;

	}


	public static boolean start(boolean daemon) throws InterruptedException {
		Thread httpThread = null;
		Thread mqThread = null;

		if (MockServerConfig.getConfig(EnumConfigType.HTTP) != null) {
			httpThread = startHttpServer(daemon);
		}
		if (MockServerConfig.getConfig(EnumConfigType.MQ) != null) {
			mqThread = startMqServer(daemon);
		}
		if(!daemon) {
			if (httpThread != null) {
				httpThread.join();
			}
			if (mqThread != null) {
				mqThread.join();
			}
			Thread.currentThread().join();
		}


		return true;
	}

	/** Start http core.
	 *
	 *
	 * */
	private static Thread startHttpServer(boolean daemon) {
		final HttpMockServerThread httpMockServerThread = new HttpMockServerThread();
		final Thread httpThread = new Thread(httpMockServerThread);
		httpThread.setDaemon(daemon);
		httpThread.start();
		return httpThread;
	}

	/** Start mq core.
	 *
	 *
	 * */
	private static Thread startMqServer(boolean daemon) {
		final MqMockServerThread mqMockServerThread = new MqMockServerThread();
		final Thread mqThread = new Thread(mqMockServerThread);
		mqThread.setDaemon(daemon);
		mqThread.start();
		return mqThread;
	}

	public static String getLogOptionStr() {
		return logOptionStr;
	}

	public static void setLogOptionStr(String logOptionStr) {
		TSMockMain.logOptionStr = logOptionStr;
	}
}