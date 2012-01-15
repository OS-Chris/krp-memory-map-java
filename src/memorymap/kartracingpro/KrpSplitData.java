/**
 * 
 */
package memorymap.kartracingpro;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpSplitData {

	public int split;
	public int splitTime; /* milliseconds */
	public int bestDiff; /* milliseconds. Difference with best lap */
	
	/**
	 * 
	 */
	public KrpSplitData() {}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Split # : ").append(split).append("\n");
		sb.append("Split Time (ms) : ").append(splitTime).append("\n");
		sb.append("Best Difference : ").append(bestDiff);
		return sb.toString();
	}
	
}