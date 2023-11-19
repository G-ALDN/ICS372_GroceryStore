import java.io.*;
import java.lang.reflect.Array;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Random;

/**
 * 
 * GroceryStoreUI class. Used to create a command line interface that the user may interact with to use the GroceryStore.
 * Command line commands are saved as int variables. Various methods for interacting with the 
 * GroceryStore system exist.
 *
 */

public class GroceryStoreUI {
	
	private static GroceryStoreUI userInterface;
	private static GroceryStore groceryStore; 
	private BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	private static final int EXIT = 0;
	private static final int ENROLL_MEMBER = 1;
	private static final int REMOVE_MEMBER = 2;
	private static final int GET_MEMBER_INFO = 3;
	private static final int ADD_PRODUCTS = 4;
	private static final int CHECK_OUT = 5;
	private static final int GET_PRODUCT_INFO = 6;
	private static final int PROCESS_SHIPMENT = 7;
	private static final int UPDATE_PRICE = 8;
	private static final int PRINT_TRANSACTIONS = 9;
	private static final int LIST_MEMBERS = 10;
	private static final int LIST_PRODUCTS = 11;
	private static final int LIST_OUTSTANDING_ORDERS = 12;
	private static final int SAVE = 13;
	private static final int RETRIEVE = 14;
	private static final int HELP = 15;
	
	/**
	 * Constructor is private to maintain singleton pattern. Conditionally looks for any saved data.
	 * Otherwise, it gets a singleton Library object.
	 */
	private GroceryStoreUI() {
		if (yesOrNo("Would you like to look for saved data and use it?")) {
			retrieve();
		} else {
			if(yesOrNo("Would you like to start the TESTING environment?")){
				createTestEnvironment();
			} else{
				groceryStore = GroceryStore.instance();
			}
		}
	}

	/**
	 * Supports the singleton pattern
	 * 
	 * @return the singleton object
	 */
	public static GroceryStoreUI instance() {
		if (userInterface == null) {
			return userInterface = new GroceryStoreUI();
		} else {
			return userInterface;
		}
	}

