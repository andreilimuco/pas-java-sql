import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDate;

/**
 * @author LemuelLimuco
 */
public class Policyholder {
	DBConnection dbCon = new DBConnection();
	Connection con = dbCon.getDBConnection();
	
	public Policyholder() {
		
	}
	
	public void insertPolholder(String customerAccNo, String policyNo, String fName, String lName, LocalDate birthday, String policyholderAddress, String driversLicenseNo, LocalDate licenseIssued) {
		try {
			  String insertPolicyholder = "INSERT INTO policyholders (account_no, policy_no, first_name, last_name, birth_date, address, drivers_license, drivers_license_issued) VALUES ('"+customerAccNo+"','"+policyNo+"', '"+fName+"','"+lName+"','"+birthday+"','"+policyholderAddress+"','"+driversLicenseNo+"','"+licenseIssued+"')";
			  PreparedStatement stat = con.prepareStatement(insertPolicyholder);
			  stat.execute();
		  } catch(Exception e) {
			  e.printStackTrace();
		  }
	}
}
