package com.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
@TableName(value = "killgoods")
public class KillGoods {
    @TableId(value = "killid",type = IdType.AUTO)
    private int killid;
    private int goodsid;
    private int stockcount;
    private Date startdate;
    private Date enddate;
    private int killprice;

    @TableField(value = "goodsname",exist = false)
    private String goodsname;
    @TableField(value = "goodsimg",exist = false)
    private String goodsimg;
    @TableField(value = "goodsprice",exist = false)
    private double goodsprice;

}
