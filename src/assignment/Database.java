package assignment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

/** Class that connect and manages the database. 
 * 
 * @author Alvin Lee
 *
 */

public class Database {
	// JDBC driver name and database URL
	final static String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
//	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	final static String DB_URL = "jdbc:mysql://localhost/";
	
	// Database credentials 
	private Properties credentials = new Properties(); 
	final static String USER = "root";
	final static String PASS = "root"; 
	
	// Connection and statement 
	private Connection conn;

	// Database name
	private String database = "subtables"; 
	
	private static List<String> systemNames = Arrays.asList("CLAR_VOLT",
															"CLAR_ANG",
															"AMHE_VOLT",
															"AMHE_ANG",
															"WINL_VOLT",
															"WINL_ANG",
															"BOWM_VOLT",
															"BOWM_ANG",
															"TROY_VOLT",
															"TROY_ANG",
															"MAPL_VOLT",
															"MAPL_ANG",
															"GRAN_VOLT",
															"GRAN_ANG",
															"WAUT_VOLT",
															"WAUT_ANG",
															"CROSS_VOLT",
															"CROSS_ANG"); 
	
	public Database() {
		try {			
			// Register JDBC driver
			Class.forName(JDBC_DRIVER);
			
			// Properties for connection (credentials) 
			credentials.setProperty("user", USER);
			credentials.setProperty("password", PASS);
			credentials.setProperty("useSSL", "false");		// Stops time-zone error from appearing
						
			// Connect to the database
			this.conn = DriverManager.getConnection(DB_URL + database, credentials);
		}
		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}
		catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		}
	}
	
	public double[][] getData(String tableName, int tableSize) {
		String quary = "select * from " + tableName + " where time=?"; 
		
		ArrayList<Map<String, Double>> dataset = new ArrayList<Map<String, Double>>(); 

		try {
			for (int i = 0; i <= tableSize; i++) {
				// Prepare statement 
				PreparedStatement pstm = conn.prepareStatement(quary);
				pstm.setInt(1, i);

				ResultSet resultSet = pstm.executeQuery();
				
				// Check for empty result set
				if (resultSet.next()) {
					// Store results in a map
					Map<String, Double> measurements = new HashMap<String, Double>();
					
					// Get name and value
					do {
						String name = resultSet.getString("name"); 
						double value = resultSet.getDouble("value");
						measurements.put(name, new Double(value)); 
					} while (resultSet.next());
					
					// Add system state at specific time to dataset
					dataset.add(measurements); 
				}				
				pstm.close(); 
			}			
		}
		catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		}
		
		return convertMatrix(dataset);
	}
	
	public double[][] convertMatrix(ArrayList<Map<String, Double>> mapList) {		
		double[][] matrix = new double[mapList.size()][systemNames.size()];
		
		for (int i = 0; i < mapList.size(); i++) {
			for (int j = 0; j < systemNames.size(); j++) {
				matrix[i][j] = mapList.get(i).get(systemNames.get(j));
			}
		}
		return matrix;
	}
}
