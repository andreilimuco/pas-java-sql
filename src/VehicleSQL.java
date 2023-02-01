import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Scanner;

public class VehicleSQL {
	ValidationSQL validation = new ValidationSQL();
	Scanner sc = new Scanner(System.in);
	DBConnection dbCon = new DBConnection();
	Connection con = dbCon.getDBConnection();
	private String make;
	private String model;
	private int year;
	private String type;
	private String fuelType;
	private double purchasePrice;
	private String color;
	private double premiumCharged;
	
	public void addVehicles(LocalDate licenseIssued, String policyNo) {
		System.out.println("~ Vehicle Details ~");
		System.out.print("Enter make: ");
		make = sc.next() + sc.nextLine();
		System.out.print("Enter model: ");
		model = sc.next() + sc.nextLine();
		year = (int) validation.validateNumber("Enter year (yyyy): ");
		System.out.print("Enter type: ");
		type = sc.next() + sc.nextLine();
		System.out.print("Enter fuel type: ");
		fuelType = sc.next() + sc.nextLine();
		purchasePrice = validation.validateAmount("Enter purchase price: ");
		System.out.print("Enter color: ");
		color = sc.next() + sc.nextLine();
		
		RatingEngineSQL ratingEngine = new RatingEngineSQL(year, purchasePrice, licenseIssued);
		premiumCharged = ratingEngine.getPremium();
		System.out.println();
		System.out.println("Premium charged for this specific vehicle: " + premiumCharged);
		System.out.println();
		
//		insertVehicle(policyNo);
		storeVehiclesIntoDB(policyNo);
	}
	
//	public void insertVehicle(String policyNo) {
//		try {
//			String insVeh = "INSERT INTO vehicles (policy_id, make, model, year, type, fuel_type, purchase_price, color, premium_charged) VALUES ('"+policyNo+"','"+make+"','"+model+"','"+year+"','"+type+"','"+fuelType+"','"+purchasePrice+"','"+color+"','"+premiumCharged+"')";
//			PreparedStatement stmt = con.prepareStatement(insVeh);
//			stmt.execute();
//		  } catch(Exception e) {
//			  e.printStackTrace();
//		  }
//	}
	
	public void storeVehiclesIntoDB(String policyNo) {
		String table = "vehicles";
		String columns = "policy_no, make, model, year, type, fuel_type, purchase_price, color, premium_charged";
		String values = "'"+policyNo+"','"+make+"','"+model+"','"+year+"','"+type+"','"+fuelType+"','"+purchasePrice+"','"+color+"','"+premiumCharged+"'";
		dbCon.insertIntoDB(table, columns, values);
	}
	
	public void deleteVehicle(String policyNo) {
		try {
			String deleteVeh = "DELETE FROM vehicles WHERE policy_no = '"+policyNo+"'";
			PreparedStatement stmt = con.prepareStatement(deleteVeh);
			stmt.execute();
			System.out.println("Vehicle Deleted");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void displayVehDetails(String policyNo, double policyPremium) {
		try {
			String selectVehicle = "SELECT * FROM vehicles WHERE policy_no = '"+policyNo+"'";
			PreparedStatement statement = con.prepareStatement(selectVehicle);
			ResultSet resSet = statement.executeQuery();
			System.out.println();
			System.out.println("--------------------------------------------");
			System.out.println("~ Vehicle/s Details ~");
			System.out.println("--------------------------------------------");
			while(resSet.next()) {
				String make = resSet.getString("make");
				String model = resSet.getString("model");
				int year = resSet.getInt("year");
				String type = resSet.getString("type");
				String fuelType = resSet.getString("fuel_type");
				double purchPrice = resSet.getDouble("purchase_price");
				String color = resSet.getString("color");
				double premiumCharged = resSet.getDouble("premium_charged");
				
				System.out.println("Make: " + make);
				System.out.println("Model: " + model);
				System.out.println("Year: " + year);
				System.out.println("Type: " + type);
				System.out.println("Fuel Type: " + fuelType);
				System.out.println("Purchase Price: " + purchPrice);
				System.out.println("Color: " + color);
				System.out.println("Premium Charged: " + premiumCharged);
				System.out.println();
			}
			
			System.out.println("Policy Premium Amount: " + policyPremium);
			System.out.println();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
