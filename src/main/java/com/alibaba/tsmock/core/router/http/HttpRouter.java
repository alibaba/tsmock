/**
 * Copyright © 2017 Alibaba Inc . All rights reserved.
 *
 * @Title: RouteMapper.java
 * @Prject: tsmock
 * @Package: com.alibaba.tsmock.repository
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:30:56
 * @version: v1.0
 */
package com.alibaba.tsmock.core.router.http;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.tsmock.config.EnumConfigType;
import com.alibaba.tsmock.config.MockServerConfig;
import com.alibaba.tsmock.po.http.HttpMockConfig;
import com.alibaba.tsmock.po.http.HttpRequest;
import com.alibaba.tsmock.po.http.HttpRoute;

import io.undertow.server.HttpServerExchange;

/**
 * The Class RouteMapper.
 *
 * @ClassName: RouteMapper
 * @Description:
 * @author: qinjun.qj
 * @date: 2017-1-7 16:30:56
 */
public final class HttpRouter {
    private static Logger logger = LoggerFactory.getLogger(HttpRouter.class);

    private static final Map<String, List<HttpRoute>> httpRouterMap = new HashMap<String, List<HttpRoute>>();

    /**
     * Instantiates a new route mapper.
     */
    private HttpRouter() {
    }

    /**
     * Load map by key.
     */
    public static void initRouter(List<HttpRoute> httpRouteList) {
        synchronized (httpRouterMap) {
            httpRouterMap.clear();

            for (final HttpRoute httpRoute : httpRouteList) {
                final String key = HttpRouterHelper.createRouterKey(httpRoute.getRequest().method,
                        httpRoute.getRequest().path,
                        ((HttpMockConfig) MockServerConfig.getConfig(EnumConfigType.HTTP)).getBaseUrl());
                if (httpRouterMap.get(key) == null) {
                    final List<HttpRoute> httpRoutes = new ArrayList<HttpRoute>();
                    httpRoutes.add(httpRoute);
                    httpRouterMap.put(key, httpRoutes);
                } else {
                    httpRouterMap.get(key).add(httpRoute);
                }
            }
        }
    }

    /**
     * Gets the route list.
     *
     * @param key the key
     * @return the route list
     */
    public static List<HttpRoute> getRoutesByKey(final String key) {
        if (httpRouterMap.containsKey(key)) {
            return httpRouterMap.get(key);
        }
        return null;
    }

