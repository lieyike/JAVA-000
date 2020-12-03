package org.example;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

public class App {

    public static void main(String[] args) throws Exception {
        File yamlFile = new File(ClassLoader.getSystemResource("config.yaml").getFile());
        DataSource dataSource =  YamlShardingSphereDataSourceFactory.createDataSource(yamlFile);
        Connection connection = dataSource.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement("select * from GoodsTransaction.Goods");

        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            int id = resultSet.getInt(1);
            String name = resultSet.getString(2);
            System.out.println("id: " + id + " name: " + name );
        }

    }
}
