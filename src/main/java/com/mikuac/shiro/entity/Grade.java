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
public class Grade implements Serializable {

    @TableId
    private String id;

    private String studentnumber;

    private String coursename;

    private String courseproperty;

    private Float credit;

    private Integer grade;

    private Float gpa;

    private String teacher;

    private String term;

    private String year;

    private String studentname;

    @Override
    public String toString() {
        return
                "课程名：" + coursename +
                        ", 课程属性：" + courseproperty +
                        ", 学分：" + credit +
                        ", 成绩：" + grade +
                        ", 绩点：" + gpa +
                        ", 教师：" + teacher
                ;
    }
}

