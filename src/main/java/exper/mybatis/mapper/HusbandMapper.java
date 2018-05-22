package exper.mybatis.mapper;


import exper.mybatis.beans.HusbandBean;

public interface HusbandMapper {
    /**
     * 根据id查询丈夫信息
     * @param id
     * @return
     * @throws Exception
     */
    public HusbandBean selectHusbandById (int id) throws Exception;

    /**
     * 根据id查询丈夫与妻子信息
     * @param id
     * @return
     * @throws Exception
     */
    public HusbandBean selectHusbandAndWife(int id) throws Exception;

}
