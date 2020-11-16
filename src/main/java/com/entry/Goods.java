package com.entry;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Goods {
    @TableId
    private int goodsid;
    private String goodsname;
    private String goodsimg;
    private String goodscontent;
    private int goodsprice;
    private int goodsstock;
}
