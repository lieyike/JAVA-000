package org.example;

import org.apache.shardingsphere.driver.api.yaml.YamlShardingSphereDataSourceFactory;

import javax.sql.DataSource;
import java.io.File;
import java.util.HashMap;

public class App {

    public static void main(String[] args) throws Exception {

        File yamlFile = new File(ClassLoader.getSystemResource("config.yaml").getFile());
        HashMap<String, DataSource> dataSourceHashMap = new HashMap<String, DataSource>();
        YamlShardingSphereDataSourceFactory.createDataSource(dataSourceHashMap, yamlFile);
    }
}
