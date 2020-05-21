package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.model.entity.SysUserPostEntity;
import javassist.bytecode.LineNumberAttribute;

import java.util.List;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface SysUserPostservice extends IService<SysUserPostEntity>{

    String getPostNameByUserId(Long userId);

    void saveOrUpdate(Long userId, List<Long> postId);

    List<Long> queryPostList(Long userId);
}
