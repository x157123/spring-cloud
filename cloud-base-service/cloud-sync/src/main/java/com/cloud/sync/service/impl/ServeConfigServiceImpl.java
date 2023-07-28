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
import com.cloud.sync.mapper.ServeConfigMapper;
import com.cloud.sync.param.ServeConfigParam;
import com.cloud.sync.query.ServeConfigQuery;
import com.cloud.sync.service.ServeConfigService;
import com.cloud.sync.vo.ServeConfigVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class ServeConfigServiceImpl implements ServeConfigService {

    private final ServeConfigMapper serveConfigMapper;

    /**
     * 使用构造方法注入
     *
     * @param serveConfigMapper 同步启动服务Mapper服务
     */
    public ServeConfigServiceImpl(ServeConfigMapper serveConfigMapper) {
        this.serveConfigMapper = serveConfigMapper;
    }

    /**
     * 保存对象
     *
     * @param serveConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(ServeConfigParam serveConfigParam) {
        ValidationUtils.validate(serveConfigParam);
        ServeConfigVo serveConfigVo = this.findByServerId(serveConfigParam.getServeId());
        ServeConfig serveConfig = BeanUtil.copyProperties(serveConfigVo, ServeConfig::new);
        if (serveConfig != null && serveConfig.getId() != null) {
            if (serveConfigParam.getOffSet() != null) {
                serveConfig.setOffSet(serveConfigParam.getOffSet());
            }
            if (serveConfigParam.getState() != null) {
                serveConfig.setState(serveConfigParam.getState());
            }
            this.update(serveConfig);
        } else {
            serveConfig = BeanUtil.copyProperties(serveConfigParam, ServeConfig::new);
            serveConfig.setVersion(DataVersionUtils.next());
            serveConfig.setState(1);
            serveConfigMapper.insert(serveConfig);
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
     * @return 返回list结果
     */
    @Override
    public List<ServeConfigVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeConfig::getId, ids);
        List<ServeConfigVo> list = queryWrapper(queryWrapper);
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
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(ServeConfig::getId, ids);
        serveConfigMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param serveConfigQuery 查询条件
     * @param pageParam        分页条件
     * @return 分页数据
     */
    @Override
    public IPage<ServeConfigVo> queryPage(ServeConfigQuery serveConfigQuery, PageParam pageParam) {
        IPage<ServeConfig> iPage = serveConfigMapper.queryPage(OrderUtil.getPage(pageParam), serveConfigQuery);
        return iPage.convert(serveConfig -> BeanUtil.copyProperties(serveConfig, ServeConfigVo::new));
    }

    /**
     * 通过服务名获取配置
     *
     * @param serveId
     * @return
     */
    @Override
    public ServeConfigVo findByServerId(Long serveId) {
        LambdaQueryWrapper<ServeConfig> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ServeConfig::getServeId, serveId);
        List<ServeConfigVo> serveConfigVos = queryWrapper(queryWrapper);
        if (CollectionUtils.isEmpty(serveConfigVos)) {
            return null;
        }
        return CollectionUtils.firstElement(serveConfigVos);
    }

    /**
     * 更新状态
     *
     * @param serveId
     * @param state
     */
    @Override
    public void state(Long serveId, int state) {
        ServeConfigParam serveConfigParam = new ServeConfigParam();
        serveConfigParam.setServeId(serveId);
        serveConfigParam.setState(state);
        this.save(serveConfigParam);
    }

    /**
     * 通过Id 更新数据
     *
     * @param serveConfig 前端更新集合
     * @return 更新成功状态
     */
    private Boolean update(ServeConfig serveConfig) {
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
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return 返回转化后的数据
     */
    private List<ServeConfigVo> queryWrapper(LambdaQueryWrapper<ServeConfig> queryWrapper) {
        // 数据查询
        List<ServeConfig> serveConfigEntities = serveConfigMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(serveConfigEntities, ServeConfigVo::new);
    }
}