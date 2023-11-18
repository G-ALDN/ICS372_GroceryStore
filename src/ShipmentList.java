import java.util.ArrayList;

public class ShipmentList {
	
    private ArrayList<Product> productsOnOrder =  new ArrayList<>();
    private int totalOrders = 0;

    public boolean getOrderStatus(String productName){
        for(Product product : productsOnOrder){
            if (product.getProductName().equals(productName)) {
                return true;
            }
        }
        return false;
    }

    public boolean getOrderStatus(int productID){
        for(Product product : productsOnOrder){
            if (product.getProductID() == productID) {
                return true;
            }
        }
        return false;
    }
    
    public ArrayList<Product> getProductsOnOrder() {
    	return this.productsOnOrder;
    }

    public Product get(int index){
        try{
            return productsOnOrder.get(index);
        }
        catch (IndexOutOfBoundsException e){
            System.out.println("Product out of bounds of the ProductsOnOrder array");
            return null;
        }
    }

    public boolean addProductOrder(Product product){
        boolean success = productsOnOrder.add(product);
        if (success) {
            totalOrders++;
        }
        return success;
    }

    public void removeProductOrder(int id){
        boolean success = productsOnOrder.removeIf(product -> product.getProductID() == id);
        if (success)
            totalOrders--;
    }

    public void print(){
        System.out.println("Current Products on Order:");
        for (Product product : productsOnOrder) {
            product.print();
        }
    }
}
