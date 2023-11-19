import java.util.ArrayList;

// Collection class to hold Member objects

public class MemberList {

	private ArrayList<Member> memberList;
	private static int totalMembers;
	private static int memberIDCounter = 100; // used to auto generate and increment member ID's. Starts at 100.
	
	/**
     * Constructor
     * 
     */
	public MemberList() {
		this.memberList = new ArrayList<Member>();
		totalMembers = 0;
	}
	
	public static int getTotalMembers() {
		return totalMembers;
	}
	
	public static int getMemberIDCounter() {
		return memberIDCounter;
	}

	public ArrayList<Member> getMemberList() {
		return this.memberList;
	}
	
	/**
     * Used for adding a new Member to the MemberList
     * @param Member member - the member to be added.
     * 
     */
	public boolean addMember(Member member) {
		boolean success = this.memberList.add(member);
		if (success) {
			totalMembers++;
			memberIDCounter++;
		}
		return success;
	}
	
	
	/**
	 * Removes a member with the specified member ID
	 * 
	 * @param member ID
	 * @return boolean success indicator
	 * 
	 */
	public boolean removeMember(int memberID) {
		boolean success = false;
		// attempting to find the member with the matching memberID
		Member matchingMember = getMember(memberID);
		// if the member was not found we got a null object back
		if (matchingMember == null) return false;
		// member was found, so we attempt to remove them
		success = memberList.remove(matchingMember);
		if (success) totalMembers--;
		return success;
	}
	
	
	public Member get(int index) {
		return memberList.get(index);
	}
	
	/**
     * method to find and return the member with the matching memberID
     * @param memberID - the ID of the member we would like to get. 
     * @return Member - if the member is found in the memberList, the member object is returned.
     * @return null - if the member is not found, null is returned.
     */
	public Member getMember(int memberID) {
		for (Member member : memberList) {
			if (member.getMemberID() == memberID) {
				return member;
			}
		}
		// member was not found
		return null;
	}
	
	/**
	 * Print method for printing details about every Member in the MemberList.
	 */
	public void print() {
		for (Member member : memberList) {
			member.print();
		}
	}
}

