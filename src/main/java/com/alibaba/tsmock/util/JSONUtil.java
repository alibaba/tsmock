package com.alibaba.tsmock.util;

/**
 * Created by qinjun.qj on 2018/2/7.
 */
public class JSONUtil {
    public static String minify(String originJson) {
        String minifyJson=null;
        String[] originJsonLines = originJson.split(System.lineSeparator());
        for (int i=0;i<originJsonLines.length;i++) {
            if (originJsonLines[i].matches("\\s*//[^/]*")) {
                originJsonLines[i]="";
            }
        }

        StringBuilder minifyJsonSb= new StringBuilder();
        for(String originJsonLine : originJsonLines){
            if(!originJsonLine.equals("")){
                minifyJsonSb.append(originJsonLine).append(System.lineSeparator());
            }
        }
        minifyJson = minifyJsonSb.toString();

        return minifyJson;
    }

}
