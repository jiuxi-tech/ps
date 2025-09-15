package com.jiuxi.module.user.app.command.dto;

/**
 * 删除用户命令
 */
public class UserDeleteCommand {
    
    private String personId;
    private String operator;
    
    // Getters and Setters
    public String getPersonId() {
        return personId;
    }
    
    public void setPersonId(String personId) {
        this.personId = personId;
    }
    
    public String getOperator() {
        return operator;
    }
    
    public void setOperator(String operator) {
        this.operator = operator;
    }
}