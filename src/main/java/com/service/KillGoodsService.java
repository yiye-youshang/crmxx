package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entry.KillGoods;

import java.util.List;

public interface KillGoodsService extends IService<KillGoods> {
    public List<KillGoods> selectAll();

    public KillGoods selectById(int id);

    public int killByKillid(int id);
}
