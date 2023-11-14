import java.util.ArrayList;
import java.time.*;

public class Transaction {
    int memberID;
    ArrayList<LineItem> lineItemList;
    int totalProducts;
    ZonedDateTime dateOfSale;
    double total;

    public Transaction(int memberID, ArrayList<LineItem> lineItemList, int totalProducts, double total) {
        this.memberID = memberID;
        this.lineItemList = lineItemList;
        this.totalProducts = totalProducts;
        this.total = total;
        dateOfSale = java.time.ZonedDateTime.now();
    }

    public int getMemberID() {
        return memberID;
    }

    public ZonedDateTime getDateOfSale() {
        return dateOfSale;
    }

    public double getTotal() {
        return total;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void print() {
        System.out.println("Transaction                        MemberID: " + memberID);
        System.out.println(".......................................................");
        for (LineItem l : lineItemList) {
            l.print();
        }
        System.out.println("\nTotal: " + total);
        System.out.println(
                "Date: " + dateOfSale.getYear() + "-" + dateOfSale.getMonthValue() + "-" + dateOfSale.getDayOfMonth());

    }

}
