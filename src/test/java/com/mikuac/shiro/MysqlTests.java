package com.mikuac.shiro;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotContainer;
import com.mikuac.shiro.entity.*;
import com.mikuac.shiro.service.CourseService;
import com.mikuac.shiro.service.GradeService;
import com.mikuac.shiro.service.UserService;
import com.mikuac.shiro.utils.LoginUtils;
import com.mikuac.shiro.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.*;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class MysqlTests {
    @Autowired
    private UserService userService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private CourseService courseService;
    @Resource
    private BotContainer botContainer;

    @Test
    public void Login() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String username = "2021405102";
        String password = "qwayspn@12";
        //获取当前时间戳
        long time = System.currentTimeMillis();
        //构造url
        String url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={0}";
        url = MessageFormat.format(url, time);
        //发送请求，获取响应
        HttpRequest request = HttpUtil.createGet(url);
        HttpResponse response = request.execute();
        //提取html中id为input[name="csrftoken"]的value
        String html = response.body();
        Document document = Jsoup.parse(html);
        Elements elements = document.select("input[name=\"csrftoken\"]");
        String csrftoken = elements.get(0).attr("value");

        //构造请求url
        url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_getPublicKey.html?time={0}";
        url = MessageFormat.format(url, time);
        request = HttpUtil.createGet(url);
        //发送请求，获取公钥
        response = request.execute();
        JSONObject jsonObject = JSONObject.parseObject(response.body());
        String modulus = jsonObject.get("modulus").toString();
        String exponent = jsonObject.get("exponent").toString();
        //构造请求url
        url = "http://jwcweb.lcu.edu.cn/jwglxt/xtgl/login_slogin.html?time={0}";
        url = MessageFormat.format(url, time);
        request = HttpUtil.createPost(url);
        //设置请求参数
        request.form("csrftoken", csrftoken);
        request.form("yhm", username);
        request.form("mm", RSAUtils.encrypt(modulus, exponent, password));
        request.form("mm", RSAUtils.encrypt(modulus, exponent, password));
        //发送请求获取cookies
        response = request.execute();
        String cookies = "route=" + response.getCookieValue("route") + ";" + "JSESSIONID=" + response.getCookieValue("JSESSIONID");
        //存储到数据库
        User user = new User();
        user.setQqnumber("2028350271");
        user.setCookies(cookies);
        user.setStudentnumber(username);
        user.setPassword(password);
        userService.saveOrUpdate(user);
    }

    @Test
    public void test() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InterruptedException {
        HttpResponse response = LoginUtils.loginRequest("2021405102", "qwayspn@12");
        String cookie = "route=" + response.getCookieValue("route") + ";" + "JSESSIONID=" + response.getCookieValue("JSESSIONID");
        System.out.println(cookie);
        System.out.println(response);


        //post请求http://jwxt.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102
        String url = "http://jwcweb.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", cookie);
        //添加表单
        request = HttpUtil.createPost(url);
        request.form("xnm", "2021");
        request.form("xqm", "3");
        request.form("_search", "false");
        request.form("nd", "1668693695203");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "5");
        System.out.println(request);
        //发送请求
        response = request.execute();
        System.out.println(response);
        //用jsonfast2将result转换为JSONObject
        JSONObject grade = JSON.parseObject(response.body());
        //获取成绩
        JSONArray gradeJSONArray = grade.getJSONArray("items");

        List<Grade> gradeList = new LinkedList<>();
        StringBuilder message = new StringBuilder();
        StringBuilder message1 = new StringBuilder();
        for (Object o : gradeJSONArray) {
            JSONObject jsonObject = (JSONObject) o;
            Grade grade1 = new Grade();
            grade1.setCoursename(jsonObject.getString("kcmc"));
            grade1.setCourseproperty(jsonObject.getString("kcxzmc"));
            grade1.setCredit(Float.parseFloat(jsonObject.getString("xf")));
            grade1.setGrade(Integer.parseInt(jsonObject.getString("bfzcj")));
            grade1.setGpa(Float.parseFloat(jsonObject.getString("jd")));
            grade1.setStudentnumber(jsonObject.getString("xh"));
            grade1.setTeacher(jsonObject.getString("tjrxm"));
            grade1.setId(grade1.getStudentnumber() + grade1.getCoursename());
            grade1.setTerm(jsonObject.getString("xqmmc"));
            grade1.setYear(jsonObject.getString("xnmmc"));
            gradeList.add(grade1);
            System.out.println(grade1);
            message.append(grade1.toString()).append("\n");
            message1.append("课程名：").append(grade1.getCoursename()).append("    ").append("成绩：").append(grade1.getGrade()).append("\n");
        }
        System.out.println(gradeList);
        System.out.println(message);
        System.out.println(message1);
        gradeService.saveOrUpdateBatch(gradeList);
    }

    @Test
    public void test1() throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException, InterruptedException {
        User user = userService.getById("2028350271");
        //查找数据库grade中的
        List<Grade> gradeList = gradeService.list(new QueryWrapper<Grade>().eq("studentnumber", user.getStudentnumber()));
        StringBuilder message = new StringBuilder();
        StringBuilder message1 = new StringBuilder();
        for (Grade grade : gradeList) {
            message.append(grade.toString()).append("\n");
            message1.append(grade.getCoursename()).append("    ").append(grade.getGrade()).append("\n");
        }
        System.out.println(message);
        System.out.println(message1);
        //获取机器人
        long botId = 2904918755L;
        // 通过机器人账号取出 Bot 对象
        Bot bot = botContainer.robots.get(botId);
        // 调用 Bot 对象方法
        bot.sendPrivateMsg(2028350271L, String.valueOf(message), false);
        bot.sendPrivateMsg(2028350271L, String.valueOf(message1), false);
    }

    @Test
    public void test2() {
        //请求https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=diseaseh5Shelf
        String url = "https://api.inews.qq.com/newsqa/v1/query/inner/publish/modules/list?modules=diseaseh5Shelf";
        //发送请求
        HttpResponse response = HttpUtil.createGet(url).execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject jsonObject = JSON.parseObject(response.body());
        //jsonObject转换为Epidemic对象
        EpidemicJson epidemicJson = JSON.toJavaObject(jsonObject, EpidemicJson.class);
        List<EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX> areaTree = epidemicJson.getData().getDiseaseh5Shelf().getAreaTree().get(0).getChildren();
        Map<String, EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX> provinceMap = new HashMap<>();
        for (EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX childrenX : areaTree) {
            provinceMap.put(childrenX.getName(), childrenX);
        }
        Map<String, EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX.Children> cityMap = new HashMap<>();
        for (EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX childrenX : areaTree) {
            for (EpidemicJson.Data.Diseaseh5Shelf.AreaTree.ChildrenX.Children children : childrenX.getChildren()) {
                cityMap.put(children.getName(), children);
            }
        }
        StringBuilder message = new StringBuilder();
        String city = "北京";
        message.append("新增本土").append(provinceMap.get(city).getToday().getLocalConfirmAdd()).append("例").append(" ")
                .append("新增本土无症状").append(provinceMap.get(city).getToday().getWzzAdd()).append("例").append(" ")
                .append("现有确诊").append(provinceMap.get(city).getToday().getConfirm()).append("例").append("\n")
                .append("累计确诊").append(provinceMap.get(city).getTotal().getConfirm()).append("例").append(" ")
                .append("累计治愈").append(provinceMap.get(city).getTotal().getHeal()).append("例").append(" ")
                .append("累计死亡").append(provinceMap.get(city).getTotal().getDead()).append("例").append("\n");
        System.out.println(message);
        StringBuilder message1 = new StringBuilder();
        String city1 = "聊城";
        message1.append("新增本土").append(cityMap.get(city1).getToday().getLocalConfirmAdd()).append("例").append(" ")
                .append("新增本土无症状").append(cityMap.get(city1).getToday().getWzzAdd()).append("例").append(" ")
                .append("现有确诊").append(cityMap.get(city1).getToday().getConfirm()).append("例").append("\n")
                .append("累计确诊").append(cityMap.get(city1).getTotal().getConfirm()).append("例").append(" ")
                .append("累计治愈").append(cityMap.get(city1).getTotal().getHeal()).append("例").append(" ")
                .append("累计死亡").append(cityMap.get(city1).getTotal().getDead()).append("例").append("\n");
        System.out.println(message1);
    }

    @Test
    public void test3() {
        //请求https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total
        String url = "https://www.zhihu.com/api/v3/feed/topstory/hot-lists/total";
        //发送请求
        HttpResponse response = HttpUtil.createGet(url).execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject jsonObject = JSON.parseObject(response.body());
        //获取data数组
        JSONArray data = jsonObject.getJSONArray("data");
        List<ZhiHu> zhiHuList = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            JSONObject jsonObject1 = data.getJSONObject(i);
            ZhiHu zhiHu = new ZhiHu();
            zhiHu.setTitle(jsonObject1.getJSONObject("target").getString("title"));
            url = jsonObject1.getJSONObject("target").getString("url");
            String newUrl = url.substring(0, 8) + url.substring(12, 30) + url.substring(31);
            zhiHu.setUrl(newUrl);
            zhiHu.setContent(jsonObject1.getJSONObject("target").getString("excerpt"));
            zhiHu.setHeatDegree(jsonObject1.getString("detail_text"));
            String id = jsonObject1.getString("id");
            int index = id.indexOf("_");
            //获取第一个下划线前面的字符串
            id = id.substring(0, index);
            zhiHu.setId(String.valueOf(Integer.parseInt(id) + 1));
            zhiHuList.add(zhiHu);
        }
        int index = 1;
        System.out.println(zhiHuList.get(index - 1));
        String message = "1-20";
        //用-分割字符串
        String[] str = message.split("-");
        int start = Integer.parseInt(str[0]) - 1;
        int end = Integer.parseInt(str[1]) - 1;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = start; i <= end; i++) {
            stringBuilder.append(zhiHuList.get(i).toString()).append("\n");
        }
        System.out.println(stringBuilder);
        //生成0-49的随机数
        int random = (int) (Math.random() * 50);
        System.out.println(zhiHuList.get(random));
    }

    @Test
    public void test4() {
        //https://weibo.com/ajax/statuses/hot_band
        String url = "https://weibo.com/ajax/statuses/hot_band";
        //发送请求
        HttpResponse response = HttpUtil.createGet(url).execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject jsonObject = JSON.parseObject(response.body());
        //获取data数组
        JSONArray data = jsonObject.getJSONObject("data").getJSONArray("band_list");
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < 50; ++i) {
            stringBuilder.append("排行：").append(Integer.parseInt(data.getJSONObject(i).getString("rank")) + 1).append(" 标题：").append(data.getJSONObject(i).getString("note")).append("\n");
        }
        System.out.println(stringBuilder);
    }

    /**
     * 更新课表
     */
    @Test
    public void test5() {
        //请求http://jwxt.lcu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151&su=2021405102
        String url = "http://jwxt.lcu.edu.cn/jwglxt/kbcx/xskbcx_cxXsKb.html?gnmkdm=N2151&su=2021405102";
        //获取cookie，并设置
        User user = userService.getById("2028350271");
        String cookies = user.getCookies();
        cookies = "route=6ac742ba71e071875d289325c98ae1b8;JSESSIONID=AFF57B728B700AB643B5AE2018CE7154";
        System.out.println(cookies);
        //发送请求
        List<String> terms = new ArrayList<>();
        terms.add("0");
        terms.add("3");
        terms.add("12");
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", cookies)
                .form("xnm", "2021")
                .form("xqm", terms.get(1));
        System.out.println(request);
        //执行请求，获取返回结果
        HttpResponse response = request.execute();
        System.out.println(response);
        //用jsonfast2将result转换为JSONObject
        JSONObject jsonObject = JSON.parseObject(response.body());
        String studentNumber = jsonObject.getJSONObject("xsxx").getString("XH");
        //获取kbList数组
        JSONArray kbList = jsonObject.getJSONArray("kbList");
        List<Course> courseList = new ArrayList<>();
        for (int i = 0; i < kbList.size(); i++) {
            JSONObject jsonObject1 = kbList.getJSONObject(i);
            Course course = new Course();
            course.setSite(jsonObject1.getString("cdmc"));
            course.setTeacher(jsonObject1.getString("xm"));
            course.setCoursename(jsonObject1.getString("kcmc"));
            course.setDay(jsonObject1.getString("xqj"));
            course.setSection(jsonObject1.getString("jcor"));
            course.setCourseProperty(jsonObject1.getString("kcxz"));
            course.setOpeningweek(jsonObject1.getString("zcd"));
            course.setCredit(jsonObject1.getString("xf"));
            course.setStudentnumber(studentNumber);
            course.setId(studentNumber + course.getCoursename());
            courseList.add(course);
        }
        courseService.saveOrUpdateBatch(courseList);
    }

    @Test
    public void test6() {

    }
}
