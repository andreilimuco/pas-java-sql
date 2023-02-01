import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;
import java.util.Scanner;

public class CustomerAccountSQL {
	Scanner sc = new Scanner(System.in);
	private DBConnection dbCon = new DBConnection();
	private Connection con = dbCon.getDBConnection();
	private int accountNo;
	private String firstName;
	private String lastName;
	private String address;
	private String enteredFName;
	private String enteredLName;
	
	public CustomerAccountSQL() {
		setAccNo();
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public String getAddress() {
		return address;
	}
	
	public String getAccNo() {
		String accNo = String.format("%04d", accountNo);
		return accNo;
	}
	
	public void getAccNameAndAddress(String accNo) {
		try {
			String query = "SELECT * FROM customer_accounts WHERE account_no = '"+accNo+"'";
			PreparedStatement stmt = con.prepareStatement(query);
			ResultSet rset = stmt.executeQuery();
			rset.next();
			firstName = rset.getString("first_name");
			lastName = rset.getString("last_name");
			address = rset.getString("address");
			System.out.println("First Name: " + firstName);
			System.out.println("Last Name: " + lastName);
			System.out.println("Address: " + address);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setAccNo() {
		Random rnd = new Random();
	    int number = rnd.nextInt(9999); //Generates a 4-digit random number.
	    accountNo = number;
	}
	
	public void createAccount() {
		System.out.println();
		System.out.print("Enter first name: ");
		firstName = sc.next() + sc.nextLine().trim();
		System.out.print("Enter last name: ");
		lastName = sc.next() + sc.nextLine().trim();
		System.out.print("Enter address: ");
		address = sc.next() + sc.nextLine().trim();
		storeAccountIntoDB();
		System.out.println("ACCOUNT CREATED SUCCESSFULLY");
		System.out.println();
		System.out.println("New account created with account no: " + getAccNo());
		System.out.println();
	}
	
	public void promptName() {
		System.out.println();
		System.out.print("Enter first name: ");
		enteredFName = sc.next() + sc.nextLine().trim();
		System.out.print("Enter last name: ");
		enteredLName = sc.next() + sc.nextLine().trim();
	}
	
	public void displayAccountDetails(PolicyholderSQL policyholder) {
		try {
			String query = "SELECT * FROM customer_accounts WHERE first_name = '"+enteredFName+"' AND last_name = '"+enteredLName+"'";
			PreparedStatement stat = con.prepareStatement(query);
			ResultSet rset = stat.executeQuery();
			if(rset.next()) {
				int accNo = rset.getInt("account_no");
				String fName = rset.getString("first_name");
				String lName = rset.getString("last_name");
				String cusAddress = rset.getString("address");
				System.out.println();
				System.out.println("--------------------------------------------");
				System.out.println("~ Customer Account Details ~");
				System.out.println("--------------------------------------------");
				System.out.println("Account No: " + String.format("%04d", accNo));
				System.out.println("First Name: " + fName.toUpperCase());
				System.out.println("Last Name: " + lName.toUpperCase());
				System.out.println("Address: " + cusAddress.toUpperCase());
				System.out.println();
				System.out.println("--------------------------------------------");
				System.out.println("~ Policyholders and their Policy Nos ~");
				System.out.println("--------------------------------------------");
				policyholder.searchPolicyholder(accNo);
			} else {
				System.out.println();
				System.out.println("ACCOUNT NOT FOUND. PLEASER TRY AGAIN.");
				System.out.println();
			}
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	}

	public void storeAccountIntoDB() {
		String table = "customer_accounts";
		String columns = "account_no, first_name, last_name, address";
		String values = "'"+getAccNo()+"','"+firstName+"','"+lastName+"','"+address+"'";
		dbCon.insertIntoDB(table, columns, values);
	}
}
