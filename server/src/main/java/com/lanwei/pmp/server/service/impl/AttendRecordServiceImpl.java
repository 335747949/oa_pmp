package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lanwei.pmp.common.utils.PageUtil;
import com.lanwei.pmp.common.utils.QueryUtil;
import com.lanwei.pmp.model.entity.AttendRecordEntity;
import com.lanwei.pmp.model.mapper.AttendRecordDao;
import com.lanwei.pmp.server.service.AttendRecordService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author lanwei
 * @email 335747949@qq.com
 */
@Service("attendRecordService")
public class AttendRecordServiceImpl extends ServiceImpl<AttendRecordDao,AttendRecordEntity>implements AttendRecordService{

    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        //分页查询 ~ 复杂的分页模糊查询 ~ 完全不同于以前的写法
        IPage page = new QueryUtil<AttendRecordEntity>().getQueryPage(params);

        List<AttendRecordEntity> list = baseMapper.queryPage(page,params);

        page.setRecords(list);

        return new PageUtil(page);
    }
}
