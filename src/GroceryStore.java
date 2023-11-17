import java.util.ArrayList;

public class GroceryStore {

	private MemberList members = new MemberList();
	private ProductList products = new ProductList();
	private TransactionList transactions = new TransactionList();
	private ShipmentList shipments = new ShipmentList();
	private Cart cart;
	private static GroceryStore groceryStore;

	/**
	 * Private for the singleton pattern Creates the catalog and member collection
	 * objects
	 */
	private GroceryStore() {
	}

	/**
	 * Supports the singleton pattern
	 * 
	 * @return the singleton object
	 */
	public static GroceryStore instance() {
		if (groceryStore == null) {
			return groceryStore = new GroceryStore();
		} else {
			return groceryStore;
		}
	}

	/**
	 * Attempts to add a new member to the MemberList.
	 * 
	 * @param Member to be added
	 * @return boolean success indicator
	 */
	public boolean addMember(Member member) {
		boolean success = members.addMember(member);
		return success;
	}

	/**
	 * Attempts to call the removeMember method in MemberList to remove a specified
	 * member with matching memberID.
	 * 
	 * @param int memberID to be removed
	 * @return boolean success indicator
	 */
	public boolean removeMember(int memberID) {
		boolean success = members.removeMember(memberID);
		return success;
	}

	/**
	 * Attempts to retrieve members from the members MemberList with a matching
	 * name.
	 * 
	 * @param String name, the name to use as the search parameter
	 * @return ArrayList of Members with matching names
	 */
	public ArrayList<Member> retrieveMembersByName(String name) {
		ArrayList<Member> memberList = members.getMemberList();
		ArrayList<Member> matchingMembers = new ArrayList<Member>();

		for (Member member : memberList) {
			// comparing by lowercase to get rid of case sensitivityd
			if (member.getMemberName().toLowerCase().equals(name.toLowerCase())) {
				matchingMembers.add(member);
			}
		}
		return matchingMembers;
	}

	/**
	 * Prints information of all of the current members in the MemberList
	 * If there are no members in the system currently, the user is notified.
	 */
	public void listAllMembers() {
		ArrayList<Member> memberList = members.getMemberList();
		if (memberList.isEmpty()) {
			System.out.println("There are currently no members in the system.");

		} else {
			for (Member member : memberList) {
				member.print();
			}
		}
	}

	/**
	 * Attempts to restock a product by adding the product to the ShipmentList.
	 * Quantity ordered should be 2x minimum restock quantity.
	 * 
	 * @param Product to be ordered
	 * @return boolean success indicator
	 */
	public boolean restockProduct(Product product) {
		boolean success = shipments.addProductOrder(product);
		return success;
	}

	/**
	 * Attempts to add a new product to the ProductList catalog.
	 * 
	 * @return boolean success indicator
	 */
	public boolean addProductToCatalog(Product product) {
		boolean success = products.addProduct(product);
		return success;
	}

	// get product by int product ID
	public Product getProduct(int productID) {
		return products.getProduct(productID);
	}

	// get product by String product name
	public Product getProduct(String productName) {
		return products.getProduct(productName);
	}

	/**
	 * Prints information of all of the current products in the ProductList
	 * If there are no products in the system currently, the user is notified.
	 */
	public void listAllProducts() {
		ArrayList<Product> productList = products.getProductList();
		if (productList.isEmpty()) {
			System.out.println("There are currently no products in the system.\n");

		} else {
			for (Product product : productList) {
				product.print();
			}
		}
	}

	public boolean createCart(int memberID) {

		Member m = members.getMember(memberID);
		if (m == null)
			return false;

		cart = new Cart(memberID);
		return true;
	}

	public Cart getCart() {
		return cart;
	}

	public boolean addProductToCart(int productID, int quantity) {
		Product product = products.getProduct(productID);
		if (product == null) {
			System.out.println("Unrecognizable Product ID. Try Again.");
			return false;
		}


		if (product.getCurrentStock() < quantity) {
			System.out.println("Product stock is insufficient for this order. Try Again.");
			return false;
		}
		if (product.getCurrentStock() - quantity >= product.getRestockAmount()) {
			shipments.addProductOrder(product);
		}
		for (LineItem l : cart.getInCart()) {
			if (l.getProduct().equals(product)) {
				if (l.getQuantity() + quantity > product.getCurrentStock()) {
					System.out.println("Product stock is insufficient for this order. Try Again.");
					return false;
				}
				l.setQuantity(l.getQuantity() + quantity);
				cart.print();
				return true;

			}
		}
		LineItem item = new LineItem(product, quantity);
		boolean success = cart.addLineItemToCart(item);

		cart.print();
		return success;
	}

	public double finalizeCart(double money) {
		double totalPrice = cart.calculateSales();
		if (money < totalPrice) {
			return totalPrice - money;
		}
		for (LineItem l : cart.getInCart()) {
			l.getProduct().setCurrentStock(l.getProduct().getCurrentStock() - l.getQuantity());
			if (l.getProduct().getCurrentStock() < l.getProduct().getRestockAmount()) {
				shipments.addProductOrder(l.getProduct());
			}
		}
		totalPrice -= money;
		Transaction finalTransaction = cart.createTransaction();
		transactions.addTransaction(finalTransaction);
		return totalPrice;

	}

	/**
	 * Processes shipments for products that are currently on order.
	 * If the product is not on order, the user is notified.
	 * 
	 * @param int productID
	 * @return boolean success indicator
	 */
	public boolean processShipment(int productID) {
		boolean orderFound = shipments.getOrderStatus(productID);

		if (orderFound) {
			Product product = products.getProduct(productID);
			System.out.println(product.getProductName() + " is on order.");
			System.out.println("------- Quantity on order: " + product.getRestockAmount() * 2 + " -------");
			// updating stock quantity.
			product.setCurrentStock(product.getCurrentStock() + (product.getRestockAmount() * 2));
			System.out.println("------- Stock updated. -------");
			// this product's shipment is now processed, so we remove the order from
			// shipments.
			shipments.removeProductOrder(productID);
			product.print(); // print updated details.
			return true;

		} else { // product was not on order
			return false;
		}
	}

	/**
	 * Lists info for all products that are currently on order in the shipments
	 * ShipmentList
	 * 
	 */
	public void listOutstandingOrders() {
		ArrayList<Product> productsOnOrder = shipments.getProductsOnOrder();

		if (productsOnOrder.isEmpty()) {
			System.out.println("There are currently no products on order.\n");
		} else {
			System.out.println("------------ Products on order: ------------\n");
			for (Product product : productsOnOrder) {
				System.out.println("----------------------------------");
				System.out.println("Name: " + product.getProductName());
				System.out.println("ID: " + product.getProductID());
				System.out.println("Current Stock: " + product.getCurrentStock());
				System.out.println("Restock Amount: " + product.getRestockAmount());
				System.out.println("Quantity on order: " + product.getRestockAmount() * 2);
				System.out.println("----------------------------------\n");
			}
		}
	}

	/**
	 * Attempts to update the price of a product in the catalog, and prints updated
	 * information on success.
	 * If the product does not exist, returns false.
	 * 
	 * @param int    productID to update
	 * @param double newPrice price to update
	 * @return boolean success indicator
	 */
	public boolean updatePrice(int productID, double newPrice) {
		Product product = products.getProduct(productID);

		if (product == null) {
			return false;
		} else {
			product.setPrice(newPrice);
			System.out.println("Price has been updated.");
			product.print();
			return true;
		}
	}

}
