/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: ScriptEngine.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.scriptengine
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:35
 * @version: v1.0
 */
package com.alibaba.tsmock.scriptengine;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bsh.Interpreter;

/** The Class ScriptEngine.
 *
 * @ClassName: ScriptEngine
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:46:35 */
public class ScriptEngine {

	/** The i. */
	private static Interpreter i = new Interpreter();

	/** The Constant logger. */
	private final static Logger logger = LoggerFactory.getLogger(ScriptEngine.class);

	/** Load script from file.
	 *
	 * @param fileName
	 *            the file name
	 * @return the string */
	public static String loadScriptFromFile(String fileName) {
		try {
			return FileUtils.readFileToString(new File(fileName));
		} catch (final IOException ioe) {
			logger.error("Get exception when read script file,exception:" + ioe);
			return null;
		}
	}

	/** Execute with boolean result.
	 *
	 * @param script
	 *            the script
	 * @return the boolean */
	public static Boolean executeWithBooleanResult(String script) {
		logger.debug("Run the script:" + script);

		try {
			final Object o = i.eval(script);
			if (o instanceof Boolean) {
				return (Boolean) o;
			} else {
				return false;
			}
		} catch (final Exception e) {
			logger.error("Get exceptiion when run script,exception:" + e);
			return false;
		}

	}

	/** Execute with string result.
	 *
	 * @param script
	 *            the script
	 * @return the string */
	public static String executeWithStringResult(String script) {
		logger.debug("Run script:" + script);

		try {
			final Object o = i.eval(script);
			if (o instanceof String) {
				return (String) o;
			} else {
				return null;
			}
		} catch (final Exception e) {
			logger.error("Get exceptiion when run script,exception:" + e);
			return null;
		}

	}

	/** Execute with object result.
	 *
	 * @param script
	 *            the script
	 * @return the object */
	public static Object executeWithObjectResult(String script) {
		logger.debug("Run script:" + script);
		try {
			final Object o = i.eval(script);
			return o;
		} catch (final Exception e) {
			logger.error("Get exceptiion when run script,exception:" + e);
			return null;
		}

	}

	/** Pre process response.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessHttpRequest(String script, String requestBody, String queryStr) {
		script = StringUtils.replace(script, "${request_body}", requestBody);
		script = StringUtils.replace(script, "${request_query_str}", queryStr);

		return script;
	}

	/** Pre process response.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessHttpResponse(String script, String requestBody, String responseBody,
	        String queryStr) {
		script = StringUtils.replace(script, "${response_body}", responseBody);
		script = StringUtils.replace(script, "${request_body}", requestBody);
		script = StringUtils.replace(script, "${request_query_str}", queryStr);

		return script;
	}

	/** Pre process callback.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param info
	 *            the info
	 * @param value
	 *            the value
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessHttpCallback(String script, String requestBody, String responseBody, String info,
	        String value, String queryStr) {
		script = StringUtils.replace(script, "${info}", info);
		script = StringUtils.replace(script, "${value}", value);
		script = StringUtils.replace(script, "${request_body}", requestBody);
		script = StringUtils.replace(script, "${response_body}", responseBody);
		script = StringUtils.replace(script, "${request_query_str}", queryStr);

		return script;
	}

	/** Pre process response.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessMqRequest(String script, String requestKey, String requestBody) {
		script = StringUtils.replace(script, "${request_key}", requestKey);
		script = StringUtils.replace(script, "${request_body}", requestBody);

		return script;
	}

	/** Pre process response.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessMqResponse(String script, String requestKey, String requestBody, String responseKey,
	        String responseBody) {
		script = StringUtils.replace(script, "${request_key}", requestKey);
		script = StringUtils.replace(script, "${request_body}", requestBody);
		script = StringUtils.replace(script, "${response_key}", responseKey);
		script = StringUtils.replace(script, "${response_body}", responseBody);

		return script;
	}

	/** Pre process callback.
	 *
	 * @param script
	 *            the script
	 * @param requestBody
	 *            the request body
	 * @param responseBody
	 *            the response body
	 * @param info
	 *            the info
	 * @param value
	 *            the value
	 * @param queryStr
	 *            the query str
	 * @return the string */
	public static String preProcessMqCallback(String script, String requestKey, String requestBody, String responseKey,
	        String responseBody, String info, String value) {
		script = StringUtils.replace(script, "${request_key}", requestKey);
		script = StringUtils.replace(script, "${request_body}", requestBody);
		script = StringUtils.replace(script, "${response_key}", responseKey);
		script = StringUtils.replace(script, "${response_body}", responseBody);
		script = StringUtils.replace(script, "${info}", info);
		script = StringUtils.replace(script, "${value}", value);

		return script;
	}
}
