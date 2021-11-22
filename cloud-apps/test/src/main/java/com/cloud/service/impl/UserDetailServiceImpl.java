package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.UserDetail;
import com.cloud.vo.UserDetailVo;
import com.cloud.mapper.UserDetailMapper;
import com.cloud.query.UserDetailQuery;
import com.cloud.service.UserDetailService;
import com.cloud.param.UserDetailParam;
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
public class UserDetailServiceImpl implements UserDetailService {

    private UserDetailMapper userDetailMapper;

    /**
     * 保存对象
     *
     * @param userDetailParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(UserDetailParam userDetailParam) {
        UserDetail userDetail = BeanUtil.copyProperties(userDetailParam, UserDetail::new);
        if(userDetailParam.getId()!=null){
            return this.updateById(userDetail);
        }
        userDetailMapper.insert(userDetail);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public UserDetailVo findById(Long id) {
        List<UserDetailVo> userDetailVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(userDetailVos)){
            return null;
        }
        return CollectionUtils.firstElement(userDetailVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<UserDetailVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<UserDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserDetail::getId, ids);
        List<UserDetail> userDetailEntities = userDetailMapper.selectList(queryWrapper);
        List<UserDetailVo> list = BeanUtil.copyListProperties(userDetailEntities, UserDetailVo::new);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param userDetailQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<UserDetailVo> findByList(UserDetailQuery userDetailQuery) {
        IPage<UserDetailVo> iPage = this.queryPage(userDetailQuery, new PageParam());
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
        LambdaQueryWrapper<UserDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserDetail::getId, ids);
        userDetailMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param userDetailQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<UserDetailVo> queryPage(UserDetailQuery userDetailQuery, PageParam pageParam) {
        IPage<UserDetail> iPage = userDetailMapper.queryPage(OrderUtil.getPage(pageParam), userDetailQuery);
        IPage<UserDetailVo> page = iPage.convert(userDetail -> BeanUtil.copyProperties(userDetail, UserDetailVo::new));
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param userDetail 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(UserDetail userDetail) {
        LambdaQueryWrapper<UserDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserDetail::getId, userDetail.getId());
        userDetailMapper.update(userDetail, queryWrapper);
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     * @param userIds
     * @return
     */
    @Override
    public List<UserDetailVo> findByUserId(List<Long> userIds){
		LambdaQueryWrapper<UserDetail> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(UserDetail::getUserId, userIds);
        List<UserDetail> userDetailEntities = userDetailMapper.selectList(queryWrapper);
        List<UserDetailVo> list = BeanUtil.copyListProperties(userDetailEntities, UserDetailVo::new);
        return list;
	}
}
