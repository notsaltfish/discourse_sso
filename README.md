# discourse_sso
## A simple java demo for discourse sso
1. 代码是在当论坛跳转到登录地址成功登录之后的处理逻辑，将事先设置好的sso_key对payload进行HMAC-SHA256
与发送过来的HMAC-SHA256摘要进行校验，不匹配则直接返回

    String hmacsha256 = EncryptUtils.HMACSHA256(sso, key);
        if (!StringUtils.equalsIgnoreCase(hmacsha256, sig)) {
        return null;
    }
            
2. 获取登录的用户信息，并且将payload解码重新与用户信息字段拼接为queryString
在使用HMAC-SHA256进行摘要计算，然后控制前端转发到discourse论坛即可