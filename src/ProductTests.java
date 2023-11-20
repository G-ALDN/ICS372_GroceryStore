import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ProductTests {
    /**
     * Covers use case 4: Adding a member and any tangential business processes.
     */
    private GroceryStore groceryStore = GroceryStore.instance();
    private Product product1;
    private Product product2;
    private Product product3;
    private ProductList productList;

    @Before
    public void setUp() {
        productList = new ProductList();
        product1 = new Product("Coke", 12345, 10, 1.99);
        product2 = new Product("Pepsi", 12346, 10, 1.99, 5);
        product3 = new Product("Coffee", 10231, 10, 7.99, 2);
    }

    @Test
    public void testAddProductEmpty() {
        assertTrue("No products should be in list.", productList.getProductList().isEmpty());
    }

    @Test
    public void testAddProductSuccess(){
        boolean result = groceryStore.addProductToCatalog(product1);
        assertTrue("Coke should be added to product list.", result);
    }


    @Test
    public void testAddProductDuplicate(){
        groceryStore.addProductToCatalog(product1);
        boolean result = groceryStore.addProductToCatalog(product1);
        assertFalse("Duplicate Product should not be allowed.", result);
    }

    @Test
    public void testGetProductInCatalogByID(){
        groceryStore.addProductToCatalog(product2);
        Product p = groceryStore.getProduct(product2.getProductID());
        assertNotNull("Product of that ID should exist in calendar.", p);
    }

    @Test
    public void testGetProductInCatalogByName(){
        groceryStore.addProductToCatalog(product2);
        Product p = groceryStore.getProduct(product2.getProductName());
        assertNotNull("Product of that name should exist in calendar.", p);
    }

    @Test
    public void testNoMatchingProductInCatalogByID(){
        Product p1 = groceryStore.getProduct(product3.getProductID());
        //System.out.println(p1.getProductID());
        assertNull("No product of that ID should exist in calendar.", p1);
    }

    @Test
    public void testNoMatchingProductInCatalogByName(){
        Product p2 = groceryStore.getProduct(product3.getProductName());
        assertNull("No product of that name should exist in calendar.", p2);
    }

    @Test
    public void testAddNullProduct(){
        boolean result = groceryStore.addProductToCatalog(null);
        assertTrue("Null products are ignored.", result);
    }


}