package exper.mybatis.service;


import exper.mybatis.beans.KeyBean;
import exper.mybatis.beans.LockBean;
import exper.mybatis.mapper.KeyMapper;
import exper.mybatis.mapper.LockMapper;
import exper.mybatis.tools.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.List;

public class OneToManyService {
    public static void main(String[] args) {
        // saveLock();
        // batchSaveKeys();
        findLockAndKeys();
    }

    private static void findLockAndKeys() {

        SqlSession session = MybatisUtil.getSession();
        LockMapper lm = session.getMapper(LockMapper.class);
        LockBean lock = lm.findLockAndKeys(1);
        System.out.println(lock);
    }

    private static void batchSaveKeys() {

        SqlSession session = MybatisUtil.getSession();
        LockMapper lm = session.getMapper(LockMapper.class);
        KeyMapper km = session.getMapper(KeyMapper.class);

        LockBean lock = lm.findLockById(1);
        List<KeyBean> keys = new ArrayList<KeyBean>();
        for(int i = 0; i < 5; i++){
            KeyBean key = new KeyBean(null, "钥匙"+i, lock);
            keys.add(key);
        }
        km.batchSaveKeys(keys);
        session.commit();
    }

    private static void saveLock() {
        SqlSession session = MybatisUtil.getSession();
        LockMapper lm = session.getMapper(LockMapper.class);
        LockBean lock = new LockBean(null, "锁1", null);
        lm.saveLock(lock);
        session.commit();
    }
}
