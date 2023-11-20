import java.time.ZonedDateTime;
import java.io.*;
import java.util.*;

/**
 * GroceryStore is a facade class that handles operations that
 * are called from the GroceryStoreUI user interface.
 */


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
		return members.addMember(member);
	}

	/**
	 * Attempts to call the removeMember method in MemberList to remove a specified
	 * member with matching memberID.
	 * 
	 * @param int memberID to be removed
	 * @return boolean success indicator
	 */
	public boolean removeMember(int memberID) {
		return members.removeMember(memberID);
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
		ArrayList<Member> matchingMembers = new ArrayList<>();

		for (Member member : memberList) {
			// comparing by lowercase to get rid of case sensitivity
			if (member.getMemberName().equalsIgnoreCase(name)) {
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
		return shipments.addProductOrder(product);
	}

	/**
	 * Attempts to add a new product to the ProductList catalog.
	 * 
	 * @return boolean success indicator
	 */
	public boolean addProductToCatalog(Product product) {
		return products.addProduct(product);
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

	/**
	 * Initiates new Cart
	 * 
	 * @param memberID Member ID linked to Cart
	 * @return boolean valid Member ID checker
	 */
	public boolean createCart(int memberID) {

		Member m = members.getMember(memberID);
		if (m == null)
			return false;

		cart = new Cart(memberID);
		return true;
	}

	/**
	 * Getter for Cart
	 * 
	 * @return Cart object
	 */
	public Cart getCart() {
		return cart;
	}

	/**
	 * Add LineItems to Current Cart using Products and Quantities
	 * Checks Product Stock before adding, if insufficient it fails
	 * If current Product already exists in Cart, quantity gets updated in cart
	 * Fails if invalid Product ID
	 * 
	 * @param productID product to be added
	 * @param quantity  quantity of product to be added
	 * @return boolean success indicator
	 */
	public boolean addProductToCart(int productID, int quantity) {
		Product product = products.getProduct(productID);
		if (product == null) {
			System.out.println("Unrecognizable Product ID. Try Again.");
			return false;
		}
		if (quantity <= 0) {
			System.out.println("Enter Valid Quantity.");
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

	/**
	 * Finalize Transaction
	 * Money is provided until the amount is sufficient to cover transaction
	 * Items are reordered if their stock falls too low
	 * Transaction is created and saved
	 * 
	 * @param money Money used to cover transaction;
	 * @return Remaining Balance or Change if amount is negative
	 */
	public double finalizeCart(double money) {
		double totalPrice = cart.calculateSales();
		if (money < totalPrice) {
			return totalPrice - money;
		}
		for (LineItem l : cart.getInCart()) {
			l.getProduct().setCurrentStock(l.getProduct().getCurrentStock() - l.getQuantity());
			if (l.getProduct().getCurrentStock() <= l.getProduct().getRestockAmount()) {
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

	/**
	 * Prints Transactions in Grocery Store in between two dates
	 * 
	 * @param d1 Date 1
	 * @param d2 Date 2
	 * @return boolean success indicator
	 */
	public boolean printTransactions(ZonedDateTime d1, ZonedDateTime d2) {
		if (d1.isAfter(d2)) {
			System.out.println("Date 1 needs to be before Date 2. Try Again.\n");
			return false;
		}
		ArrayList<Transaction> tArr = transactions.getTransactionsByDate(d1, d2);
		if (tArr.isEmpty()) {
			System.out.println("No Transactions Found.");
			return true;
		}
		for (Transaction t : tArr) {
			t.print();
		}
		return true;
	}

	public boolean save(){
		String working_dir = System.getProperty("user.dir");
		String fileName = "grocery_store.txt"; // Specifies a default file name.
		Scanner scanner = new Scanner(System.in);
		File file;
		int newFileNameCounter = 1;
		boolean nameSuccessful = false;
		boolean saveSuccessful = false;

		// Attempt to query file name to save as from user in terminal
		while(!nameSuccessful){
			System.out.print("Save file as: ");
			try {
				fileName = scanner.nextLine();
			}
			catch (IOError ioe) {
				System.out.println(ioe.getMessage());
				ioe.printStackTrace();
				return false;
			}
			fileName = fileName.trim();

			if(!fileName.contains(".txt")){
				System.out.print("Not a valid file extension. Please use .txt.");
			} else if (fileName.isEmpty()) {
				System.out.println("Invalid or Empty file passed.");
			}
			else{
				nameSuccessful = true;
			}
		}
		// Attempt to save to current working directory
		while(!saveSuccessful){
			try{
				file = new File(working_dir + File.separator + fileName);

				if(!file.createNewFile()){
					String tempString;
					tempString = fileName.substring(0, fileName.indexOf("."));
					fileName = tempString + newFileNameCounter + ".txt";
					System.out.println("File already exists. Attempting to save as " + (fileName));
					newFileNameCounter++;
				}
				else{
					saveSuccessful = true;
				}
			}
			catch (IOException ioe){
				System.out.println(ioe.getMessage());
				ioe.printStackTrace();
				return false;
			}
		}

		FileWriter saveState;

		try{
			saveState = new FileWriter(fileName);

			for(Member member : members.getMemberList())
			{
				saveState.write("Member|");
				// Member Attributes: Name (First Last), MemberID (3 digit int), Address, Phone, Enrollment Date
				saveState.write(member.getMemberName() + "|" + Integer.toString(member.getMemberID()) +
						"|" + member.getAddress() + "|" + member.getPhoneNumber() + "|" +
						member.getEnrollmentDate() + "\n");
			}

			for(Product product : products.getProductList())
			{
				saveState.write("Product|");
				// Product Attributes: productName, productID, restockAmount, price, currentStock
				saveState.write(product.getProductName() + "|" + Integer.toString(product.getProductID()) +
						"|" + Integer.toString(product.getRestockAmount()) + "|" + Double.toString(product.getPrice()) + "|" +
						Integer.toString(product.getCurrentStock()) + "\n");
			}

			for(Product shipment : shipments.getProductsOnOrder())
			{
				saveState.write("Shipment|");
				// Shipment Attributes: productName, productID, price, currentStock
				saveState.write(shipment.getProductName() + "|" + Integer.toString(shipment.getProductID()) +
						"|" + Double.toString(shipment.getPrice()) + Integer.toString(shipment.getCurrentStock()) +
						"\n");
			}

			for(Transaction transaction : transactions.getAllTransactions())
			{
				saveState.write("Transaction|");
				// Transactions Attributes: memberID,{List of LineItems}, Total Products, date
				for(LineItem lineItem : transaction.lineItemList){
					saveState.write(Integer.toString(transaction.memberID) + "|" +
							lineItem.getProduct().getProductName() + "|" +
							Integer.toString(lineItem.getProduct().getProductID()) + "|" +
							Integer.toString(lineItem.getProduct().getRestockAmount()) + "|" +
							Double.toString(lineItem.getProduct().getPrice()) + "|" +
							Integer.toString(lineItem.getProduct().getCurrentStock()) + "|" +
							Integer.toString((int)lineItem.getQuantity()) + "|" +
							Double.toString(lineItem.getPrice()) + "|" +
							transaction.totalProducts + "|" +
							transaction.getTotal() + "*");
				}
				saveState.write("\n");
			}
			saveState.close();
		}
		catch (IOException ioe){
			System.out.println(ioe.getMessage());
			ioe.printStackTrace();
			return false;
		}
		// Now need to write MemberList, ProductList, TransactionList, ShipmentList to file
		return true;
	}

	public boolean open(File file) {
		try {
			Scanner stateText = new Scanner(file);
			while (stateText.hasNextLine()) {
				String line = stateText.nextLine();
				StringTokenizer tokenizer = new StringTokenizer(line, "|*\n\r\f");
				String token = tokenizer.nextToken();

				if(token.equalsIgnoreCase("member")){
					parseMemberFromFile(tokenizer);
				} else if (token.equalsIgnoreCase("product")) {
					parseProductFromFile(tokenizer);
				} else if(token.equalsIgnoreCase("shipment")){
					parseShipmentFromFile(tokenizer);
				} else if(token.equalsIgnoreCase("transaction")){
					parseTransactionFromFile(tokenizer);
				}
				else{
					System.out.println("Unknown Text State Parameter \"" + token + "\"");
					return false;
				}
			}
			stateText.close();
			} catch(FileNotFoundException e){
				System.out.println("A file error has occurred.");
				System.out.println(e.getMessage());
				e.printStackTrace();
				return false;
			}


			return true;
		}

	private void parseShipmentFromFile(StringTokenizer tokens) {
		// Product name, product id, price, current stock
		int argCounter = 1;
		String name = null;
		int id = -1;
		int currentStock = -1;
		double price = -1;

		while (tokens.hasMoreTokens()) {
			if (argCounter > 4) {
				System.out.println("Invalid file format: Too many Product/Shipment parameters.");
				break;
			}
			// Parameter Order: Name, ID, Address, phone
			String param = tokens.nextToken();
			//System.out.println(param);

			switch (argCounter) {
				case 1:
					name = param;
					argCounter++;
					break;
				case 2:
					id = Integer.parseInt(param);
					argCounter++;
					break;
				case 3:
					price = Double.parseDouble(param);
					argCounter++;
					break;
				case 4:
					currentStock = Integer.parseInt(param);
					argCounter++;
					break;
			}
		}

		if(name == null || id == -1 || price == -1 || currentStock == -1){
			System.out.println("Null pointer in parameters, check to see that file structure is correct: [..],Name,ID,Price,Current Stock");
		}
		else{
			Product product = new Product(name, id, 0, price, currentStock);
			GroceryStore.instance().addProductToCatalog(product);
		}
	}

	private void parseTransactionFromFile(StringTokenizer tokens) {
		// trans.MemberID, line.productName, line.ID, line.restockAmount, line.price, line.currentStock, line.quantity,
		// line.total price

		while (tokens.hasMoreTokens()) {
			int memberID = -1;
			String productName = null;
			int productID = -1;
			int restock = -1;
			double productPrice = -1;
			int currentStock = -1;
			int quantity = -1;
			double linePrice = -1;
			double totalPrice = -1;
			int totalProducts = -1;
			int argCounter = 1;
			LineItem line;
			ArrayList<LineItem> lineItems = new ArrayList<>();
			boolean readFlag = true;

			while (readFlag && tokens.hasMoreTokens()) {
				String param = tokens.nextToken();

				if (argCounter > 10) {
					System.out.println("Invalid file format: Too many Transaction parameters.");
					break;
				}
				// Parameter Order: Name, ID, Address, phone

				switch (argCounter) {
					case 1:
						memberID = Integer.parseInt(param);
						argCounter++;
						break;
					case 2:
						productName = param;
						argCounter++;
						break;
					case 3:
						productID = Integer.parseInt(param);
						argCounter++;
						break;
					case 4:
						restock = Integer.parseInt(param);
						argCounter++;
						break;
					case 5:
						productPrice = Double.parseDouble(param);
						argCounter++;
						break;
					case 6:
						currentStock = Integer.parseInt(param);
						argCounter++;
						break;
					case 7:
						quantity = Integer.parseInt(param);
						argCounter++;
						break;
					case 8:
						linePrice = Double.parseDouble(param);
						argCounter++;
						break;
					case 9:
						totalProducts = Integer.parseInt(param);
						argCounter++;
						break;
					case 10:
						totalPrice = Double.parseDouble(param);
						argCounter++;
						readFlag = false;
						break;
				}
			}

			if (memberID == -1 || productName == null || productID == -1 || restock == -1 || productPrice == -1 || currentStock == -1 ||
					quantity == -1 || totalPrice == -1) {
				System.out.println("Null pointer in parameters, check to see that file structure is correct: [..],Name,ID,Restock,Price,Current Stock");
			} else {
				Product product = new Product(productName, productID, restock, productPrice, currentStock);
				line = new LineItem(product, quantity);
				lineItems.add(line);
			}

			Transaction transaction = new Transaction(memberID, lineItems, totalProducts, totalPrice);
			GroceryStore.instance().transactions.addTransaction(transaction);
		}

	}

	private void parseProductFromFile(StringTokenizer tokens) {
		// Product name, product id, current stock, restock, price
		int argCounter = 1;
		String name = null;
		int id = -1;
		int currentStock = -1;
		int restock = -1;
		double price = -1;

		while (tokens.hasMoreTokens()) {
			if (argCounter > 5) {
				System.out.println("Invalid file format: Too many Product parameters.");
				break;
			}
			// Parameter Order: Name, ID, Address, phone
			String param = tokens.nextToken();
			//System.out.println(param);

			switch (argCounter) {
				case 1:
					name = param;
					argCounter++;
					break;
				case 2:
					id = Integer.parseInt(param);
					argCounter++;
					break;
				case 3:
					restock = Integer.parseInt(param);
					argCounter++;
					break;
				case 4:
					price = Double.parseDouble(param);
					argCounter++;
					break;
				case 5:
					currentStock = Integer.parseInt(param);
					argCounter++;
					break;
			}
		}

		if(name == null || id == -1 || restock == -1 || price == -1 || currentStock == -1){
			System.out.println("Null pointer in parameters, check to see that file structure is correct: [..],Name,ID,Restock,Price,Current Stock");
		}
		else{
			Product product = new Product(name, id, restock, price);
			product.setCurrentStock(currentStock);
			GroceryStore.instance().addProductToCatalog(product);
		}
	}

	public void parseMemberFromFile(StringTokenizer tokens) {
		int argCounter = 1;
		String name = null;
		int id = -1;
		String address = null;
		String phone = null;
		ZonedDateTime enrollmentDate = null;

		while (tokens.hasMoreTokens()) {
			if (argCounter > 5) {
				System.out.println("Invalid file format: Too many Member parameters.");
				break;
			}
			// Parameter Order: Name, ID, Address, phone
			String param = tokens.nextToken();
			//System.out.println(param);

			switch (argCounter) {
				case 1:
					name = param;
					argCounter++;
					break;
				case 2:
					id = Integer.parseInt(param);
					argCounter++;
					break;
				case 3:
					address = param;
					argCounter++;
					break;
				case 4:
					phone = param;
					argCounter++;
					break;
				case 5:
					enrollmentDate = ZonedDateTime.parse(param);
					argCounter++;
					break;
			}
		}

		if(name == null || id == -1 || address == null || phone == null){
			System.out.println("Null pointer in parameters, check to see that file structure is correct: [..],Name,ID,Address,Phone");
		}
		else{
			Member member = new Member(name, address, phone);
			member.setMemberID(id);
			member.setEnrollmentDate(enrollmentDate);
			GroceryStore.instance().addMember(member);
		}
	}

	public void createTestEnvironment () {
		ArrayList<String> firstName = new ArrayList<>(Arrays.asList("Liam", "Olivia", "Noah", "Ava", "Sophia", "Mia", "Jackson", "Aiden", "Lucas", "Sophia"));
		ArrayList<String> lastName = new ArrayList<>(Arrays.asList("Smith", "Johnson", "Brown", "Davis", "Jones", "Miller", "Wilson", "Moore", "Taylor", "Anderson"));
		ArrayList<String> productName = new ArrayList<>(Arrays.asList("Zesty Zucchini Chips", "Mango Tango Smoothie",
                "Savory Sea Salt Popcorn", "ChocoCherry Bliss Bars", "Pineapple Paradise Punch", "Crispy Caramel Corn",
                "Berry Burst Energy Bites", "Lemonade Fizz Fountains", "Spicy Sriracha Snackers", "Coconut Cream Dream",
                "Sour Apple Sparklers", "Chocolate Avalanche Cookies", "Honey Mustard Pretzel Twists", "Raspberry Ripple Yogurt",
                "Cheesy Jalapeño Poppers", "Blueberry Delight Muffins", "Pomegranate Power Shots", "Maple Glazed Donuts", "Cucumber Cooler Elixir",
                "Garlic Parmesan Pita Chips"));
		ArrayList<Integer> productID = new ArrayList<>(Arrays.asList(58391, 24756, 83914, 62578, 37129, 96482, 51873,
                72659, 49215, 36587, 84726, 15934, 63875, 71426, 29643, 48317, 57296, 42635, 75148, 91364));
		ArrayList<Integer> restockAmount = new ArrayList<>(Arrays.asList(2, 3, 4, 5, 6, 7, 8, 9, 10, 11));
		ArrayList<String> address = new ArrayList<>(Arrays.asList("1234 Elm Street, Apt 201, Springfield, CA 12345",
                "5678 Oak Avenue, Suite 102, Willowville, NY 67890", "4321 Maple Lane, Unit 304, Cedarville, TX 54321",
                "9876 Birch Road, Apt 405, Pinecrest, FL 87654", "2468 Pine Street, Suite 506, Oakwood, AZ 23456",
                "1357 Cedar Avenue, Apt 607, Redwood, WA 76543", "8765 Aspen Lane, Suite 708, Birchwood, GA 87654",
                "5432 Spruce Road, Apt 809, Willowbrook, OR 43210", "7890 Sycamore Lane, Unit 910, Mapleview, NC 98765",
                "3210 Birch Street, Suite 1011, Pineville, MI 21098"));
		ArrayList<Double> productPrices = new ArrayList<>(Arrays.asList(3.75, 6.50, 2.95, 8.25, 4.10, 5.75, 7.99, 3.50, 2.25, 6.95, 5.49, 3.25, 4.75, 7.50, 4.95, 4.25, 9.99, 3.95, 6.75, 5.25));
		ArrayList<String> phoneNumber = new ArrayList<>(Arrays.asList("555-123-4567", "555-234-5678", "555-345-6789",
                "555-456-7890", "555-567-8901", "555-678-9012", "555-789-0123", "555-890-1234", "555-901-2345", "555-012-3456"));
		Random random = new Random();

		// Create Random Members
		for (int i = 0; i < firstName.size(); i++) {
			// Member: Name (as first + last), Address, PhoneNumber, EnrollmentDate
			int firstNameIndex = random.nextInt(firstName.size());
			int lastNameIndex = random.nextInt(firstName.size());
			Member member = new Member(firstName.get(firstNameIndex) + " " + lastName.get(lastNameIndex), address.get(i), phoneNumber.get(i));
			GroceryStore.instance().addMember(member);
		}

		// Create Random Products
		for (int i = 0; i < productName.size(); i++) {
			// Member: Name (as first + last), Address, PhoneNumber, EnrollmentDate
			int priceIndex = random.nextInt(productName.size());
			int restockIndex = random.nextInt(restockAmount.size());
			Product product = new Product(productName.get(i), productID.get(i), restockAmount.get(restockIndex), productPrices.get(priceIndex));
			GroceryStore.instance().addProductToCatalog(product);
		}

		// Create 3 transactions per customer
		for (int i = 0; i < members.getMemberList().size(); i++) {
			// Member: Name (as first + last), Address, PhoneNumber, EnrollmentDate
			ArrayList<LineItem> lineItems = new ArrayList<>();
			int totalProducts = 0;
			double totalPrice = 0;

			for(int j = 0; j < 3; j++)
			{
				int productIndex = random.nextInt(products.getProductList().size());
				int randomQuantity = random.nextInt(10);
				Product product = products.get(productIndex);
				LineItem line = new LineItem(product, randomQuantity);
				lineItems.add(line);
			}

			for(LineItem l : lineItems)
			{
				totalProducts += l.getQuantity();
				totalPrice += l.getPrice();
			}
			Transaction transaction = new Transaction(members.get(i).getMemberID(), lineItems, totalProducts, totalPrice);
			GroceryStore.instance().transactions.addTransaction(transaction);
		}
	}
}


