package com.mikuac.shiro;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.enums.WeatherEnum;
import com.mikuac.shiro.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.HttpCookie;
import java.text.MessageFormat;
import java.util.List;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestOne {
    //@Test
    public static List<HttpCookie> loginJWG() throws Exception {
        String username = "2021405102";
        String password = "qwayspn@12";
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
        System.out.println(csrftoken);

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
        System.out.println(RSAUtils.encrypt(modulus, exponent, password));
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
        System.out.println(request.getUrl());
        //请求
        response = request.execute();
        //输出响应内容和响应头
        System.out.println(response.body());
        System.out.println(response.headers());
        System.out.println(response.getCookieStr());
        System.out.println(response.getCookies());
        return response.getCookies();
        //输出请求头
    }

    @Test
    public void test() {
        log.info(WeatherEnum.TODAY.getMessage());
    }

    @Test
    public void getGrade() throws Exception {
        //post请求http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102
        String url = "http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).body("xnm=2021&xqm=12");
        //设置cookie
        request.cookie(loginJWG().toString());
        System.out.println(request.getUrl());
        System.out.println(loginJWG().get(0).toString());
        //发送请求
        HttpResponse response = request.execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject grade = JSON.parseObject(response.body());
        //获取成绩
        JSONArray gradeJSONArray = grade.getJSONArray("items");
        for (Object o : gradeJSONArray) {
            JSONObject gradeJSON = (JSONObject) o;
            String kcmc = (String) gradeJSON.get("kcmc");
            String xf = (String) gradeJSON.get("xf");
            String cj = (String) gradeJSON.get("cj");
            String msg = kcmc + " " + xf + "学分" + " " + "成绩为" + cj;
            System.out.println(msg);
        }
    }
}
