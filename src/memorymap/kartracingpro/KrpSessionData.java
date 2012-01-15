/**
 * 
 */
package memorymap.kartracingpro;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpSessionData {

	public byte session; /* 0 = testing; 1 = practice; 2 = qualify; 3 = pre-final; 4 = final */
	public byte conditions; /* 0 = sunny; 1 = cloudy; 2 = rainy */
	public float airTemperature; /* degrees Celsius */
	public float trackTemperature; /* degrees Celsius */
	
	/**
	 * 
	 */
	public KrpSessionData() {}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Session : ").append(session).append("\n");
		sb.append("Conditions : ").append(conditions).append("\n");
		sb.append("Air Temperature (degrees celcius) : ").append(airTemperature).append("\n");
		sb.append("Track Temperature (degrees celcius) : ").append(trackTemperature);
		return sb.toString();
	}
	
}