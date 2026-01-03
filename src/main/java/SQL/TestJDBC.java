package SQL;



import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestJDBC {

    private static final String IP = "127.0.0.1";       // Địa chỉ máy cài SQL Server
    private static final String PORT = "1433";          // Cổng mặc định SQL Server
    private static final String DB = "JAVAUSER";      // Tên database
    private static final String USER = "sa";            // Tài khoản SQL Server
    private static final String PASSWORD = "123456";    // Mật khẩu SQL Server



    public static Connection connect() {
        Connection conn = null;
        try {
           
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");

           
            String connectionUrl = "jdbc:sqlserver://" + IP + ":" + PORT + 
                                   ";databaseName=" + DB + 
                                   ";encrypt=true;trustServerCertificate=true;";

           
            conn = DriverManager.getConnection(connectionUrl, USER, PASSWORD);
           
        } catch (Exception e) {
           
            e.printStackTrace();
        }
        return conn;
    }

    public static void testQuery() {
        try (Connection conn = connect()) {
            if (conn != null) {
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT TOP 5 TENSP, DonGia FROM SANPHAM");

                while (rs.next()) {
                    String tenMon = rs.getString("TENSP");
                    int gia = rs.getInt("DONGIA");
                    System.out.println(tenMon + " - " + gia);
                }

            } else {
                System.out.println("Kết nối thất bại (conn == null)");
            }
        } catch (Exception e) {
            System.out.println("Lỗi truy vấn: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    
        public static void main(String[] args) {
        testQuery();
    }
}
