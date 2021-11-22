package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.User;
import com.cloud.vo.UserVo;
import com.cloud.mapper.UserMapper;
import com.cloud.query.UserQuery;
import com.cloud.service.UserService;
import com.cloud.param.UserParam;
import com.cloud.vo.ImgVo;
import com.cloud.service.ImgService;
import com.cloud.vo.UserDetailVo;
import com.cloud.service.UserDetailService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author liulei
 */
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;

    private ImgService imgService;

    private UserDetailService userDetailService;

    /**
     * 保存对象
     *
     * @param userParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(UserParam userParam) {
        User user = BeanUtil.copyProperties(userParam, User::new);
        if(userParam.getId()!=null){
            return this.updateById(user);
        }
        userMapper.insert(user);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public UserVo findById(Long id) {
        List<UserVo> userVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(userVos)){
            return null;
        }
        return CollectionUtils.firstElement(userVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<UserVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, ids);
        List<User> userEntities = userMapper.selectList(queryWrapper);
        List<UserVo> list = BeanUtil.copyListProperties(userEntities, UserVo::new);
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param userQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<UserVo> findByList(UserQuery userQuery) {
        IPage<UserVo> iPage = this.queryPage(userQuery, new PageParam());
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
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, ids);
        userMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param userQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<UserVo> queryPage(UserQuery userQuery, PageParam pageParam) {
        IPage<User> iPage = userMapper.queryPage(OrderUtil.getPage(pageParam), userQuery);
        IPage<UserVo> page = iPage.convert(user -> BeanUtil.copyProperties(user, UserVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param user 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(User user) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(User::getId, user.getId());
        userMapper.update(user, queryWrapper);
        return Boolean.TRUE;
    }


	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<UserVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(UserVo::getId).collect(Collectors.toList());
            Map<Long, List<ImgVo>> imgMap = imgService.findByUserId(ids).stream().collect(Collectors.groupingBy(ImgVo::getUserId));
            Map<Long, List<UserDetailVo>> userDetailMap = userDetailService.findByUserId(ids).stream().collect(Collectors.groupingBy(UserDetailVo::getUserId));
            for (UserVo user : list) {
                user.setImgVOList(imgMap.get(user.getId()));
                user.setUserDetailVOList(userDetailMap.get(user.getId()));
            }
        }
    }
}
