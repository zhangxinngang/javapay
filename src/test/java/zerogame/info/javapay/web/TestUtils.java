/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package zerogame.info.javapay.web;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 *
 * @author zhangxingang
 */
public class TestUtils {
    public static String getTBTStatus(String loginString) {
        String result = JSON.parseObject(loginString).get("status").toString();
        return result;
    }
}
