package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.Dept;
import com.cloud.vo.DeptVo;
import com.cloud.mapper.DeptMapper;
import com.cloud.query.DeptQuery;
import com.cloud.service.DeptService;
import com.cloud.param.DeptParam;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
@AllArgsConstructor
public class DeptServiceImpl implements DeptService {

    private DeptMapper deptMapper;

    /**
     * 保存对象
     *
     * @param deptParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(DeptParam deptParam) {
        Dept dept = BeanUtil.copyProperties(deptParam, Dept::new);
        if(deptParam.getId()!=null){
            return this.updateById(dept);
        }
        deptMapper.insert(dept);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public DeptVo findById(Long id) {
        List<DeptVo> deptVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(deptVos)){
            return null;
        }
        return CollectionUtils.firstElement(deptVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<DeptVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getId, ids);
        List<Dept> deptEntities = deptMapper.selectList(queryWrapper);
        List<DeptVo> list = BeanUtil.copyListProperties(deptEntities, DeptVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param deptQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<DeptVo> findByList(DeptQuery deptQuery) {
        IPage<DeptVo> iPage = this.queryPage(deptQuery, new PageParam());
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
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getId, ids);
        deptMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param deptQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<DeptVo> queryPage(DeptQuery deptQuery, PageParam pageParam) {
        IPage<Dept> iPage = deptMapper.queryPage(OrderUtil.getPage(pageParam), deptQuery);
        IPage<DeptVo> page = iPage.convert(dept -> BeanUtil.copyProperties(dept, DeptVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param dept 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(Dept dept) {
        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getId, dept.getId());
        deptMapper.update(dept, queryWrapper);
        return Boolean.TRUE;
    }

}
