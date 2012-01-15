/**
 * 
 */
package memorymap.kartracingpro;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpEventData {

	public String driverName;
	public String kartName;
	public byte driveType;
	public byte gearCount;
	public float maxRpm;
	public float limiter;
	public float shiftRpm;
	public float engineOptimalTemperature;
	public float engineTemperatureAlarms[];
	public float maxFuel;
	public String category;
	public String dash;
	public String trackName;
	public float trackLength;
	
	/**
	 * 
	 */
	public KrpEventData() {}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
	    sb.append("Driver Name : ").append(driverName).append("\n");
	    sb.append("Kart Name : ").append(kartName).append("\n");
	    sb.append("Drive Type : ").append(driveType).append("\n");
	    sb.append("# Of Gears : ").append(gearCount).append("\n");
	    sb.append("Max RPM : ").append(maxRpm).append("\n");
	    sb.append("Limiter : ").append(limiter).append("\n");
	    sb.append("Shift RPM : ").append(shiftRpm).append("\n");
	    sb.append("Engine Optimal Temperature : ").append(engineOptimalTemperature).append("\n");
	    sb.append("Engine Temperature Alarm (Lower) : ").append(engineTemperatureAlarms[0]).append("\n");
	    sb.append("Engine Temperature Alarm (Upper) : ").append(engineTemperatureAlarms[1]).append("\n");
	    sb.append("Max Fuel (Litres) : ").append(maxFuel).append("\n");
	    sb.append("Category : ").append(category).append("\n");
	    sb.append("Dash : ").append(dash).append("\n");
	    sb.append("Track Name : ").append(trackName).append("\n");
	    sb.append("Track Length (metres) : ").append(trackLength);
		return sb.toString();
	}
	
}
