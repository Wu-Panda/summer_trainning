import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class shopHistory {
    private String time; // 时间
    private ArrayList<ProductPurchase> products; // 购买的商品清单
    public shopHistory(ArrayList<product> products) {
        this.products = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.time = dateFormat.format(new Date());

        for (product p : products) {
            ProductPurchase purchase = new ProductPurchase(p.getName(), p.getAmount());
            this.products.add(purchase); // 存储购买信息
        }
    }
    public String getTime() {
        return time;
    }
    public ArrayList<ProductPurchase> getProducts() {
        return products;
    }
}
class ProductPurchase {
    private String name;
    private int quantity;

    public ProductPurchase(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public int getQuantity() {
        return quantity;
    }
}