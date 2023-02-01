import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Scanner;

public class PolicyholderSQL {
	Scanner sc = new Scanner(System.in);
	ValidationSQL validation = new ValidationSQL();
	DBConnection dbCon = new DBConnection();
	Connection con = dbCon.getDBConnection();
	String fName;
	String lName;
	LocalDate bdate;
	String polholderAddress;
	String licenseNo;
	LocalDate licenseIssued;

	public LocalDate getLicenseIssued() {
		return licenseIssued;
	}
	
	public void storePolholderIntoDB(String customerAccNo, String policyNo) {
		String table = "policyholders";
		String columns = "account_no, policy_no, first_name, last_name, birth_date, address, drivers_license, drivers_license_issued";
		String values = "'"+customerAccNo+"','"+policyNo+"', '"+fName+"','"+lName+"','"+bdate+"','"+polholderAddress+"','"+licenseNo+"','"+licenseIssued+"'";
		dbCon.insertIntoDB(table, columns, values);
	}
	
	public void searchPolicyholder(int accNo) {
		try {
			String selectPolholder = "SELECT * FROM policyholders WHERE account_no = '"+accNo+"'";
			PreparedStatement statement = con.prepareStatement(selectPolholder);
			ResultSet rset = statement.executeQuery();
			int result = 0;
			while(rset.next()) {
				result++;
				String policyNum = rset.getString("policy_no");
				String polholderFNameResult = rset.getString("first_name");
				String polholderLNameResult = rset.getString("last_name");
				System.out.println(polholderFNameResult.toUpperCase() + " " + polholderLNameResult.toUpperCase() + " - " + policyNum);
			}
			if(result == 0) {
				System.out.println("--");
				System.out.println();
			}
			System.out.println();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayPolicyholderDetails(String polNum) {
		try {
			String selectPolholder = "SELECT * FROM policyholders WHERE policy_no = '"+polNum+"'";
			PreparedStatement stat = con.prepareStatement(selectPolholder);
			ResultSet rset = stat.executeQuery();
			rset.next();
			String fName = rset.getString("first_name");
			String lName = rset.getString("last_name");
			String bdate = rset.getString("birth_date");
			String polholderAddress = rset.getString("address");
			String licenseNo = rset.getString("drivers_license");
			String licenseIssued = rset.getString("drivers_license_issued");
			System.out.println();
			System.out.println("--------------------------------------------");
			System.out.println("~ Policyholder Details ~");
			System.out.println("--------------------------------------------");
			System.out.println("First Name: " + fName);
			System.out.println("Last Name: " + lName);
			System.out.println("Birthdate: " + bdate);
			System.out.println("Address: " + polholderAddress);
			System.out.println("Driver's License Number: " + licenseNo);
			System.out.println("Date on which driver's license was first issued: " + licenseIssued);
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deletePolicyholder(String policyNo) {
		try {
			String deletePolholder = "DELETE FROM policyholders WHERE policy_no = '"+policyNo+"'";
			PreparedStatement stmt = con.prepareStatement(deletePolholder);
			stmt.execute();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	 public void addPolicyholder(CustomerAccountSQL cus, String accNo, String policyNo, VehicleSQL vehicle, PolicyholderSQL policyholder) {
		 boolean isAnsValid;
		  do {
			  isAnsValid = false;
			  System.out.print("Are you also the policyholder? <y/n>: ");
			  String ans = sc.next() + sc.nextLine().trim();
			  if(ans.equalsIgnoreCase("y")) {
				  System.out.println("~ Policy Holder Details ~");
				  cus.getAccNameAndAddress(accNo);
				  fName = cus.getFirstName();
				  lName = cus.getLastName();
				  polholderAddress = cus.getAddress();
				  bdate = validation.validateDate("Enter date of birth (yyyy-mm-dd): ");
				  System.out.print("Enter driver's license no: ");
				  licenseNo = sc.next() + sc.nextLine();
				  licenseIssued = validation.validateDate("Enter date on which driver’s license was first issued (yyyy-mm-dd): ");
				  System.out.println();
				  storePolholderIntoDB(accNo, policyNo);
			  } else if(ans.equalsIgnoreCase("n")) {
				  System.out.println();
				  System.out.println("~ Policy Holder Details ~");
				  System.out.print("Enter first name: ");
				  fName = sc.next() + sc.nextLine();
				  System.out.print("Enter last name: ");
				  lName = sc.next() + sc.nextLine();
				  bdate = validation.validateDate("Enter date of birth (yyyy-mm-dd): ");
				  System.out.print("Enter address: ");
				  polholderAddress = sc.next() + sc.nextLine();
				  System.out.print("Enter driver's license no: ");
				  licenseNo = sc.next() + sc.nextLine();
				  licenseIssued = validation.validateDate("Enter date on which driver’s license was first issued (yyyy-mm-dd): ");
				  System.out.println();
				  storePolholderIntoDB(accNo, policyNo);
			  } else {
				  System.out.println("PLEASE ENTER Y OR N ONLY.");
				  isAnsValid = true;
			  }
		  } while(isAnsValid);
	 }
}
