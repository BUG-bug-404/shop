package com.core.utils;

import java.io.Serializable;

/**
 * 接收分页的参数Bean
 * @author admin
 *
 */
public class PageBean implements Serializable{

	private static final long serialVersionUID = 5600543687644637513L;
	
	//当前页
	private int pageNum = 0;
	//每页的数量
    private int pageSize = 20;
    
    
	public int getPageNum() {
		return pageNum;
	}
	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	
	@Override
	public String toString() {
		return "PageBean [pageNum=" + pageNum + ", pageSize=" + pageSize + "]";
	}
    
	
}
