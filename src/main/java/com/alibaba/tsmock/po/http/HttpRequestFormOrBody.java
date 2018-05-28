package com.alibaba.tsmock.po.http;

import java.util.Map;

/**
 * Created by qinjun.qj on 2018/5/2.
 */
public class HttpRequestFormOrBody {
    String body;
    Map<String,String> form;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Map<String, String> getForm() {
        return form;
    }

    public void setForm(Map<String, String> form) {
        this.form = form;
    }
}
