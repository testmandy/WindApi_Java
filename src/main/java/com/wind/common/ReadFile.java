package com.wind.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @Author mandy
 * @Create 2019/10/17 11:25
 */
public class ReadFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("env", Locale.CHINA);

    public static String getUrl(InterfaceName name){
        String testUrl;
        String uri = "";
        String base = bundle.getString("base.url");

        if (name == InterfaceName.LOGIN) {
            uri = bundle.getString("login.url");
        }
        if (name == InterfaceName.MINECARDS) {
            uri = bundle.getString("myCards.url");
        }
        if (name == InterfaceName.NEARSPHERE) {
            uri = bundle.getString("nearSphere.url");
        }


        testUrl  = base + uri;
        System.out.println(testUrl);
        return testUrl;


    }


    public static String getData(String key){
        String data = bundle.getString(key);
        return data;
    }


}


