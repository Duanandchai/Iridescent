package com.duan.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.duan.entity.TbOpc;
import java.util.List;
public interface OpcService extends IService<TbOpc> {



    //获取PLC1500节点下的数据
    List<TbOpc> getPLC1500Value();


    //获取系统运行节点下的数据
    List<TbOpc> getRunningValue();

    //获取系统告警节点下的数据
    List<TbOpc> getWarnValue();



}
