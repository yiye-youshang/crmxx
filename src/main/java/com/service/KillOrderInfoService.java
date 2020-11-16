package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entry.KillOrderInfo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface KillOrderInfoService extends IService<KillOrderInfo> {
}
