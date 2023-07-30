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
import com.cloud.sync.mapper.ConnectConfigMapper;
import com.cloud.sync.param.ConnectConfigParam;
import com.cloud.sync.query.ConnectConfigQuery;
import com.cloud.sync.service.ConnectConfigService;
import com.cloud.sync.service.ServeService;
import com.cloud.sync.vo.ConnectConfigVo;
import com.cloud.sync.vo.ServeVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    private final ConnectConfigMapper connectConfigMapper;

    private final ServeService serveService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigMapper 数据库配置Mapper服务
     */
    public ConnectConfigServiceImpl(ConnectConfigMapper connectConfigMapper, ServeService serveService) {
        this.connectConfigMapper = connectConfigMapper;
        this.serveService = serveService;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(ConnectConfigParam connectConfigParam) {
        ValidationUtils.validate(connectConfigParam);
        ConnectConfig connectConfig = BeanUtil.copyProperties(connectConfigParam, ConnectConfig::new);
        if (connectConfig != null && connectConfig.getId() != null) {
            this.update(connectConfig);
        } else {
            connectConfig.setVersion(DataVersionUtils.next());
            connectConfigMapper.insert(connectConfig);
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
     * @return 返回list结果
     */
    @Override
    public List<ConnectConfigVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<ConnectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConnectConfig::getId, ids);
        List<ConnectConfigVo> list = queryWrapper(queryWrapper);
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
        if (CollectionUtils.isEmpty(ids)) {
            return Boolean.FALSE;
        }
        LambdaQueryWrapper<ConnectConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ConnectConfig::getId, ids);
        connectConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param connectConfigQuery 查询条件
     * @param pageParam          分页条件
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
     * @return 更新成功状态
     */
    private Boolean update(ConnectConfig connectConfig) {
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
            Map<Long, List<ServeVo>> serveMap = serveService.findByWriteConnectId(ids).stream().collect(Collectors.groupingBy(ServeVo::getWriteConnectId));
            for (ConnectConfigVo connectConfig : list) {
                connectConfig.setServeVoList(serveMap.get(connectConfig.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return 返回转化后的数据
     */
    private List<ConnectConfigVo> queryWrapper(LambdaQueryWrapper<ConnectConfig> queryWrapper) {
        // 数据查询
        List<ConnectConfig> connectConfigEntities = connectConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(connectConfigEntities, ConnectConfigVo::new);
    }
}