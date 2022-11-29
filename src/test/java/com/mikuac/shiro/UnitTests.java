package com.mikuac.shiro;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.common.limit.RateLimiter;
import com.mikuac.shiro.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.MessageFormat;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>UnitTests class.</p>
 *
 * @author zero
 * @version $Id: $Id
 * @since 1.3.7
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UnitTests {

    @Resource
    private RateLimiter rateLimiter;

    /**
     * <p>rateLimiterTest.</p>
     */
    @Test
    public void rateLimiterTest() {
        //请求get接口https://v0.yiketianqi.com/api?unescape=1&version=v91&appid=43656176&appsecret=I42og6Lm&ext=&cityid=&city=聊城
        String url = "https://v0.yiketianqi.com/api?unescape=1&version=v91&appid=43656176&appsecret=I42og6Lm&ext=&cityid=&city=聊城";
        String result = HttpUtil.get(url);
        //用jsonfast2将result转换为JSONObject
        JSONObject weather = JSON.parseObject(result);


        //当天天气
        JSONObject weatherJSON = (JSONObject) weather.getJSONArray("data").get(0);

        System.out.println(weatherJSON.get("day") + "天气为" + weatherJSON.get("wea") + "最低" + weatherJSON.get("tem2") + "ºC。" + " " + weatherJSON.get("narrative"));

        //获取后六天天气
        JSONArray weatherJSONArray = weather.getJSONArray("data");
        for (int i = 1; i < weatherJSONArray.size(); i++) {
            weatherJSON = (JSONObject) weatherJSONArray.get(i);
            String day = (String) weatherJSON.get("day");
            String wea = (String) weatherJSON.get("wea");
            String narrative = (String) weatherJSON.get("narrative");
            String tem2 = (String) weatherJSON.get("tem2");
            System.out.println(day + "天气为" + wea + " " + "最低" + tem2 + "ºC。" + narrative);
        }
    }

    @Test
    public void getGrade() {
        //post请求http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102
        String url = "http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", "JSESSIONID=B33E8263D7BD43562C374C3AFC85C079;route=6ac742ba71e071875d289325c98ae1b8").body("xnm=2021&xqm=12");
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

    @Test
    public void getClassInfo() {
        //请求http://jwxt.lcu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151&su=2021405102，带上cookie，获取课表
        String url = "http://jwxt.lcu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", "JSESSIONID=C50C261C5B0AE6E99EE7482BB810D2DC; route=29dae7b054e6d5569418bfa553808bde").body("xnm=2022&xqm=3");
        HttpResponse response = request.execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject classInfo = JSON.parseObject(response.body());
        //获取课表
        JSONArray classInfoJSONArray = classInfo.getJSONArray("kbList");
        for (Object o : classInfoJSONArray) {
            JSONObject classInfoJSON = (JSONObject) o;
            String kcmc = (String) classInfoJSON.get("kcmc");
            String cdmc = (String) classInfoJSON.get("cdmc");
            String jc = (String) classInfoJSON.get("jc");
            //xm
            String xm = (String) classInfoJSON.get("xm");
            //xqjmc
            String xqjmc = (String) classInfoJSON.get("xqjmc");
            //zcd
            String zcd = (String) classInfoJSON.get("zcd");
            String msg = kcmc + "\n" + cdmc + "\n" + jc + "\n" + xm + "\n" + xqjmc + "\n" + zcd;
            System.out.println(msg);
        }
    }

    @Test
    public void getePidemic() {
        //请求https://api.inews.qq.com/newsqa/v1/automation/foreign/country/ranklist
        String url = "https://api.inews.qq.com/newsqa/v1/automation/foreign/country/ranklist";
        String result = HttpUtil.get(url);
        //用jsonfast2将result转换为JSONObject
        JSONObject epidemic = JSON.parseObject(result);
        //获取疫情
        JSONArray epidemicJSONArray = epidemic.getJSONArray("data");
        List<String> list = new LinkedList<>();
        for (Object o : epidemicJSONArray) {
            JSONObject epidemicJSON = (JSONObject) o;
            String name = epidemicJSON.get("name").toString();
            String confirm = epidemicJSON.get("confirm").toString();
            String heal = epidemicJSON.get("heal").toString();
            String dead = epidemicJSON.get("dead").toString();
            String msg = name + " " + confirm + "确诊" + " " + heal + "治愈" + " " + dead + "死亡";
            list.add(msg);
        }
        //输出疫情
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void getWeiBOHotBand() {
        String url = "https://weibo.com/ajax/statuses/hot_band";
        String result = HttpUtil.get(url);
        //用jsonfast2将result转换为JSONObject
        JSONObject hotBand = JSON.parseObject(result);
        //获取热搜
        JSONObject hotBandJSON = hotBand.getJSONObject("data");
        JSONArray hotBandJSONArray = hotBandJSON.getJSONArray("band_list");
        List<String> list = new LinkedList<>();
        for (Object o : hotBandJSONArray) {
            hotBandJSON = (JSONObject) o;
            String label_name = hotBandJSON.get("label_name").toString();
            String category = hotBandJSON.get("category").toString();
            //word
            String word = hotBandJSON.get("word").toString();
            String msg = label_name + " " + category + " " + word;
            list.add(msg);
        }
        //输出热搜
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void getZhiHuHotBand() {
        //https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true
        String url = "https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total?limit=50&desktop=true";
        String result = HttpUtil.get(url);
        //用jsonfast2将result转换为JSONObject
        //result=StringEscapeUtils.unescapeHtml(result);
        JSONObject hotBand = JSON.parseObject(result);
        //获取热搜
        JSONArray hotBandJSONArray = hotBand.getJSONArray("data");
        List<String> list = new LinkedList<>();
        for (Object o : hotBandJSONArray) {
            JSONObject hotBandJSON = (JSONObject) o;
            //detail_text
            String detail_text = hotBandJSON.get("detail_text").toString();
            //target内的title
            JSONObject target = hotBandJSON.getJSONObject("target");
            String title = target.get("title").toString();
            //target内的excerpt
            String excerpt = target.get("excerpt").toString();
            String msg = detail_text + "\n" + title + "\n" + excerpt;
            list.add(msg);
        }
        //输出热搜
        for (String s : list) {
            System.out.println(s);
        }
    }

    @Test
    public void loginJWG() throws Exception {
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
        String cookie = "route=" + response.getCookieValue("route") + ";" + "JSESSIONID=" + response.getCookieValue("JSESSIONID");
        System.out.println(cookie);
        System.out.println(response);

        //请求http://jwxt.lcu.edu.cn/jwglxt/xsxk/tjxkyzb_cxXkResultTjxkYzb.html?doType=query&gnmkdm=N253520
        url = "http://jwcweb.lcu.edu.cn/jwglxt/xsxk/tjxkyzb_cxXkResultTjxkYzb.html?doType=query&gnmkdm=N253520";
        //Post请求，请求参数为表单，参数为xnm=2021&xqm=12&_search=false&nd=1668693062061&queryModel.showCount=15&queryModel.currentPage=1&queryModel.sortName=&queryModel.sortOrder=asc&time=1
        request = HttpUtil.createPost(url);
        request.header("Cookie", cookie);
        request.form("xnm", "2022");
        request.form("xqm", "3");
        request.form("_search", "false");
        request.form("nd", "1668693062061");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "1");
        response = request.execute();
        System.out.println(response);


        url = "http://jwcweb.lcu.edu.cn/jwglxt/xsxk/tjxkyzb_cxXkResultTjxkYzb.html?doType=query&gnmkdm=N253520";
        //Post请求，请求参数为表单，参数为xnm=2021&xqm=12&_search=false&nd=1668693062061&queryModel.showCount=15&queryModel.currentPage=1&queryModel.sortName=&queryModel.sortOrder=asc&time=1
        request = HttpUtil.createPost(url);
        request.header("Cookie", cookie);
        request.form("xnm", "2022");
        request.form("xqm", "3");
        request.form("_search", "false");
        request.form("nd", "1668693062061");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "1");
        response = request.execute();
        System.out.println(response);

        //请求http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102
        url = "http://jwcweb.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //Post请求，请求参数为表单，参数为xnm=2021&xqm=3&_search=false&nd=1668693695203&queryModel.showCount=15&queryModel.currentPage=1&queryModel.sortName=&queryModel.sortOrder=asc&time=5
        request = HttpUtil.createPost(url);
        request.header("Cookie", cookie);
        request.form("xnm", "2021");
        request.form("xqm", "3");
        request.form("_search", "false");
        request.form("nd", "1668693695203");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "5");
        response = request.execute();
        System.out.println(response);
    }

    @Test
    public void LoginUPlus() throws NoSuchFieldException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        //http://ee-c.lcu.edu.cn/api/auth/key
        String url = "http://ee-c.lcu.edu.cn/api/auth/key";
        //发送请求
        HttpRequest request = HttpUtil.createGet(url);
        //请求
        HttpResponse response = request.execute();
        //获取data内的publicKey
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        jsonObject = (JSONObject) jsonObject.get("data");
        System.out.println(jsonObject);
        String publicKey = jsonObject.getString("publicKey");
        //请求POST
        //	http://ee-c.lcu.edu.cn/api/auth/login
        url = "http://ee-c.lcu.edu.cn/api/auth/login";
        request = HttpUtil.createPost(url);


        String password = "2021405102+abc2028350271";
        String cryptogram = RSAUtils.encryptOne(publicKey, password);
        //新建json，设置mode为“Password”，设置key为publicKey，设置cryptogram为cryptogram
        JSONObject json = new JSONObject();
        json.put("mode", "Password");
        json.put("key", publicKey);
        json.put("cryptogram", cryptogram);
        request.body(String.valueOf(json));
        //发送请求
        response = request.execute();
        System.out.println(response);
    }
}
