package exper.mybatis.mapper;

import exper.mybatis.beans.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface UserMapper {
    /**
     * 新增用戶
     * @param user
     * @return
     * @throws Exception
     */
    public int insertUser(@Param("user") User user) throws Exception;
    /**
     * 修改用戶
     * @param user
     * @param id
     * @return
     * @throws Exception
     */
    public int updateUser (@Param("u") User user,@Param("id") int id) throws Exception;
    /**
     * 刪除用戶
     * @param id
     * @return
     * @throws Exception
     */
    public int deleteUser(int id) throws Exception;
    /**
     * 根据id查询用户信息
     * @param id
     * @return
     * @throws Exception
     */
    public User selectUserById(int id) throws Exception;
    /**
     * 查询所有的用户信息
     * @return
     * @throws Exception
     */
    public List<User> selectAllUser() throws Exception;

    /**
     * 批量增加用户
     * @param users
     * @return
     * @throws Exception
     */
    public int batchInsertUser(@Param("users") List<User> users) throws Exception;

    /**
     * 批量删除
     * @param list
     * @return
     * @throws Exception
     */
    public int batchDeleteUser(@Param("list") List<Integer> list) throws Exception;

    /**
     * 分页查询
     * @param params
     * @return
     * @throws Exception
     */
    public List<User> pagerUser(Map<String, Object> params) throws Exception;

    public int countUser(Map<String, Object> params) throws Exception;
}
