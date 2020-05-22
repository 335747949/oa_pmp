package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.QueryUtil;
import com.lanwei.pmp.model.entity.SysDictEntity;
import com.lanwei.pmp.model.mapper.SysDictDao;
import com.lanwei.pmp.server.service.SysDictService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao,SysDictEntity>implements SysDictService{

    //分页模糊查询
    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        String name = params.get("name") != null ? (String) params.get("name") : "";

        IPage queryPage = new QueryUtil<SysDictEntity>().getQueryPage(params);
        QueryWrapper wrapper = new QueryWrapper<SysDictEntity>()
                .like(StringUtils.isNotBlank(name.trim()),"name",name.trim());
        IPage<SysDictEntity> page = page(queryPage,wrapper);

        return new PageUtil(page);
    }
}
