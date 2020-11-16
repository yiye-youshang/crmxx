package com.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.entry.KillGoods;

import java.util.List;

public interface KillGoodMapper extends BaseMapper<KillGoods> {
    //查询秒杀商品列表
    public List<KillGoods> goodslist();
    //查询某个商品详情
    public KillGoods goodsById(int id);

    //
    public int subKillGoodsByKillid(int killid);
}
