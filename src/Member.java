import java.time.ZonedDateTime;

// Member class

public class Member {

	private String memberName;
	private int memberID;
	private String address;
	private String phoneNumber;
	private double feePaid;
	private ZonedDateTime enrollmentDate;
	
	/**
     * Constructor
     * 
     * @param String name - name of the member
     * @param String address - member's address
     * @param String phoneNumber - member's phone number
     * 
     * Members should only be created after paying the $30 fee, so we set the fee paid on construction.
     * 
     */
	public Member(String name, String address, String phoneNumber) {
		this.memberName = name;
		this.memberID = MemberList.getMemberIDCounter(); // set memberID as the next value of the member ID counter
		this.address = address; 
		this.phoneNumber = phoneNumber;
		this.feePaid = 30.0;
		this.enrollmentDate = java.time.ZonedDateTime.now(); // initialize date as time of creation
	}


	public String getMemberName() {
		return memberName;
	}


	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}


	public int getMemberID() {
		return memberID;
	}


	public void setMemberID(int memberID) {
		this.memberID = memberID;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}



	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public ZonedDateTime getEnrollmentDate() {
		return enrollmentDate;
	}


	public void setEnrollmentDate(ZonedDateTime enrollmentDate) {
		this.enrollmentDate = enrollmentDate;
	}

	
	
	/**
     * Print method to output Member details to console.
     * 
     */
	public void print() {
		String output = "---------------- Member Info ---------------\n";
		output += "Member ID: " + memberID + "\n";
		output += "Name: " + memberName + "\n";
		output += "Address: " + address + "\n";
		output += "Phone Number: " + phoneNumber + "\n";
		output += "Fee paid: " + feePaid + "\n";
		output += "Enrollment Date: " + enrollmentDate.toString().substring(0, 10) + "\n"; // strip the date of just year / month / day
		output += "---------------------------------------\n";
		System.out.println(output);
	}
	
	
}
