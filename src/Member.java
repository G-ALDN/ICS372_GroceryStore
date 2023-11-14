import java.time.ZonedDateTime;

public class Member {

	private String memberName;
	private int memberID;
	private String address;
	private String phoneNumber;
	private ZonedDateTime enrollmentDate;
	
	
	public Member(String name, int memberID, String address, String phoneNumber) {
		this.memberName = name;
		this.memberID = memberID;
		this.address = address; 
		this.phoneNumber = phoneNumber;
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
	
	// print a string representation of the member's details
	public void print() {
		String output = "------------------------------------\n";
		output += "Member ID: " + memberID + "\n";
		output += "Name: " + memberName + "\n";
		output += "Address: " + address + "\n";
		output += "Phone Number: " + phoneNumber + "\n";
		output += "Enrollment Date: " + enrollmentDate.toString() + "\n";
		output += "------------------------------------\n";
		System.out.println(output);
	}
	
	
}
