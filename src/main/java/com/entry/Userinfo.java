package com.entry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Userinfo {
    @TableId(value = "userid", type = IdType.AUTO)
    private int userid;
    private String username;
    private String password;
    private String realname;
}
