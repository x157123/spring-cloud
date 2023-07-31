package com.cloud.sync.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.exceptions.DataException;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.core.utils.DataVersionUtils;
import com.cloud.common.core.utils.ValidationUtils;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.sync.entity.JoinTable;
import com.cloud.sync.vo.JoinTableVo;
import com.cloud.sync.mapper.JoinTableMapper;
import com.cloud.sync.query.JoinTableQuery;
import com.cloud.sync.service.JoinTableService;
import com.cloud.sync.param.JoinTableParam;
import org.apache.commons.collections4.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class JoinTableServiceImpl implements JoinTableService {

    private final JoinTableMapper joinTableMapper;

    /**
     * 使用构造方法注入
     *
     * @param joinTableMapper Mapper服务
     */
    public JoinTableServiceImpl(JoinTableMapper joinTableMapper){
        this.joinTableMapper = joinTableMapper;
    }

    /**
     * 保存对象
     *
     * @param joinTableParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    @Transactional
    public Boolean save(JoinTableParam joinTableParam) {
        ValidationUtils.validate(joinTableParam);
        JoinTable joinTable = BeanUtil.copyProperties(joinTableParam, JoinTable::new);
        if (joinTable != null && joinTable.getId() != null) {
            this.update(joinTable);
        }else{
            joinTable.setVersion(DataVersionUtils.next());
            joinTableMapper.insert(joinTable);
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
    public JoinTableVo findById(Long id) {
        List<JoinTableVo> joinTableVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(joinTableVos)) {
            return null;
        }
        return CollectionUtils.firstElement(joinTableVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<JoinTableVo> findByIds(List<Long> ids) {
        if (CollectionUtils.isEmpty(ids)) {
            return new ArrayList();
        }
        LambdaQueryWrapper<JoinTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(JoinTable::getId, ids);
        List<JoinTableVo> list = queryWrapper(queryWrapper);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param joinTableQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<JoinTableVo> findByList(JoinTableQuery joinTableQuery) {
        IPage<JoinTableVo> iPage = this.queryPage(joinTableQuery, new PageParam());
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
        LambdaQueryWrapper<JoinTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(JoinTable::getId, ids);
        joinTableMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param joinTableQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<JoinTableVo> queryPage(JoinTableQuery joinTableQuery, PageParam pageParam) {
        IPage<JoinTable> iPage = joinTableMapper.queryPage(OrderUtil.getPage(pageParam), joinTableQuery);
        return iPage.convert(joinTable -> BeanUtil.copyProperties(joinTable, JoinTableVo::new));
    }
	
	/**
     * 传入多个Id 查询数据
     *
     * @param connectIds id集合
     * @return  返回查询结果
     */
    @Override
    public List<JoinTableVo> findByConnectId(List<Long> connectIds){
        if (connectIds == null || connectIds.size() == 0) {
            return new ArrayList<>();
        }
        List<JoinTableVo> dataList = new ArrayList<>();
        LambdaQueryWrapper<JoinTable> queryWrapper = new LambdaQueryWrapper<>();
        List<List<Long>> subLists = ListUtils.partition(connectIds, 5000);
        for(List<Long> list : subLists) {
            queryWrapper.in(JoinTable::getConnectId, list);
            dataList.addAll(queryWrapper(queryWrapper));
        }
        return dataList;
    }

    /**
     * 通过Id 更新数据
     *
     * @param joinTable 前端更新集合
     * @return  更新成功状态
     */
    private Boolean update(JoinTable joinTable) {
        LambdaQueryWrapper<JoinTable> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(JoinTable::getId, joinTable.getId())
                .eq(JoinTable::getVersion, joinTable.getVersion());
        joinTable.setVersion(DataVersionUtils.next());
        int count = joinTableMapper.update(joinTable, queryWrapper);
        if (count <= 0) {
            throw new DataException("数据保存异常,未更新到任何数据");
        }
        return Boolean.TRUE;
    }


    /**
     * 查询数据列表
     *
     * @param queryWrapper 查询条件
     * @return  返回转化后的数据
     */
    private List<JoinTableVo> queryWrapper(LambdaQueryWrapper<JoinTable> queryWrapper){
        // 数据查询
        List<JoinTable> joinTableEntities = joinTableMapper.selectList(queryWrapper);
        // 数据转换
        return BeanUtil.copyListProperties(joinTableEntities, JoinTableVo::new);
    }
}