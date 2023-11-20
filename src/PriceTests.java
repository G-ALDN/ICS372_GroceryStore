import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
public class PriceTests {
    /**
     * Covers use case 8: Price changes.
     */
    private GroceryStore groceryStore = GroceryStore.instance();
    private Product product1;

    @Before
    public void setUp() {
        product1 = new Product("Coke", 12345, 10, 1.99, 5);
        groceryStore.addProductToCatalog(product1);
    }

    @Test
    public void testChangePrice() {
        boolean result =  groceryStore.updatePrice(product1.getProductID(), 3.99);
        assertTrue("Price should be updated in catalog.", result);
    }

    @Test
    public void testInvalidProductID() {
        boolean result =  groceryStore.updatePrice(9, 3.99);
        assertFalse("No such product ID should exist in the catalog.", result);
    }

}
