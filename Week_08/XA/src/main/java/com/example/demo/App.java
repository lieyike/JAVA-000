package com.example.demo;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;
import org.apache.shardingsphere.transaction.core.TransactionType;
import org.apache.shardingsphere.transaction.core.TransactionTypeHolder;

import javax.sql.DataSource;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class App {

    public static void main(String[] args) throws IOException, SQLException {

        File yamlFile = new File(ClassLoader.getSystemResource("config.yaml").getFile());
        DataSource dataSource = YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
        cleanData(dataSource);
        TransactionTypeHolder.set(TransactionType.XA);

        Connection conn = dataSource.getConnection();

        String sql = "insert into t_order (user_id, order_id) VALUES (?, ?);";

        System.out.println("First XA insert successful");
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (int i = 1; i < 11; i++) {
                statement.setLong(1, i);
                statement.setLong(2, i);
                statement.executeUpdate();
            }
            conn.commit();
            System.out.println("Second XA Start insert data");
        } catch (Exception e) {
            System.out.println("Second XA insert failed");
            conn.rollback();
        }


        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            conn.setAutoCommit(false);
            for (int i = 1; i < 11; i++) {
                statement.setLong(1, i + 5);
                statement.setLong(2, i + 5);
                statement.executeUpdate();
            }
            conn.commit();
            System.out.println("Second XA insert successful");
        } catch (Exception e) {
            System.out.println("Second XA insert failed");
            conn.rollback();
        } finally {
            conn.close();
        }
    }


    public static void cleanData(DataSource dataSource) {
        try (Connection connection = dataSource.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("delete from t_order;");
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
