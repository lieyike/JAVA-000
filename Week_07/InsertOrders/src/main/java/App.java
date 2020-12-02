import com.sun.org.apache.xpath.internal.operations.Or;

import java.math.RoundingMode;
import java.sql.*;
import java.util.HashSet;
import java.util.Set;

public class App {

    private static String jdbcUrl = "jdbc:mysql://127.0.0.1:3307/GoodsTransaction?serverTimezone=UTC&rewriteBatchedStatements=true";
    private static String user = "root";
    private static String password = "123456";
//    private static int times = 10;
//    private static int times = 1000; // 1000000;
    private static int times = 1000000; // 1000000;

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(jdbcUrl
                , user, password)) {

            if (conn != null) {
                System.out.println("Connected to the database!");
            } else {
                System.out.println("Failed to make connection!");
            }

            Statement deleteStatement = conn.createStatement();

            System.out.println("Delete order_details result: " + deleteStatement.execute("DELETE FROM `GoodsTransaction`.`order_details` WHERE (`Goods_goods_id` > '0')"));

            deleteStatement = conn.createStatement();
            System.out.println("Delete GoodsTransaction result: " + deleteStatement.execute("DELETE FROM `GoodsTransaction`.`Order` WHERE (`order_id` > '0')"));
//


            long startTime = System.currentTimeMillis();
            ways2(conn);
            long endTime = System.currentTimeMillis();
            System.out.println("duration " + (endTime - startTime));



//            PreparedStatement preparedStatement = conn.prepareStatement("begin");
//            preparedStatement.addBatch("delete from test where id = '3'");
//            preparedStatement.addBatch("insert into test values('3', 'C')");
//            preparedStatement.addBatch("update test set name = 'D' where id = '3'");
//            preparedStatement.addBatch("commit");
//            preparedStatement.addBatch("end");
//            int resultNum =  preparedStatement.executeUpdate();
//            System.out.println("resultNum " + resultNum );
        } catch (SQLException e) {
            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 1000条 花费 19,047 19秒, 100万 要 19,000 秒 要 5小时16分钟40秒
    public static void ways1(Connection conn) throws Exception {

        for (int i = 1; i <= times; i++) {
            Orders orders = getData(i);

            String User_user_id = String.valueOf(orders.getUser().getUser_id());
            String address = orders.getAddress();
            String photoNumber = orders.getPhotoNumber();
            String price = orders.getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString();
            String sql = "INSERT INTO GoodsTransaction.Order (`order_id`, `User_user_id`, `address`, `photonumber`, `status`, `total_price`) VALUES (?, ?, ?, ?, 'done', ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, orders.getId());
            preparedStatement.setString(2, User_user_id);
            preparedStatement.setString(3, address);
            preparedStatement.setString(4, photoNumber);
            preparedStatement.setString(5, price);

            int resultNum = preparedStatement.executeUpdate();
//            System.out.println("Orders resultNum " + resultNum);

            Set<Goods> goodsSet = orders.getGoodsSet();
            for (Goods goods: goodsSet) {
                String single_order_price = goods.getSingle_order_price().setScale(2, RoundingMode.HALF_UP).toPlainString();
                String goodsSql = "INSERT INTO `GoodsTransaction`.`order_details` (`Goods_goods_id`, `Order_order_id`, `goods_quntity`, `single_order_price`)" +
                        " VALUES ( ?, ?, ?, ?)";
                preparedStatement = conn.prepareStatement(goodsSql);
                preparedStatement.setInt(1, goods.getId());
                preparedStatement.setInt(2, orders.getId());
                preparedStatement.setInt(3, goods.getGoods_quantity());
                preparedStatement.setString(4, single_order_price);
                resultNum = preparedStatement.executeUpdate();
//                System.out.println("Goods resultNum " + resultNum);

            }

        }

    }
    // beofre add rewriteBatchedStatements=true 1000 1,984 ms  100万 about 33分钟20秒
    // after add rewriteBatchedStatements=true 1000 121 ms,  100万 40,930 ms
    public static void ways2(Connection conn) throws Exception {
        conn.setAutoCommit(false);
        String sql1 = "INSERT INTO GoodsTransaction.Order (`order_id`, `User_user_id`, `address`, `photonumber`, `status`, `total_price`) VALUES (?, ?, ?, ?, 'done', ?)";
        String sql2 = "INSERT INTO `GoodsTransaction`.`order_details` (`Goods_goods_id`, `Order_order_id`, `goods_quntity`, `single_order_price`) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement1 = conn.prepareStatement(sql1);
        PreparedStatement preparedStatement2 = conn.prepareStatement(sql2);
        for (int i = 1; i <= times; i++) {
            Orders orders = getData(i);

            String User_user_id = String.valueOf(orders.getUser().getUser_id());
            String address = orders.getAddress();
            String photoNumber = orders.getPhotoNumber();
            String price = orders.getPrice().setScale(2, RoundingMode.HALF_UP).toPlainString();

            preparedStatement1.setInt(1, orders.getId());
            preparedStatement1.setString(2, User_user_id);
            preparedStatement1.setString(3, address);
            preparedStatement1.setString(4, photoNumber);
            preparedStatement1.setString(5, price);
            preparedStatement1.addBatch();

            Set<Goods> goodsSet = orders.getGoodsSet();
            for (Goods goods: goodsSet) {
                String single_order_price = goods.getSingle_order_price().setScale(2, RoundingMode.HALF_UP).toPlainString();
                preparedStatement2.setInt(1, goods.getId());
                preparedStatement2.setInt(2, orders.getId());
                preparedStatement2.setInt(3, goods.getGoods_quantity());
                preparedStatement2.setString(4, single_order_price);
                preparedStatement2.addBatch();
            }

        }
        conn.setAutoCommit(true);
        int[] resultNum = preparedStatement1.executeBatch();
        int[] resultNum2 = preparedStatement2.executeBatch();
//        System.out.println("Orders resultNum " + resultNum);
    }

    public static Orders getData(int id) {
        User user1 = new User(1, "User1");
        Goods goods1 = new Goods(1, "apple");
        Set<Goods> set = new HashSet<>();
        set.add(goods1);
        Orders orders = new Orders(id, user1, set);

        return orders;
    }


}
