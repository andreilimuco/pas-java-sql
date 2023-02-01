import java.time.LocalDate;
import java.time.Period;

public class RatingEngineSQL {
	private double premium;
	private double vehPurchPrice;
	private double vehPriceFactor;
	private int licenseNumOfYears;
	private static double totalPremium;
	
	public RatingEngineSQL() {
		
	}
	
	public RatingEngineSQL(int year, double purchasePrice, LocalDate licenseIssued) {
		setVehPurchPrice(purchasePrice);
		setVehPriceFactor(year);
		setLicenseNumOfYears(licenseIssued);
		calcPremium();
	}
	
	public void setVehPriceFactor(int year) {
		LocalDate now = LocalDate.now();
		int currentYear = now.getYear();
		int vehAge = currentYear - year;
		if(vehAge <= 1) {
			vehPriceFactor = 0.01;
		} else if(vehAge <= 3 && vehAge > 1) {
			vehPriceFactor = 0.008;
		} else if(vehAge <= 5 && vehAge > 3) {
			vehPriceFactor  = 0.007;
		} else if(vehAge <= 10 && vehAge > 5) {
			vehPriceFactor = 0.006;
		} else if(vehAge <= 15 && vehAge > 10) {
			vehPriceFactor = 0.004;
		} else if(vehAge <= 20 && vehAge > 15) {
			vehPriceFactor = 0.002;
		} else if(vehAge <= 40 && vehAge > 20) {
			vehPriceFactor = 0.001;
		}
	}
	
	public void setVehPurchPrice(double purchasePrice) {
		vehPurchPrice = purchasePrice;
	}
	
	public double getVehPurchPrice() {
		return vehPurchPrice;
	}
	
	public void setLicenseNumOfYears(LocalDate licenseIssued) {
		LocalDate dateNow = LocalDate.now();
		licenseNumOfYears = Period.between(licenseIssued, dateNow).getYears();
	}
	
	public void calcPremium() {
		premium = (vehPurchPrice * vehPriceFactor) + ((vehPurchPrice / 100) / licenseNumOfYears);
		calcTotalPremium(premium);
	}
	
	public double getPremium() {
		return premium;
	}
	
	public void calcTotalPremium(double premium) {
		totalPremium += premium;
	}
	
	public double getTotalPremium() {
		return totalPremium;
	}
	
	public void resetTotalPremium() {
		totalPremium = 0;
	}
}
