package com.duan.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 存数据库的实体类
 */
@Data
@TableName("tb_opc")
@AllArgsConstructor
@NoArgsConstructor
public class TbOpc {
    @TableId
    private int id;
    private String name;
    private String address;
    private String value;
    private String description;

    public TbOpc(String name, String address, String value, String description) {
        this.name = name;
        this.address = address;
        this.value = value;
        this.description = description;
    }
}
