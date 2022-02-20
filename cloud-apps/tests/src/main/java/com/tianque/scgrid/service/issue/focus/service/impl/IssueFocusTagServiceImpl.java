package com.tianque.scgrid.service.issue.focus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.tianque.scgrid.service.issue.focus.entity.IssueFocusTag;
import com.tianque.scgrid.service.issue.focus.vo.IssueFocusTagVo;
import com.tianque.scgrid.service.issue.focus.mapper.IssueFocusTagMapper;
import com.tianque.scgrid.service.issue.focus.query.IssueFocusTagQuery;
import com.tianque.scgrid.service.issue.focus.service.IssueFocusTagService;
import com.tianque.scgrid.service.issue.focus.param.IssueFocusTagParam;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * @author liulei
 */
@Service
public class IssueFocusTagServiceImpl implements IssueFocusTagService {

    private IssueFocusTagMapper issueFocusTagMapper;

    /**
     * 使用构造方法注入
     *
     * @param issueFocusTagMapper
     */
    public IssueFocusTagServiceImpl(IssueFocusTagMapper issueFocusTagMapper){
        this.issueFocusTagMapper = issueFocusTagMapper;
    }

    /**
     * 保存对象
     *
     * @param issueFocusTagParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(IssueFocusTagParam issueFocusTagParam) {
        IssueFocusTag issueFocusTag = BeanUtil.copyProperties(issueFocusTagParam, IssueFocusTag::new);
        if (issueFocusTagParam.getId() != null) {
            return this.updateById(issueFocusTag);
        }
        issueFocusTagMapper.insert(issueFocusTag);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public IssueFocusTagVo findById(Long id) {
        List<IssueFocusTagVo> issueFocusTagVos = this.findByIds(Collections.singletonList(id));
        if (CollectionUtils.isEmpty(issueFocusTagVos)) {
            return null;
        }
        return CollectionUtils.firstElement(issueFocusTagVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<IssueFocusTagVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<IssueFocusTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueFocusTag::getId, ids);
        List<IssueFocusTag> issueFocusTagEntities = issueFocusTagMapper.selectList(queryWrapper);
        List<IssueFocusTagVo> list = BeanUtil.copyListProperties(issueFocusTagEntities, IssueFocusTagVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param issueFocusTagQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<IssueFocusTagVo> findByList(IssueFocusTagQuery issueFocusTagQuery) {
        IPage<IssueFocusTagVo> iPage = this.queryPage(issueFocusTagQuery, new PageParam());
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
        LambdaQueryWrapper<IssueFocusTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueFocusTag::getId, ids);
        issueFocusTagMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param issueFocusTagQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<IssueFocusTagVo> queryPage(IssueFocusTagQuery issueFocusTagQuery, PageParam pageParam) {
        IPage<IssueFocusTag> iPage = issueFocusTagMapper.queryPage(OrderUtil.getPage(pageParam), issueFocusTagQuery);
        IPage<IssueFocusTagVo> page = iPage.convert(issueFocusTag -> BeanUtil.copyProperties(issueFocusTag, IssueFocusTagVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param issueFocusTag 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(IssueFocusTag issueFocusTag) {
        LambdaQueryWrapper<IssueFocusTag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(IssueFocusTag::getId, issueFocusTag.getId());
        issueFocusTagMapper.update(issueFocusTag, queryWrapper);
        return Boolean.TRUE;
    }

}
