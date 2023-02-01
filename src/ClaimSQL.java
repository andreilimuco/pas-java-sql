import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Random;
import java.util.Scanner;

/**
 * @author LemuelLimuco
 */
public class ClaimSQL {
	ValidationSQL validation = new ValidationSQL();
	Scanner sc = new Scanner(System.in);
	DBConnection dbCon = new DBConnection();
	Connection con = dbCon.getDBConnection();
	private String enteredPolicyNum;
	private String enteredClaimNo;
	private int claimNo;
	private LocalDate accidentDate;
	private String accidentAddress;
	private String accidentDescription;
	private String damageDescription;
	private double repairCost;
	
	public ClaimSQL() {
		setClaimNo();
	}
	
	public void promptPolicyNo() {
		System.out.println();
		System.out.print("Enter your 6-digit policy no: ");
		enteredPolicyNum = sc.next() + sc.nextLine();
	}
	
	public void fileClaim() {
		try {
			String selectPolNum = "SELECT * FROM policies WHERE policy_no = '"+enteredPolicyNum+"'";
			PreparedStatement stmt = con.prepareStatement(selectPolNum);
			ResultSet rset = stmt.executeQuery();
			if(rset.next()) {
				String policyNo = rset.getString("policy_no");
				populateClaimDetails();
				storeClaimIntoDB(policyNo);
				System.out.println();
				System.out.println("CLAIM FILED SUCCESSFULLY");
				System.out.println("New claim created with claim no: " + getClaimNo());
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
	
	public void populateClaimDetails() {
		ValidationSQL validation = new ValidationSQL();
		System.out.println();
		accidentDate = validation.validateDate("Enter date of accident: ");
		System.out.print("Enter address where accident happened: ");
		accidentAddress = sc.next() + sc.nextLine().trim();
		System.out.print("Enter description of accident: ");
		accidentDescription = sc.next() + sc.nextLine().trim();
		System.out.print("Enter description of damage to vehicle: ");
		damageDescription = sc.next() + sc.nextLine().trim();
		repairCost = validation.validateAmount("Enter estimated cost of repairs: ");
	}
	
	public void setClaimNo() {
		Random rnd = new Random();
		int number = rnd.nextInt(99999); //Generates a 5-digit random number from 0 to 99999.
		this.claimNo = number;
	}
	
	public String getClaimNo() {
		String claimNumber = String.format("C%05d", claimNo); //formats claim number with C at the beginning plus the random generated number
		return claimNumber;
	}
	
	public void storeClaimIntoDB(String policyNo) {
		String table = "claims";
		String columns = "claim_no, policy_no, accident_date, accident_address, accident_description, damage_description, repair_cost";
		String values = "'"+getClaimNo()+"', '"+policyNo+"', '"+accidentDate+"', '"+accidentAddress+"', '"+accidentDescription+"', '"+damageDescription+"', '"+repairCost+"'";
		dbCon.insertIntoDB(table, columns, values);
	}
	
	public void promptClaimNo() {
		System.out.println();
		System.out.print("Enter your 6-digit alphanumeric Claim No: ");
		enteredClaimNo = sc.next() + sc.nextLine();
	}
	
	public void displayClaim() {
		try {
			String selectClaim = "SELECT * FROM claims WHERE claim_no = '"+enteredClaimNo+"'";
			PreparedStatement stmt = con.prepareStatement(selectClaim);
			ResultSet rset = stmt.executeQuery();
			int result = 0;
			while(rset.next()) {
				result++;
				String claimNum = rset.getString("claim_no");
				String accidentDate = rset.getString("accident_date");
				String accidentAddress = rset.getString("accident_address");
				String accidentDescription = rset.getString("accident_description");
				String damageDescription = rset.getString("damage_description");
				double repairCost = rset.getDouble("repair_cost");
				System.out.println();
				System.out.println("--------------------------------------------");
				System.out.println("~ Claim Details ~");
				System.out.println("--------------------------------------------");
				System.out.println("Claim No: " + claimNum);
				System.out.println("Date of Accident: " + accidentDate);
				System.out.println("Address where accident happened: " + accidentAddress);
				System.out.println("Description of accident: " + accidentDescription);
				System.out.println("Description of damage to vehicle: " + damageDescription);
				System.out.println("Estimated cost of repairs: " + repairCost);
				System.out.println();
			}
			if(result == 0) {
				System.out.println();
				System.out.println("CLAIM NOT FOUND. PLEASE TRY AGAIN.");
				System.out.println();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
