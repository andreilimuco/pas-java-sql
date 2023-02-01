import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class DBConnection {
	static Connection conn = null;
//	CustomerAccountSQL cus = new CustomerAccountSQL();
	
	public Connection getDBConnection() { // db connection test
        try {
            if(conn == null) {
                conn = DriverManager.getConnection // Step 1: Construct a database 'Connection' object called 'conn'
                ("jdbc:mysql://localhost:3306/pas_system", "root", "root123");
            }

        } catch(Exception ex) {
            ex.printStackTrace();
            // Close conn and stmt - Done automatically by try-with-resources (JDK 7)
        }
        return conn;
    }
	
	public void insertIntoDB(String table, String columns, String values) {
		try {
			  String query = "INSERT INTO "+table+" ("+columns+") VALUES ("+values+")";
			  PreparedStatement stmt = conn.prepareStatement(query);
			  stmt.execute();
		  } catch(Exception ex) {
			  ex.printStackTrace();
		  }
	}
	
	public void selectFromDB() {
		
	}
	
	
}
