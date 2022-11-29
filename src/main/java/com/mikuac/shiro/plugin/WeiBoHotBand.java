package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@Shiro
public class WeiBoHotBand {

    @PrivateMessageHandler(cmd = "微博热搜榜")
    public void getHotBand(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
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
        bot.sendPrivateMsg(event.getUserId(), stringBuilder.toString(), false);
    }

}
