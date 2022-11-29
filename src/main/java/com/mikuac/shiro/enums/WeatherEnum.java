package com.mikuac.shiro.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum WeatherEnum {
    /**
     * 今日天气
     */
    TODAY(0, "今天天气"),
    /**
     * 明日天气
     */
    TOMORROW(1, "明天天气"),
    /**
     * 后天天气
     */
    AFTERTOMORROW(2, "后天天气"),
    /**
     * 七天天气
     */
    SEVENDAYS(3, "七天天气"),
    /**
     * 七天其中一天天气
     */
    SEVENDAY(4, "第[0-6]天天气");
    private final int code;
    private final String message;

}
