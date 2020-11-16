package com.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@TableName(value = "killorderinfo")
public class KillOrderInfo {
    @TableId(value = "killorderid",type = IdType.AUTO)
    private int killorderid;
    private int userid;
    private int goodsid;
    private int orderid;
}
