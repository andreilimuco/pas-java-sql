import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Date;
import java.util.Random;
import java.util.Scanner;

public class PolicySQL {
	ValidationSQL validate = new ValidationSQL();
	Scanner sc = new Scanner(System.in);
	DBConnection dbCon = new DBConnection();
	Connection con = dbCon.getDBConnection();
	private String enteredAccNo;
	private String enteredPolicyNum;
	private int policyNo;
	private LocalDate effectiveDate;
	private LocalDate expirationDate;
	private double policyPremium;
	
	public PolicySQL() {
		setPolicyNo();
	}
	
	public void setPolicyNo() {
		Random rnd = new Random();
	    int number = rnd.nextInt(999999); // It will generate 6 digit random number from 0 to 999999
	    this.policyNo = number;
	}
	
	public String getPolicyNo() {
		String polNo = String.format("%06d", policyNo);
		return polNo;
	}
	
	public void setExpirationDate() {
		this.expirationDate = effectiveDate.plusMonths(6);
	}
	
	public void setNewExpirationDate(LocalDate expirationDate) {
		this.expirationDate = expirationDate;
	}
	
	public LocalDate getExpirationDate() {
		return expirationDate;
	}
	
	public double getPolicyPremium() {
		return policyPremium;
	}
	
	public void setPolicyPremium(double policyPremium) {
		this.policyPremium = policyPremium;
	}
	
	public void promptPolicyNo() {
		System.out.println();
		System.out.print("Enter your 6-digit policy no: ");
		enteredPolicyNum = sc.next() + sc.nextLine();
	}
	
