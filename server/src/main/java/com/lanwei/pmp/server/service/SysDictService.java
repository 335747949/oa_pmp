package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.SysDictEntity;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysDictService extends IService<SysDictEntity>{

    PageUtil queryPage(Map<String ,Object> params);

}
