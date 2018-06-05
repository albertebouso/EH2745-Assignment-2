package assignment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import assignment.util.Function;
import assignment.util.Pair;
import assignment.util.PairComparator;

public class KNearestNeighbors {
	private int k; 
	
	public KNearestNeighbors() {
	}
	
	public KNearestNeighbors(int k) {
		this.k = k;
	}
	
//	public List<double[][]> classify(double[][] dataset, List<double[][]> clusters) {
//		List<double[][]> classification = Function.create2DArrayList(clusters.size()); 
	public List<Integer> classify(double[][] dataset, List<double[][]> clusters) {
		List<Integer> classification = new ArrayList<Integer>(); 
		
		try {
			for (double[] sample : dataset) {
				List<Pair> distList = new ArrayList<Pair>(); 
				
				// Calculate distance
				for (int i = 0; i < clusters.size(); i++) {
					for (double[] clusterSample : clusters.get(i)) {
						double dist = Function.euclideanDist(sample, clusterSample); 
						distList.add(new Pair(i, dist)); 
					}
				}
				
				// Sort distance
				Collections.sort(distList, new PairComparator());
				
				// Classify by majority count
				int[] count = new int[clusters.size()]; 
				for (int i = 0; i < this.k; i++) {
					count[distList.get(i).key]++; 
				}
				int idx = Function.getMaxIndex(count); 
				classification.add(idx); 
//				classification.set(idx, Function.appendArray(classification.get(index), sample));
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return classification; 
	}
}
