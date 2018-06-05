package assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import assignment.util.Function;

public class PowerSystem {
	String[] busName; 
	private List<String> systemNames; 
	private List<String> classTags;
	private Map<Integer, String> classLink; 
	private Map<String, String> classDespt; 
	
	private double[] scaleMax; 
	private double[] scaleMin; 
	private double[] scaleRange; 

	public PowerSystem() {
		initalizeSystemParam(); 
	}
	
	/**
	 * Initialize the names of the system parameters
	 */
	public void initalizeSystemParam() {
		busName = new String[] {
				"CLAR",
				"AMHE",
				"WINL",
				"BOWM",
				"TROY",
				"MAPL",
				"GRAN",
				"WAUT",
				"CROSS",
		};
		
		systemNames = new ArrayList<String>(); 
		for (String name : busName) {
			systemNames.add(name + "_VOLT");
			systemNames.add(name + "_ANG");
		}
		
		classTags = Arrays.asList("HIGH_LOAD",
								  "SHUTDOWN",
								  "LOW_LOAD",
								  "DISCONNECT");
		
		classDespt = new HashMap<String, String>(); 
		classDespt.put("HIGH_LOAD", "High load rate during peak hours");
		classDespt.put("SHUTDOWN", "Shut down of generator for maintenance");
		classDespt.put("LOW_LOAD", "Low load rate during night");
		classDespt.put("DISCONNECT", "Disconnection of a line for maintenance");
	}

	public void initalizeScaling(double[][] matrix) {
		try {
			scaleMax = new double[matrix[0].length];
			scaleMin = new double[matrix[0].length];
			
			for (int i = 0; i < matrix[0].length; i++) {
				double[] tmp = new double[matrix.length]; 
	
				for (int j = 0; j < matrix.length; j++) {
					tmp[j] = matrix[j][i]; 
				}	
				scaleMax[i] = Function.getMax(tmp); 
				scaleMin[i] = Function.getMin(tmp); 
			}
			scaleRange = Function.subtractArray(scaleMax, scaleMin);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Function to convert an array of maps to an 2D array
	 * @param mapList		Array of the rows from the database sorted by time
	 * @return				2D array of the data from the database
	 */
	public double[][] convertMatrix(ArrayList<Map<String, Double>> mapList) {		
		double[][] matrix = new double[mapList.size()][systemNames.size()];
		
		for (int i = 0; i < mapList.size(); i++) {
			for (int j = 0; j < systemNames.size(); j++) {
				matrix[i][j] = mapList.get(i).get(systemNames.get(j));
			}
		}
		return matrix;
	}
	
	/**
	 * Normalize the values of the matrix between 0 and 1
	 * @param matrix		Input matrix 
	 * @return				Normalized matrix
	 */
	public double[][] scaleMatrix(double[][] matrix) {
		double[][] out = new double[matrix.length][matrix[0].length]; 
		
		try {
			for (int i = 0; i < matrix.length; i++) {
				out[i] = Function.subtractArray(matrix[i], scaleMin); 
				out[i] = Function.divideArray(out[i], scaleRange);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return out; 
	}
	
	/** 
	 * Return values of normalized matrix to original values
	 * @param matrix		Normalized matrix
	 */
	public double[][] unscaleMatrix(double[][] matrix) {
		double[][] out = new double[matrix.length][matrix[0].length]; 
		
		try {
			for (int i = 0; i < matrix.length; i++) {
				out[i] = Function.multiplyArray(matrix[i], scaleRange); 
				out[i] = Function.addArray(out[i], scaleMin);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return out; 
	}
	
	/**
	 * Function to associate the cluster means to the system states
	 * @param means				Array of the cluster means
	 * @throws Exception
	 */
	public void linkClusters(double[][] means) throws Exception {
		classLink = new HashMap<Integer, String>(); 
		
		// Generator shutdown for maintenance 
		// Identified when difference in angle is ~0
		double[] shutDownAng = new double[means.length];
		int winlIdx = Function.getIndex(busName, "WINL") * 2 + 1; 
		int maplIdx = Function.getIndex(busName, "MAPL") * 2 + 1; 
		
		for (int i = 0; i < means.length; i++) {
			shutDownAng[i] = means[i][winlIdx] - means[i][maplIdx];
		}
		int shutdownIdx = Function.getMinIndex(shutDownAng); 
		classLink.put(shutdownIdx, "SHUTDOWN"); 
		
		// High load rate and low load rate 
		int bowmVoltIdx = Function.getIndex(busName, "BOWM") * 2; 
		int bowmAngIdx = Function.getIndex(busName, "BOWM") * 2 + 1; 
		double[] powerFlow = new double[means.length];
		
		for (int i = 0; i < means.length; i++) {
			powerFlow[i] = means[i][bowmVoltIdx] * Math.sin(-Math.toRadians(means[i][bowmAngIdx])); 
		}
		int highIdx = Function.getMaxIndex(powerFlow); 
		addClassLink(highIdx, "HIGH_LOAD"); 
		
		int lowIdx = Function.getMinIndex(powerFlow); 
		addClassLink(lowIdx, "LOW_LOAD"); 
		
		// Disconnection by process of elimination 
		for (int i = 0; i < means.length; i++) {
			Boolean found = false; 
			for (int key : classLink.keySet()) {
				if (i == key) {
					found = true; 
					break;
				}
			}
			if (!found) {
				addClassLink(i, "DISCONNECT"); 
				break;
			}
		}
	}
	
	/**
	 * Add system state to the class link 
	 * @param idx				Index of the cluster
	 * @param className			System state name
	 * @throws Exception
	 */
	public void addClassLink(int idx, String className) throws Exception {
		// Check for duplicates 
		for (int key : classLink.keySet()) {
			if (idx == key) {
				throw new Exception("Class link conflict"); 
			}
		}		
		classLink.put(idx, className); 
	}
	
	public String[] getBusName() {
		return busName; 
	}
	
	/**
	 * Display the links between the clusters and the system states
	 * @param k			Number of clusters
	 */
	public void printLinks(int k) {
		System.out.println(""); 
		for (int i = 0; i < k; i++) {
			String line = String.format("Cluster %d is ", i+1); 
			String cluster = classDespt.get(classLink.get(i)); 
			System.out.println(line + cluster);
		}
	}
}
