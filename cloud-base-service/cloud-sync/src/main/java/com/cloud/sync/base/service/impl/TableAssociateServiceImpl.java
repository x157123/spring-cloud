package com.cloud.sync.base.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.base.service.TableAssociateService;
import com.cloud.sync.entity.TableAssociate;
import com.cloud.sync.base.mapper.TableAssociateMapper;
import com.cloud.sync.param.TableAssociateParam;
import com.cloud.sync.query.TableAssociateQuery;
import com.cloud.sync.vo.TableAssociateVo;
import org.apache.commons.collections4.ListUtils;
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
public class TableAssociateServiceImpl implements TableAssociateService {

    private final TableAssociateMapper tableAssociateMapper;

    /**
     * 使用构造方法注入
     *
     * @param tableAssociateMapper Mapper服务
     */
    public TableAssociateServiceImpl(TableAssociateMapper tableAssociateMapper) {
        this.tableAssociateMapper = tableAssociateMapper;
    }

    /**
     * 保存对象
     *
     * @param tableAssociateParams 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(List<TableAssociateParam> tableAssociateParams) {
        List<TableAssociate> list = BeanUtil.copyListProperties(tableAssociateParams, TableAssociate::new);

        LambdaQueryWrapper<TableAssociate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableAssociate::getServeId, tableAssociateParams.get(0).getServeId());
        tableAssociateMapper.delete(queryWrapper);
        for (TableAssociate tableAssociate : list) {
            tableAssociate.setVersion(DataVersionUtils.next());
            tableAssociateMapper.insert(tableAssociate);
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
    public TableAssociateVo findById(Long id) {
        List<TableAssociateVo> tableAssociateVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(tableAssociateVos)) {
            return null;
        }
        return CollectionUtils.firstElement(tableAssociateVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return 返回list结果
     */
    @Override
    public List<TableAssociateVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<TableAssociate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableAssociate::getId, ids);
        List<TableAssociateVo> list = queryWrapper(queryWrapper);
        return list;
    }

    /**
     * @param serveIds
     * @return
     */
    @Override
    public List<TableAssociateVo> findByServeId(List<Long> serveIds) {
        if (serveIds == null || serveIds.size() == 0) {
            return new ArrayList<>();
        }
        List<TableAssociateVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<TableAssociate> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(serveIds, 5000);
        for (List<Long> list : subLists) {
            queryWrapper.in(TableAssociate::getServeId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param tableAssociateQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<TableAssociateVo> findByList(TableAssociateQuery tableAssociateQuery) {
        IPage<TableAssociateVo> iPage = this.queryPage(tableAssociateQuery, new PageParam());
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
        LambdaQueryWrapper<TableAssociate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(TableAssociate::getId, ids);
        tableAssociateMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tableAssociateQuery 查询条件
     * @param pageParam           分页条件
     * @return 分页数据
     */
    @Override
    public IPage<TableAssociateVo> queryPage(TableAssociateQuery tableAssociateQuery, PageParam pageParam) {
        IPage<TableAssociate> iPage = tableAssociateMapper.queryPage(OrderUtil.getPage(pageParam), tableAssociateQuery);
        return iPage.convert(tableAssociate -> BeanUtil.copyProperties(tableAssociate, TableAssociateVo::new));
    }

    /**
     * 通过Id 更新数据
     *
     * @param tableAssociate 前端更新集合
     * @return 更新成功状态
     */
    private Boolean update(TableAssociate tableAssociate) {
        LambdaQueryWrapper<TableAssociate> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TableAssociate::getId, tableAssociate.getId())
                .eq(TableAssociate::getVersion, tableAssociate.getVersion());
        tableAssociate.setVersion(DataVersionUtils.next());
        int count = tableAssociateMapper.update(tableAssociate, queryWrapper);
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
    private List<TableAssociateVo> queryWrapper(LambdaQueryWrapper<TableAssociate> queryWrapper) {
        // 数据查询
        List<TableAssociate> tableAssociateEntities = tableAssociateMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(tableAssociateEntities, TableAssociateVo::new);
    }
}