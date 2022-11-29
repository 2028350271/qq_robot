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
public class User implements Serializable {

    @TableId
    private String qqnumber;

    private String studentnumber;

    private String password;

    private String cookies;


}

