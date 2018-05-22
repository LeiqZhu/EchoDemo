package exper.mybatis.mapper;

import exper.mybatis.beans.KeyBean;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface KeyMapper {
    /**
     * 批量添加钥匙
     * @return
     * 提倡 这样使用 @Param("keys")
     */
    public int batchSaveKeys(@Param("keys")List<KeyBean> keys);
}
