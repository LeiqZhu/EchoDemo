package pj.dataserver.main.db;


import pj.dataserver.main.bean.AirConStat;
import pj.dataserver.main.bean.FanStat;
import pj.dataserver.util.JacksonUtil;

import java.io.IOException;
import java.util.Map;

public class Dbsql {

    public static class FanSql{

        public static final String querySql = "SELECT * FROM demo.fans_status ORDER BY time DESC LIMIT 1";
        public static final String insertSql = "INSERT IGNORE INTO fans_status(stat,speed,shake,timer_on,timer_off,time) VALUES(?,?,?,?,?,?)";

        public static Object[] evlParam(Map<String,Object> map) throws IOException {
            FanStat fanStat = JacksonUtil.jackson.with2CamelCase()
                    .withIgnoreUnknowPro()
                    .withCamel2Lower()
                    .obj2Bean(map,FanStat.class);

            Object[] params = {
                    fanStat.getStat(),
                    fanStat.getSpeed(),
                    fanStat.getShake(),
                    fanStat.getTimerOn(),
                    fanStat.getTimerOff(),
                    fanStat.getTime()};
            return params;
        }
    }

    public static class AirConSql{

        public static final String querySql = "SELECT * FROM demo.aircon_status ORDER BY time DESC LIMIT 1";
        public static final String insertSql = "INSERT IGNORE INTO aircon_status(stat,module,temp,speed,shake," +
                "timer_on,timer_off,time) VALUES(?,?,?,?,?, ?,?,?)";

        public static Object[] evlParam(Map<String,Object> map) throws IOException {
            AirConStat airConStat = JacksonUtil.jackson.with2CamelCase()
                    .withIgnoreUnknowPro()
                    .withCamel2Lower()
                    .obj2Bean(map,AirConStat.class);

            Object[] params = {
                    airConStat.getStat(),
                    airConStat.getModule(),
                    String.valueOf(airConStat.getTemp()),
                    airConStat.getSpeed(),
                    airConStat.getShake(),

                    airConStat.getTimerOn(),
                    airConStat.getTimerOff(),
                    airConStat.getTime()};
            return params;
        }
    }
}
