package com.mikuac.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mikuac.shiro.dao.HuffmanCodeDao;
import com.mikuac.shiro.entity.HuffmanCode;
import com.mikuac.shiro.service.HuffmanCodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class HuffmanCodeServiceImpl extends ServiceImpl<HuffmanCodeDao, HuffmanCode> implements HuffmanCodeService {
    @Resource
    private HuffmanCodeDao huffmanCodeDao;
}
