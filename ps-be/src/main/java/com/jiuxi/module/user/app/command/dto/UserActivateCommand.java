package com.jiuxi.module.user.app.command.dto;

/**
 * 激活用户命令
 */
public class UserActivateCommand {
    
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