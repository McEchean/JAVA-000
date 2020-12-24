package io.kimmking.rpcfx.client.invoke.nettyclient.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * desc:
 *
 * @author : caokunliang
 * creat_date: 2019/8/22 0022
 * creat_time: 16:24
 **/
public class ModuleUtils {

    static Pattern pattern = Pattern.compile("http://([0-9.]+):([0-9]{2,5})(/.*)");


    /**
     * 提取ip
     * @param url,格式如："http://192.168.19.89:8080/CAD_WebService"
     * @return
     */
    public static String extractIp(String  url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        Matcher matcher = pattern.matcher(url);

        boolean b = matcher.find();
        if (b) {
            String ip = matcher.group(1);
            return ip;
        }
        return null;
    }

    /**
     * 提取ip和端口
     * @param url,格式如："http://192.168.19.89:8080/CAD_WebService"
     * @return
     */
    public static String[] extractIpAndPort(String  url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        Matcher matcher = pattern.matcher(url);

        boolean b = matcher.find();
        if (b) {
            String ip = matcher.group(1);
            String port = matcher.group(2);
            return new String[]{ip,port};
        }

        return null;
    }

    /**
     * 提取端口
     * @param url,格式如："http://192.168.19.89:8080/CAD_WebService"
     * @return
     */
    public static String extractPort(String  url) {
        if (StringUtils.isBlank(url)) {
            return null;
        }
        Matcher matcher = pattern.matcher(url);

        boolean b = matcher.find();
        if (b) {
            String port = matcher.group(2);

            return port;
        }
        return null;
    }

}
