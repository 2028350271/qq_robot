package com.mikuac.shiro.plugin;


import cn.hutool.http.HttpResponse;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.User;
import com.mikuac.shiro.service.UserService;
import com.mikuac.shiro.utils.LoginUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


@Component
@Slf4j
public class LoginEduSystem extends BotPlugin {

    @Autowired
    private UserService userService;

    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String message = event.getRawMessage();
        //分割字符串，空格间隔，3个字符串
        String[] split = message.split(" ");
        String school = split[0];
        if (!"登录".equals(split[0])) {
            return MESSAGE_IGNORE;
        }
        String username = split[1];
        String password = split[2];
        HttpResponse response = LoginUtils.loginRequest(username, password);
        //获取http状态码
        if (response.getStatus() == 302) {
            String cookies = "route=" + response.getCookieValue("route") + ";" + "JSESSIONID=" + response.getCookieValue("JSESSIONID");
            //存储到数据库
            User user = new User();
            user.setQqnumber(String.valueOf(event.getUserId()));
            user.setCookies(cookies);
            user.setStudentnumber(username);
            user.setPassword(password);
            userService.saveOrUpdate(user);
            bot.sendPrivateMsg(event.getUserId(), "登录成功", false);
            return MESSAGE_IGNORE;
        } else {
            bot.sendPrivateMsg(event.getUserId(), "登录失败，账号或密码错误", false);
            System.out.println(response.body());
            return MESSAGE_IGNORE;
        }
    }
}
