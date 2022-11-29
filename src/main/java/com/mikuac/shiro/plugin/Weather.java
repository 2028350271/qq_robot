package com.mikuac.shiro.plugin;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.mikuac.shiro.core.Bot;
import com.mikuac.shiro.core.BotPlugin;
import com.mikuac.shiro.dto.event.message.PrivateMessageEvent;
import com.mikuac.shiro.enums.WeatherEnum;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class Weather extends BotPlugin {
    @Override
    public int onPrivateMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event) {
        String message = event.getRawMessage();
        //分割字符串，空格间隔，空格前为城市名，空格后为天气类型

        String[] split = message.split(" ");
        if (!"天气".equals(split[0])) {
            return MESSAGE_IGNORE;
        }
        String city = split[1];
        String type = split[2];
        //日志输出
        log.info("城市名：" + city);
        log.info("天气类型：" + type);
        String url = "https://v0.yiketianqi.com/api?unescape=1&version=v91&appid=12475369&appsecret=1jmXlbvz&city={0}";
        url = url.replace("{0}", city);
        String result = HttpUtil.get(url);
        log.info("请求结果：" + result);
        JSONObject weather = JSON.parseObject(result);
        JSONArray weatherDataJsonArray = weather.getJSONArray("data");
        if (WeatherEnum.TODAY.getMessage().equals(type)) {
            sendWeatherMessage(bot, event, weatherDataJsonArray, 0);
        } else if (WeatherEnum.TOMORROW.getMessage().equals(type)) {
            //明天天气
            sendWeatherMessage(bot, event, weatherDataJsonArray, 1);
        } else if (WeatherEnum.AFTERTOMORROW.getMessage().equals(type)) {
            //后天天气
            sendWeatherMessage(bot, event, weatherDataJsonArray, 2);
        } else if (WeatherEnum.SEVENDAYS.getMessage().equals(type)) {
            //七天天气
            StringBuilder messageOne = new StringBuilder();
            for (int i = 0; i < 7; i++) {
                JSONObject weatherJson = (JSONObject) weatherDataJsonArray.get(i);
                String msg = weatherJson.get("day") + "\n" + weatherJson.get("wea") + "\n" + "当天最高温度" + weatherJson.get("tem1")
                        + "ºC。" + "\n" + "当天最低温度" + weatherJson.get("tem2") + "ºC。";
                if (i != 6) {
                    messageOne.append(msg).append("\n").append("\n");
                } else {
                    messageOne.append(msg);
                }
            }
            bot.sendPrivateMsg(event.getUserId(), messageOne.toString(), false);
        }
        //匹配type为“第n天天气”,n为0-6的数字
        else if (type.matches(WeatherEnum.SEVENDAY.getMessage())) {
            //第n天天气
            String day = type.substring(1, 2);
            log.info("第n天天气：" + day);
            sendWeatherMessage(bot, event, weatherDataJsonArray, Integer.parseInt(day));
        }
        return MESSAGE_IGNORE;
    }

    public void sendWeatherMessage(@NotNull Bot bot, @NotNull PrivateMessageEvent event, @NotNull JSONArray weatherDataJsonArray, int day) {
        JSONObject weatherJson = (JSONObject) weatherDataJsonArray.get(day);
        String msg = weatherJson.get("day") + "\n" + weatherJson.get("wea") + "\n" + "当前温度" + weatherJson.get("tem")
                + "ºC" + "\n" + "当天最高温度" + weatherJson.get("tem1") + "ºC。" + "\n" + "当天最低温度" + weatherJson.get("tem2") + "ºC。";
        bot.sendPrivateMsg(event.getUserId(), msg, false);
    }
}
