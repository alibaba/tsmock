package com.alibaba.tsmock.api.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by qinjun.qj on 2018/8/6.
 */
@ApplicationPath("/rest/*")
public class RestApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        HashSet<Class<?>> classes = new HashSet<Class<?>>();
        classes.add(HttpConfig.class);
        classes.add(RestartHttpMock.class);
        return classes;
    }
}
