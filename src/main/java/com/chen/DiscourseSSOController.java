package com.chen;


import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 一个简单的处理Discourse 论坛
 * SS0 单点登录的代码案列，
 * 具体的登录页面以及登录的逻辑这里不提供，本代码主要负责的是
 * 登录成功之后如何处理Discourse发送过来的数据，并且校验
 * 切生成自己的数据转发到Discourse
 */
@Controller
public class DiscourseSSOController {

    /**
     * 你设置的discourse sso 单点登录的
     * secret_key
     */
    @Value("${discourse.ssoKey}")
    private String key;

    /**
     * discourse的论坛 网址
     */
    @Value("${discourse.url}")
    private String discourseUrl;

    @RequestMapping("/discourse/sso")
    public ResponseEntity discourseSSO(@RequestParam String sso, @RequestParam String sig, HttpServletRequest request
            , HttpServletResponse response) throws Exception {

        String hmacsha256 = EncryptUtils.HMACSHA256(sso, key);
        if (!StringUtils.equalsIgnoreCase(hmacsha256, sig)) {
            return null;
        }
        String decodeSSO = EncryptUtils.base64Decode(sso);
        Map<String, String> params = new HashMap<>();
        params.put("username", URLEncoder.encode("xxxx", "UTF-8"));
        params.put("email", URLEncoder.encode("xxxx", "UTF-8"));
        params.put("name", URLEncoder.encode("xxxxx", "UTF-8"));
        params.put("external_id", URLEncoder.encode("xxxxx", "UTF-8"));
        String[] split = decodeSSO.split("[&]");
        for (String s : split) {
            String[] strings = s.split("[=]");
            if (strings.length > 1 && StringUtils.equals("nonce", strings[0])) {
                params.put(strings[0], URLEncoder.encode(strings[1], "UTF-8"));
            }
        }

        StringBuffer stringBuffer = new StringBuffer();
        Set<Map.Entry<String, String>> entries = params.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        String payload = stringBuffer.substring(0, stringBuffer.length() - 1);

        payload = EncryptUtils.base64Encode(payload);
        String requestSig = EncryptUtils.HMACSHA256(payload, key).toLowerCase();
        payload = URLEncoder.encode(payload, "UTF-8");
        response.sendRedirect(discourseUrl + "session/sso_login?sso=" + payload + "&sig=" + requestSig);
        return null;
    }
}
