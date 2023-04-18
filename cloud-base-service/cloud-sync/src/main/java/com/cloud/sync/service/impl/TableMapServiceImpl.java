package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.tableMap;
import com.cloud.sync.vo.tableMapVo;
import com.cloud.sync.mapper.tableMapMapper;
import com.cloud.sync.query.tableMapQuery;
import com.cloud.sync.service.tableMapService;
import com.cloud.sync.param.tableMapParam;
import com.cloud.sync.vo.serveTableVo;
import com.cloud.sync.service.serveTableService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class tableMapServiceImpl implements tableMapService {

    private final tableMapMapper tableMapMapper;

    private final serveTableService serveTableService;

    /**
     * 使用构造方法注入
     *
     * @param tableMapMapper 表映射Mapper服务
     * @param serveTableService  同步表Mapper服务
     */
    public tableMapServiceImpl(tableMapMapper tableMapMapper, serveTableService serveTableService){
        this.tableMapMapper = tableMapMapper;
        this.serveTableService = serveTableService;
    }

    /**
     * 保存对象
     *
     * @param tableMapParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(tableMapParam tableMapParam) {
        ValidationUtils.validate(tableMapParam);
        tableMap tableMap = BeanUtil.copyProperties(tableMapParam, tableMap::new);
        if (tableMap != null && tableMap.getId() != null) {
            LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(tableMap::getId, tableMap.getId())
                    .eq(tableMap::getVersion, tableMap.getVersion());
            tableMap.setVersion(DataVersionUtils.next());
            return this.update(queryWrapper, tableMap);
        }
        tableMapMapper.insert(tableMap);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public tableMapVo findById(Long id) {
        List<tableMapVo> tableMapVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(tableMapVos)) {
            return null;
        }
        return CollectionUtils.firstElement(tableMapVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<tableMapVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getId, ids);
        List<tableMapVo> list = queryWrapper(queryWrapper);
        //封装关联数据
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param tableMapQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<tableMapVo> findByList(tableMapQuery tableMapQuery) {
        IPage<tableMapVo> iPage = this.queryPage(tableMapQuery, new PageParam());
        //封装关联数据
		this.setParam(iPage.getRecords());
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
        LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getId, ids);
        tableMapMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tableMapQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<tableMapVo> queryPage(tableMapQuery tableMapQuery, PageParam pageParam) {
        IPage<tableMap> iPage = tableMapMapper.queryPage(OrderUtil.getPage(pageParam), tableMapQuery);
        IPage<tableMapVo> page = iPage.convert(tableMap -> BeanUtil.copyProperties(tableMap, tableMapVo::new));
		this.setParam(page.getRecords());
        return page;
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param writeConnectIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<tableMapVo> findBywriteConnectId(List<Long> writeConnectIds){
        if (writeConnectIds == null || writeConnectIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getwriteConnectId, writeConnectIds);
        return queryWrapper(queryWrapper);
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param writeTableIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<tableMapVo> findBywriteTableId(List<Long> writeTableIds){
        if (writeTableIds == null || writeTableIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getwriteTableId, writeTableIds);
        return queryWrapper(queryWrapper);
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param readTableIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<tableMapVo> findByreadTableId(List<Long> readTableIds){
        if (readTableIds == null || readTableIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getreadTableId, readTableIds);
        return queryWrapper(queryWrapper);
	}
	
	/**
     * 传入多个Id 查询数据
     *
     * @param readConnectIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<tableMapVo> findByreadConnectId(List<Long> readConnectIds){
        if (readConnectIds == null || readConnectIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<tableMap> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(tableMap::getreadConnectId, readConnectIds);
        return queryWrapper(queryWrapper);
	}

    /**
     * 通过Id 更新数据
     *
     * @param tableMap 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(LambdaQueryWrapper<tableMap> queryWrapper, tableMap tableMap) {
        tableMap.setVersion(DataVersionUtils.next());
        int count = tableMapMapper.update(tableMap, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<tableMapVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(tableMapVo::getId).collect(Collectors.toList());
            Map<Long, List<serveTableVo>> serveTableMap = serveTableService.findBytableMapId(ids).stream().collect(Collectors.groupingBy(serveTableVo::gettableMapId));
            for (tableMapVo tableMap : list) {
                tableMap.setserveTableVOList(serveTableMap.get(tableMap.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<tableMapVo> queryWrapper(LambdaQueryWrapper<tableMap> queryWrapper){
        // 数据查询
        List<tableMap> tableMapEntities = tableMapMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(tableMapEntities, tableMapVo::new);
    }
}