package com.mikuac.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mikuac.shiro.dao.CourseDao;
import com.mikuac.shiro.entity.Course;
import com.mikuac.shiro.service.CourseService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class CourseServiceImpl extends ServiceImpl<CourseDao, Course> implements CourseService {
    @Resource
    private CourseDao courseDao;


}
