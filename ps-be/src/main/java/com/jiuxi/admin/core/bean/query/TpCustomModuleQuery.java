package com.jiuxi.admin.core.bean.query;

import java.io.Serializable;


/**
 * 自定义模块信息表 存储模块的信息，按钮信息、路由信息
 *
 * @author pand
 * @email 
 * @date 2021-05-11 11:22:40
 */
public class TpCustomModuleQuery implements Serializable {

    private static final long serialVersionUID = 1L;

    
    /**
     * 模块名称
     */
    private String mname;
    
    /**
     * 模块编码 表名,自动添加前缀: tp_json_
     */
    private String mcode;

    /**
     * 当前页
     */
    private Integer current;

    /**
     * 每页记录数
     */
    private Integer size;


	public String getMname() {
	    return mname;
	}

	public void setMname(String mname) {
	    this.mname = mname;
	}

	public String getMcode() {
	    return mcode;
	}

	public void setMcode(String mcode) {
	    this.mcode = mcode;
	}

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
