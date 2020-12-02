import java.math.BigDecimal;
import java.util.Random;

public class Goods {
    private static Random random = new Random();

    private int id;
    private String name;
    private BigDecimal single_order_price;
    private int goods_quantity;

    public Goods(int id, String name) {
        this.id = id;
        this.name = name;
        goods_quantity = random.nextInt(100);
        single_order_price = new BigDecimal(random.nextInt(1000));
    }

    public BigDecimal getSingle_order_price() {
        return single_order_price;
    }

    public void setSingle_order_price(BigDecimal single_order_price) {
        this.single_order_price = single_order_price;
    }

    public int getGoods_quantity() {
        return goods_quantity;
    }

    public void setGoods_quantity(int goods_quantity) {
        this.goods_quantity = goods_quantity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
