import java.util.ArrayList;

// collection class to hold Member objects

public class MemberList {

	private ArrayList<Member> memberList;
	private int totalMembers;
	
	public MemberList() {
		this.memberList = new ArrayList<Member>();
		this.totalMembers = 0;
	}
	
	public int getTotalMembers() {
		return totalMembers;
	}

	public ArrayList<Member> getMemberList() {
		return this.memberList;
	}
	
	public void addMember(Member member) {
		this.memberList.add(member);
		this.totalMembers++;
	}
	
	public Member get(int index) {
		return memberList.get(index);
	}
	
	// method to find and return the member with the matching memberID
	// if the member is found in the memberList, the member object is returned.
	// if the member is not found, null is returned. 
	public Member getMember(int memberID) {
		for (Member member : memberList) {
			if (member.getMemberID() == memberID) {
				return member;
			}
		}
		// member was not found
		return null;
	}
}

