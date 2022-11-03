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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class ServeTableServiceImpl implements ServeTableService {

    private ServeTableMapper serveTableMapper;

    /**
     * 使用构造方法注入
     *
     * @param serveTableMapper
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
    public Boolean save(ServeTableParam serveTableParam) {
        ValidationUtils.validate(serveTableParam);
        ServeTable serveTable = BeanUtil.copyProperties(serveTableParam, ServeTable::new);
        if (serveTableParam.getId() != null) {
            return this.updateById(serveTable);
        }
        serveTableMapper.insert(serveTable);
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
        LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeTable::getId, ids);
        List<ServeTable> serveTableEntities = serveTableMapper.selectList(queryWrapper);
        //数据转换
        List<ServeTableVo> list = BeanUtil.copyListProperties(serveTableEntities, ServeTableVo::new);
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
        IPage<ServeTableVo> page = iPage.convert(serveTable -> BeanUtil.copyProperties(serveTable, ServeTableVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param serveTable 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(ServeTable serveTable) {
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
     * 传入多个Id 查询数据
     *
     * @param serveIds
     * @return
     */
    @Override
    public List<ServeTableVo> findByServeId(List<Long> serveIds){
        if (serveIds == null || serveIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeTable::getServeId, serveIds);
        List<ServeTable> serveTableEntities = serveTableMapper.selectList(queryWrapper);
        //数据转换
        List<ServeTableVo> list = BeanUtil.copyListProperties(serveTableEntities, ServeTableVo::new);
        return list;
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param tableMapIds
     * @return
     */
    @Override
    public List<ServeTableVo> findByTableMapId(List<Long> tableMapIds){
        if (tableMapIds == null || tableMapIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<ServeTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeTable::getTableMapId, tableMapIds);
        List<ServeTable> serveTableEntities = serveTableMapper.selectList(queryWrapper);
        //数据转换
        List<ServeTableVo> list = BeanUtil.copyListProperties(serveTableEntities, ServeTableVo::new);
        return list;
	}
}
