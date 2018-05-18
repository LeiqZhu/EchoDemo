package pj.dataserver.main;

import io.netty.handler.codec.http.FullHttpResponse;
import pj.dataserver.main.db.Dbsql;
import pj.dataserver.main.db.Dbsql.FanSql;
import pj.dataserver.main.db.Dbsql.AirConSql;
import pj.dataserver.server.RequestHandler;
import pj.dataserver.util.Convert;
import pj.dataserver.util.JdbcUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OperationServer{

    public static class UpdateStatHandler extends RequestHandler {
        @Override
        public void post() {
            Map<String, Object> reqMap = this.getPostAttrs();
            operate(reqMap);

            write(sendMap);
        }

        public void operate(Map<String, Object> reqMap){
            int device = Convert.toInt(reqMap.get("device"));

            try {
                switch (device){
                    case 1:
                        JdbcUtil.jdbcUtil.insert(Dbsql.FanSql.insertSql,FanSql.evlParam(reqMap));
                        break;
                    case 2:
                        JdbcUtil.jdbcUtil.insert(AirConSql.insertSql,AirConSql.evlParam(reqMap));
                        break;
                    case 3:
                        JdbcUtil.jdbcUtil.insert(AirConSql.insertSql,AirConSql.evlParam(reqMap));
                        break;
                    case 4:
                        JdbcUtil.jdbcUtil.insert(AirConSql.insertSql,AirConSql.evlParam(reqMap));
                        break;
                    case 5:
                        JdbcUtil.jdbcUtil.insert(AirConSql.insertSql,AirConSql.evlParam(reqMap));
                        break;
                    case 6:
                        JdbcUtil.jdbcUtil.insert(AirConSql.insertSql,AirConSql.evlParam(reqMap));
                        break;
                    default:
                            break;
                }
                sendMap.put("msg","");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void addServerHeader(final FullHttpResponse response) {
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.headers().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.headers().set("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
        }
    }

    public static class GetStatHandler extends RequestHandler{
        @Override
        public void post() {
            Map<String, Object> reqMap = this.getPostAttrs();
            operate(reqMap);

            write(sendMap);
        }

        public void operate(Map<String, Object> reqMap){
            int device = Convert.toInt(reqMap.get("device"));

            Map<String,Object> resultMap = new HashMap<>();
            try {
                switch (device){
                    case 1:
                        resultMap = JdbcUtil.jdbcUtil.queryOne(FanSql.querySql,new Object[]{});
                        break;
                    case 2:
                        resultMap = JdbcUtil.jdbcUtil.queryOne(AirConSql.querySql,new Object[]{});
                        break;
                    default:
                        break;
                }
                sendMap.putAll(resultMap);
                sendMap.put("msg","");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void addServerHeader(final FullHttpResponse response) {
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.headers().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.headers().set("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
        }
    }

    public static class AllStatHandler extends RequestHandler{
        @Override
        public void post() {
            Map<String, Object> reqMap = this.getPostAttrs();
            operate(reqMap);

            write(sendMap);
        }

        public void operate(Map<String, Object> reqMap){
            String tableName = checkDevice(reqMap);
            String start = Convert.toString(reqMap.get("start"));
            String end = Convert.toString(reqMap.get("end"));
            String sqlFormat = "SELECT * FROM %s WHERE time between '%s' AND '%s' ORDER BY time ASC";
            String sql = String.format(sqlFormat,tableName,start,end);
            System.out.println(sql);

            List<Map<String,Object>> resultMapList = new ArrayList<>();
            try {
                resultMapList = JdbcUtil.jdbcUtil.getAllMapList(sql);
                sendMap.put("list",resultMapList);
                sendMap.put("msg","");
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected void addServerHeader(final FullHttpResponse response) {
            response.headers().set("Access-Control-Allow-Origin", "*");
            response.headers().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            response.headers().set("Access-Control-Allow-Headers", "X-Requested-With, Content-Type");
        }
    }

    public static String checkDevice(Map<String, Object> reqMap){
        int device = Convert.toInt(reqMap.get("device"));
        String tableName = "";
        switch (device){
            case 1:
                tableName = "fans_status";
                break;
            case 2:
                tableName = "aircon_status";
                break;
            case 3:
                tableName = "temp_sensor";
                break;
            case 4:
                tableName = "light_sensor";
                break;
            case 5:
                tableName = "carbon_sensor";
                break;
            case 6:
                tableName = "light_sensor";
                break;
            case 7:
                tableName = "forma_sensor";
                break;
            case 8:
                tableName = "outlet";
                break;
            default:
                    break;
        }
        return tableName;
    }

    public static void main(String[] args) {
        String sql = "SELECT * FROM %s WHERE time between %s AND %s ORDER BY time ASC";
        String s = String.format(sql,"abdsa","dsa","aaa");
        System.out.println(s);
    }
}
