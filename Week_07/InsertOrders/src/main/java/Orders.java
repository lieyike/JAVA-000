import java.math.BigDecimal;
import java.util.Random;
import java.util.Set;

public class Orders {
    static Random random = new Random();

    private int id;
    private User user;
    private Set<Goods> goodsSet;
    private String address = "Address1";
    private String photoNumber = "12345";
    private String status = "Done";
    private BigDecimal price;

    public Orders(int id, User user, Set<Goods> goods) {
        this.id = id;
        this.user = user;
        this.goodsSet = goods;
        price = new BigDecimal(random.nextInt(1000));
    }

    public static Random getRandom() {
        return random;
    }

    public static void setRandom(Random random) {
        Orders.random = random;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Goods> getGoodsSet() {
        return goodsSet;
    }

    public void setGoodsSet(Set<Goods> goodsSet) {
        this.goodsSet = goodsSet;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(String photoNumber) {
        this.photoNumber = photoNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
