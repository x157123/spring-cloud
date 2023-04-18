package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.connectConfig;
import com.cloud.sync.vo.connectConfigVo;
import com.cloud.sync.mapper.connectConfigMapper;
import com.cloud.sync.query.connectConfigQuery;
import com.cloud.sync.service.connectConfigService;
import com.cloud.sync.param.connectConfigParam;
import com.cloud.sync.vo.columnConfigVo;
import com.cloud.sync.service.columnConfigService;
import com.cloud.sync.vo.serveConfigVo;
import com.cloud.sync.service.serveConfigService;
import com.cloud.sync.vo.tableConfigVo;
import com.cloud.sync.service.tableConfigService;
import com.cloud.sync.vo.tableMapVo;
import com.cloud.sync.service.tableMapService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class connectConfigServiceImpl implements connectConfigService {

    private final connectConfigMapper connectConfigMapper;

    private final columnConfigService columnConfigService;

    private final serveConfigService serveConfigService;

    private final tableConfigService tableConfigService;

    private final tableMapService tableMapService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigMapper 数据库配置Mapper服务
     * @param columnConfigService  同步数据库列配置Mapper服务
     * @param serveConfigService  同步启动服务Mapper服务
     * @param tableConfigService  同步表配置Mapper服务
     * @param tableMapService  表映射Mapper服务
     */
    public connectConfigServiceImpl(connectConfigMapper connectConfigMapper, columnConfigService columnConfigService, serveConfigService serveConfigService, tableConfigService tableConfigService, tableMapService tableMapService){
        this.connectConfigMapper = connectConfigMapper;
        this.columnConfigService = columnConfigService;
        this.serveConfigService = serveConfigService;
        this.tableConfigService = tableConfigService;
        this.tableMapService = tableMapService;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(connectConfigParam connectConfigParam) {
        ValidationUtils.validate(connectConfigParam);
        connectConfig connectConfig = BeanUtil.copyProperties(connectConfigParam, connectConfig::new);
        if (connectConfig != null && connectConfig.getId() != null) {
            LambdaQueryWrapper<connectConfig> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(connectConfig::getId, connectConfig.getId())
                    .eq(connectConfig::getVersion, connectConfig.getVersion());
            connectConfig.setVersion(DataVersionUtils.next());
            return this.update(queryWrapper, connectConfig);
        }
        connectConfigMapper.insert(connectConfig);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public connectConfigVo findById(Long id) {
        List<connectConfigVo> connectConfigVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(connectConfigVos)) {
            return null;
        }
        return CollectionUtils.firstElement(connectConfigVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<connectConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<connectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(connectConfig::getId, ids);
        List<connectConfigVo> list = queryWrapper(queryWrapper);
        //封装关联数据
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param connectConfigQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<connectConfigVo> findByList(connectConfigQuery connectConfigQuery) {
        IPage<connectConfigVo> iPage = this.queryPage(connectConfigQuery, new PageParam());
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
        LambdaQueryWrapper<connectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(connectConfig::getId, ids);
        connectConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param connectConfigQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<connectConfigVo> queryPage(connectConfigQuery connectConfigQuery, PageParam pageParam) {
        IPage<connectConfig> iPage = connectConfigMapper.queryPage(OrderUtil.getPage(pageParam), connectConfigQuery);
        IPage<connectConfigVo> page = iPage.convert(connectConfig -> BeanUtil.copyProperties(connectConfig, connectConfigVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param connectConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(LambdaQueryWrapper<connectConfig> queryWrapper, connectConfig connectConfig) {
        connectConfig.setVersion(DataVersionUtils.next());
        int count = connectConfigMapper.update(connectConfig, queryWrapper);
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
    private void setParam(List<connectConfigVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(connectConfigVo::getId).collect(Collectors.toList());
            Map<Long, List<columnConfigVo>> columnConfigMap = columnConfigService.findByconnectId(ids).stream().collect(Collectors.groupingBy(columnConfigVo::getconnectId));
            Map<Long, List<serveConfigVo>> serveConfigMap = serveConfigService.findByreadConnectId(ids).stream().collect(Collectors.groupingBy(serveConfigVo::getreadConnectId));
            Map<Long, List<tableConfigVo>> tableConfigMap = tableConfigService.findByconnectId(ids).stream().collect(Collectors.groupingBy(tableConfigVo::getconnectId));
            Map<Long, List<tableMapVo>> tableMapMap = tableMapService.findByreadConnectId(ids).stream().collect(Collectors.groupingBy(tableMapVo::getreadConnectId));
            for (connectConfigVo connectConfig : list) {
                connectConfig.setcolumnConfigVOList(columnConfigMap.get(connectConfig.getId()));
                connectConfig.setserveConfigVOList(serveConfigMap.get(connectConfig.getId()));
                connectConfig.settableConfigVOList(tableConfigMap.get(connectConfig.getId()));
                connectConfig.settableMapVOList(tableMapMap.get(connectConfig.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<connectConfigVo> queryWrapper(LambdaQueryWrapper<connectConfig> queryWrapper){
        // 数据查询
        List<connectConfig> connectConfigEntities = connectConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(connectConfigEntities, connectConfigVo::new);
    }
}