    public static HttpRoute findMatchedRoute(HttpServerExchange exchange, HttpRequest httpRequest, boolean isProxy) {
        synchronized (httpRouterMap) {
            boolean getFlag = false;
            HttpRoute findHttpRoute = null;
            // find match route based on request
            // 1st find route match the method+path,
            // 2nd find the route match the query param within the 1st result
            // 3rd find the route match the form param or body within the 2nd result
            final Map<HttpRoute, HttpRouterMatchedLevel> matchedRouteMap = new LinkedHashMap<HttpRoute, HttpRouterMatchedLevel>();
            final String requestKey = HttpRouterHelper.createRequestKey(exchange);
            final String[] methodPath = requestKey.split("_");
            if (methodPath[0].equalsIgnoreCase("get")) {
                getFlag=true;
            }
            //1st match, get all the route match the method+path
            final List<HttpRoute> matchedRoute1st = httpRouterMap.get(requestKey);

            if ((matchedRoute1st != null) && (matchedRoute1st.size() > 0)) {
                //init the matchedRouteMap, later, will remove some until all match over
                for (HttpRoute httpRoute : matchedRoute1st) {
                    matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH);
                    logger.debug("[HTTP] After first match(method+path), route:{},match level:{}", httpRoute, HttpRouterMatchedLevel.METHOD_PATH);
                }


                final Map<String, List<String>> requestQueryParamMap = httpRequest.getQueryParams();
                if ((requestQueryParamMap != null) && (requestQueryParamMap.size() != 0)) {
                    Iterator<Map.Entry<HttpRoute,HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        HttpRoute httpRoute = iterator.next().getKey();
                        final Map<String, List<String>> routeQueryParamMap = httpRoute.getRequest().queryParam2Map();
                        if (routeQueryParamMap == null || routeQueryParamMap.size() == 0) {
                            //Request with query param, route no query param, still match,  as lower level route will match higher level request, method+path will match method+path+query param
                            //matchedRouteMap.remove(httpRoute);
                        } else {
                            //Request and route query param both not null, match it
                            //this flag is for not match case, the route need to be removed
                            boolean queryParamMatch = true;
                            //below 2 flag is for get all name and value match, the route level need to be setted to METHOD_PATH_QUERYPARAMNAME_QUERYPARAMVALUE
                            boolean allQueryParamNameValueMatch = false;
                            for (final String requestQueryParamName : requestQueryParamMap.keySet()) {
                                logger.debug("[HTTP] Requests query param name:" + requestQueryParamName);
                                // don't need else, it's match = true
                                // not find in route, assume it's match
                                if (routeQueryParamMap.containsKey(requestQueryParamName)) {
                                    //param name match
                                    logger.debug("[HTTP] One query param name match:" + requestQueryParamName);

                                    final List<String> requestQueryParamValues = requestQueryParamMap
                                            .get(requestQueryParamName);

                                    final List<String> routeQueryParamValues = routeQueryParamMap
                                            .get(requestQueryParamName);

                                    if (requestQueryParamValues == null || requestQueryParamValues.size() == 0) {
                                        if ((routeQueryParamValues != null) && (routeQueryParamValues.size() != 0)) {
                                            // request without query param, route with query param, match fail
                                            queryParamMatch = false;
                                            break;
                                        } else {
                                            //request without query param, route without query param, match
                                            matchedRouteMap.put(httpRoute,HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAMNAME);
                                        }
                                    } else {
                                        if ((routeQueryParamValues == null) || (routeQueryParamValues.size() == 0)) {
                                            //request with query param, route without query param, still match
                                            matchedRouteMap.put(httpRoute,HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAMNAME);
                                        } else {
                                            boolean allRequestValuesMatch = true;
                                            boolean allRouteValuesMatch = true;
                                            for (final String requestQueryParamValue : requestQueryParamValues) {
                                                boolean thisRouteMatchRequestValue = false;
                                                for (final String routeQueryParamValue : routeQueryParamValues) {
                                                    if (routeQueryParamValue.equals(requestQueryParamValue)) {
                                                        thisRouteMatchRequestValue = true;
                                                        break;
                                                    }
                                                }
                                                if (!thisRouteMatchRequestValue) {
                                                    allRequestValuesMatch = false;
                                                    break;
                                                }
                                            }
                                            for (final String routeQueryParamValue : routeQueryParamValues) {
                                                boolean thisRequestMatchRouteValue = false;
                                                for (final String requestQueryParamValue : requestQueryParamValues) {
                                                    if (requestQueryParamValue.equals(routeQueryParamValue)) {
                                                        thisRequestMatchRouteValue = true;
                                                        break;
                                                    }
                                                }
                                                if (!thisRequestMatchRouteValue) {
                                                    allRouteValuesMatch = false;
                                                    break;
                                                }
                                            }
                                            if (allRequestValuesMatch && allRouteValuesMatch) {
                                                if (allQueryParamNameValueMatch==false) {
                                                    allQueryParamNameValueMatch = true;
                                                }
                                                continue;
                                            } else {
                                                queryParamMatch = false;
                                                allQueryParamNameValueMatch=false;
                                                break;
                                            }
                                        }
                                    }
                                } else {
                                    queryParamMatch = false;
                                    break;
                                }
                            }

                            for (final String routeQueryParamName : routeQueryParamMap.keySet()) {
                                if(!requestQueryParamMap.containsKey(routeQueryParamName)) {
                                    queryParamMatch=false;
                                    break;
                                }
                            }

                            if (queryParamMatch == false) {
                                iterator.remove();
                            }
                            else {
                                if (allQueryParamNameValueMatch) {
                                    matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAMNAME_QUERYPARAMVALUE);
                                }
                            }
                        }
                    }
                } else {
                    Iterator<Map.Entry<HttpRoute,HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        HttpRoute httpRoute = iterator.next().getKey();
                        final Map<String, List<String>> routeQueryParamMap = httpRoute.getRequest().queryParam2Map();
                        //request no query param , route with query param , not match
                        if (routeQueryParamMap != null && routeQueryParamMap.size() != 0) {
                            iterator.remove();
                        }
                        else {
                            //do nothing
                        }
                    }
                }
                if (getFlag==false) {
                    boolean formRequest = false;
                    if (matchedRouteMap != null && matchedRouteMap.size() > 0) {
                        for (HttpRoute httpRoute : matchedRouteMap.keySet()) {
                            HttpRouterMatchedLevel httpRouterMatchedLevel = matchedRouteMap.get(httpRoute);
                            logger.debug("[HTTP] After second match(query param), route:{},match level:{}", httpRoute, httpRouterMatchedLevel);
                        }
                        final Map<String, String> requestFormParamMap = httpRequest.getFormParams();
                        if ((requestFormParamMap != null) && (requestFormParamMap.size() != 0)) {
                            formRequest = true;
                            Iterator<Map.Entry<HttpRoute, HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                            while (iterator.hasNext()) {
                                HttpRoute httpRoute = iterator.next().getKey();
                                final Map<String, String> routeFormParamMap = httpRoute.getRequest().formParam2Map();
                                if (routeFormParamMap == null || routeFormParamMap.size() == 0) {
                                    //Request with form param, route no form param, still match,  as lower level route will match higher level request, method+path will match method+path+query param
                                    //matchedRouteMap.remove(httpRoute);
                                } else {
                                    //Request and route query param both not null, match it
                                    boolean formParamMatch = true;
                                    boolean allFormParamValueMatch = false;
                                    for (final String requestFormParamName : requestFormParamMap.keySet()) {
                                        logger.debug("[HTTP] Requests form param name:" + requestFormParamName);
                                        // don't need else, it's match = true
                                        // not find in route, assume it's match
                                        if (routeFormParamMap.containsKey(requestFormParamName)) {
                                            //param name match
                                            logger.debug("[HTTP] One form param name match:" + requestFormParamName);

                                            final String requestFormParamValue = requestFormParamMap
                                                    .get(requestFormParamName);

                                            final String routeFormParamValue = routeFormParamMap
                                                    .get(requestFormParamName);

                                            if (StringUtils.isEmpty(requestFormParamValue)) {
                                                if (!StringUtils.isEmpty(routeFormParamValue)) {
                                                    // no route query param value, assume it's match
                                                    formParamMatch = false;
                                                    break;
                                                } else {
                                                    matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAM_FORMPARAMNAME_OR_BODY);
                                                }
                                            } else {
                                                if (StringUtils.isEmpty(routeFormParamValue)) {
                                                    matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAM_FORMPARAMNAME_OR_BODY);
                                                } else {
                                                    if (requestFormParamValue.equals(routeFormParamValue)) {
                                                        if (allFormParamValueMatch == false) {
                                                            allFormParamValueMatch = true;
                                                        }
                                                        continue;
                                                    } else {
                                                        formParamMatch = false;
                                                        allFormParamValueMatch = false;
                                                        break;
                                                    }
                                                }
                                            }
                                        } else {
                                            formParamMatch = false;
                                            break;
                                        }
                                    }

                                    for (final String routeFormParamName : routeFormParamMap.keySet()) {
                                        if (!requestFormParamMap.containsKey(routeFormParamName)) {
                                            formParamMatch = false;
                                            break;
                                        }
                                    }
                                    if (formParamMatch == false) {
                                        iterator.remove();
                                    } else {
                                        if (allFormParamValueMatch) {
                                            matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAM_FORMPARAMNAME_FORMPARAMVALUE_OR_BODY);
                                        }
                                    }
                                }
                            }
                        } else {
                            Iterator<Map.Entry<HttpRoute, HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                            while (iterator.hasNext()) {
                                HttpRoute httpRoute = iterator.next().getKey();
                                final Map<String, String> routeFormParamMap = httpRoute.getRequest().formParam2Map();
                                //request no query param , route with query param , not match
                                if (routeFormParamMap != null && routeFormParamMap.size() != 0) {
                                    iterator.remove();
                                } else {
                                    //do nothing
                                }
                            }
                        }

                    } else {
                        //do nothing
                    }

                    if (formRequest == false) {
                        if (matchedRouteMap != null && matchedRouteMap.size() > 0) {
                            for (HttpRoute httpRoute : matchedRouteMap.keySet()) {
                                HttpRouterMatchedLevel httpRouterMatchedLevel = matchedRouteMap.get(httpRoute);
                                logger.debug("[HTTP] After third match(form param), route:{},match level:{}", httpRoute, httpRouterMatchedLevel);
                            }
                            String requestBody = httpRequest.getBody();
                            if (!StringUtils.isEmpty(requestBody)) {
                                Iterator<Map.Entry<HttpRoute, HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    HttpRoute httpRoute = iterator.next().getKey();
                                    final String routeBodyRegex = httpRoute.getRequest().getBodyRegex();
                                    if (StringUtils.isEmpty(routeBodyRegex)) {

                                    } else {
                                        final Pattern routeBodyPattern = Pattern.compile(routeBodyRegex);
                                        final Matcher routeBodyMatcher = routeBodyPattern.matcher(requestBody);
                                        if (routeBodyMatcher.find()) {
                                            logger.info("[HTTP] Route Match:[METHOD|PATH|BODY]:[{}|{}|{}]", methodPath[0],
                                                    methodPath[1], routeBodyRegex);
                                            matchedRouteMap.put(httpRoute, HttpRouterMatchedLevel.METHOD_PATH_QUERYPARAM_FORMPARAMNAME_OR_BODY);
                                        } else {
                                            iterator.remove();
                                        }
                                    }
                                }
                            } else {
                                Iterator<Map.Entry<HttpRoute, HttpRouterMatchedLevel>> iterator = matchedRouteMap.entrySet().iterator();
                                while (iterator.hasNext()) {
                                    HttpRoute httpRoute = iterator.next().getKey();
                                    final String routeBodyRegex = httpRoute.getRequest().getBodyRegex();
                                    //request no body , route with body , not match
                                    if (!StringUtils.isEmpty(routeBodyRegex)) {
                                        iterator.remove();
                                    } else {
                                        //do nothing
                                    }
                                }
                            }
                        } else {
                            //do nothing
                        }
                    }
                }


                if (matchedRouteMap!=null && matchedRouteMap.size()>0) {
                    HttpRouterMatchedLevel highestLevel = null;
                    for (HttpRoute httpRoute : matchedRouteMap.keySet()) {
                        HttpRouterMatchedLevel httpRouterMatchedLevel = matchedRouteMap.get(httpRoute);
                        if (findHttpRoute==null) {
                            findHttpRoute = httpRoute;
                            highestLevel = httpRouterMatchedLevel;
                        }
                        else {
                            if (httpRouterMatchedLevel.ordinal() > highestLevel.ordinal()) {
                                findHttpRoute = httpRoute;
                            }
                        }
                    }
                    if (isProxy) {
                        logger.debug("[HTTP] [Proxy] Find matched route:" + findHttpRoute);
                    } else {
                        logger.debug("[HTTP]  Find matched route:" + findHttpRoute);
                    }
                }
                else {
                    if (isProxy) {
                        logger.warn("[HTTP] [Proxy] Not find matched route");
                    } else {
                        logger.warn("[HTTP] Not find matched route");
                    }
                }
            } else {
                //1st match ,no route find, assign findHttpRoute null, assign again just for better readable
                if (isProxy) {
                    logger.warn("[HTTP] [Proxy] Not find matched route");
                } else {
                    logger.warn("[HTTP] Not find matched route");
                }
            }
            return findHttpRoute;
        }
    }
}
