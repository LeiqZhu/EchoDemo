package exper.presto;

import java.sql.*;

public final class PrestoJdbcUtils {
    static {
        try {
            Class.forName("com.facebook.presto.jdbc.PrestoDriver").newInstance();

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * 获取连接
     */
    public static Connection getConnection() throws SQLException {
        String url = "jdbc:presto://db.emr.aws.112gs.com:8889/hive/ad_logdata";
        String user = "hive";
        String password = "hive";

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

    public static void main(String[] args) {
        try {
            Connection connection = getConnection();
            System.out.println(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
