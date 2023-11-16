
public class Product {

	private String productName;
	private int productID;
	private int currentStock;
	private int restockAmount;
	private double price;

	public Product(String productName, int productID, int restockAmount, int stock, double price) {
		this.productName = productName;
		this.productID = productID;
		this.restockAmount = restockAmount;
		this.price = price;
		this.currentStock = stock;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String name) {
		this.productName = name;
	}

	public int getProductID() {
		return productID;
	}

	public void setProductID(int id) {
		this.productID = id;
	}

	public int getCurrentStock() {
		return currentStock;
	}

	public void setCurrentStock(int currentStock) {
		this.currentStock = currentStock;
	}

	public int getRestockAmount() {
		return restockAmount;
	}

	public void setRestockAmount(int restockThreshold) {
		this.restockAmount = restockThreshold;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public void updateStock(int quantity) {
		this.currentStock += quantity;
	}

	public void print() {
		System.out.println("---------- Product Info ----------");
		System.out.println("Name: " + this.getProductName());
		System.out.println("ID: " + this.getProductID());
		System.out.println("Current Stock: " + this.getCurrentStock());
		System.out.println("Restock level: " + this.getRestockAmount());
		System.out.println("Unit Price: " + this.getPrice());
		System.out.println("----------------------------------\n");
	}
}
