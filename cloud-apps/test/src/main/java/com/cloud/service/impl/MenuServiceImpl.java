package com.cloud.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.cloud.common.core.utils.BeanUtil;
import com.cloud.common.mybatis.page.PageParam;
import com.cloud.common.mybatis.util.OrderUtil;
import com.cloud.entity.Menu;
import com.cloud.vo.MenuVo;
import com.cloud.mapper.MenuMapper;
import com.cloud.query.MenuQuery;
import com.cloud.service.MenuService;
import com.cloud.param.MenuParam;
import com.cloud.vo.MenuVo;
import com.cloud.service.MenuService;
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
public class MenuServiceImpl implements MenuService {

    private MenuMapper menuMapper;

    private MenuService menuService;

    /**
     * 保存对象
     *
     * @param menuParam 前端传入对象
     * @return 返回保存成功状态
     */
    @Override
    public Boolean save(MenuParam menuParam) {
        Menu menu = BeanUtil.copyProperties(menuParam, Menu::new);
        if(menuParam.getId()!=null){
            return this.updateById(menu);
        }
        menuMapper.insert(menu);
        return Boolean.TRUE;
    }

    /**
     * 通过Id查询数据
     *
     * @param id 业务Id
     * @return 返回VO对象
     */
    @Override
    public MenuVo findById(Long id) {
        List<MenuVo> menuVos = this.findByIds(Collections.singletonList(id));
        if(CollectionUtils.isEmpty(menuVos)){
            return null;
        }
        return CollectionUtils.firstElement(menuVos);
    }

    /**
     * 传入多个Id 查询数据
     *
     * @param ids 多个id
     * @return  返回list结果
     */
    @Override
    public List<MenuVo> findByIds(List<Long> ids) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getId, ids);
        List<Menu> menuEntities = menuMapper.selectList(queryWrapper);
        List<MenuVo> list = BeanUtil.copyListProperties(menuEntities, MenuVo::new);
		this.setParam(list);
		return list;
    }

    /**
     * 根据查询条件 查询列表
     *
     * @param menuQuery 查询条件
     * @return 返回list结果
     */
    @Override
    public List<MenuVo> findByList(MenuQuery menuQuery) {
        IPage<MenuVo> iPage = this.queryPage(menuQuery, new PageParam());
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
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getId, ids);
        menuMapper.delete(queryWrapper);
        return Boolean.TRUE;
    }

    /**
     * 数据分页查询
     *
     * @param menuQuery 查询条件
     * @param pageParam 分页条件
     * @return 分页数据
     */
    @Override
    public IPage<MenuVo> queryPage(MenuQuery menuQuery, PageParam pageParam) {
        IPage<Menu> iPage = menuMapper.queryPage(OrderUtil.getPage(pageParam), menuQuery);
        IPage<MenuVo> page = iPage.convert(menu -> BeanUtil.copyProperties(menu, MenuVo::new));
		this.setParam(page.getRecords());
        return page;
    }

    /**
     * 通过Id 更新数据
     *
     * @param menu 前端更新集合
     * @return  更新成功状态
     */
    private Boolean updateById(Menu menu) {
        LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getId, menu.getId());
        menuMapper.update(menu, queryWrapper);
        return Boolean.TRUE;
    }

	
	/**
     * 传入多个Id 查询数据
     * @param nodeIds
     * @return
     */
    @Override
    public List<MenuVo> findByNodeId(List<Long> nodeIds){
		LambdaQueryWrapper<Menu> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Menu::getNodeId, nodeIds);
        List<Menu> menuEntities = menuMapper.selectList(queryWrapper);
        List<MenuVo> list = BeanUtil.copyListProperties(menuEntities, MenuVo::new);
        return list;
	}

	/**
     * 补充关联表数据查询
     *
     * @param list 列表数据
     */
    private void setParam(List<MenuVo> list) {
        if (list != null) {
            List<Long> ids = list.stream().map(MenuVo::getId).collect(Collectors.toList());
            Map<Long, List<MenuVo>> menuMap = menuService.findByNodeId(ids).stream().collect(Collectors.groupingBy(MenuVo::getNodeId));
            for (MenuVo user : list) {
                menu.setMenuVOList(menuMap.get(menu.getId()));
            }
        }
    }
}
