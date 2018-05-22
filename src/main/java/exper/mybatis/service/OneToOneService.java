package exper.mybatis.service;


import exper.mybatis.beans.HusbandBean;
import exper.mybatis.beans.WifeBean;
import exper.mybatis.mapper.HusbandMapper;
import exper.mybatis.mapper.WifeMapper;
import exper.mybatis.tools.MybatisUtil;
import org.apache.ibatis.session.SqlSession;

public class OneToOneService {

    public static void main(String[] args) {
        selectHusbandAndWife();

    }


    private static void selectHusbandAndWife() {
        SqlSession session = MybatisUtil.getSession();
        HusbandMapper hm = session.getMapper(HusbandMapper.class);
        WifeMapper wm = session.getMapper(WifeMapper.class);
        try {
            HusbandBean husband = hm.selectHusbandAndWife(1);
            WifeBean wife = wm.selectWifeByHusbandId(1);
            System.out.println(wife);
            husband.setWife(wife);
            System.out.println(husband);
            session.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}