package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.model.entity.SysLogEntity;
import com.lanwei.pmp.model.mapper.SysLogDao;
import com.lanwei.pmp.server.service.SysLogService;
import org.springframework.stereotype.Service;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao,SysLogEntity> implements SysLogService{

}
