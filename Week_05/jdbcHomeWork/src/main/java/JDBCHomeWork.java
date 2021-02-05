import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.*;

public class JDBCHomeWork {

    private static String jdbcUrl = "jdbc:mysql://127.0.0.1:3307/test?serverTimezone=UTC";
    private static String user = "root";
    private static String password = "123456";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl
                , user, password)) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }



            Statement statement = conn.createStatement();

            ResultSet resultSet = statement.executeQuery("select * from test");

            while(resultSet.next()) {
               int id =  resultSet.getInt(1);
               String name =  resultSet.getString(2);
               System.out.println("id: " + id + " name: " + name);
            }

            Statement deleteStatement = conn.createStatement();
            System.out.println("result: " + deleteStatement.execute("delete from test where id = '3'"));

            Statement insertStatement = conn.createStatement();
            System.out.println("result: " + insertStatement.execute("insert into test values('3', 'C')"));

            Statement updateStatement = conn.createStatement();
            System.out.println("result " + updateStatement.execute("update test set name = 'D' where id = '3'"));

            PreparedStatement preparedStatement = conn.prepareStatement("begin");
            preparedStatement.addBatch("delete from test where id = '3'");
            preparedStatement.addBatch("insert into test values('3', 'C')");
            preparedStatement.addBatch("update test set name = 'D' where id = '3'");
            preparedStatement.addBatch("commit");
            preparedStatement.addBatch("end");
            int resultNum =  preparedStatement.executeUpdate();
            System.out.println("resultNum " + resultNum );

        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }


        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(user);
        config.setPassword(password);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource dataSource = new HikariDataSource(config);

        try {
            Connection connection = dataSource.getConnection();

            ResultSet resultSet = connection.createStatement().executeQuery("select * from test");

            while(resultSet.next()) {
                int id =  resultSet.getInt(1);
                String name =  resultSet.getString(2);
                System.out.println("id: " + id + " name: " + name);
            }

            PreparedStatement deleted = connection.prepareStatement("delete from test where id = '3'");
            System.out.println("result: " + deleted.executeUpdate());

            PreparedStatement insert = connection.prepareStatement("insert into test values('3', 'C')");
            System.out.println("result: " + insert.executeUpdate());

            PreparedStatement update = connection.prepareStatement("update test set name = 'D' where id = '3'");
            System.out.println("result: " + update.executeUpdate());

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}

