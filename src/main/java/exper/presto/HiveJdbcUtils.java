package exper.presto;

import java.sql.*;

public final class HiveJdbcUtils {

    public static void main(String[] args) {
        try {
            System.out.println(getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    static {
        try {
            Class.forName("org.apache.hive.jdbc.HiveDriver").newInstance();
            //Class.forName("com.amazon.hive.jdbc41.HS2Driver").newInstance();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取连接
     */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:hive2://db.emr.aws.112gs.com:10000/ad_log_temp";
        String user = "hive";
        String password = "";

        return DriverManager.getConnection(url, user, password);
    }

    /**
     * 释放连接等资源
     * 
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, Statement st, Connection conn) {
        if (rs != null)
            try {
                rs.close();
                rs = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        if (st != null)
            try {
                st.close();
                st = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        if (conn != null)
            try {
                conn.close();
                conn = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
    }

    /**
     * 释放连接等资源
     * 
     * @param rs
     * @param st
     * @param conn
     */
    public static void free(ResultSet rs, PreparedStatement st, Connection conn) {
        if (rs != null)
            try {
                rs.close();
                rs = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        if (st != null)
            try {
                st.close();
                st = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
        if (conn != null)
            try {
                conn.close();
                conn = null;
            }
            catch (SQLException e) {
                e.printStackTrace();
            }
    }
}
