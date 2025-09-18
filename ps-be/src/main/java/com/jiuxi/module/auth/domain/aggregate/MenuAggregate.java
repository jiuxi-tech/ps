package com.jiuxi.module.auth.domain.aggregate;

import com.jiuxi.module.auth.domain.event.MenuCreatedEvent;
import com.jiuxi.module.auth.domain.event.MenuEvent;
import com.jiuxi.module.auth.domain.event.MenuStatusChangedEvent;
import com.jiuxi.module.auth.domain.model.entity.Menu;
import com.jiuxi.module.auth.domain.model.entity.MenuStatus;
import com.jiuxi.module.auth.domain.model.entity.MenuType;
import com.jiuxi.module.auth.domain.model.vo.MenuCode;
import com.jiuxi.module.auth.domain.model.vo.MenuId;
import com.jiuxi.module.auth.domain.model.vo.MenuPath;
import com.jiuxi.module.auth.domain.model.vo.TenantId;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 菜单聚合根
 * 增强版本，包含领域事件、不变性保证和业务规则
 * 
 * @author DDD Refactor
 * @date 2025-09-18
 */
public class MenuAggregate {
    
    private final Menu menu;
    private final List<MenuEvent> domainEvents;
    
    public MenuAggregate(Menu menu) {
        this.menu = menu;
        this.domainEvents = new ArrayList<>();
    }
    
    /**
     * 创建新菜单
     */
    public static MenuAggregate create(MenuCode menuCode, String menuName, MenuType menuType, 
                                     TenantId tenantId, String creator) {
        Menu menu = new Menu(menuCode.getValue(), menuName, menuType);
        menu.setTenantId(tenantId.getValue());
        menu.setCreator(creator);
        menu.setCreateTime(LocalDateTime.now());
        
        MenuAggregate aggregate = new MenuAggregate(menu);
        
        // 发布菜单创建事件
        MenuId menuId = new MenuId(menu.getMenuId());
        aggregate.addDomainEvent(new MenuCreatedEvent(menuId, tenantId, menuCode, 
                                                     menuName, menuType, creator));
        
        return aggregate;
    }
    
    /**
     * 添加子菜单（增强版）
     */
    public void addChild(MenuAggregate childAggregate, String operator) {
        validateCanAddChild(childAggregate.getMenu());
        
        menu.addChild(childAggregate.getMenu());
        
        // 更新操作信息
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
    }
    
    /**
     * 更改菜单状态（增强版）
     */
    public void changeStatus(MenuStatus newStatus, String operator) {
        MenuStatus oldStatus = menu.getStatus();
        
        if (oldStatus.equals(newStatus)) {
            return; // 状态未变化
        }
        
        validateStatusChange(oldStatus, newStatus);
        
        menu.setStatus(newStatus);
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
        
        // 发布状态变更事件
        MenuId menuId = new MenuId(menu.getMenuId());
        TenantId tenantId = new TenantId(menu.getTenantId());
        addDomainEvent(new MenuStatusChangedEvent(menuId, tenantId, oldStatus, newStatus, operator));
    }
    
    /**
     * 设置菜单路径（增强版）
     */
    public void setMenuPath(MenuPath menuPath, String operator) {
        validateMenuPath(menuPath);
        
        menu.setMenuPath(menuPath.getValue());
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
    }
    
    /**
     * 移动菜单到新父菜单下
     */
    public void moveToParent(MenuAggregate newParentAggregate, String operator) {
        if (newParentAggregate != null) {
            validateCanMoveToParent(newParentAggregate.getMenu());
            
            menu.setParentMenuId(newParentAggregate.getMenu().getMenuId());
            menu.setMenuLevel(newParentAggregate.getMenu().getMenuLevel() + 1);
        } else {
            // 移动到根级别
            menu.setParentMenuId(null);
            menu.setMenuLevel(1);
        }
        
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
    }
    
    /**
     * 设置为外链菜单
     */
    public void setAsExternalLink(String externalUrl, String operator) {
        if (externalUrl == null || externalUrl.trim().isEmpty()) {
            throw new IllegalArgumentException("外链URL不能为空");
        }
        
        if (!externalUrl.startsWith("http://") && !externalUrl.startsWith("https://")) {
            throw new IllegalArgumentException("外链URL必须以http://或https://开头");
        }
        
        menu.setMenuUri(externalUrl);
        menu.setAsExternal();
        menu.setUpdator(operator);
        menu.setUpdateTime(LocalDateTime.now());
    }
    
    /**
     * 验证是否可以添加子菜单
     */
    private void validateCanAddChild(Menu child) {
        if (child == null) {
            throw new IllegalArgumentException("子菜单不能为null");
        }
        
        if (menu.isButton()) {
            throw new IllegalStateException("按钮类型菜单不能添加子菜单");
        }
        
        if (menu.isExternal()) {
            throw new IllegalStateException("外链菜单不能添加子菜单");
        }
        
        if (child.getMenuId().equals(menu.getMenuId())) {
            throw new IllegalStateException("菜单不能添加自己作为子菜单");
        }
        
        // 检查层级深度限制
        if (menu.getMenuLevel() != null && menu.getMenuLevel() >= 5) {
            throw new IllegalStateException("菜单层级深度不能超过5级");
        }
    }
    
    /**
     * 验证状态变更
     */
    private void validateStatusChange(MenuStatus oldStatus, MenuStatus newStatus) {
        // 如果有子菜单，不能停用
        if (MenuStatus.INACTIVE.equals(newStatus) && !menu.getChildren().isEmpty()) {
            throw new IllegalStateException("存在子菜单时不能停用菜单");
        }
    }
    
    /**
     * 验证菜单路径
     */
    private void validateMenuPath(MenuPath menuPath) {
        if (menuPath == null) {
            throw new IllegalArgumentException("菜单路径不能为null");
        }
        
        if (menu.isExternal() && !menuPath.isExternalLink()) {
            throw new IllegalStateException("外链菜单的路径必须是有效的外部链接");
        }
    }
    
    /**
     * 验证是否可以移动到指定父菜单
     */
    private void validateCanMoveToParent(Menu newParent) {
        if (newParent.getMenuId().equals(menu.getMenuId())) {
            throw new IllegalStateException("菜单不能移动到自己下面");
        }
        
        // 检查是否会形成循环引用
        if (isDescendantOf(newParent, menu)) {
            throw new IllegalStateException("不能将菜单移动到其子菜单下面");
        }
    }
    
    /**
     * 检查是否是祖先菜单
     */
    private boolean isDescendantOf(Menu potentialDescendant, Menu ancestor) {
        if (potentialDescendant.getParentMenuId() == null) {
            return false;
        }
        
        if (potentialDescendant.getParentMenuId().equals(ancestor.getMenuId())) {
            return true;
        }
        
        // 这里在实际应用中需要递归查询数据库，暂时简化
        return false;
    }
    
    /**
     * 添加领域事件
     */
    private void addDomainEvent(MenuEvent event) {
        domainEvents.add(event);
    }
    
    /**
     * 获取并清空领域事件
     */
    public List<MenuEvent> popDomainEvents() {
        List<MenuEvent> events = new ArrayList<>(domainEvents);
        domainEvents.clear();
        return events;
    }
    
    /**
     * 获取领域事件（只读）
     */
    public List<MenuEvent> getDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
    
    /**
     * 获取菜单实体
     */
    public Menu getMenu() {
        return menu;
    }
}