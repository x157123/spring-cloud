package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.ServeTable;
import com.cloud.sync.vo.ServeTableVo;
import com.cloud.sync.mapper.ServeTableMapper;
import com.cloud.sync.query.ServeTableQuery;
import com.cloud.sync.service.ServeTableService;
import com.cloud.sync.param.ServeTableParam;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class ServeTableServiceImpl implements ServeTableService {

    private final ServeTableMapper serveTableMapper;

    /**
     * 使用构造方法注入
     *
     * @param serveTableMapper 同步表Mapper服务
     */
    public ServeTableServiceImpl(ServeTableMapper serveTableMapper){
        this.serveTableMapper = serveTableMapper;
    }

    /**
     * 保存对象
     *
     * @param serveTableParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(ServeTableParam serveTableParam) {
        ValidationUtils.validate(serveTableParam);
        ServeTable serveTable = BeanUtil.copyProperties(serveTableParam, ServeTable::new);
        if (serveTable != null && serveTable.getId() != null) {
            this.update(serveTable);
        }else{
            serveTable.setVersion(DataVersionUtils.next());
            serveTableMapper.insert(serveTable);
        }
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ServeTableVo findById(Long id) {
        List<ServeTableVo> serveTableVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(serveTableVos)) {
            return null;
        }
        return CollectionUtils.firstElement(serveTableVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<ServeTableVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeTable::getId, ids);
        List<ServeTableVo> list = queryWrapper(queryWrapper);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param serveTableQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<ServeTableVo> findByList(ServeTableQuery serveTableQuery) {
        IPage<ServeTableVo> iPage = this.queryPage(serveTableQuery, new PageParam());
        return iPage.getRecords();
    }

    /**
     * 传入多个Id 并删除
     *
     * @param ids id集合
     * @return 删除情况状态
     */
    @Override
    public Boolean removeByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeTable::getId, ids);
        serveTableMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param serveTableQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<ServeTableVo> queryPage(ServeTableQuery serveTableQuery, PageParam pageParam) {
        IPage<ServeTable> iPage = serveTableMapper.queryPage(OrderUtil.getPage(pageParam), serveTableQuery);
        return iPage.convert(serveTable -> BeanUtil.copyProperties(serveTable, ServeTableVo::new));
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param serveIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<ServeTableVo> findByServeId(List<Long> serveIds){
        if (serveIds == null || serveIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ServeTableVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(serveIds, 5000);
        for(List<Long> list : subLists) {
            queryWrapper.in(ServeTable::getServeId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param tableMapIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<ServeTableVo> findByTableMapId(List<Long> tableMapIds){
        if (tableMapIds == null || tableMapIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ServeTableVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(tableMapIds, 5000);
        for(List<Long> list : subLists) {
            queryWrapper.in(ServeTable::getTableMapId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }

    /**
     * 通过Id 更新数据
     *
     * @param serveTable 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(ServeTable serveTable) {
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServeTable::getId, serveTable.getId())
                .eq(ServeTable::getVersion, serveTable.getVersion());
        serveTable.setVersion(DataVersionUtils.next());
        int count = serveTableMapper.update(serveTable, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }


    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<ServeTableVo> queryWrapper(LambdaQueryWrapper<ServeTable> queryWrapper){
        // 数据查询
        List<ServeTable> serveTableEntities = serveTableMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(serveTableEntities, ServeTableVo::new);
    }
}