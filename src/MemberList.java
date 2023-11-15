import java.util.ArrayList;

// collection class to hold Member objects

public class MemberList {

	private ArrayList<Member> memberList;
	private static int totalMembers;
	private static int memberIDCounter = 100;
	
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
	
	
//	 method to find and return the member with the matching memberID
//	 if the member is found in the memberList, the member object is returned.
//	 if the member is not found, null is returned. 
	public Member getMember(int memberID) {
		for (Member member : memberList) {
			if (member.getMemberID() == memberID) {
				return member;
			}
		}
		// member was not found
		return null;
	}
	
	
	public void print() {
		for (Member member : memberList) {
			member.print();
		}
	}
}

