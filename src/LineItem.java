
public class LineItem {
    Product product;
    int quantiity;
    double price;

    public LineItem(Product product, int quantiity) {
        this.product = product;
        this.quantiity = quantiity;
        price = product.getPrice() * quantiity;
    }

    public void setQuantity(int quantiity) {
        this.quantiity = quantiity;
        price = product.getPrice() * quantiity;
    }

    public int getQuantity() {
        return quantiity;
    }

    public double getPrice() {
        return price;
    }

    public void print() {
        System.out.println(product.getProductName() + " QTY: " + quantiity + " Price: $" + price);
    }

}
