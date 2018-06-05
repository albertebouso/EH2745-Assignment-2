package assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class MainProgram {
	private static int kMeans = 4;
	private static int kNeighbors = 5; 
	private static int maxItr = 1000; 
	
	private static String dbName = "subtables"; 

	private static String trainTable = "measurements"; 
	private static int trainSize = 1000; 
	
	private static String testTable = "analog_values"; 
	private static int testSize = 30; 
	
	private static PowerSystem system;
	private static KMeans kMean;
	private static KNearestNeighbors kNN;
	
	public static void main (String[] args) {	
		try {
			system = new PowerSystem(); 
			kMean = new KMeans(kMeans, maxItr); 
			kNN = new KNearestNeighbors(kNeighbors); 
			
			// Connect to database and retrieve data 
			Database database = new Database(dbName); 
			ArrayList<Map<String, Double>> trainMap = database.getData(trainTable, trainSize);
			ArrayList<Map<String, Double>> testMap = database.getData(testTable, testSize); 
			List<Integer> timeStamps = database.getTime(testTable, testSize);
			double[][] train = system.convertMatrix(trainMap); 
			double[][] test = system.convertMatrix(testMap); 
			
			// Normalize data
			system.initalizeScaling(train); 
			train = system.scaleMatrix(train); 
			test = system.scaleMatrix(test); 
			
			// Run KNN algorithm
			kMean.train(train); 
			
			// Find links between clusters and states
			system.linkClusters(kMean.getMeans());
			
			// Classify test data 
			List<double[][]> clusters = kMean.cluster(train); 
			List<Integer> classes = kNN.classify(test, clusters);
			
			// Display results 
			system.printLinks(kMeans);	
			printMeans(); 
			printResults(classes, timeStamps); 
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}	
	
	/**
	 * Function to print the values of the means
	 */
	public static void printMeans() {
		double[][] means = system.unscaleMatrix(kMean.getMeans()); 
		
		System.out.println("\nMeans of the clusters");
		String title = "Bus Name\t"; 
		for (int i = 1; i < kMeans+1; i++) {
			title = title.concat("Cluster " + i + "\t\t"); 
		}
		System.out.println(title);
		
		String[] busName = system.getBusName(); 
		for (int i = 0; i < busName.length; i++) {
			String line = busName[i] + "\t\t";
			for (double[] mean : means) {
				String data = String.format("%.5f j%.5f\t", mean[i*2], mean[i*2+1]);
				line = line.concat(data);
			}
			System.out.println(line); 
		}
	}
	
	/**
	 * Function to print the test dataset classification results 
	 */
	public static void printResults(List<Integer> classes, List<Integer> time) throws Exception {
		if (classes.size() != time.size()) {
			throw new Exception("Mismatched list sizes"); 
		}
		
		System.out.println("\nTest dataset results");
		for (int i = 0; i < classes.size(); i++) {
			String line = String.format("Time %d is in cluster %d", time.get(i), classes.get(i)+1); 
			System.out.println(line);
		}
	}
}
