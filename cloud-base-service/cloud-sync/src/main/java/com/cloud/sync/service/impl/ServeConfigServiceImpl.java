package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.serveConfig;
import com.cloud.sync.vo.serveConfigVo;
import com.cloud.sync.mapper.serveConfigMapper;
import com.cloud.sync.query.serveConfigQuery;
import com.cloud.sync.service.serveConfigService;
import com.cloud.sync.param.serveConfigParam;
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
public class serveConfigServiceImpl implements serveConfigService {

    private final serveConfigMapper serveConfigMapper;

    private final serveTableService serveTableService;

    /**
     * 使用构造方法注入
     *
     * @param serveConfigMapper 同步启动服务Mapper服务
     * @param serveTableService  同步表Mapper服务
     */
    public serveConfigServiceImpl(serveConfigMapper serveConfigMapper, serveTableService serveTableService){
        this.serveConfigMapper = serveConfigMapper;
        this.serveTableService = serveTableService;
    }

    /**
     * 保存对象
     *
     * @param serveConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(serveConfigParam serveConfigParam) {
        ValidationUtils.validate(serveConfigParam);
        serveConfig serveConfig = BeanUtil.copyProperties(serveConfigParam, serveConfig::new);
        if (serveConfig != null && serveConfig.getId() != null) {
            LambdaQueryWrapper<serveConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(serveConfig::getId, serveConfig.getId())
                    .eq(serveConfig::getVersion, serveConfig.getVersion());
            serveConfig.setVersion(DataVersionUtils.next());
            return this.update(queryWrapper, serveConfig);
        }
        serveConfigMapper.insert(serveConfig);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public serveConfigVo findById(Long id) {
        List<serveConfigVo> serveConfigVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(serveConfigVos)) {
            return null;
        }
        return CollectionUtils.firstElement(serveConfigVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<serveConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<serveConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(serveConfig::getId, ids);
        List<serveConfigVo> list = queryWrapper(queryWrapper);
        //封装关联数据
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param serveConfigQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<serveConfigVo> findByList(serveConfigQuery serveConfigQuery) {
        IPage<serveConfigVo> iPage = this.queryPage(serveConfigQuery, new PageParam());
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
        LambdaQueryWrapper<serveConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(serveConfig::getId, ids);
        serveConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param serveConfigQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<serveConfigVo> queryPage(serveConfigQuery serveConfigQuery, PageParam pageParam) {
        IPage<serveConfig> iPage = serveConfigMapper.queryPage(OrderUtil.getPage(pageParam), serveConfigQuery);
        IPage<serveConfigVo> page = iPage.convert(serveConfig -> BeanUtil.copyProperties(serveConfig, serveConfigVo::new));
		this.setParam(page.getRecords());
        return page;
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param readConnectIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<serveConfigVo> findByreadConnectId(List<Long> readConnectIds){
        if (readConnectIds == null || readConnectIds.size() == 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<serveConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(serveConfig::getreadConnectId, readConnectIds);
        return queryWrapper(queryWrapper);
	}

    /**
     * 通过Id 更新数据
     *
     * @param serveConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(LambdaQueryWrapper<serveConfig> queryWrapper, serveConfig serveConfig) {
        serveConfig.setVersion(DataVersionUtils.next());
        int count = serveConfigMapper.update(serveConfig, queryWrapper);
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
    private void setParam(List<serveConfigVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(serveConfigVo::getId).collect(Collectors.toList());
            Map<Long, List<serveTableVo>> serveTableMap = serveTableService.findByserveId(ids).stream().collect(Collectors.groupingBy(serveTableVo::getserveId));
            for (serveConfigVo serveConfig : list) {
                serveConfig.setserveTableVOList(serveTableMap.get(serveConfig.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<serveConfigVo> queryWrapper(LambdaQueryWrapper<serveConfig> queryWrapper){
        // 数据查询
        List<serveConfig> serveConfigEntities = serveConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(serveConfigEntities, serveConfigVo::new);
    }
}