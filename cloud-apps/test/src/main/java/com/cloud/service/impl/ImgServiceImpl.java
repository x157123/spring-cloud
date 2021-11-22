package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.Img;
import com.cloud.vo.ImgVo;
import com.cloud.mapper.ImgMapper;
import com.cloud.query.ImgQuery;
import com.cloud.service.ImgService;
import com.cloud.param.ImgParam;
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
public class ImgServiceImpl implements ImgService {

    private ImgMapper imgMapper;

    /**
     * 保存对象
     *
     * @param imgParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(ImgParam imgParam) {
        Img img = BeanUtil.copyProperties(imgParam, Img::new);
        if(imgParam.getId()!=null){
            return this.updateById(img);
        }
        imgMapper.insert(img);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public ImgVo findById(Long id) {
        List<ImgVo> imgVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(imgVos)){
            return null;
        }
        return CollectionUtils.firstElement(imgVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<ImgVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Img> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Img::getId, ids);
        List<Img> imgEntities = imgMapper.selectList(queryWrapper);
        List<ImgVo> list = BeanUtil.copyListProperties(imgEntities, ImgVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param imgQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<ImgVo> findByList(ImgQuery imgQuery) {
        IPage<ImgVo> iPage = this.queryPage(imgQuery, new PageParam());
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
        LambdaQueryWrapper<Img> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Img::getId, ids);
        imgMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param imgQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<ImgVo> queryPage(ImgQuery imgQuery, PageParam pageParam) {
        IPage<Img> iPage = imgMapper.queryPage(OrderUtil.getPage(pageParam), imgQuery);
        IPage<ImgVo> page = iPage.convert(img -> BeanUtil.copyProperties(img, ImgVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param img 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(Img img) {
        LambdaQueryWrapper<Img> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Img::getId, img.getId());
        imgMapper.update(img, queryWrapper);
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     * @param userIds
     * @return
     */
    @Override
    public List<ImgVo> findByUserId(List<Long> userIds){
		LambdaQueryWrapper<Img> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Img::getUserId, userIds);
        List<Img> imgEntities = imgMapper.selectList(queryWrapper);
        List<ImgVo> list = BeanUtil.copyListProperties(imgEntities, ImgVo::new);
        return list;
	}
}
