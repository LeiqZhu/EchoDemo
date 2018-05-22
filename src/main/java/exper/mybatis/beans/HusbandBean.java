package exper.mybatis.beans;

import java.io.Serializable;

/**
 * one to one
 * @author acer
 *
 */
public class HusbandBean implements Serializable {


    private static final long serialVersionUID = 1L;

    private Integer id;
    private String name;
    private WifeBean wife;
    public HusbandBean() {
        super();
    }
    public HusbandBean(Integer id, String name, WifeBean wife) {
        super();
        this.id = id;
        this.name = name;
        this.wife = wife;
    }
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public WifeBean getWife() {
        return wife;
    }
    public void setWife(WifeBean wife) {
        this.wife = wife;
    }
    @Override
    public String toString() {
        return "Husband [id=" + id + ", name=" + name + ", wife=" + wife + "]";
    }



}
