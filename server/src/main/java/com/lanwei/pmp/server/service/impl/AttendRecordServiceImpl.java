package com.lanwei.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lanwei.pmp.common.utils.Constant;
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

    //查询所有数据
    @Override
    public List<AttendRecordEntity> selectAll(Map<String, Object> params) {
        return baseMapper.selectExportData(params);
    }

    //中间站转换处理为导出服务
    @Override
    public List<Map<Integer, Object>> manageExport(List<AttendRecordEntity> list) {
        List<Map<Integer, Object>> listMap= Lists.newLinkedList();
        //"ID","部门名称","姓名","日期","打卡状态","打卡开始时间","打卡结束时间","工时/小时"

        if (list!=null && !list.isEmpty()){
            list.stream().forEach(entity -> {
                try {
                    Map<Integer,Object> rowMap= Maps.newHashMap();

                    rowMap.put(0,entity.getId());
                    rowMap.put(1,entity.getDeptName());
                    rowMap.put(2,entity.getUserName());
                    rowMap.put(3, Constant.DATE_FORMAT.format(entity.getCreateTime()));
                    Constant.AttendStatus status= Constant.AttendStatus.byCode(entity.getStatus().intValue());
                    rowMap.put(4,status!=null?status.getMsg():"空");
                    rowMap.put(5, Constant.DATE_TIME_FORMAT.format(entity.getStartTime()));
                    rowMap.put(6, Constant.DATE_TIME_FORMAT.format(entity.getEndTime()));
                    rowMap.put(7,entity.getTotal());

                    listMap.add(rowMap);
                }catch (Exception e){}
            });
        }

        return listMap;
    }
}
