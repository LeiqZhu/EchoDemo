package exper.mybatis.mapper;

import exper.mybatis.beans.LockBean;
import org.apache.ibatis.annotations.Param;

public interface LockMapper {
    /**
     * 添加锁
     * @param lock
     * @return
     */
    public int saveLock(@Param("lock")LockBean lock);

    /**
     * 根据ID查询锁的资料
     * @param id
     * @return
     */
    public LockBean findLockById(int id);

    /**
     * 根据ID查询锁与钥匙的资料
     * one2many
     * @param id
     * @return
     */
    public LockBean findLockAndKeys(int id);

}
