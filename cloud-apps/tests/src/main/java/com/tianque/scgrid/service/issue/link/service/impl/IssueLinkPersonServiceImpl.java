package com.tianque.scgrid.service.issue.link.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.tianque.scgrid.service.issue.link.entity.IssueLinkPerson;
import com.tianque.scgrid.service.issue.link.vo.IssueLinkPersonVo;
import com.tianque.scgrid.service.issue.link.mapper.IssueLinkPersonMapper;
import com.tianque.scgrid.service.issue.link.query.IssueLinkPersonQuery;
import com.tianque.scgrid.service.issue.link.service.IssueLinkPersonService;
import com.tianque.scgrid.service.issue.link.param.IssueLinkPersonParam;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class IssueLinkPersonServiceImpl implements IssueLinkPersonService {

    private IssueLinkPersonMapper issueLinkPersonMapper;

    /**
     * 使用构造方法注入
     *
     * @param issueLinkPersonMapper
     */
    public IssueLinkPersonServiceImpl(IssueLinkPersonMapper issueLinkPersonMapper){
        this.issueLinkPersonMapper = issueLinkPersonMapper;
    }

    /**
     * 保存对象
     *
     * @param issueLinkPersonParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(IssueLinkPersonParam issueLinkPersonParam) {
        IssueLinkPerson issueLinkPerson = BeanUtil.copyProperties(issueLinkPersonParam, IssueLinkPerson::new);
        if (issueLinkPersonParam.getId() != null) {
            return this.updateById(issueLinkPerson);
        }
        issueLinkPersonMapper.insert(issueLinkPerson);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public IssueLinkPersonVo findById(Long id) {
        List<IssueLinkPersonVo> issueLinkPersonVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(issueLinkPersonVos)) {
            return null;
        }
        return CollectionUtils.firstElement(issueLinkPersonVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<IssueLinkPersonVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<IssueLinkPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueLinkPerson::getId, ids);
        List<IssueLinkPerson> issueLinkPersonEntities = issueLinkPersonMapper.selectList(queryWrapper);
        List<IssueLinkPersonVo> list = BeanUtil.copyListProperties(issueLinkPersonEntities, IssueLinkPersonVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param issueLinkPersonQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<IssueLinkPersonVo> findByList(IssueLinkPersonQuery issueLinkPersonQuery) {
        IPage<IssueLinkPersonVo> iPage = this.queryPage(issueLinkPersonQuery, new PageParam());
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
        LambdaQueryWrapper<IssueLinkPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueLinkPerson::getId, ids);
        issueLinkPersonMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param issueLinkPersonQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<IssueLinkPersonVo> queryPage(IssueLinkPersonQuery issueLinkPersonQuery, PageParam pageParam) {
        IPage<IssueLinkPerson> iPage = issueLinkPersonMapper.queryPage(OrderUtil.getPage(pageParam), issueLinkPersonQuery);
        IPage<IssueLinkPersonVo> page = iPage.convert(issueLinkPerson -> BeanUtil.copyProperties(issueLinkPerson, IssueLinkPersonVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param issueLinkPerson 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(IssueLinkPerson issueLinkPerson) {
        LambdaQueryWrapper<IssueLinkPerson> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueLinkPerson::getId, issueLinkPerson.getId());
        issueLinkPersonMapper.update(issueLinkPerson, queryWrapper);
        return Boolean.TRUE;
    }

}
