package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.Permissions;
import com.cloud.vo.PermissionsVo;
import com.cloud.mapper.PermissionsMapper;
import com.cloud.query.PermissionsQuery;
import com.cloud.service.PermissionsService;
import com.cloud.param.PermissionsParam;
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
public class PermissionsServiceImpl implements PermissionsService {

    private PermissionsMapper permissionsMapper;

    /**
     * 保存对象
     *
     * @param permissionsParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(PermissionsParam permissionsParam) {
        Permissions permissions = BeanUtil.copyProperties(permissionsParam, Permissions::new);
        if(permissionsParam.getId()!=null){
            return this.updateById(permissions);
        }
        permissionsMapper.insert(permissions);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public PermissionsVo findById(Long id) {
        List<PermissionsVo> permissionsVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(permissionsVos)){
            return null;
        }
        return CollectionUtils.firstElement(permissionsVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<PermissionsVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Permissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permissions::getId, ids);
        List<Permissions> permissionsEntities = permissionsMapper.selectList(queryWrapper);
        List<PermissionsVo> list = BeanUtil.copyListProperties(permissionsEntities, PermissionsVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param permissionsQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<PermissionsVo> findByList(PermissionsQuery permissionsQuery) {
        IPage<PermissionsVo> iPage = this.queryPage(permissionsQuery, new PageParam());
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
        LambdaQueryWrapper<Permissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permissions::getId, ids);
        permissionsMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param permissionsQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<PermissionsVo> queryPage(PermissionsQuery permissionsQuery, PageParam pageParam) {
        IPage<Permissions> iPage = permissionsMapper.queryPage(OrderUtil.getPage(pageParam), permissionsQuery);
        IPage<PermissionsVo> page = iPage.convert(permissions -> BeanUtil.copyProperties(permissions, PermissionsVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param permissions 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(Permissions permissions) {
        LambdaQueryWrapper<Permissions> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Permissions::getId, permissions.getId());
        permissionsMapper.update(permissions, queryWrapper);
        return Boolean.TRUE;
    }

}
