import java.util.ArrayList;

public class TransactionList {

	private ArrayList<Transaction> transactions;
	
	public TransactionList() {
		this.transactions = new ArrayList<Transaction>();
	}
	
	public void addTransaction(Transaction tx) {
		this.transactions.add(tx);
	}
	
	
	public ArrayList<Transaction> getTransactionsByMember(int memberID) {
		
		ArrayList memberTransactions = new ArrayList<Transaction>();
		
		for (Transaction tx : transactions) {
			if (tx.getMemberID() == memberID) { // TODO
				memberTransactions.add(tx);
			}
		return memberTransactions;
		}
	}
	
	
	public ArrayList<Transaction> getTransactionsByDate(Date startDate, Date endDate) {
		
		ArrayList dateTransactions = new ArrayList<Transaction>();
		
		for (Transaction tx : transactions) {
			if (tx.getDate() >= startDate && tx.getDate() <= endDate) { // TODO
				dateTransactions.add(tx);
			}
			return dateTransactions;
		}
	}
	
	
	public Transaction get(int index) {
		try {
			Transaction tx = transactions.get(index);
			return tx;
			
		} catch(IndexOutOfBoundsException x) {
			System.out.println("Transaction out of bounds of the TransactionList array");
			return null;
		}
	}
	
	public void print() {
		for (Transaction tx : transactions) {
			tx.print();
		}
	}
	
}
