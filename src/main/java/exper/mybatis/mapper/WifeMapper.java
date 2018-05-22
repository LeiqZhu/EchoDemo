package exper.mybatis.mapper;

import exper.mybatis.beans.WifeBean;

public interface WifeMapper {
    /**
     * 根据丈夫id查询妻子
     * @param id
     * @return
     * @throws Exception
     */
    public WifeBean selectWifeByHusbandId(int id) throws Exception;
}
