package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.Tag;
import com.cloud.vo.TagVo;
import com.cloud.mapper.TagMapper;
import com.cloud.query.TagQuery;
import com.cloud.service.TagService;
import com.cloud.param.TagParam;
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
public class TagServiceImpl implements TagService {

    private TagMapper tagMapper;

    /**
     * 保存对象
     *
     * @param tagParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(TagParam tagParam) {
        Tag tag = BeanUtil.copyProperties(tagParam, Tag::new);
        if(tagParam.getId()!=null){
            return this.updateById(tag);
        }
        tagMapper.insert(tag);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public TagVo findById(Long id) {
        List<TagVo> tagVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(tagVos)){
            return null;
        }
        return CollectionUtils.firstElement(tagVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<TagVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, ids);
        List<Tag> tagEntities = tagMapper.selectList(queryWrapper);
        List<TagVo> list = BeanUtil.copyListProperties(tagEntities, TagVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param tagQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<TagVo> findByList(TagQuery tagQuery) {
        IPage<TagVo> iPage = this.queryPage(tagQuery, new PageParam());
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
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, ids);
        tagMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param tagQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<TagVo> queryPage(TagQuery tagQuery, PageParam pageParam) {
        IPage<Tag> iPage = tagMapper.queryPage(OrderUtil.getPage(pageParam), tagQuery);
        IPage<TagVo> page = iPage.convert(tag -> BeanUtil.copyProperties(tag, TagVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param tag 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(Tag tag) {
        LambdaQueryWrapper<Tag> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Tag::getId, tag.getId());
        tagMapper.update(tag, queryWrapper);
        return Boolean.TRUE;
    }

}
