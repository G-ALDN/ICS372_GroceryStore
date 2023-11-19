import java.util.ArrayList;

// Collection class for holding Product objects

public class ProductList {
	
    private ArrayList<Product> productList = new ArrayList<>();
    private int totalProducts = 0;

    /**
     * Get method to get a product with a specified productID
     * @param int productID - the product ID we are looking to find
     * @return Product if found, null if not found.
     * 
     */
    public Product getProduct(int productID){
        for(Product product : productList){
            if(product.getProductID() == productID){
                return product;
            }
        }
        return null;
    }

    /**
     * Get method to get a product with a specified product name * case insensitive *
     * @param String productName - the product name we are looking to find
     * @return Product if found, null if not found.
     * 
     */
    public Product getProduct(String productName){
        for(Product product : productList){
            if (product.getProductName().toLowerCase().equals(productName.toLowerCase())) {
                return product;
            }
        }
        return null;
    }
    
    public ArrayList<Product> getProductList() {
    	return this.productList;
    }

    /**
     * Get a product in the ProductList at a specified index.
     * @param int index - the index of the product in the list.
     * @return Product if exists, null if not exists.
     * 
     */
    public Product get(int index){
        try{
             return productList.get(index);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Product out of bounds of the ProductList array");
            return null;
        }
    }
    
    /**
     * Used for adding Products to the ProductList
     * @param Product product - the product object to be added.
     * @return boolean success indication.
     * 
     */
    public boolean addProduct(Product product){
        boolean success = productList.add(product);
        if (success) {
            totalProducts++;
        }
        return success;
    }
    
    /**
     * Print method for printing Product information for every
     * product in the list.
     */
    public void print(){
        System.out.println("Current Products in Catalog:");
        for (Product product : productList) {
            product.print();
        }
    }
}
