package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.SysPostEntity;

import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysPostService extends IService<SysPostEntity>{

    PageUtil queryPage(Map<String ,Object> params);

    void savePost(SysPostEntity entity);

    void updatePost(SysPostEntity entity);

    void deletePatch(Long[] ids);
}
