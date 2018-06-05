package assignment.util;

import java.util.Comparator;

public class PairComparator implements Comparator<Pair> {
	@Override
	public int compare(Pair a, Pair b) {
		return a.value < b.value ? -1 : a.value == b.value ? 0 : 1; 
	}
}
