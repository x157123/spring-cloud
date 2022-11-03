package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.ConnectConfig;
import com.cloud.sync.vo.ConnectConfigVo;
import com.cloud.sync.mapper.ConnectConfigMapper;
import com.cloud.sync.query.ConnectConfigQuery;
import com.cloud.sync.service.ConnectConfigService;
import com.cloud.sync.param.ConnectConfigParam;
import com.cloud.sync.vo.ColumnConfigVo;
import com.cloud.sync.service.ColumnConfigService;
import com.cloud.sync.vo.ServeConfigVo;
import com.cloud.sync.service.ServeConfigService;
import com.cloud.sync.vo.TableConfigVo;
import com.cloud.sync.service.TableConfigService;
import com.cloud.sync.vo.TableMapVo;
import com.cloud.sync.service.TableMapService;
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
public class ConnectConfigServiceImpl implements ConnectConfigService {

    private ConnectConfigMapper connectConfigMapper;

    private ColumnConfigService columnConfigService;

    private ServeConfigService serveConfigService;

    private TableConfigService tableConfigService;

    private TableMapService tableMapService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigMapper
     * @param columnConfigService;
     * @param serveConfigService;
     * @param tableConfigService;
     * @param tableMapService;
     */
    public ConnectConfigServiceImpl(ConnectConfigMapper connectConfigMapper, ColumnConfigService columnConfigService, ServeConfigService serveConfigService, TableConfigService tableConfigService, TableMapService tableMapService){
        this.connectConfigMapper = connectConfigMapper;
        this.columnConfigService=columnConfigService;
        this.serveConfigService=serveConfigService;
        this.tableConfigService=tableConfigService;
        this.tableMapService=tableMapService;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(ConnectConfigParam connectConfigParam) {
        ValidationUtils.validate(connectConfigParam);
        ConnectConfig connectConfig = BeanUtil.copyProperties(connectConfigParam, ConnectConfig::new);
        if (connectConfigParam.getId() != null) {
            return this.updateById(connectConfig);
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
    public ConnectConfigVo findById(Long id) {
        List<ConnectConfigVo> connectConfigVos = this.findByIds(Collections.singletonList(id));
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
    public List<ConnectConfigVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<ConnectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConnectConfig::getId, ids);
        List<ConnectConfig> connectConfigEntities = connectConfigMapper.selectList(queryWrapper);
        //数据转换
        List<ConnectConfigVo> list = BeanUtil.copyListProperties(connectConfigEntities, ConnectConfigVo::new);
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
    public List<ConnectConfigVo> findByList(ConnectConfigQuery connectConfigQuery) {
        IPage<ConnectConfigVo> iPage = this.queryPage(connectConfigQuery, new PageParam());
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
        LambdaQueryWrapper<ConnectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConnectConfig::getId, ids);
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
    public IPage<ConnectConfigVo> queryPage(ConnectConfigQuery connectConfigQuery, PageParam pageParam) {
        IPage<ConnectConfig> iPage = connectConfigMapper.queryPage(OrderUtil.getPage(pageParam), connectConfigQuery);
        IPage<ConnectConfigVo> page = iPage.convert(connectConfig -> BeanUtil.copyProperties(connectConfig, ConnectConfigVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param connectConfig 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(ConnectConfig connectConfig) {
        LambdaQueryWrapper<ConnectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ConnectConfig::getId, connectConfig.getId())
                .eq(ConnectConfig::getVersion, connectConfig.getVersion());
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
    private void setParam(List<ConnectConfigVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(ConnectConfigVo::getId).collect(Collectors.toList());
            Map<Long, List<ColumnConfigVo>> columnConfigMap = columnConfigService.findByConnectId(ids).stream().collect(Collectors.groupingBy(ColumnConfigVo::getConnectId));
            Map<Long, List<ServeConfigVo>> serveConfigMap = serveConfigService.findByReadConnectId(ids).stream().collect(Collectors.groupingBy(ServeConfigVo::getReadConnectId));
            Map<Long, List<TableConfigVo>> tableConfigMap = tableConfigService.findByConnectId(ids).stream().collect(Collectors.groupingBy(TableConfigVo::getConnectId));
            Map<Long, List<TableMapVo>> tableMapMap = tableMapService.findByReadConnectId(ids).stream().collect(Collectors.groupingBy(TableMapVo::getReadConnectId));
            for (ConnectConfigVo connectConfig : list) {
                connectConfig.setColumnConfigVOList(columnConfigMap.get(connectConfig.getId()));
                connectConfig.setServeConfigVOList(serveConfigMap.get(connectConfig.getId()));
                connectConfig.setTableConfigVOList(tableConfigMap.get(connectConfig.getId()));
                connectConfig.setTableMapVOList(tableMapMap.get(connectConfig.getId()));
            }
        }
    }
}
