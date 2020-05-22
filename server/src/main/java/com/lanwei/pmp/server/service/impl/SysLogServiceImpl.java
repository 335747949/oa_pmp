package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.QueryUtil;
import com.lanwei.pmp.model.entity.SysLogEntity;
import com.lanwei.pmp.model.mapper.SysLogDao;
import com.lanwei.pmp.server.service.SysLogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysLogService")
public class SysLogServiceImpl extends ServiceImpl<SysLogDao,SysLogEntity> implements SysLogService{

    //分页列表模糊查询
    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        String key = (params.get("key")!=null)? (String)params.get("key").toString() : "";

        IPage queryPage = new QueryUtil<SysLogEntity>().getQueryPage(params);
        QueryWrapper wrapper = new QueryWrapper<SysLogEntity>()
                .like(StringUtils.isNotBlank(key.trim()),"username",key.trim())
                .or(StringUtils.isNotBlank(key.trim()))
                .like(StringUtils.isNotBlank(key.trim()),"operation",key.trim());

        IPage resPage = page(queryPage,wrapper);

        return new PageUtil(resPage);
    }

    @Override
    public void truncate() {
        baseMapper.truncate();
    }
}
