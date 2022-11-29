package com.mikuac.shiro.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
@Getter
@Setter
@Accessors(chain = true)
public class Course implements Serializable {

    @TableId
    private String id;

    private String studentnumber;

    private String coursename;

    private String site;

    private String section;

    private String teacher;

    private String day;

    private String openingweek;

    private String credit;

    private String courseProperty;

    @Override
    public String toString() {
        return
                "课程名：" + coursename + "\n" +
                        "地点：" + site + "\n" +
                        "节次" + section + "\n" +
                        "教师:" + teacher + "\n" +
                        "星期：" + "\n" +
                        "开课周" + "\n" +
                        "学分" + "\n" +
                        "课程属性" + courseProperty + "\n"
                ;
    }
}

