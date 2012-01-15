/**
 * 
 */
package memorymap.kartracingpro;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpLapData {

	public int lapTime; /* milliseconds */
	public boolean best;
	public int lapNum;
	
	/**
	 * 
	 */
	public KrpLapData() {}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Lap Time (ms) : ").append(lapTime).append("\n");
		sb.append("Is Best Lap : ").append(best).append("\n");
		sb.append("Lap Number : ").append(lapNum);
		return sb.toString();
	}
	
}