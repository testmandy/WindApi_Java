package com.wind.common;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * @Author mandy
 * @Create 2019/10/17 11:25
 */
public class ReadEnv {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application",Locale.CHINA);


    public static String getData(String key){
        String data = bundle.getString(key);
        return data;
    }

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
        if (name == InterfaceName.PUBLISHCARDURL) {
            uri = bundle.getString("publishCard.url");
        }
        if (name == InterfaceName.MODIFYCARDURL) {
            uri = bundle.getString("modifyCard.url");
        }
        if (name == InterfaceName.REMOVECARDURL) {
            uri = bundle.getString("removeCard.url");
        }
        if (name == InterfaceName.TOPICLISTURL) {
            uri = bundle.getString("topicList.url");
        }

        testUrl  = base + uri;
        System.out.println(testUrl);
        return testUrl;

    }

}


