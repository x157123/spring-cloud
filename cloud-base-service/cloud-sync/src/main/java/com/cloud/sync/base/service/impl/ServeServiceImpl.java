package com.cloud.sync.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.base.service.*;
import com.cloud.sync.entity.Serve;
import com.cloud.sync.base.mapper.ServeMapper;
import com.cloud.sync.param.*;
import com.cloud.sync.query.ServeQuery;
import com.cloud.sync.vo.*;
import org.apache.commons.collections4.ListUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
public class ServeServiceImpl implements ServeService {

    private final ServeMapper serveMapper;

    private final TableAssociateService tableAssociateService;

    private final TableConfigService tableConfigService;

    private final ServeConfigService serveConfigService;

    private final ColumnConfigService columnConfigService;

    private final ConnectConfigService connectConfigService;

    /**
     * 使用构造方法注入
     *
     * @param serveMapper 表映射Mapper服务
     */
    public ServeServiceImpl(ServeMapper serveMapper, TableAssociateService tableAssociateService
            , TableConfigService tableConfigService, ServeConfigService serveConfigService
            , ColumnConfigService columnConfigService, @Lazy ConnectConfigService connectConfigService) {
        this.serveMapper = serveMapper;
        this.tableAssociateService = tableAssociateService;
        this.tableConfigService = tableConfigService;
        this.serveConfigService = serveConfigService;
        this.columnConfigService = columnConfigService;
        this.connectConfigService = connectConfigService;
    }

