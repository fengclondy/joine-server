package com.github.joine.admin.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.github.joine.admin.common.util.TreeUtil;
import com.github.joine.admin.model.dto.MenuTree;
import com.github.joine.admin.model.entity.SysMenu;
import com.github.joine.admin.service.SysMenuService;
import com.github.joine.common.constant.CommonConstant;
import com.github.joine.common.util.R;
import com.github.joine.common.vo.MenuVO;
import com.github.joine.common.vo.UserVO;
import com.github.joine.common.web.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author JenphyJohn
 * @date 2017/10/31
 */
@RestController
@RequestMapping("/menu")
public class MenuController extends BaseController {
    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 通过角色名称查询用户菜单
     *
     * @param role 角色名称
     * @return 菜单列表
     */
    @GetMapping("/findMenuByRole/{role}")
    public List<MenuVO> findMenuByRole(@PathVariable String role) {
        return sysMenuService.findMenuByRoleName(role);
    }

    /**
     * 返回当前用户的树形菜单集合
     *
     * @return 当前用户的树形菜单
     */
    @GetMapping(value = "/userMenu")
    public List<MenuTree> userMenu(UserVO userVO) {
        // 获取符合条件得菜单
        Set<MenuVO> all = new HashSet<>();
        userVO.getRoleList().forEach(role -> all.addAll(sysMenuService.findMenuByRoleName(role.getRoleCode())));

        List<MenuTree> menuTreeList = all.stream().filter(menuVO -> CommonConstant.MENU.equals(menuVO.getType()))
                .map(MenuTree::new)
                .sorted(Comparator.comparing(MenuTree::getSort))
                .collect(Collectors.toList());

        return TreeUtil.bulid(menuTreeList, -1);
    }

    /**
     * 返回树形菜单集合
     *
     * @return 树形菜单
     */
    @GetMapping(value = "/allTree")
    public List<MenuTree> getTree() {
        SysMenu condition = new SysMenu();
        condition.setDelFlag(CommonConstant.STATUS_NORMAL);
        return TreeUtil.bulidTree(sysMenuService.selectList(new EntityWrapper<>(condition)), -1);
    }
    
    /**
     * 返回角色的菜单集合
     *
     * @param roleName 角色名称
     * @return 属性集合
     */
    @GetMapping("/roleTree/{roleName}")
    public List<Integer> roleTree(@PathVariable String roleName) {
        List<MenuVO> menus = sysMenuService.findMenuByRoleName(roleName);
        List<Integer> menuList = new ArrayList<>();
        for (MenuVO menuVo : menus) {
            menuList.add(menuVo.getMenuId());
        }
        return menuList;
    }

    /**
     * 通过ID查询菜单的详细信息
     *
     * @param id 菜单ID
     * @return 菜单详细信息
     */
    @GetMapping("/{id}")
    public SysMenu menu(@PathVariable Integer id) {
        return sysMenuService.selectById(id);
    }

    /**
     * 新增菜单
     *
     * @param sysMenu 菜单信息
     * @return success/false
     */
    @PostMapping
    public R<Boolean> menu(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.insert(sysMenu));
    }

    /**
     * 删除菜单
     *
     * @param id 菜单ID
     * @return success/false
     * TODO  级联删除下级节点
     */
    @DeleteMapping("/{id}")
    public R<Boolean> menuDel(@PathVariable Integer id) {
        return new R<>(sysMenuService.deleteMenu(id));
    }

    @PutMapping
    public R<Boolean> menuUpdate(@RequestBody SysMenu sysMenu) {
        return new R<>(sysMenuService.updateMenuById(sysMenu));
    }

}
