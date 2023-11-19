import java.util.ArrayList;
import java.time.*;

public class Transaction {
    int memberID;
    ArrayList<LineItem> lineItemList;
    int totalProducts;
    ZonedDateTime dateOfSale;
    double total;

    /**
     * Constructor
     * 
     * @param memberID      memberid associated with transaction
     * @param lineItemList  list of items in transaction
     * @param totalProducts totalProducts in transaction
     * @param total         total price of transaction
     */
    public Transaction(int memberID, ArrayList<LineItem> lineItemList, int totalProducts, double total) {
        this.memberID = memberID;
        this.lineItemList = lineItemList;
        this.totalProducts = totalProducts;
        this.total = total;
        dateOfSale = java.time.ZonedDateTime.now();
    }

    /**
     * Getter Member ID
     * 
     * @return int value for memberid
     */
    public int getMemberID() {
        return memberID;
    }

    /**
     * Getter Date of Transaction
     * 
     * @return ZonedDateTime object
     */
    public ZonedDateTime getDateOfSale() {
        return dateOfSale;
    }

    /**
     * Getter Total Price
     * 
     * @return total price amount
     */
    public double getTotal() {
        return total;
    }

    /**
     * Getter Total Products
     * 
     * @return number of total products
     */
    public int getTotalProducts() {
        return totalProducts;
    }

    /**
     * Print Transaction
     */
    public void print() {
        System.out.println("Transaction                                        MemberID: " + memberID);
        System.out.println("................................................................");
        for (LineItem l : lineItemList) {
            l.print();
        }
        System.out.println("\nTotal: $" + total);
        System.out.println(
                "Date: " + dateOfSale.getYear() + "-" + dateOfSale.getMonthValue() + "-" + dateOfSale.getDayOfMonth());

    }

}
