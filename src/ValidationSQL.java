import java.time.LocalDate;
import java.util.Scanner;

public class ValidationSQL {
	Scanner sc = new Scanner(System.in);
	
	public double validateAmount(String prompt) {
		boolean isAmountValid;
		do {
			isAmountValid = false;
			try {
				System.out.print(prompt);
				String num = sc.next() + sc.nextLine();
				int number = Integer.parseInt(num);
				if(number == 0) {
					  System.out.println("0 IS NOT ALLOWED. PLEASE ENTER A VALUE MORE THAN ZERO");
					  return validateAmount(prompt);
				  } else if(number < 1) {
					  System.out.println("NEGATIVE VALUE IS NOT ALLOWED. PLEASE ENTER A VALUE MORE THAN ZERO");
					  return validateAmount(prompt);
				  }
				  else {
					  return number;
				  }
			} catch(Exception e) {
				System.out.println("INVALID INPUT");
				isAmountValid = true;
			}
		} while(isAmountValid);
		return 0;
	}
	

	public int validateNumber(String prompt) { //method for number validation
		
		 boolean isNumber;
		  do {
			  isNumber = false;
			  try {
				  System.out.print(prompt);
				  if(prompt.contains("year")) {
					  int year = sc.nextInt();
					  LocalDate dateNow = LocalDate.now();
					  int currentYear = dateNow.getYear();
					  if(year >= 1885 && year <= currentYear) {
						  return year;
					  } else {
						  System.out.println("PLEASE ENTER A VALID YEAR");
						  return validateNumber(prompt);
					  }
				  } else if(prompt.contains("number")) {
					  int number;
					  String userInput = sc.nextLine();
					  number = Integer.parseInt(userInput);
					  return number;
				  }
			  } catch(Exception e) {
				  if(prompt.contains("year")) {
					  System.out.println("INVALID INPUT. PLEASE ENTER YEAR IN NUMBERS");
					  sc.next();
					  isNumber = true; //This is what will get the prompt to loop back
				  } else {
					  System.out.println("INVALID INPUT. PLEASE ENTER NUMBERS ONLY");
					  isNumber = true;
				  }
			  }
		  } while(isNumber);
		return 0;
	}
	
	public LocalDate validateDate(String prompt) { //method for date validation
		boolean isDateFormatValid;
		  do {
			  isDateFormatValid = false;
			    try {
			    	System.out.print(prompt);
			    	if(prompt.contains("birth")) {
			    		String dateEntered = sc.next();
						LocalDate date = LocalDate.parse(dateEntered);
						if(date.isBefore(LocalDate.now())) {
							return date;
						} else {
							System.out.println("PLEASE ENTER PAST OR PRESENT DATE ONLY");
							return validateDate(prompt);
						}
			    	} else if(prompt.contains("license")) {
			    		String dateEntered = sc.next();
						LocalDate date = LocalDate.parse(dateEntered);
			    		if(date.isBefore(LocalDate.now())) {
			    			return date;
			    		} else {
			    			System.out.println("PLEASE ENTER PAST OR PRESENT DATE ONLY");
			    			return validateDate(prompt);
			    		}
			    	} else if(prompt.contains("accident")) {
			    		String dateEntered = sc.next();
						LocalDate date = LocalDate.parse(dateEntered);
			    		if(date.isAfter(LocalDate.now())) {
			    			System.out.println("ACCIDENT DATE CANNOT BE A FUTURE DATE");
			    			return validateDate(prompt);
			    		} else {
			    			return date;
			    		}
			    	} else {
			    		String dateEntered = sc.next();
						LocalDate date = LocalDate.parse(dateEntered);
						return date;
			    	}
			    } catch (Exception e) {
			    	System.out.println("PLEASE ENTER DATE ACCORDING TO REQUIRED DATE FORMAT");
			        isDateFormatValid = true;
			    }
			} while (isDateFormatValid);
		return null;
	}
	
	public LocalDate validateExpirationDate(String prompt, String effDate, String expiDate) {
		boolean isExpirationDateValid;
		do {
			isExpirationDateValid = false;
			try {
				System.out.print(prompt);
				String dateEntered = sc.next();
				LocalDate newExpirationDate = LocalDate.parse(dateEntered);
				LocalDate effectiveDate = LocalDate.parse(effDate);
				LocalDate expirationDate = LocalDate.parse(expiDate);
				if(newExpirationDate.isBefore(effectiveDate)) {
					System.out.println("NEW EXPIRATION DATE CANNOT BE EARLIER THAN EFFECTIVE DATE");
					return validateExpirationDate(prompt, effDate, expiDate);
				} else if(newExpirationDate.isEqual(effectiveDate)) {
					System.out.println("NEW EXPIRATION DATE CANNOT BE THE SAME WITH EFFECTIVE DATE");
					return validateExpirationDate(prompt, effDate, expiDate);
				} else if(newExpirationDate.isAfter(expirationDate)) {
					System.out.println("NEW EXIPIRATION DATE CANNOT BE LATER THAN CURRENT EXPIRATION DATE");
					return validateExpirationDate(prompt, effDate, expiDate);
				} else if(newExpirationDate.isEqual(expirationDate)) {
					System.out.println("PLEASE ENTER NEW EXPIRATION DATE EARLIER THAN CURRENT EXPIRATION DATE");
					return validateExpirationDate(prompt, effDate, expiDate);
				} else {
					return newExpirationDate;
				}
			} catch(Exception e) {
				System.out.println("PLEASE ENTER DATE ACCORDING TO REQUIRED DATE FORMAT");
				isExpirationDateValid = true;
			}
		} while(isExpirationDateValid);
		return null;
	}
}
