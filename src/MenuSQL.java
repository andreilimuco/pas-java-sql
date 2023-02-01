import java.util.Scanner;

public class MenuSQL {
	public void displayMenu() {
		CustomerAccountSQL customerAcc = new CustomerAccountSQL();
		PolicySQL policy = new PolicySQL();
		ClaimSQL claim = new ClaimSQL();
		PolicyholderSQL polholder = new PolicyholderSQL();
		VehicleSQL vehicle = new VehicleSQL();
		RatingEngineSQL ratingEngine = new RatingEngineSQL();
		String choice;
		Scanner sc = new Scanner(System.in);
		
		do {
			System.out.println("==============================================");
			System.out.println("                  PAS SYSTEM");
			System.out.println("==============================================");
			System.out.println("[1] - Create a new Customer Account");
			System.out.println("[2] - Get a policy quote and buy the policy");
			System.out.println("[3] - Cancel a specific policy");
			System.out.println("[4] - File an accident claim against a policy");
			System.out.println("[5] - Search for a Customer account");
			System.out.println("[6] - Search for and display a specific policy");
			System.out.println("[7] - Search for and display a specific claim");
			System.out.println("[8] - Exit the PAS System");
			System.out.println("==============================================");
			System.out.print("Enter your choice: ");
			choice = sc.next() + sc.nextLine();
			switch(choice) {
			case "1":
				  CustomerAccountSQL cusAcc = new CustomerAccountSQL();
				  cusAcc.createAccount();
				  break;
				
			case "2":
				  PolicySQL pol = new PolicySQL();
				  pol.promptAccountNo();
				  pol.getPolicyQuote(customerAcc, vehicle, polholder, ratingEngine);
				  break;
				
			case "3":
				  policy.promptPolicyNo();
				  policy.updateExpirationDate();
				  break;
				
			case "4":
				  ClaimSQL accidentClaim = new ClaimSQL();
				  accidentClaim.promptPolicyNo();
				  accidentClaim.fileClaim();
				  break;
				
			case "5":
				  customerAcc.promptName();
				  customerAcc.displayAccountDetails(polholder);
				  break;
				
			case "6":
				  policy.promptPolicyNo();
				  policy.displayPolicyDetails(polholder, vehicle);
				  break;
				
			case "7":
				  claim.promptClaimNo();
				  claim.displayClaim();
				  break;
				
			case "8":
				  System.out.println();
				  System.out.println("THANK YOU!");
				  break;
				
			default:
				  System.out.println();
				  System.out.println("INVALID INPUT");
				  System.out.println();
				  break;
			}
			
		} while(!choice.equals("8"));
		sc.close();
	}
}
