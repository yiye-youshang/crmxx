package com.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName(value = "Orderinfo")
public class OrderInfo {
    @TableId(value = "orderid",type = IdType.AUTO)
    private int orderid;
    private int userid;
    private int goodsid;
    private String goodsname;
    private int goodscount;
    private int goodsprice;
    private int orderstatus;
    private Date createdate;
    private Date paydate;

}