	public void updateExpirationDate() {
		ValidationSQL validation = new ValidationSQL();
		try {
			String selectPolNum = "SELECT * FROM policies WHERE policy_no = '"+enteredPolicyNum+"'";
			PreparedStatement stat = con.prepareStatement(selectPolNum);
			ResultSet rset = stat.executeQuery();
			
			if(rset.next()) {
				String policyNum = rset.getString("policy_no");
				String effectiveDate = rset.getString("effective_date");
				String expirationDate = rset.getString("expiration_date");
				System.out.println();
				System.out.println("----------------------------------------------");
				System.out.println("Effective Date: " + effectiveDate);
				System.out.println("----------------------------------------------");
				System.out.println("Current Expiration Date: " + expirationDate);
				System.out.println("----------------------------------------------");
				System.out.println();
				LocalDate newExpirationDate = validation.validateExpirationDate("Enter new expiration date: ", effectiveDate, expirationDate);
				//this.expirationDate = newExpirationDate;
				setNewExpirationDate(newExpirationDate);
				updateCurrExpDate(newExpirationDate, policyNum);
				
				System.out.println("POLICY CANCELLED SUCCESSFULLY");
				System.out.println();
				System.out.println("The policy's new expiration date is: " + getExpirationDate());
				System.out.println();
			} else {
				System.out.println();
				System.out.println("POLICY NOT FOUND. PLEASE TRY AGAIN.");
				System.out.println();
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
//	public void insertPolicy(String policyNo, String customerAccNo, LocalDate effectiveDate, LocalDate expirationDate, double policyPremium) {
//		try {
//			  String insertPolicy = "INSERT INTO policies VALUES ('"+policyNo+"','"+customerAccNo+"','"+effectiveDate+"','"+expirationDate+"','"+policyPremium+"')";
//			  PreparedStatement stat = con.prepareStatement(insertPolicy);
//			  stat.execute();
//		  } catch(Exception e) {
//			  e.printStackTrace();
//		  }
//	}
	
	public void storePolicyIntoDB() {
		String table = "policies";
		String columns = "policy_no, effective_date, expiration_date, policy_premium";
		String values = "'"+getPolicyNo()+"','"+effectiveDate+"','"+expirationDate+"','"+policyPremium+"'";
		dbCon.insertIntoDB(table, columns, values);
	}
	
	public void updateCurrExpDate(LocalDate newExpirationDate, String policyNo) {
		try {
			String updateCurrExpDate = "UPDATE policies SET expiration_date = '"+newExpirationDate+"' WHERE policy_no = '"+policyNo+"'";
			PreparedStatement stmt = con.prepareStatement(updateCurrExpDate);
			stmt.execute();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayPolicyDetails(PolicyholderSQL polholder, VehicleSQL vehicle) {
		try {
			String selectPolicy = "SELECT * FROM policies WHERE policy_no = '"+enteredPolicyNum+"'";
			PreparedStatement stmt = con.prepareStatement(selectPolicy);
			ResultSet rset = stmt.executeQuery();
			if(rset.next()) {
				String polNum = rset.getString("policy_no");
				Date effDate = rset.getDate("effective_date");
				Date expDate = rset.getDate("expiration_date");
				double policyPremium = rset.getDouble("policy_premium");
				System.out.println();
				System.out.println("--------------------------------------------");
				System.out.println("~ Policy Details ~");
				System.out.println("--------------------------------------------");
				System.out.println("Policy No: " + polNum);
				System.out.println("Effective Date: " + effDate);
				System.out.println("Expiration Date: " + expDate);
				polholder.displayPolicyholderDetails(polNum);
				vehicle.displayVehDetails(polNum, policyPremium);
			} else {
				System.out.println();
				System.out.println("POLICY NOT FOUND. PLEASE TRY AGAIN.");
				System.out.println();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public void promptAccountNo() {
		System.out.println();
		System.out.print("Enter your 4-digit account number: ");
		enteredAccNo = sc.next() + sc.nextLine();
		System.out.println();
	}
	
	public void getPolicyQuote(CustomerAccountSQL customerAcc, VehicleSQL vehicle, PolicyholderSQL policyholder, RatingEngineSQL ratingEngine) {
		 try {
			  String selectCusAcc = "SELECT * FROM customer_accounts WHERE account_no = '"+enteredAccNo+"'";
			  PreparedStatement stmt = con.prepareStatement(selectCusAcc);
			  ResultSet rset = stmt.executeQuery();
			  if(rset.next()) {
				  String accNo = rset.getString("account_no");
				  effectiveDate = validate.validateDate("Enter effective date (yyyy-mm-dd): ");
				  setExpirationDate();
				  System.out.println("Your policy coverage will expire on " + expirationDate);
				  System.out.println();
//				  policyholderPrompt(customerAcc, accNo, vehicle, policyholder, ratingEngine);
				  policyholder.addPolicyholder(customerAcc, accNo, getPolicyNo(), vehicle, policyholder);
				  vehicle.addVehicles(policyholder.getLicenseIssued(), getPolicyNo());
				  addVehiclePrompt(vehicle, policyholder.getLicenseIssued());
				  setPolicyPremium(ratingEngine.getTotalPremium());
				  storePolicyIntoDB();
				  System.out.println("Policy Premium Amount: " + policyPremium);
				  buyPolicyPrompt(ratingEngine, vehicle, policyholder);
			  } else {
				  System.out.println("ACCOUNT NOT FOUND. PLEASE TRY AGAIN.");
				  System.out.println();
			  }
			  
		  } catch(Exception ex) {
			  ex.printStackTrace();
		  }
	}
	
	 public void addVehiclePrompt(VehicleSQL vehicle, LocalDate licenseIssued) {
		 boolean isAnsValid;
		  do {
			  isAnsValid = false;
			  System.out.print("Add more vehicles? <y/n>: ");
			  String ans = sc.next() + sc.nextLine().trim();
			  if(ans.equalsIgnoreCase("y")) {
				  vehicle.addVehicles(licenseIssued, getPolicyNo());
				  addVehiclePrompt(vehicle, licenseIssued);
			  } else if(ans.equalsIgnoreCase("n")) {
				  System.out.println();
			  } else {
				  System.out.println("Please enter y or n only");
				  isAnsValid = true;
			  }
		  } while(isAnsValid);
	  }
	 
	 public void buyPolicyPrompt(RatingEngineSQL re, VehicleSQL vehicle, PolicyholderSQL polholder) {
		 boolean isAnsValid;
		 do {
			 isAnsValid = false;
			 System.out.print("Buy policy? <y/n>: ");
			 String ans = sc.next() + sc.nextLine().trim();
			 if(ans.equalsIgnoreCase("y")) {
				 System.out.println("POLICY BOUGHT SUCCESSFULLY");
				 System.out.println("New policy bought with policy no: " + getPolicyNo());
				 System.out.println();
				 re.resetTotalPremium();
			 } else if(ans.equalsIgnoreCase("n")) {
				 System.out.println("THANK YOU FOR INQUIRING!");
				 vehicle.deleteVehicle(getPolicyNo());
				 deletePolicy();
				 polholder.deletePolicyholder(getPolicyNo());
				 re.resetTotalPremium();
			 }
		 } while(isAnsValid);
	 }
	 
	 public void deletePolicy() {
		 try {
			 String deletePol = "DELETE FROM policies WHERE policy_no = '"+getPolicyNo()+"'";
			 PreparedStatement stmt = con.prepareStatement(deletePol);
			 stmt.execute();
		 } catch(Exception e) {
			 e.printStackTrace();
		 }
	 }
	 
//	 public void policyholderPrompt(CustomerAccountSQL cus, String accNo, VehicleSQL vehicle, PolicyholderSQL policyholder, RatingEngineSQL re) {
//		 boolean isAnsValid;
//		  do {
//			  isAnsValid = false;
//			  System.out.print("Are you also the policyholder? <y/n>: ");
//			  String ans = sc.next() + sc.nextLine().trim();
//			  if(ans.equalsIgnoreCase("y")) {
//				  System.out.println("~ Policy Holder Details ~");
//				  cus.getAccNameAndAddress(accNo);
//				  String fName = cus.getFirstName();
//				  String lName = cus.getLastName();
//				  String policyholderAddress = cus.getAddress();
//				  System.out.println("First Name: " + fName);
//				  System.out.println("Last Name: " + lName);
//				  System.out.println("Address: " + policyholderAddress);
//				  LocalDate birthday = validate.validateDate("Enter date of birth (yyyy-mm-dd): ");
//				  System.out.print("Enter driver's license no: ");
//				  String driversLicenseNo = sc.next() + sc.nextLine();
//				  LocalDate licenseIssued = validate.validateDate("Enter date on which driver’s license was first issued (yyyy-mm-dd): ");
//				  System.out.println();
//				  policyholder.insertPolholder(accNo, getPolicyNo(), fName, lName, birthday, policyholderAddress, driversLicenseNo, licenseIssued);
//				  vehicle.addVehicles(licenseIssued, getPolicyNo());
//				  addVehiclePrompt(vehicle, licenseIssued);
////				  RatingEngineSQL re = new RatingEngineSQL();
//				  setPolicyPremium(re.getTotalPremium());
////				  insertPolicy(getPolicyNo(), accNo, effectiveDate, expirationDate, policyPremium);
////				  storePolicyIntoDB();
////				  System.out.println("Policy Premium Amount: " + policyPremium);
////				  buyPolicyPrompt(re, vehicle, policyholder);
//			  } else if(ans.equalsIgnoreCase("n")) {
//				  System.out.println();
//				  System.out.println("~ Policy Holder Details ~");
//				  System.out.print("Enter first name: ");
//				  String fName = sc.next() + sc.nextLine();
//				  System.out.print("Enter last name: ");
//				  String lName = sc.next() + sc.nextLine();
//				  LocalDate birthday = validate.validateDate("Enter date of birth (yyyy-mm-dd): ");
//				  System.out.print("Enter address: ");
//				  String policyholderAddress = sc.next() + sc.nextLine();
//				  System.out.print("Enter driver's license no: ");
//				  String driversLicenseNo = sc.next() + sc.nextLine();
//				  LocalDate licenseIssued = validate.validateDate("Enter date on which driver’s license was first issued (yyyy-mm-dd): ");
//				  System.out.println();
//				  policyholder.insertPolholder(accNo, getPolicyNo(), fName, lName, birthday, policyholderAddress, driversLicenseNo, licenseIssued);
//				  vehicle.addVehicles(licenseIssued, getPolicyNo());
//				  addVehiclePrompt(vehicle, licenseIssued);
////				  RatingEngineSQL re = new RatingEngineSQL();
//				  setPolicyPremium(re.getTotalPremium());
//				  //insertPolicy(getPolicyNo(), accNo, effectiveDate, expirationDate, policyPremium);
////				  storePolicyIntoDB();
////				  System.out.println("Policy Premium Amount: " + policyPremium);
////				  buyPolicyPrompt(re, vehicle, policyholder);
//			  } else {
//				  System.out.println("PLEASE ENTER Y OR N ONLY.");
//				  isAnsValid = true;
//			  }
//		  } while(isAnsValid);
//	 }
}