	/**
	 * Gets a token after prompting
	 * @param prompt - whatever the user enters as prompt
	 * @return - the token from the user
	 * 
	 */
	public String getToken(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				StringTokenizer tokenizer = new StringTokenizer(line, "\n\r\f");
				if (tokenizer.hasMoreTokens()) {
					return tokenizer.nextToken();
				}
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);
	}

	/**
	 * Queries for a yes or no and returns true for yes and false for no
	 * @param prompt The string to be prepended to the yes/no prompt
	 * @return true for yes and false for no
	 * 
	 */
	private boolean yesOrNo(String prompt) {
		String more = getToken(prompt + " (Y|y)[es] or anything else for no");
		if (more.charAt(0) != 'y' && more.charAt(0) != 'Y') {
			return false;
		}
		return true;
	}

	/**
	 * Method to be called for retrieving saved data. Uses the GroceryStore
	 * "open" method for retrieval.
	 * 
	 */
	private void retrieve() {
		try {
			if (groceryStore == null) {
				//groceryStore = GroceryStore.instance();
				// TODO File open statement goes here. must be implemented in GroceryStore first.
				String working_dir = System.getProperty("user.dir");
				Scanner scanner = new Scanner(System.in);
				String fileName;
				File loadState;
				boolean stepSuccess = false;

				while (!stepSuccess) {
					System.out.print("Load file: ");
					try {
						fileName = scanner.nextLine();

						if(fileName.isEmpty()) {
							System.out.println("Enter a valid file name.");
						}

						else{
							loadState = new File(working_dir + File.separator + fileName);
							boolean exists = loadState.exists();

							if(!loadState.isFile())
							{
								System.out.println("File does not exist.");
							}
							else {
								GroceryStore.instance().open(loadState);
								stepSuccess = true;
							}
						}

					} catch (IOError ioe) {
						System.out.println(ioe.getMessage());
						ioe.printStackTrace();
						break;
					}
				}

				/*
				if (groceryStore != null) {
					System.out.println(" The Grocery Store has been successfully retrieved from the file \n");
				} else {
					System.out.println("File doesnt exist; creating new Grocery Store");
					groceryStore = GroceryStore.instance();
				}*/
			}
		} catch (Exception x) {
			x.printStackTrace();
		}
	}

	/**
	 * Prompts for a command from the keyboard
	 * @return a valid command
	 * 
	 */
	public int getCommand() {
		do {
			try {
				int value = Integer.parseInt(getToken("Enter command: " + HELP + " for help"));
				if (value >= EXIT && value <= HELP) {
					return value;
				}
			} catch (NumberFormatException nfe) {
				System.out.println("Enter a number");
			}
		} while (true);
	}

	/**
	 * Gets a String input after prompting
	 * 
	 * @param prompt - whatever the user wants as prompt
	 * @return - the token from the keyboard
	 * 
	 */
	public String getStringInput(String prompt) {
		do {
			try {
				System.out.println(prompt);
				String line = reader.readLine();
				return line;
			} catch (IOException ioe) {
				System.exit(0);
			}
		} while (true);

	}

	/**
	 * Converts the string input to a number
	 * 
	 * @param prompt the string for prompting
	 * @return the integer corresponding to the string
	 * 
	 */
	public int getIntInput(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				Integer number = Integer.valueOf(item);
				return number.intValue();
			} catch (NumberFormatException nfe) {
				System.out.println("Please input a number ");
			}
		} while (true);
	}
	
	/**
	 * Converts the string input to a double
	 * 
	 * @param prompt the string for prompting
	 * @return the double corresponding to the string
	 * 
	 */
	public double getDoubleInput(String prompt) {
		do {
			try {
				String item = getToken(prompt);
				Double number = Double.valueOf(item);
				return number.doubleValue();
			} catch (NumberFormatException nfe) {
				System.out.println("Please input a number ");
			}
		} while (true);
	}

	/**
	 * Method to be called for adding a member. Prompts the user for the appropriate
	 * values and uses the appropriate GroceryStore method for adding the member.
	 * 
	 */
	public void addMember() {
		String memberName = getStringInput("Enter the member's name");
		String address = getStringInput("Enter your address");
		String phoneNum = getStringInput("Enter your phone number");
		
		double moneyInput = getDoubleInput("$30 member fee is required on sign up. Please enter payment amount.");
		
		// loops asking for more payment until the $30 fee has been met
		while (moneyInput < 30) {
			System.out.println("$" + moneyInput + " entered. Remaining balance required: " + (30 - moneyInput));
			moneyInput += getDoubleInput("Please enter the remaining amount owed.");
		}
		System.out.println("Payment successful. Change to return to customer is: " + (moneyInput - 30));
		
		Member newMember = new Member(memberName, address, phoneNum);
		boolean success = GroceryStore.instance().addMember(newMember);
		
		if (!success) {
			System.out.println("Could not add member");
		} else {
			System.out.println("Success. " + memberName + "'s member ID is: " + newMember.getMemberID());
		}
	}
	
	/**
	 * Method to be called for removing a member. Prompts the user for the member ID
	 * of the member to be removed. Confirmation or failure messages are displayed after attempting to remove the member.
	 * 
	 */
	public void removeMember() {
		int memberID = getIntInput("Enter the ID of the member you would like to remove. * (OR -1 to cancel) *");
		if (memberID == -1) return; // cancel and exit on -1 input
		boolean success = GroceryStore.instance().removeMember(memberID);
		
		if (!success) {
			System.out.println("Could not remove member with member ID: " + memberID);
		} else {
			System.out.println("Success. Member ID: " + memberID + " was removed.");
		}
	}

	/**
	 * Attempts to find members in the MemberList with the specified name
	 * 
	 * The process repeats until the user has given a name that matches one or more names in the MemberList
	 * Once one or more members are found, their information is printed.
	 */
	public void getMemberInfo() {
		boolean success = false;
		
		while (!success) {
			String memberName = getStringInput("Enter the name of the member you would like to get info for * (OR -1 to cancel member search) *");
			// if user enters 0, then we exit
			if (memberName.equals("-1")) break;
			ArrayList<Member> matchingMembers = GroceryStore.instance().retrieveMembersByName(memberName);
			
			if (!matchingMembers.isEmpty()) {
				success = true;
				// found members, so we print their information
				for (Member member : matchingMembers) {
					member.print();
				}
				break;
				
			} else {
				System.out.println("Could not find any members with that specified name. Trying again...");
				continue;
			}
		}
	}

	/**
	 * Calls GroceryStore to prints information of all of the current members in MemberList
	 * If there are no members in the system currently, the user is notified.
	 */
	public void listMembers() {
		GroceryStore.instance().listAllMembers();
	}

	/**
	 * Method that adds products to the ProductList catalog.
	 * Attempts to create a new product using user prompted inputs.
	 * If product creation is successfull, attempts to place an order 2x the minimum restock quantity.
	 * User is prompted whether or not they would like to continue adding another product or exit.
	 */
	public void addProducts() {
		
		while (true) {
			String productName = getStringInput("Enter the name of the product");
			int productID = getIntInput("Enter the product ID for the product (must be numbers only)");
			double price = getDoubleInput("Enter the current price for this product");
			int restockQty = getIntInput("Enter the minimum restock quantity for this product");
			//int currentStock = 0;
			Product newProduct = new Product(productName, productID, restockQty, price);
			
			// adding the new product to the catalog.
			boolean success = GroceryStore.instance().addProductToCatalog(newProduct);
			
			if (success) {
				System.out.println("This product has been successfully added.");
				newProduct.print();
				System.out.println("Ordering product...");
				
				// now we attempt to order 2x restock quantity for the new product
				boolean orderSuccess = GroceryStore.instance().restockProduct(newProduct);
				if (orderSuccess) {
					System.out.println(newProduct.getProductName() + " has been ordered, Quantity: " + newProduct.getRestockAmount() * 2);
				} else {
					System.out.println("Failure ordering " + newProduct.getProductName() + ". Please try again.");
					continue;
				}
			}
			// prompt the user if they would like to continue adding another product, or exit
			boolean addAnother = yesOrNo("Would you like to add another product?");
			if (addAnother) {
				continue;
			} else {
				break;
			}
		}
	}
	
	/**
	 * Displays information about a Product given a user prompted String product name. 
	 * User is prompted whether or not they would like to search for another product or exit.
	 */
	public void getProductInfo() {
		
		while (true) {
			String productName = getStringInput("Enter the product name, or -1 to cancel");
			if (productName.equals("-1")) break; // exit on -1 to cancel
			Product productResult = GroceryStore.instance().getProduct(productName);
			
			if (productResult != null) {
				// product was found, so print the details
				productResult.print();
			} else {
				// product was not found.
				System.out.println("A matching product was not found. Please try again.");
				continue;
			}
			// prompt the user whether or not they would like to search for another product or exit
			boolean searchAgain = yesOrNo("Would you like to get info about another product?");
			if (searchAgain) {
				continue;
			} else {
				break;
			}
		}
	}
	
	/**
	 * Lists info for all products in the ProductList catalog
	 * 
	 */
	public void listProducts() {
		GroceryStore.instance().listAllProducts();
	}

	/**
	 * Checkout Method Process:
	 * 1. Prompt for Member ID and check if valid. User can cancel process if they
	 * wish.
	 * 2. Prompt for Product ID to be added to Cart and continue until user
	 * finalizes transaction.
	 * 3. Prompt for sufficient amount of money to cover transaction, prompting for
	 * more if not sufficient. User can cancel here as well.
	 * 4. Displays amount of change needed to be returned and finishes transaction
	 * 
	 */
	public void checkout() {
		while (true) {
			int memberID = getIntInput("Enter the member id (or enter -1 to cancel):");
			if (memberID == -1)
				break;
			boolean isMember = GroceryStore.instance().createCart(memberID);

			if (!isMember) {
				System.out.println("The entered MemberID was incorrect. Try again.");
			} else {
				break;
			}
		}

		while (true) {
			int productID = getIntInput("Enter the product id (or enter -1 to finalize):");
			if (productID >= 0) {
				int quantity = getIntInput("Enter the quantity of item:");
				GroceryStore.instance().addProductToCart(productID, quantity);
				continue;


			} else {
				break;
			}
		}
		double moneyInputted = 0;
		while (true) {
			if (GroceryStore.instance().getCart().getInCart().isEmpty())
				break;
			double money = getDoubleInput("Enter amount of money (or enter -1 to cancel transaction):");
			if (money < 0) {
				break;
			}
			moneyInputted += money;
			double moneyLeft = GroceryStore.instance().finalizeCart(moneyInputted);
			if (moneyLeft > 0) {
				System.out.println("Money Remaining: $" + moneyLeft);
				continue;
			} else {
				System.out.println("Change: $" + Math.abs(moneyLeft));
				System.out.println("Please return the change to the customer.");
				System.out.println("---------------- Transaction Complete ----------------");
				break;
			}

		}
	}

	/**
	 * Processes shipments for products that are currently on order.
	 * If the product is not on order, the user is notified. 
	 * Process loops until the user does not want to process any more shipments. 
	 */
	public void processShipment() {
		
		while (true) {
			int productID = getIntInput("Please enter a product ID, or -1 to cancel.");
			if (productID == -1) break; // exit on -1 input
			
			boolean success = GroceryStore.instance().processShipment(productID);
			if (success) {
				boolean processAgain = yesOrNo("Would you like to process another shipment?");
				if (processAgain) {
					continue;
				} else {
					break;
				}
			} else {
				System.out.println("The product ID was not found on any orders. Please try again.");
			}
		}
		
		
	}
	
	/**
	 * Updates the price of a product in the catalog. Prompts user for the producID and price to update.
	 * If the product does not exist, user is notified.
	 * Process loops until the user does not want to update any more products.
	 */
	public void updatePrice() {
		
		while (true) {
			int productID = getIntInput("Please enter the ID of the product you would like to update");
			double newPrice = getDoubleInput("Please enter the new price for this product");
			boolean success = GroceryStore.instance().updatePrice(productID, newPrice);
			
			if (success) {
				boolean goAgain = yesOrNo("Would you like to update the price of another product?");
				if (goAgain) {
					continue;
				} else {
					break;
				}
			} else { // success false, product was not found
				System.out.println("This product was not found in the system. Please try again.");
			}
		}
	}

	/**
	 * Print all Transactions between two specified dates provided by user.
	 * If Date is invalid, it will prompt for the correct date.
	 * If no transactions exist within that time frame
	 */
	public void printTransactions() {
		while (true) {
			ZonedDateTime Date1 = null;
			try {
				Date1 = ZonedDateTime.of(
						LocalDate.of(getIntInput("Date 1\nYear: "), getIntInput("Month: "), getIntInput("Day: ")),
						LocalTime.MIDNIGHT, ZonedDateTime.now().getZone());
			} catch (Exception e) {
				System.out.println("Enter Correct Date Values (Month: 1-12, Day: 1-31 based on month).");
				continue;
			}
			ZonedDateTime Date2 = null;
			try {
				Date2 = ZonedDateTime.of(
						LocalDate.of(getIntInput("Date 2\nYear: "), getIntInput("Month: "), getIntInput("Day: ")),
						LocalTime.of(23, 59), ZonedDateTime.now().getZone());
			} catch (Exception e) {
				System.out.println("Enter Correct Date Values (Month: 1-12, Day: 1-31).");
				continue;
			}

			System.out.println("Printing Transactions Between " + Date1.getYear() + "-" + Date1.getMonthValue() + "-"
					+ Date1.getDayOfMonth() + " and "
					+ Date2.getYear() + "-" + Date2.getMonthValue() + "-" + Date2.getDayOfMonth() + "... ");
			boolean success = GroceryStore.instance().printTransactions(Date1, Date2);
			if (success) {
				break;
			}

		}
	}

	/**
	 * Lists info for all products that are currently on order in the ShipmentList
	 * 
	 */
	public void listOutstandingOrders() {
		GroceryStore.instance().listOutstandingOrders();
	}

	public void save(){
		GroceryStore.instance().save();
	}

	public void createTestEnvironment(){
		System.out.println("Creating test environment...");
		GroceryStore.instance().createTestEnvironment();
	}
	
	/**
	 * Displays the help screen
	 * 
	 */
	public void help() {
		System.out.println("Enter a number between 0 and 15 as explained below:");
		System.out.println(EXIT + ": to Exit\n");
		System.out.println(ENROLL_MEMBER + ": to enroll a member");
		System.out.println(REMOVE_MEMBER + ": to remove a member");
		System.out.println(GET_MEMBER_INFO + ": to get info about members");
		System.out.println(ADD_PRODUCTS + ": to add products");
		System.out.println(CHECK_OUT + ": to check out");
		System.out.println(GET_PRODUCT_INFO  + ": to get product info");
		System.out.println(PROCESS_SHIPMENT + ": to process a shipment");
		System.out.println(UPDATE_PRICE + ": to update the price of a product");
		System.out.println(PRINT_TRANSACTIONS + ": to print transactions");
		System.out.println(LIST_MEMBERS + ": to list members");
		System.out.println(LIST_PRODUCTS + ": to list products");
		System.out.println(LIST_OUTSTANDING_ORDERS + ": to list outstanding orders");
		System.out.println(SAVE + ": to save data");
		System.out.println(RETRIEVE + ": to retrieve saved data");
	}

	/**
	 * Method for running the command processes. Calls the appropriate methods for the
	 * different functionalities based on user input.
	 * 
	 */
	public void process() {
		int command;
		help();
		while ((command = getCommand()) != EXIT) {
			switch (command) {
			case ENROLL_MEMBER:
				addMember();
				break;
			case REMOVE_MEMBER:
				removeMember();
				break;
			case GET_MEMBER_INFO:
				getMemberInfo();
				break;
			case ADD_PRODUCTS:
				addProducts();
				break;
			case CHECK_OUT:
				checkout();
				break;
			case GET_PRODUCT_INFO:
				getProductInfo(); 
				break;
			case PROCESS_SHIPMENT:
				processShipment();
				break;
			case UPDATE_PRICE:
				updatePrice();
				break;
			case PRINT_TRANSACTIONS:
				printTransactions();
				break;
			case LIST_MEMBERS:
				listMembers();
				break;
			case LIST_PRODUCTS:
				listProducts();
				break;
			case LIST_OUTSTANDING_ORDERS:
				listOutstandingOrders();
				break;
			case SAVE:
				save();
				break;
			case RETRIEVE:
				retrieve(); // retrieve not working from the menu
				break;
			case HELP:
				help();
				break;
			}
		}
	}

	
	/**
	 * The method to start the application. It just calls process().
	 * 
	 */
	public static void main(String[] args) {
		GroceryStoreUI.instance().process();
	}

}
