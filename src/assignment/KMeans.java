package assignment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import assignment.util.Function;

public class KMeans {
	private int k, maxItr;
	
	private double[][] means; 
	
	public KMeans() {
	}
	
	public KMeans(int k, int threshold) {
		this.k = k; 
		this.maxItr = threshold; 
	}

	/**
	 * Function for the K-means algorithm 
	 * @param train		Array of the training set 
	 * @return			Array of the clustered training set
	 */
	public void train(double[][] train) {
		try {
			// Randomly initialize the means
			initializeMeans(train);
			
			// KNN algorithm 
			for (int itr = 0; itr < maxItr; itr++) {
				// Initialize zero arrays for each cluster
				List<double[]> cluster = new ArrayList<double[]>();
				for (int i = 0; i < this.k; i++) {
					double[] zeros = new double[means[0].length]; 
					cluster.add(zeros); 
				}
				
				for (double[] sample : train) {
					double minDist = Double.MAX_VALUE; 
					int sampleCluster = 0; 
					
					// Calculate the distance between training sample and each mean
					for (int i = 0; i < means.length; i++) {
						double dist = Function.euclideanDist(sample, means[i]); 
						if (dist < minDist) {
							minDist = dist; 
							sampleCluster = i; 
						}
					}
					
					// Add training sample to cluster with lowest distance 
					for (int i = 0; i < sample.length; i++) {
						cluster.get(sampleCluster)[i] = sample[i]; 
					}
				}
				
				// Calculate new means
				for (int i = 0; i < means.length; i++) {
					for (double x : cluster.get(i)) {
						x /= cluster.get(i).length; 
					}
					means[i] = cluster.get(i); 
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	/**
	 * Randomly initialize the means based on the K-means++ algorithm 
	 * @param k		number of means
	 * @throws Exception 
	 */
	public void initializeMeans(double[][] train) throws Exception {
		// Randomly choose the first mean
		means = new double[k][]; 
		int nMeans = 0;	// Variable to keep track of how many means have been initialized 
		means[nMeans] = train[(int)(Math.random() * (train.length))].clone(); 
		nMeans++; 
		
		// Randomly choose the remainder of the means weighted by the distance between 
		// the point and the closest mean
		for (int i = 1; i < k; i++) {
			double[] dists = new double[train.length]; 
			Arrays.fill(dists, Double.MAX_VALUE);
			
			for (int j = 0; j < nMeans; j++) {
				for (int k = 0; k < train.length; k++) {
					double dist = Function.euclideanDist(means[j], train[k]); 
					if (dist < dists[k]) {
						dists[k] = dist; 
					}
				}
			}
			means[nMeans] = train[weightedRandom(dists)]; 
			nMeans++; 
		}
	}
		
	/**
	 * Function to generate weighted random integer number 
	 * @param weight	Array of weights 	
	 * @return 			Index of array
	 * @throws Exception 
	 */
	public int weightedRandom(double[] weight) throws Exception {
		// Generate cumulative sum array of the weights
		double[] cWeight = new double[weight.length+1]; 
		double sum = 0; 
		cWeight[0] = sum; 
		for (int i = 1; i < cWeight.length; i++) {
			sum += weight[i-1]; 
			cWeight[i] = sum; 
		}
		
		// Generate random number
		double rand = Math.random() * cWeight[cWeight.length-1]; 
		
		// Find index of array where the random number lies 
		for (int i = 0; i < weight.length; i++) {
			if (cWeight[i] <= rand && cWeight[i+1] > rand) {
				return i; 
			}
		}
		throw new Exception("Index not found in weighted random");
	}
	
	/**
	 * Function to ground all the points to the closest mean 
	 * @param dataset		Array of all the points
	 * @return				List of the clusters 
	 */
	public List<double[][]> cluster(double[][] dataset) {
		// Initialize empty array for each cluster
		List<double[][]> cluster = Function.create2DArrayList(this.k); 
		
		try {
			for (double[] sample : dataset) {
				double minDist = Double.MAX_VALUE; 
				int sampleClass = 0;
				
				// Find minimum distance between sample and each mean
				for (int i = 0; i < this.k; i++) {
					double dist = Function.euclideanDist(sample, means[i]); 
					if (dist < minDist) {
						minDist = dist; 
						sampleClass = i; 
					}
				}
				
				// Add sample to cluster with lowest distance to mean
				cluster.set(sampleClass, Function.appendArray(cluster.get(sampleClass), sample)); 
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return cluster; 
	}
		
	/**
	 * Function to get the means 
	 * @return		Array of all the means
	 */
	public double[][] getMeans() {
		return means; 
	}	
}
