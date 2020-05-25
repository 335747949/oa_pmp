package com.lanwei.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.model.entity.AttendRecordEntity;

import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
public interface AttendRecordService extends IService<AttendRecordEntity>{

    PageUtil queryPage(Map<String,Object> params);

    List<AttendRecordEntity> selectAll(Map<String, Object> params);

    List<Map<Integer, Object>> manageExport(List<AttendRecordEntity> list);
}
