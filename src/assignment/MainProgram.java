package assignment;

import java.util.Arrays;

public class MainProgram {
	public static void main (String[] args) {
		// Connect to database
		Database database = new Database(); 
		
		double[][] training = database.getData("measurements", 200);
		
		System.out.println(Arrays.deepToString(training));
	}
}
