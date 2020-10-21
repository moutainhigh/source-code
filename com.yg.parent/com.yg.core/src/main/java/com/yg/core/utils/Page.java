package com.yg.core.utils;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable{

    private static final long serialVersionUID = -2090765808460776132L;
    /**
     * 页码
     */
    int index = 1;
    /**
     * 开始位置
     */
    int start = 0;
    /**
     * 单页数据量
     */
    int pageSize = 20;
    /**
     * 总页数
     */
    int totalPage;
    /**
     * 数据总量
     */
    int totalRecord;
    /**
     * 数据
     */
    List<T> data;
    
    String totalSum="0";
    

    public Page(int index) {
        setIndex(index);
    }

    public Page(int index, int pageSize) {
        if(index <= 0){
            index = 1;
        }
        this.pageSize = pageSize;
        setIndex(index);
    }

    public Page(PagerInfo pagerInfo) {
    	if(pagerInfo.getPageIndex() <= 0){
    		pagerInfo.setPageIndex(1);
        }
        this.pageSize = pagerInfo.getPageSize();
        this.index=pagerInfo.getPageIndex();
        setIndex(pagerInfo.getPageIndex());
    }
    
    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
        this.start = (index-1)*pageSize;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
        if(pageSize != 0){
            if(totalRecord%pageSize == 0){
                this.totalPage = totalRecord/pageSize;
            }else{
                this.totalPage = totalRecord/pageSize + 1;
            }
        }
    }

    public String getTotalSum() {
		return totalSum;
	}

	public void setTotalSum(String totalSum) {
		this.totalSum = totalSum;
	}

	@Override
    public String toString() {
        return "Page{" +
                "index=" + index +
                ", start=" + start +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                ", totalRecord=" + totalRecord +
                ", data=" + data +
                '}';
    }
}
