public class Order {
    private int id;
    private double price;
    private String purchase;
    private String comments;
    private String username;
    private String password;

    public Order(int id, double price, String purchase, String comments, String username, String password) {
        this.id = id;
        this.price = price;
        this.purchase = purchase;
        this.comments = comments;
        this.username = username;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public String getPurchase() {
        return purchase;
    }

    public String getComments() {
        return comments;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

}
