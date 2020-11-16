package com.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.entry.KillGoods;
import com.repository.KillGoodMapper;
import com.service.KillGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class KillGoodsServiceImpl extends ServiceImpl<KillGoodMapper, KillGoods> implements KillGoodsService {
    @Autowired(required = false)
    KillGoodMapper mapper;
    @Override
    public List<KillGoods> selectAll() {
        return mapper.goodslist();
    }

    @Override
    public KillGoods selectById(int id) {
        return mapper.goodsById(id);
    }

    @Override
    public int killByKillid(int id) {
        return mapper.subKillGoodsByKillid(id);
    }
}
