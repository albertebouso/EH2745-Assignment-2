package assignment;

public class MainProgram {
	public static void main (String[] args) {
		// Connect to database
		Database database = new Database(); 
		
		double[][] train = database.getData("measurements", 200);
		double[][] test = database.getData("analog_values", 20); 
		
		KMeans kMean = new KMeans(4, 200); 
		kMean.train(train); 
		kMean.printMeans(); 
	}
}
