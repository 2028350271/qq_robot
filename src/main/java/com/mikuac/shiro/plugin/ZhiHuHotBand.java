package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.action.common.ActionData;
import com.mikuac.shiro.dto.action.common.MsgId;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.ZhiHu;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

@Shiro
@Component
@EnableScheduling
@Slf4j
public class ZhiHuHotBand {
    @PrivateMessageHandler(cmd = "^知乎热榜.*")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String message = event.getMessage();
        //空格分割
        String[] split = message.split(" ");
        //获取第二个元素
        String s = split[1];

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

        //判断是否存在“-”
        if (s.contains("-")) {
            //存在“-”则分割
            String[] split3 = s.split("-");
            //获取第一个元素
            String s3 = split3[0];
            //获取第二个元素
            String s4 = split3[1];
            int start = Integer.parseInt(s3) - 1;
            int end = Integer.parseInt(s4) - 1;
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = start; i <= end; i++) {
                stringBuilder.append(zhiHuList.get(i).toBriefString()).append("\n");
            }
            ActionData<MsgId> actionData = bot.sendPrivateMsg(event.getUserId(), stringBuilder.toString(), false);
            if ("failed".equals(actionData.getStatus())) {
                bot.sendPrivateMsg(event.getUserId(), "发送失败,消息过长，请减小热榜范围", false);
            }
        } else if (s.contains("随机")) {
            int random = (int) (Math.random() * 50);
            bot.sendPrivateMsg(event.getUserId(), zhiHuList.get(random).toString(), false);
        } else {
            String index = split[1];
            bot.sendPrivateMsg(event.getUserId(), zhiHuList.get(Integer.parseInt(index) - 1).toString(), false);
        }
    }
}
