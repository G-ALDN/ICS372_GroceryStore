import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
public class ShipmentTests {
    /**
     * Covers use case 7: Covers processing shipments, and product restocks.
     */
    private GroceryStore groceryStore = GroceryStore.instance();
    private Product product1;
    private Product product2;
    private Product product3;

    @Before
    public void setUp() {
        product1 = new Product("Coke", 12345, 10, 1.99);
        product2 = new Product("Pepsi", 12346, 4, 1.99, 1);
        product3 = new Product("Monster", 12333, 6, 1.99, 0);
        groceryStore.addProductToCatalog(product3);
        groceryStore.restockProduct(product3);
    }

    @Test
    public void testShipmentRestock(){
        boolean result = groceryStore.restockProduct(product3);
        assertTrue("Shipment should added to restock successfully.", result);
    }

    @Test
    public void testShipmentProcessing(){
        groceryStore.restockProduct(product3);
        boolean result = groceryStore.processShipment(product3.getProductID());
        assertTrue("Shipment should be processed back to store successfully.", result);
    }
}
