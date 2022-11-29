package com.mikuac.shiro.entity;


import lombok.Data;

import java.io.Serializable;

@Data
public class ZhiHu implements Serializable {
    private String id;
    private String title;
    private String content;
    private String url;
    private String heatDegree;

    @Override
    public String toString() {
        return "热度排行：" + id + "\n" +
                "热度:" + heatDegree + "\n" +
                "标题：" + title + "\n" +
                "内容简要：" + content + "\n" +
                "地址：" + url + "\n"
                ;
    }

    public String toBriefString() {
        return "热度排行：" + id + "\n" +
                "热度:" + heatDegree + "\n" +
                "标题：" + title + "\n" +
                "地址：" + url + "\n"
                ;
    }
}
