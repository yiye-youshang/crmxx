package com.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.entry.OrderInfo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface OrderInfoService extends IService<OrderInfo> {

}
