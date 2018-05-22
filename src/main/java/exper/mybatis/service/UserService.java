package exper.mybatis.service;

import exper.mybatis.beans.User;
import exper.mybatis.mapper.UserMapper;
import exper.mybatis.tools.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserService {
    
    public static void main(String[] args) {
        // insertUser();
        // deleteUser();
        // selectUserById();
        // selectAllUser();

        // batchInsertUser();
       // batchDeleteUser();
       //  countUser();
        pagerUser();
    }

    private static void pagerUser() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("username", "kitty");
        params.put("index", 0);//从第几页开始。mysql是从0开始的
        params.put("pageSize", 5);//每页显示的数据条数
        try {
            List<User> u = mapper.pagerUser(params);
            for (User User : u) {
                System.out.println("--------"+User);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void countUser() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        Map<String,Object> params = new HashMap<String,Object>();
        params.put("username", "kitty");
        int index = 0;
        params.put("index", index);//从第几页开始。mysql是从0开始的
        params.put("pageSize", 5);//每页显示的数据条数
        int count;
        try {
            count = mapper.countUser(params);
            System.out.println(count);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void batchDeleteUser() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        List<Integer> ids = new ArrayList<Integer>();
        for(int i = 4; i < 10; i ++){
            ids.add(i);
        }
        try {
            mapper.batchDeleteUser(ids);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static void batchInsertUser() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);

        List<User> users = new ArrayList<User>();
        for(int i = 0; i < 10; i ++){
            User user = new User("kitty"+i, "123456", 6000.0);
            users.add(user);
        }
        try {
            mapper.batchInsertUser(users);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 新增用户
     */
    private static void insertUser() {
        SqlSession session = MybatisUtil.getSession();
        UserMapper mapper = session.getMapper(UserMapper.class);
        User user = new User("懿", "1314520", 7000.0);
        try {
            mapper.insertUser(user);
            System.out.println(user.toString());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
    }


    /**
     * 删除用户
     */
    private static void deleteUser(){
        SqlSession session=MybatisUtil.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
            mapper.deleteUser(1);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
    }


    /**
     * 根据id查询用户
     */
    private static void selectUserById(){
        SqlSession session=MybatisUtil.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
            User user=    mapper.selectUserById(2);
            System.out.println(user.toString());

            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
    }

    /**
     * 查询所有的用户
     */
    private static void selectAllUser(){
        SqlSession session=MybatisUtil.getSession();
        UserMapper mapper=session.getMapper(UserMapper.class);
        try {
            List<User> user=mapper.selectAllUser();
            System.out.println(user.toString());
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
            session.rollback();
        }
    }


}
