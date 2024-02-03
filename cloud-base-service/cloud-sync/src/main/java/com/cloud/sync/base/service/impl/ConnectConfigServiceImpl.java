package com.cloud.sync.base.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.base.service.ConnectConfigService;
import com.cloud.sync.base.service.JoinTableService;
import com.cloud.sync.base.service.ServeService;
import com.cloud.sync.entity.ConnectConfig;
import com.cloud.sync.base.mapper.ConnectConfigMapper;
import com.cloud.sync.param.ConnectConfigParam;
import com.cloud.sync.query.ConnectConfigQuery;
import com.cloud.sync.vo.ConnectConfigVo;
import com.cloud.sync.vo.JoinTableVo;
import com.cloud.sync.vo.ServeVo;
import com.cloud.sync.writer.ColumnData;
import com.cloud.sync.writer.DBUtil;
import com.cloud.sync.writer.DataBaseType;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.sql.Connection;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class ConnectConfigServiceImpl implements ConnectConfigService {

    private final ConnectConfigMapper connectConfigMapper;

    private final JoinTableService joinTableService;

    private final ServeService serveService;

    /**
     * 使用构造方法注入
     *
     * @param connectConfigMapper 数据库配置Mapper服务
     */
    public ConnectConfigServiceImpl(ConnectConfigMapper connectConfigMapper, JoinTableService joinTableService, ServeService serveService) {
        this.connectConfigMapper = connectConfigMapper;
        this.joinTableService = joinTableService;
        this.serveService = serveService;
    }

    /**
     * 获取数据库表信息
     *
     * @param connectId
     * @return
     */
    @Override
    public JSONArray getTables(Long connectId) {
        JSONArray jsonArray = new JSONArray();
        ConnectConfigVo connectConfigParam = this.findById(connectId);
        if (connectConfigParam == null) {
            return jsonArray;
        }
        HikariDataSource dataSource = DBUtil.getDataSource(
                DataBaseType.getDataBaseType(connectConfigParam.getType()).getJdbcUrl(connectConfigParam.getHostname(), connectConfigParam.getPort(), connectConfigParam.getDatabaseName())
                , connectConfigParam.getUser(), connectConfigParam.getPassword(), DataBaseType.getDataBaseType(connectConfigParam.getType()).getDriverClassName());
        if (dataSource != null) {
            try {
                Connection connection = dataSource.getConnection();
                if (dataSource.getConnection() != null) {
                    List<String> tables = DBUtil.getTables(connection);
                    for (String str : tables) {
                        List<ColumnData> columnDataList = DBUtil.getColumnDatas(connection, str);
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("name", str);
                        jsonObject.put("column", columnDataList);
                        jsonArray.add(jsonObject);
                    }
                    DBUtil.closeDBResources(null, connection);
                }
            } catch (Exception e) {
                throw new DataException("数据库连接错误");
            } finally {
                dataSource.close();
            }
        }
        return jsonArray;
    }

    /**
     * 保存对象
     *
     * @param connectConfigParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean test(ConnectConfigParam connectConfigParam) {
        HikariDataSource dataSource = DBUtil.getDataSource(
                DataBaseType.getDataBaseType(connectConfigParam.getType()).getJdbcUrl(connectConfigParam.getHostname(), connectConfigParam.getPort(), connectConfigParam.getDatabaseName())
                , connectConfigParam.getUser(), connectConfigParam.getPassword(), DataBaseType.getDataBaseType(connectConfigParam.getType()).getDriverClassName());
        if (dataSource != null) {
            try {
                Connection connection = dataSource.getConnection();
                if (dataSource.getConnection() != null) {
                    DBUtil.closeDBResources(null, connection);
                }
            } catch (Exception e) {
                throw new DataException("数据库连接错误");
            } finally {
                dataSource.close();
            }
            return true;
        }
        return false;
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
            connectConfig.setUpdateDate(new Date());
            this.update(connectConfig);
        } else {
            connectConfig.setVersion(DataVersionUtils.next());
            connectConfig.setCreateDate(new Date());
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
        Integer count = connectConfigMapper.delete(queryWrapper);
        if (count < ids.size()) {
            throw new DataException("删除一条数据错误");
        }
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
            Map<Long, List<JoinTableVo>> joinTableMap = joinTableService.findByConnectId(ids).stream().collect(Collectors.groupingBy(JoinTableVo::getConnectId));
            Map<Long, List<ServeVo>> serveMap = serveService.findByReadConnectId(ids).stream().collect(Collectors.groupingBy(ServeVo::getReadConnectId));
            for (ConnectConfigVo connectConfig : list) {
                connectConfig.setJoinTableVoList(joinTableMap.get(connectConfig.getId()));
                connectConfig.setServeVoList(serveMap.get(connectConfig.getId()));
                DataBaseType dataBaseType = DataBaseType.getDataBaseType(connectConfig.getType());
                connectConfig.setTypeStr(dataBaseType.getTypeName());
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