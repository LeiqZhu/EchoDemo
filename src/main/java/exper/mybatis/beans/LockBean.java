package exper.mybatis.beans;

import java.io.Serializable;
import java.util.List;

/**
 * oneTOmany
 *
 *
 */
public class LockBean implements Serializable {

    private static final long serialVersionUID = 7092410462131162665L;

    private Integer id;
    private String lock;

    private List<KeyBean> keys;

    public LockBean() {
        super();
    }

    public LockBean(Integer id, String lock, List<KeyBean> keys) {
        super();
        this.id = id;
        this.lock = lock;
        this.keys = keys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLock() {
        return lock;
    }

    public void setLock(String lock) {
        this.lock = lock;
    }

    public List<KeyBean> getKeys() {
        return keys;
    }

    public void setKeys(List<KeyBean> keys) {
        this.keys = keys;
    }

    @Override
    public String toString() {
        return "LockBean [id=" + id + ", keys=" + keys + ", lock=" + lock + "]";
    }

}