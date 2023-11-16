import java.util.ArrayList;

public class Cart {
    private int memberID;
    private ArrayList<LineItem> inCart;
    private int totalProducts;

    /**
     * Constructor
     * 
     * @param memberID - Cart linked to specific member using memberid
     */
    public Cart(int memberID) {
        this.memberID = memberID;
        inCart = new ArrayList<LineItem>();
        totalProducts = 0;
    }

    /**
     * Getter TotalProducts
     * 
     * @return total number of products
     */
    public int getTotalProducts() {
        return totalProducts;
    }

    /**
     * Add LineItem to cart during checkout
     * 
     * @param item - LineItem to add to cart
     * @return true if success or false if failure
     */
    public boolean addLineItemToCart(LineItem item) {
        if (inCart.add(item)) {
            totalProducts++;
            return true;
        }
        return false;
    }

    /**
     * Calculate sales from current cart
     * 
     * @return Cart total price
     */
    public double calculateSales() {
        double sales = 0;
        for (LineItem l : inCart) {
            sales += l.getPrice();
        }
        return sales;
    }

    /**
     * Transaction created from Cart
     * 
     * @return new Transaction object
     */
    public Transaction createTransaction() {
        return new Transaction(memberID, inCart, totalProducts, this.calculateSales());
    }
    
    
    public void print() {
        System.out.println("Cart                        MemberID: " + memberID);
        System.out.println(".......................................................");
        for (LineItem l : inCart) {
            l.print();
        }
        System.out.println("\nTotal: " + this.calculateSales());
    }

}
