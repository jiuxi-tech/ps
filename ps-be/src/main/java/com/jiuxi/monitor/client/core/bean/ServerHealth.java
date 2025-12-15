package com.jiuxi.monitor.client.core.bean;

/**
 * @ClassName: ServerHealth
 * @Description: 服务健康信息
 * @Author jiuxx
 * @Date 2024/11/21 9:29
 * @Copyright: Hangzhou Jiuxi Inc. All rights reserved.
 */
public class ServerHealth {

    /**
     * 服务名称
     */
    private String serverName;
    /**
     * 是否健康
     */
    private boolean isHealth;

    public ServerHealth(boolean isHealth, String serverName) {
        this.serverName = serverName;
        this.isHealth = isHealth;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public boolean isHealth() {
        return isHealth;
    }

    public void setHealth(boolean health) {
        isHealth = health;
    }
}
