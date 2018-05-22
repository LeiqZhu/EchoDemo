package exper.mybatis.beans;

import java.io.Serializable;

/**
 * manyTOone
 *
 *
 */
public class KeyBean implements Serializable {


    private static final long serialVersionUID = 3712545874604618746L;

    private Integer id;
    private String key;

    private LockBean lock;

    public KeyBean() {
        super();
    }

    public KeyBean(Integer id, String key, LockBean lock) {
        super();
        this.id = id;
        this.key = key;
        this.lock = lock;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public LockBean getLock() {
        return lock;
    }

    public void setLock(LockBean lock) {
        this.lock = lock;
    }

    @Override
    public String toString() {
        return "KeyBean [id=" + id + ", key=" + key + ", lock=" + lock + "]";
    }

}
