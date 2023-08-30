// 商品类
class product{
    private String id; // 商品编号
    private String name; // 商品名称
    private String producer; // 生产厂家
    private String proTime; // 生产日期
    private String type; // 型号
    private double purPrice; // 进货价
    private double retPrice; // 零售价
    private int amount; // 数量

    product(){}

    public product(String id, String name, String producer, String productionDate, String type,
                   double purchasePrice, double retailPrice, int amount) {
        this.id = id;
        this.name = name;
        this.producer = producer;
        this.proTime = productionDate;
        this.type = type;
        this.purPrice = purchasePrice;
        this.retPrice = retailPrice;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProTime() {
        return proTime;
    }

    public void setProTime(String proTime) {
        this.proTime = proTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPurPrice() {
        return purPrice;
    }

    public void setPurPrice(double purPrice) {
        this.purPrice = purPrice;
    }

    public double getRetPrice() {
        return retPrice;
    }

    public void setRetPrice(double retPrice) {
        this.retPrice = retPrice;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void updateInfo(String name, String producer, String productionDate, String type,
                           double purchasePrice, double retailPrice, int amount) {
        this.name = name;
        this.producer = producer;
        this.proTime = productionDate;
        this.type = type;
        this.purPrice = purchasePrice;
        this.retPrice = retailPrice;
        this.amount = amount;
    }
    @Override
    public String toString() {
        return String.format("编号: %-5s, 商品名称: %-20s, 生产厂家: %-15s, 生产日期: %-15s, " +
                        "型号: %-20s, 零售价: %-10.2f, 数量: %-6d",
                id, name, producer, proTime, type, retPrice, amount);
    }
}