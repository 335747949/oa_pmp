package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.SysLogEntity;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysLogService extends IService<SysLogEntity>{

        PageUtil queryPage(Map<String ,Object> params);

        void truncate();
}
