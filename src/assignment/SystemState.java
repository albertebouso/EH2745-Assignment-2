package assignment;

import java.util.Map;

public class SystemState {
	private int time; 
	Map<String, Float> state;
	
	public SystemState() {
	}
	
	public SystemState(int systemTime, Map<String, Float> measurements) {
		this.time = systemTime; 
		this.state = measurements; 
	}	
}
