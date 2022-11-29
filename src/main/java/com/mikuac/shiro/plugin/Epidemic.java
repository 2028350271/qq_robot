package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.annotation.PrivateMessageHandler;
import com.mikuac.shiro.annotation.Shiro;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.entity.EpidemicJson;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Shiro
@Component
@EnableScheduling
@Slf4j
public class Epidemic {

    @PrivateMessageHandler(cmd = "^疫情.*")
    public void fun1(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull Matcher matcher) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, InvalidKeySpecException, BadPaddingException, InvalidKeyException {
        String[] split = event.getMessage().split(" ");
        String city = split[1];
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
        if (provinceMap.get(city) != null) {
            String message = "新增本土" + provinceMap.get(city).getToday().getLocalConfirmAdd() + "例" + " " +
                    "新增本土无症状" + provinceMap.get(city).getToday().getWzzAdd() + "例" + " " +
                    "现有确诊" + provinceMap.get(city).getToday().getConfirm() + "例" + "\n" +
                    "累计确诊" + provinceMap.get(city).getTotal().getConfirm() + "例" + " " +
                    "累计治愈" + provinceMap.get(city).getTotal().getHeal() + "例" + " " +
                    "累计死亡" + provinceMap.get(city).getTotal().getDead() + "例";
            bot.sendPrivateMsg(event.getUserId(), message, false);
        } else {
            String message1 = "新增本土" + cityMap.get(city).getToday().getLocalConfirmAdd() + "例" + " " +
                    "新增本土无症状" + cityMap.get(city).getToday().getWzzAdd() + "例" + " " +
                    "现有确诊" + cityMap.get(city).getToday().getConfirm() + "例" + "\n" +
                    "累计确诊" + cityMap.get(city).getTotal().getConfirm() + "例" + " " +
                    "累计治愈" + cityMap.get(city).getTotal().getHeal() + "例" + " " +
                    "累计死亡" + cityMap.get(city).getTotal().getDead() + "例";
            bot.sendPrivateMsg(event.getUserId(), message1, false);
        }
    }
}
