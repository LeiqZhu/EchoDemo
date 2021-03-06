package exper.mybatis.beans;

import java.util.List;

/**
 * 定义一个分页对象
 *
 * @author
 *
 */
public class Pager {

    private int pageNo;// 当前页码
    private int pageTotal;// 总页码
    private int rowsTotal;// 总条数
    private int pageSize;// 每页显示条数
    private List<Object> list;// 返回的数据集合

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getRowsTotal() {
        return rowsTotal;
    }

    public void setRowsTotal(int rowsTotal) {
        this.rowsTotal = rowsTotal;
        pageTotal = rowsTotal % pageSize == 0 ? rowsTotal / pageSize : rowsTotal / pageSize + 1;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<?> getList() {
        return list;
    }

    public void setList(List<Object> list) {
        this.list = list;
    }


    @Override
    public String toString() {
        return "Pager [pageNo=" + pageNo + ", pageTotal=" + pageTotal
                + ", rowsTotal=" + rowsTotal + ", pageSize=" + pageSize
                + ", list=" + list + "]";
    }

}
