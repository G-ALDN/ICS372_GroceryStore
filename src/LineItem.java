
public class LineItem {
    private Product product;
    private int quantiity;
    private double price;

    /**
     * Constructor
     * 
     * @param product   product in LineItem
     * @param quantiity quantity of said product
     */
    public LineItem(Product product, int quantiity) {
        this.product = product;
        this.quantiity = quantiity;
        price = product.getPrice() * quantiity;
    }

    /**
     * Getter Product
     * 
     * @return product referenced in LineItem
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Setter Quantity and Update Price
     * 
     * @param quantiity value to set quantity to
     */
    public void setQuantity(int quantiity) {
        this.quantiity = quantiity;
        price = product.getPrice() * quantiity;
    }

    /**
     * Getter Quantity
     * 
     * @return quantity value
     */
    public int getQuantity() {
        return quantiity;
    }

    /**
     * Getter Price
     * 
     * @return
     */
    public double getPrice() {
        return price;
    }

    /**
     * Print LineItem
     */
    public void print() {
        System.out.println(product.getProductName() + "       QTY: " + quantiity + "       Price: $" + price);
    }

}