    /**
     * 保存对象
     *
     * @param serveParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(ServeParam serveParam) {
        Serve serve = BeanUtil.copyProperties(serveParam, Serve::new);
        if (serve != null && serve.getId() != null) {
            this.update(serve);
        } else {
            serve.setVersion(DataVersionUtils.next());
            serveMapper.insert(serve);
        }
        List<AssociateTableParam> list = serveParam.getTableConfig();

        List<TableConfigParam> tableConfigParams = new ArrayList<>();
        List<TableAssociateParam> tableAssociateParams = new ArrayList<>();
        List<ColumnConfigParam> columnConfigParams = new ArrayList<>();

        for (AssociateTableParam associateTableParam : list) {

            TableConfigParam redaTable = new TableConfigParam(serve.getId(), 1, associateTableParam.getReadTable());
            TableConfigParam writeTable = new TableConfigParam(serve.getId(), 2, associateTableParam.getWriteTable());

            TableAssociateParam tableAssociateParam = new TableAssociateParam(serve.getId(),
                    associateTableParam.getName(), 1, redaTable.getId(), redaTable.getTableName(),
                    writeTable.getId(), redaTable.getTableName());

            tableConfigParams.add(redaTable);
            tableConfigParams.add(writeTable);
            tableAssociateParams.add(tableAssociateParam);

            List<AssociateColumnParam> columns = associateTableParam.getColumns();
            Integer seq = 0;
            for (AssociateColumnParam associateColumnParam : columns) {
                seq++;
                ColumnConfigParam readColumnConfigParam = new ColumnConfigParam(redaTable.getId(), serve.getId(), seq,
                        associateColumnParam.getReadColumn(), associateColumnParam.getKey() != null ? associateColumnParam.getKey() : 0
                        , associateColumnParam.getDefaultValue(), associateColumnParam.getConvertFun());
                ColumnConfigParam writeColumnConfigParam = new ColumnConfigParam(writeTable.getId(), serve.getId(), seq,
                        associateColumnParam.getWriteColumn(), associateColumnParam.getKey() != null ? associateColumnParam.getKey() : 0
                        , associateColumnParam.getDefaultValue(), associateColumnParam.getConvertFun());
                columnConfigParams.add(readColumnConfigParam);
                columnConfigParams.add(writeColumnConfigParam);
            }
        }

        //表配置
        tableConfigService.save(tableConfigParams);
        //表关联
        tableAssociateService.save(tableAssociateParams);
        //列保存
        columnConfigService.save(columnConfigParams);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询  用于页面显示
     *
     * @param id
     * @return
     */
    @Override
    public ServeVo findServeParamById(Long id) {
        List<AssociateTableParam> associateTableParamList = new ArrayList<>();
        ServeVo serveVo = this.findById(id);
        List<TableAssociateVo> tableAssociateVoList = serveVo.getTableAssociateVoList();
        List<TableConfigVo> tableConfigVoList = serveVo.getTableConfigVoList();
        Map<Long, TableConfigVo> appleMap = tableConfigVoList.stream().collect(Collectors.toMap(TableConfigVo::getId, a -> a, (k1, k2) -> k1));
        for (TableAssociateVo tableAssociateVo : tableAssociateVoList) {
            AssociateTableParam associateTableParam = BeanUtil.copyProperties(tableAssociateVo, AssociateTableParam::new);
            TableConfigVo read = appleMap.get(tableAssociateVo.getReadTableId());
            TableConfigVo write = appleMap.get(tableAssociateVo.getWriteTableId());
            associateTableParam.setReadTable(read.getTableName());
            associateTableParam.setWriteTable(write.getTableName());
            List<AssociateColumnParam> associateColumnParams = new ArrayList<>();
            associateTableParam.setColumns(associateColumnParams);
            List<ColumnConfigVo> reader = read.getColumnConfigVoList().stream().sorted(Comparator.comparing(ColumnConfigVo::getSeq)).collect(Collectors.toList());
            List<ColumnConfigVo> writer = write.getColumnConfigVoList().stream().sorted(Comparator.comparing(ColumnConfigVo::getSeq)).collect(Collectors.toList());
            for (int i = 0; i < writer.size(); i++) {
                AssociateColumnParam associateColumnParam = new AssociateColumnParam();
                associateColumnParam.setReadColumn(reader.get(i).getColumnName());
                associateColumnParam.setWriteColumn(writer.get(i).getColumnName());
                associateColumnParam.setKey(writer.get(i).getColumnPrimaryKey());
                associateColumnParam.setDefaultValue(writer.get(i).getDef());
                associateColumnParam.setConvertFun(writer.get(i).getConvertFun());
                associateColumnParams.add(associateColumnParam);
            }
            associateTableParamList.add(associateTableParam);
        }
        serveVo.setTableConfig(associateTableParamList);
        return serveVo;
    }


    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ServeVo findById(Long id) {
        List<ServeVo> serveVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(serveVos)) {
            return null;
        }
        return CollectionUtils.firstElement(serveVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return 返回list结果
     */
    @Override
    public List<ServeVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Serve::getId, ids);
        List<ServeVo> list = queryWrapper(queryWrapper);
        //封装关联数据
        this.setParam(list);
        return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param serveQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<ServeVo> findByList(ServeQuery serveQuery) {
        IPage<ServeVo> iPage = this.queryPage(serveQuery, new PageParam());
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
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Serve::getId, ids);
        serveMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param serveQuery 查询条件
     * @param pageParam  分页条件
     * @return 分页数据
     */
    @Override
    public IPage<ServeVo> queryPage(ServeQuery serveQuery, PageParam pageParam) {
        IPage<Serve> iPage = serveMapper.queryPage(OrderUtil.getPage(pageParam), serveQuery);
        IPage<ServeVo> page = iPage.convert(serve -> BeanUtil.copyProperties(serve, ServeVo::new));
        this.setParam(page.getRecords());
        return page;
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param readConnectIds id集合
     * @return 返回查询结果
     */
    @Override
    public List<ServeVo> findByReadConnectId(List<Long> readConnectIds) {
        if (readConnectIds == null || readConnectIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ServeVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(readConnectIds, 5000);
        for (List<Long> list : subLists) {
            queryWrapper.in(Serve::getReadConnectId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param writeConnectIds id集合
     * @return 返回查询结果
     */
    @Override
    public List<ServeVo> findByWriteConnectId(List<Long> writeConnectIds) {
        if (writeConnectIds == null || writeConnectIds.size() == 0) {
            return new ArrayList<>();
        }
        List<ServeVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(writeConnectIds, 5000);
        for (List<Long> list : subLists) {
            queryWrapper.in(Serve::getWriteConnectId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }

    /**
     * 通过Id 更新数据
     *
     * @param serve 前端更新集合
     * @return 更新成功状态
     */
    private Boolean update(Serve serve) {
        LambdaQueryWrapper<Serve> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Serve::getId, serve.getId())
                .eq(Serve::getVersion, serve.getVersion());
        serve.setVersion(DataVersionUtils.next());
        int count = serveMapper.update(serve, queryWrapper);
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
    private void setParam(List<ServeVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(ServeVo::getId).collect(Collectors.toList());
            Set<Long> readDb = list.stream().map(ServeVo::getReadConnectId).collect(Collectors.toSet());
            Set<Long> writeDb = list.stream().map(ServeVo::getWriteConnectId).collect(Collectors.toSet());
            readDb.addAll(writeDb);
            Map<Long, ConnectConfigVo> configVoMap = connectConfigService.findByIds(new ArrayList<>(readDb)).stream().collect(Collectors.toMap(ConnectConfigVo::getId, a -> a, (k1, k2) -> k1));
            Map<Long, ServeConfigVo> serveConfigMap = serveConfigService.findByServeId(ids).stream().collect(Collectors.toMap(ServeConfigVo::getServeId, a -> a, (k1, k2) -> k1));
            Map<Long, List<TableAssociateVo>> tableAssociateMap = tableAssociateService.findByServeId(ids).stream().collect(Collectors.groupingBy(TableAssociateVo::getServeId));
            Map<Long, List<TableConfigVo>> tableConfigMap = tableConfigService.findByServeId(ids, null).stream().collect(Collectors.groupingBy(TableConfigVo::getServeId));
            for (ServeVo serve : list) {
                ServeConfigVo serveConfigVo = serveConfigMap.get(serve.getId());
                if (serveConfigVo != null) {
                    serve.setState(serveConfigVo.getState());
                    serve.setOffSet(serveConfigVo.getOffSet());
                }
                ConnectConfigVo connectConfigVo = configVoMap.get(serve.getReadConnectId());
                if (connectConfigVo != null) {
                    serve.setReadConnectName(connectConfigVo.getRemark());
                    serve.setReadConnectType(connectConfigVo.getType());
                }
                connectConfigVo = configVoMap.get(serve.getWriteConnectId());
                if (connectConfigVo != null) {
                    serve.setWriteConnectName(connectConfigVo.getRemark());
                    serve.setWriteConnectType(connectConfigVo.getType());
                }
                serve.setTableAssociateVoList(tableAssociateMap.get(serve.getId()));
                serve.setTableConfigVoList(tableConfigMap.get(serve.getId()));
            }
        }
    }

    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return 返回转化后的数据
     */
    private List<ServeVo> queryWrapper(LambdaQueryWrapper<Serve> queryWrapper) {
        // 数据查询
        List<Serve> serveEntities = serveMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(serveEntities, ServeVo::new);
    }
}