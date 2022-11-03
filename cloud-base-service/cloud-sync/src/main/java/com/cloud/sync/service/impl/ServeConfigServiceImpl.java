package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.ServeConfig;
import com.cloud.sync.vo.ServeConfigVo;
import com.cloud.sync.mapper.ServeConfigMapper;
import com.cloud.sync.query.ServeConfigQuery;
import com.cloud.sync.service.ServeConfigService;
import com.cloud.sync.param.ServeConfigParam;
import com.cloud.sync.vo.ServeTableVo;
import com.cloud.sync.service.ServeTableService;
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
public class ServeConfigServiceImpl implements ServeConfigService {

    private ServeConfigMapper serveConfigMapper;

    private ServeTableService serveTableService;

    /**
     * 使用构造方法注入
     *
     * @param serveConfigMapper
     * @param serveTableService;
     */
    public ServeConfigServiceImpl(ServeConfigMapper serveConfigMapper, ServeTableService serveTableService){
        this.serveConfigMapper = serveConfigMapper;
        this.serveTableService=serveTableService;
    }

    /**
     * 保存对象
     *
     * @param serveConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(ServeConfigParam serveConfigParam) {
        ValidationUtils.validate(serveConfigParam);
        ServeConfig serveConfig = BeanUtil.copyProperties(serveConfigParam, ServeConfig::new);
        if (serveConfigParam.getId() != null) {
            return this.updateById(serveConfig);
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
    public ServeConfigVo findById(Long id) {
        List<ServeConfigVo> serveConfigVos = this.findByIds(Collections.singletonList(id));
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
    public List<ServeConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeConfig::getId, ids);
        List<ServeConfig> serveConfigEntities = serveConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ServeConfigVo> list = BeanUtil.copyListProperties(serveConfigEntities, ServeConfigVo::new);
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
    public List<ServeConfigVo> findByList(ServeConfigQuery serveConfigQuery) {
        IPage<ServeConfigVo> iPage = this.queryPage(serveConfigQuery, new PageParam());
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
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeConfig::getId, ids);
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
    public IPage<ServeConfigVo> queryPage(ServeConfigQuery serveConfigQuery, PageParam pageParam) {
        IPage<ServeConfig> iPage = serveConfigMapper.queryPage(OrderUtil.getPage(pageParam), serveConfigQuery);
        IPage<ServeConfigVo> page = iPage.convert(serveConfig -> BeanUtil.copyProperties(serveConfig, ServeConfigVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param serveConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(ServeConfig serveConfig) {
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServeConfig::getId, serveConfig.getId())
                .eq(ServeConfig::getVersion, serveConfig.getVersion());
        serveConfig.setVersion(DataVersionUtils.next());
        int count = serveConfigMapper.update(serveConfig, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     *
     * @param readConnectIds
     * @return
     */
    @Override
    public List<ServeConfigVo> findByReadConnectId(List<Long> readConnectIds){
        if (readConnectIds == null || readConnectIds.size() <= 0) {
            return new ArrayList<>();
        }
		LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeConfig::getReadConnectId, readConnectIds);
        List<ServeConfig> serveConfigEntities = serveConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ServeConfigVo> list = BeanUtil.copyListProperties(serveConfigEntities, ServeConfigVo::new);
        return list;
	}

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<ServeConfigVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(ServeConfigVo::getId).collect(Collectors.toList());
            Map<Long, List<ServeTableVo>> serveTableMap = serveTableService.findByServeId(ids).stream().collect(Collectors.groupingBy(ServeTableVo::getServeId));
            for (ServeConfigVo serveConfig : list) {
                serveConfig.setServeTableVOList(serveTableMap.get(serveConfig.getId()));
            }
        }
    }
}
