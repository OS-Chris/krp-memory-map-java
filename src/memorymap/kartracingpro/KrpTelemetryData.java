/**
 * 
 */
package memorymap.kartracingpro;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpTelemetryData {

	public float onTrackTime;
	public float trackPositionPct;
	public float rpm; /* engine rpm */
	public float engineTemperature; /* degrees Celsius */
	public float waterTemperature; /* degrees Celsius */
	public byte gear; /* 0 = Neutral */
	public float fuel; /* liters */
	public float speedometer; /* meters/second */
	public float posX, posY, posZ; /* world position of a reference point attached to chassis ( not CG ) */
	public float velocityX, velocityY, velocityZ; /* velocity of CG in world coordinates. meters/second */
	public float accelerationX, accelerationY, accelerationZ; /* acceleration of CG local to chassis rotation, expressed in G ( 9.81 m/s2 ) and averaged over the latest 10ms */
	public float rotationMatrix[][]; /* [3][3] rotation matrix of the chassis */
	public float yaw, pitch, roll; /* degrees, -180 to 180 */
	public float yawVelocity, pitchVelocity, rollVelocity; /* degress / second */
	public float inputSteer; /* degrees. Negative = left */
	public float inputThrottle; /* 0 to 1 */
	public float inputBrake; /* 0 to 1 */
	public float inputFrontBrakes; /* 0 to 1 */
	public float inputClutch; /* 0 to 1. 0 = Fully engaged */
	public float wheelSpeed[]; /* [4] meters/second. 0 = front-left; 1 = front-right; 2 = rear-left; 3 = rear-right */
	
	/**
	 * 
	 */
	public KrpTelemetryData() {}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("On Track Time : ").append(onTrackTime).append("\n");
		sb.append("Track Position : ").append(trackPositionPct).append("\n");
		sb.append("RPM : ").append(rpm).append("\n");
		sb.append("Engine Temperature (degrees celcius) : ").append(engineTemperature).append("\n");
		sb.append("Water Temperature (degrees celcius) : ").append(waterTemperature).append("\n");
		sb.append("Gear : ").append(gear).append("\n");
		sb.append("Fuel (litres) : ").append(fuel).append("\n");
		sb.append("Speed (m/s) : ").append(speedometer).append("\n");
		sb.append("Position X : ").append(posX).append("\n");
		sb.append("Position Y : ").append(posY).append("\n");
		sb.append("Position Z : ").append(posZ).append("\n");
		sb.append("Velocity X : ").append(velocityX).append("\n");
		sb.append("Velocity Y : ").append(velocityY).append("\n");
		sb.append("Velocity Z : ").append(velocityZ).append("\n");
		sb.append("Accelleration X : ").append(accelerationX).append("\n");
		sb.append("Accelleration Y : ").append(accelerationY).append("\n");
		sb.append("Accelleration Z : ").append(accelerationZ).append("\n");
		sb.append("Rotation Matrix [][] : CBF ATM\n");
		sb.append("Yaw : ").append(yaw).append("\n");
		sb.append("Pitch : ").append(pitch).append("\n");
		sb.append("Roll : ").append(roll).append("\n");
		sb.append("Yaw Velocity : ").append(yawVelocity).append("\n");
		sb.append("Pitch Velocity : ").append(pitchVelocity).append("\n");
		sb.append("Roll Velocity : ").append(rollVelocity).append("\n");
		sb.append("Input Steering : ").append(inputSteer).append("\n");
		sb.append("Input Throttle : ").append(inputThrottle).append("\n");
		sb.append("Input Brakes : ").append(inputBrake).append("\n");
		sb.append("Input Front Brakes : ").append(inputFrontBrakes).append("\n");
		sb.append("Input Clutch : ").append(inputClutch).append("\n");
		sb.append("Wheel Speed [] : CBF ATM");
		return sb.toString();
	}
}
