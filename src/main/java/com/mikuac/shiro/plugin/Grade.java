package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.User;
import com.mikuac.shiro.service.GradeService;
import com.mikuac.shiro.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;

@Shiro
@Component
@EnableScheduling
@Slf4j
public class Grade {

    private final String[] terms = {"3", "12"};
    @Autowired
    private UserService userService;
    @Autowired
    private GradeService gradeService;

    @PrivateMessageHandler(cmd = "^更新获取成绩.*")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String[] yearAndTerm = event.getMessage().split(" ");
        if (!"更新获取成绩".equals(yearAndTerm[0])) {
            return;
        }
        String year = yearAndTerm[1];
        String term = yearAndTerm[2];
        User user = userService.getById(event.getUserId());
        if (user == null) {
            bot.sendPrivateMsg(event.getUserId(), "请先绑定账号", false);
            return;
        }
        String cookies = user.getCookies();

        String url = "http://jwcweb.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", cookies);
        request.form("xnm", year);
        request.form("xqm", terms[Integer.parseInt(term) - 1]);
        request.form("_search", "false");
        request.form("nd", "1668693695203");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "5");
        //发送请求
        HttpResponse response = request.execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject grade = JSON.parseObject(response.body());
        if (grade == null) {
            bot.sendPrivateMsg(event.getUserId(), "获取失败,Cookie失效或账号、密码错误、请重新登录账号。", false);
            return;
        }
        //获取成绩
        JSONArray gradeJSONArray = grade.getJSONArray("items");
        List<com.mikuac.shiro.entity.Grade> gradeList = new LinkedList<>();
        StringBuilder message = new StringBuilder();
        for (Object o : gradeJSONArray) {
            JSONObject jsonObject = (JSONObject) o;
            com.mikuac.shiro.entity.Grade grade1 = new com.mikuac.shiro.entity.Grade();
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
            grade1.setStudentname(jsonObject.getString("xm"));
            gradeList.add(grade1);
            message.append(grade1.getCoursename()).append("    ").append(grade1.getGrade()).append("\n");
        }
        bot.sendPrivateMsg(event.getUserId(), message.toString(), false);
        gradeService.saveOrUpdateBatch(gradeList);
    }

    @PrivateMessageHandler(cmd = "^更新获取成绩详情.*")
    public void fun2(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String[] yearAndTerm = event.getMessage().split(" ");
        String year = yearAndTerm[1];
        String term = yearAndTerm[2];
        User user = userService.getById(event.getUserId());
        if (user == null) {
            bot.sendPrivateMsg(event.getUserId(), "请先绑定账号", false);
            return;
        }
        String cookies = user.getCookies();

        String url = "http://jwcweb.lcu.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=2021405102";
        //headers添加到请求中
        HttpRequest request = HttpUtil.createPost(url).header("Cookie", cookies);
        request.form("xnm", year);
        request.form("xqm", terms[Integer.parseInt(term) - 1]);
        request.form("_search", "false");
        request.form("nd", "1668693695203");
        request.form("queryModel.showCount", "15");
        request.form("queryModel.currentPage", "1");
        request.form("queryModel.sortName", "");
        request.form("queryModel.sortOrder", "asc");
        request.form("time", "5");
        //发送请求
        HttpResponse response = request.execute();
        //用jsonfast2将result转换为JSONObject
        JSONObject grade = JSON.parseObject(response.body());
        if (grade == null) {
            bot.sendPrivateMsg(event.getUserId(), "获取失败,Cookie失效或账号、密码错误、请重新登录账号。", false);
            return;
        }
        //获取成绩
        JSONArray gradeJSONArray = grade.getJSONArray("items");
        List<com.mikuac.shiro.entity.Grade> gradeList = new LinkedList<>();
        StringBuilder message = new StringBuilder();
        StringBuilder message1 = new StringBuilder();
        for (Object o : gradeJSONArray) {
            JSONObject jsonObject = (JSONObject) o;
            com.mikuac.shiro.entity.Grade grade1 = new com.mikuac.shiro.entity.Grade();
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
            grade1.setStudentname(jsonObject.getString("xm"));
            gradeList.add(grade1);
            message.append(grade1.toString()).append("\n");
        }
        bot.sendPrivateMsg(event.getUserId(), message.toString(), false);
        gradeService.saveOrUpdateBatch(gradeList);
    }

    @PrivateMessageHandler(cmd = "^获取成绩.*")
    public void fun3(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) {
        String[] yearAndTerm = event.getMessage().split(" ");
        String year = yearAndTerm[1];
        String term = yearAndTerm[2];
        if (!"获取成绩".equals(yearAndTerm[0])) {
            return;
        }
        year = year + "-" + (Integer.parseInt(year) + 1);
        User user = userService.getById(event.getUserId());
        //查找数据库grade中的
        List<com.mikuac.shiro.entity.Grade> gradeList = gradeService.list(new QueryWrapper<com.mikuac.shiro.entity.Grade>().eq("studentnumber", user.getStudentnumber()).eq("year", year).eq("term", term));
        if (gradeList.size() == 0) {
            bot.sendPrivateMsg(event.getUserId(), "请先更新成绩", false);
            return;
        }
        StringBuilder message = new StringBuilder();
        for (com.mikuac.shiro.entity.Grade grade : gradeList) {
            message.append(grade.getCoursename()).append("    ").append(grade.getGrade()).append("\n");
        }
        // 调用 Bot 对象方法
        bot.sendPrivateMsg(event.getUserId(), String.valueOf(message), false);
    }

    @PrivateMessageHandler(cmd = "^获取成绩详情.*")
    public void fun4(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) {
        String[] yearAndTerm = event.getMessage().split(" ");
        String year = yearAndTerm[1];
        String term = yearAndTerm[2];
        year = year + "-" + (Integer.parseInt(year) + 1);
        User user = userService.getById(event.getUserId());
        //查找数据库grade中的
        List<com.mikuac.shiro.entity.Grade> gradeList = gradeService.list(new QueryWrapper<com.mikuac.shiro.entity.Grade>().eq("studentnumber", user.getStudentnumber()).eq("year", year).eq("term", term));
        if (gradeList.size() == 0) {
            bot.sendPrivateMsg(event.getUserId(), "请先更新成绩", false);
            return;
        }
        StringBuilder message = new StringBuilder();
        StringBuilder message1 = new StringBuilder();
        for (com.mikuac.shiro.entity.Grade grade : gradeList) {
            message.append(grade.toString()).append("\n");
        }
        // 调用 Bot 对象方法
        bot.sendPrivateMsg(event.getUserId(), String.valueOf(message), false);
    }
}
