import java.util.ArrayList;

public class ProductList {
	
    private ArrayList<Product> productList = new ArrayList<>();
    private int totalProducts = 0;

    public Product getProduct(int productID){
        for(Product product : productList){
            if(product.getProductID() == productID){
                return product;
            }
        }
        return null;
    }

    // case insensitive
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

    public Product get(int index){
        try{
             return productList.get(index);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Product out of bounds of the ProductList array");
            return null;
        }
    }
    
    public boolean addProduct(Product product){
        boolean success = productList.add(product);
        if (success) {
            totalProducts++;
        }
        return success;
    }
    
    public void print(){
        System.out.println("Current Products in Catalog:");
        for (Product product : productList) {
            product.print();
        }
    }
}
