package com.zcloud.newsbt.until;

import javax.annotation.sql.DataSourceDefinition;
import java.io.Serializable;
import java.util.List;

/**
 * @ClassName PageData
 * @Description TODO
 * @Author Dear lin
 * @Date 16:59 2023/2/6/006
 * @Version 1.0
 **/
public class PageData<T> implements Serializable {
    private static final long serialVersionUID = 1L;
    private int total;
    private List<T> list;

    /**
     * 分页
     * @param list   列表数据
     * @param total  总记录数
     */
    public PageData(List<T> list, long total) {
        this.list = list;
        this.total = (int)total;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
