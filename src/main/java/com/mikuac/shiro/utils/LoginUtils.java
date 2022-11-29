package com.mikuac.shiro.utils;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;

public class LoginUtils {
    public static HttpResponse loginRequest(String username, String password) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //获取当前时间戳
        long time = System.currentTimeMillis();
        //请求http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={}
        String url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={0}";
        url = MessageFormat.format(url, time);
        //发送请求
        HttpRequest request = HttpUtil.createGet(url);
        //请求
        HttpResponse response = request.execute();
        //提取html中id为input[name="csrftoken"]的value
        String html = response.body();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input[name=\"csrftoken\"]");
        String csrftoken = elements.get(0).attr("value");

        //请求http://jwxt.lcu.edu.cn/jwglxt/xtgl/login_getPublicKey.html?time={0}
        url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_getPublicKey.html?time={0}";
        url = MessageFormat.format(url, time);
        //发送请求
        request = HttpUtil.createGet(url);
        //请求
        response = request.execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String modulus = jsonObject.get("modulus").toString();
        String exponent = jsonObject.get("exponent").toString();
        //请求http://jwxt.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={0}
        url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={0}";
        url = MessageFormat.format(url, time);
        //请求参数为表单
        request = HttpUtil.createPost(url);
        //设置请求参数
        request.form("csrftoken", csrftoken);
        request.form("yhm", username);
        request.form("mm", RSAUtils.encrypt(modulus, exponent, password));
        request.form("mm", RSAUtils.encrypt(modulus, exponent, password));
        response = request.execute();
        return response;
    }
}
