import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
public class CheckoutTests {
    /**
     * Covers use case 5: Checking out a members car and any tangential business processes (payment, etc).
     */
    private GroceryStore groceryStore = GroceryStore.instance();
    private Cart cart;
    private Member member;
    private Product product1;
    private ProductList productList;
    private ShipmentList shipments;
    private LineItem lineItem1;
    private LineItem lineItem2;
    private Member preston;
    private Member adan;
    private Member jackson;


    @Before
    public void setUp() {
        preston = new Member("Preston", "123 Other St. NE", "612-098-7654");
        groceryStore.addMember(preston);
        adan = new Member("Adan", "987 Something St. S", "612-111-1111");
        groceryStore.addMember(adan);
        jackson = new Member("Jackson", "North Pole", "612-420-1337");
        groceryStore.addMember(jackson);
        productList = new ProductList();
        shipments = new ShipmentList();
    }

    @Test
    public void testCartSetup(){
        boolean result = groceryStore.createCart(preston.getMemberID());
        assertTrue("Cart should be created successfully.",result);
    }

    @Test
    public void testAddItemToCart() {
        groceryStore.createCart(preston.getMemberID());
        Product product1 = new Product("Pizza", 98065, 5, 12.00, 2);
        groceryStore.addProductToCatalog(product1);
        boolean result = groceryStore.addProductToCart(product1.getProductID(), 2);
        assertTrue("Adding a item to cart should return true.", result);
    }

    @Test
    public void testCreateTransaction() {
        groceryStore.createCart(adan.getMemberID());
        Product product2 = new Product("Pizza", 98723, 5, 12.0, 2);
        groceryStore.addProductToCatalog(product2);
        groceryStore.addProductToCart(product2.getProductID(), 2);
        double change = groceryStore.finalizeCart(30.0);
        System.out.println(change);
        assertEquals("Customer should receive change for pruchase.", -6.0, change);
    }

    @Test
    public void testFinalizeCartInsufficientFunds() {
        groceryStore.createCart(jackson.getMemberID());
        Product product3 = new Product("Pizza", 98765, 5, 12.0, 2);
        groceryStore.addProductToCatalog(product3);
        groceryStore.addProductToCart(product3.getProductID(), 2);
        double change = groceryStore.finalizeCart(10.0);
        System.out.println(change);
        assertEquals("Insuffcient payment is received.", 14.0, change);
    }
}
