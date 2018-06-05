package assignment.util;

import java.util.ArrayList;
import java.util.List;

public class Function {
	/**
	 * Function to calculate the Euclidean distance between two arrays
	 * @param a		Array 1
	 * @param b		Array 2
	 * @return		Euclidean distance 
	 * @throws Exception 
	 */
	public static double euclideanDist(double[] a, double[] b) throws Exception {
		if (a.length != b.length) {
			throw new Exception("Input length does not match for euclidean distance caluclation");
		}
		
		double dist = 0d; 
		for (int i = 0; i < a.length; i++) {
			dist += Math.pow(a[i] - b[i], 2);			
		}
		return Math.sqrt(dist); 
	}
	
	/**
	 * Function to append an 1D array to a 2D array
	 * @param a		2D array
	 * @param b		1D array
	 * @return		Combined 2D array
	 */
	public static double[][] appendArray(double[][] a, double[] b) {
		double[][] c = new double[][] { b }; 
        double[][] out = new double[a.length + c.length][];
        
		System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(c, 0, out, a.length, c.length);

		return out; 
	}
	
	/**
	 * Creates a list of 2D arrays 
	 * @param size		Size of the list
	 * @return			List of 2D arrays 
	 */
	public static List<double[][]> create2DArrayList(int size) {
		List<double[][]> list = new ArrayList<double[][]>();
		for (int i = 0; i < size; i++) {
			list.add(new double[][] {}); 
		}
		return list; 
	}
	
	/**
	 * Find index of the element with the maximum value in an array
	 * @param array		Array 
	 * @return			Index of maximum element
	 */
	public static int getMaxIndex(int[] array) {
		int maxIndex = 0; 
		
		for (int i = 1; i < array.length; i++) {
			maxIndex = array[i] > array[maxIndex] ? i : maxIndex;
		}
		return maxIndex; 
	}	
	
	/**
	 * Find index of the element with the maximum value in an array
	 * @param array		Array 
	 * @return			Index of maximum element
	 */
	public static int getMaxIndex(double[] array) {
		int maxIndex = 0; 
		
		for (int i = 1; i < array.length; i++) {
			maxIndex = array[i] > array[maxIndex] ? i : maxIndex;
		}
		return maxIndex; 
	}	
	
	/**
	 * Find index of the element with the minimum value in an array
	 * @param array		Array 
	 * @return			Index of minimum element
	 */
	public static int getMinIndex(double[] array) {
		int minIndex = 0; 
		
		for (int i = 1; i < array.length; i++) {
			minIndex = array[i] < array[minIndex] ? i : minIndex;
		}
		return minIndex; 
	}	

	
	/**
	 * Find maximum value in an array
	 * @param array		Array
	 * @return			Maximum value of the array
	 */
	public static double getMax(double[] array) {
		double max = array[0]; 
		
		for (int i = 1; i < array.length; i++) {
			max = array[i] > max ? array[i] : max;
		}
		return max; 
	}	
	
	/**
	 * Find minimum value in an array
	 * @param array		Array
	 * @return			Minimum value of the array
	 */
	public static double getMin(double[] array) {
		double min = array[0]; 
		
		for (int i = 1; i < array.length; i++) {
			min = array[i] < min ? array[i] : min;
		}
		return min; 
	}
	
	/** 
	 * Element-wise addition between two arrays
	 * @param x			Array 1
	 * @param y			Array 2
	 * @return			x + y
	 * @throws Exception
	 */
	public static double[] addArray(double[] x, double[] y) throws Exception {
		if (x.length != y.length) {
			throw new Exception("Array lengths do not match"); 
		}
		
		for (int i = 0; i < x.length; i++) {
			if (Double.isNaN(x[i]) || Double.isNaN(y[i])) {
				throw new Exception("Invalid value in array"); 
			}
		}
		
		double[] out = new double[x.length]; 
		for (int i = 0; i < x.length; i++) {
			out[i] = x[i] + y[i]; 
		}
		return out; 
	}
	
	/** 
	 * Element-wise subtraction between two arrays
	 * @param x			Array 1
	 * @param y			Array 2
	 * @return			x - y
	 * @throws Exception
	 */
	public static double[] subtractArray(double[] x, double[] y) throws Exception {
		if (x.length != y.length) {
			throw new Exception("Array lengths do not match"); 
		}
		
		for (int i = 0; i < x.length; i++) {
			if (Double.isNaN(x[i]) || Double.isNaN(y[i])) {
				throw new Exception("Invalid value in array"); 
			}
		}
		
		double[] out = new double[x.length]; 
		for (int i = 0; i < x.length; i++) {
			out[i] = x[i] - y[i]; 
		}
		return out; 
	}
	
	/** 
	 * Element-wise multiplication between two arrays
	 * @param x			Array 1
	 * @param y			Array 2
	 * @return			x ./ y
	 * @throws Exception
	 */
	public static double[] multiplyArray(double[] x, double[] y) throws Exception {
		if (x.length != y.length) {
			throw new Exception("Array lengths do not match"); 
		}
		
		for (int i = 0; i < x.length; i++) {
			if (Double.isNaN(x[i]) || Double.isNaN(y[i])) {
				throw new Exception("Invalid value in array"); 
			}
		}
		
		double[] out = new double[x.length]; 
		for (int i = 0; i < x.length; i++) {
			out[i] = x[i] * y[i];
		}
		return out; 
	}

	/** 
	 * Element-wise division between two arrays
	 * @param x			Array 1
	 * @param y			Array 2
	 * @return			x ./ y
	 * @throws Exception
	 */
	public static double[] divideArray(double[] x, double[] y) throws Exception {
		if (x.length != y.length) {
			throw new Exception("Array lengths do not match"); 
		}
		
		double[] out = new double[x.length]; 
		for (int i = 0; i < x.length; i++) {
			out[i] = x[i] / y[i];
			out[i] = Double.isNaN(out[i]) ? 0 : out[i]; 
		}
		return out; 
	}
	
	/**
	 * Find index of String in array
	 * @param array		Array of strings 
	 * @param x			String
	 * @return			Index of String x in array
	 * @throws Exception
	 */
	public static int getIndex(String[] array, String x) throws Exception {
		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(x)) {
				return i; 
			}
		}
		throw new Exception("String not found in array"); 
	}
}
