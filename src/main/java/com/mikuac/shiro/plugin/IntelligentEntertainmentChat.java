package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.GroupMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class IntelligentEntertainmentChat extends BotPlugin {
    @Override
    public int onGroupMessage(@NotNull Bot bot, @NotNull GroupMessageEvent event) {
        String url = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=关键词";
        String message = event.getMessage();
        //把URl中的关键词替换成用户发送的消息
        String url1 = url.replace("关键词", message);
        //发送请求
        String s = HttpUtil.get(url1);
        //变成json格式
        JSONObject jsonObject = JSON.parseObject(s);
        //获取“content”
        String content = jsonObject.getString("content");
        //发送消息
        bot.sendGroupMsg(event.getGroupId(), content, false);
        return MESSAGE_IGNORE;
    }
}
