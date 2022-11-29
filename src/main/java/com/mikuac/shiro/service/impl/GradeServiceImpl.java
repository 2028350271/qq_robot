package com.mikuac.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mikuac.shiro.dao.GradeDao;
import com.mikuac.shiro.entity.Grade;
import com.mikuac.shiro.service.GradeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class GradeServiceImpl extends ServiceImpl<GradeDao, Grade> implements GradeService {
    @Resource
    private GradeDao gradeDao;

}
