package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpResponse;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.common.utils.MsgUtils;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.User;
import com.mikuac.shiro.service.UserService;
import com.mikuac.shiro.utils.LoginUtils;
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
import java.util.regex.Matcher;

@Shiro
@Component
@EnableScheduling
@Slf4j
public class ReLoginEduSystem {

    @Autowired
    private UserService userService;

    @PrivateMessageHandler(cmd = "重新登录")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        User user = userService.getById(event.getUserId());
        if(user == null){
            bot.sendPrivateMsg(event.getUserId(), MsgUtils.builder().text("你还没有绑定教务系统账号哦").build(), false);
            return;
        }
        HttpResponse response = LoginUtils.loginRequest(user.getStudentnumber(), user.getPassword());
        if (response.getStatus() == 302) {
            String cookies = "route=" + response.getCookieValue("route") + ";" + "JSESSIONID=" + response.getCookieValue("JSESSIONID");
            user.setCookies(cookies);
            userService.saveOrUpdate(user);
            bot.sendPrivateMsg(event.getUserId(), MsgUtils.builder().text("重新登录成功").build(), false);
        }
        else{
            bot.sendPrivateMsg(event.getUserId(), MsgUtils.builder().text("重新登录失败,账号或密码错误").build(), false);
        }
    }
}
