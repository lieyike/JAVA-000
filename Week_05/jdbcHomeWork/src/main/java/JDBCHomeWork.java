import java.sql.*;

public class JDBCHomeWork {



    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://127.0.0.1:3306/test?serverTimezone=UTC", "test", "123456")) {

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

    }


}

