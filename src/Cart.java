import java.util.ArrayList;

public class Cart {
    int memberID;
    ArrayList<LineItem> inCart;
    int totalProducts;

    public Cart(int memberID) {
        this.memberID = memberID;
        inCart = new ArrayList<LineItem>();
        totalProducts = 0;
    }

    public boolean addLineItemToCart(LineItem item) {
        if (inCart.add(item)) {
            totalProducts++;
            return true;
        }
        return false;
    }

    public double calculateSales() {
        double sales = 0;
        for (LineItem l : inCart) {
            sales += l.getPrice();
        }
        return sales;
    }

    public Transaction createTransaction() {
        return new Transaction(memberID, inCart, totalProducts, this.calculateSales());
    }

}
