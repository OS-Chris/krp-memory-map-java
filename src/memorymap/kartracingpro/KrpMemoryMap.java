/**
 * 
 */
package memorymap.kartracingpro;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Kernel32Util;
import com.sun.jna.platform.win32.WinNT.HANDLE;

/**
 * 
 * @author Christian Aylward
 * @version 1.0.0
 */
public class KrpMemoryMap {

	private static final long OFFSET_EVENT_ID = 2;
	private static final long OFFSET_SESSION_ID = 6;
	private static final long OFFSET_TELEMETRY_ID = 10;
	private static final long OFFSET_LAP_ID = 14;
	private static final long OFFSET_SPLIT_ID = 18;
	
	private static final long OFFSET_STRUCT_EVENT = 22;
	private static final long OFFSET_STRUCT_SESSION = 562;
	private static final long OFFSET_STRUCT_TELEMETRY = 578;
	private static final long OFFSET_STRUCT_LAP = 742;
	private static final long OFFSET_STRUCT_SPLIT = 754;
	
	private static final byte EXPECTED_PLUGIN_VERSION = 1;
	
	private HANDLE fileHandle;
	private Pointer ptr; // pointer to the memory map created by the KartRacingPro DLL plugin
	private KrpEventData eventData;
	private KrpSessionData sessionData;
	private KrpTelemetryData telemetryData;
	private KrpLapData lapData;
	private KrpSplitData splitData;
	int lastEventId = 0, lastSessionId = 0;
	int lastTelemetryId = 0, lastLapId = 0, lastSplitId = 0;
	
	private List<KrpEventDataListener> eventListeners;
	private List<KrpSessionDataListener> sessionListeners;
	private List<KrpTelemetryDataListener> telemetryListeners;
	private List<KrpLapDataListener> lapListeners;
	private List<KrpSplitDataListener> splitListeners;
	
	/**
	 * 
	 */
	public KrpMemoryMap() {
		eventListeners = new ArrayList<KrpEventDataListener>(1);
		sessionListeners = new ArrayList<KrpSessionDataListener>(1);
		telemetryListeners = new ArrayList<KrpTelemetryDataListener>(1);
		lapListeners = new ArrayList<KrpLapDataListener>(1);
		splitListeners = new ArrayList<KrpSplitDataListener>(1);
	}
	
	public boolean open() {
		boolean output = false;
		fileHandle = Kernel32.INSTANCE.CreateFileMapping(Kernel32.INVALID_HANDLE_VALUE, null, Kernel32.PAGE_READONLY, 0, 256, "Local\\KartRacingProMemoryMap");
	    if (fileHandle == null) {
	    	System.out.println("Coudn't create file mapping : " + Kernel32Util.formatMessageFromLastErrorCode(Kernel32.INSTANCE.GetLastError()));
	    } else {
		    ptr = Kernel32.INSTANCE.MapViewOfFile(fileHandle, Kernel32.FILE_READ_DATA, 0, 0, 256);
		    if (ptr == null) {
		    	System.out.println("Couldn't get memory map : " + Kernel32Util.formatMessageFromLastErrorCode(Kernel32.INSTANCE.GetLastError()));
		    	Kernel32.INSTANCE.CloseHandle(fileHandle);
		    }
		    else {
		    	System.out.println("Got pointer to memory map");
		    	byte pluginVersion = ptr.getByte(0);
		    	if (pluginVersion != EXPECTED_PLUGIN_VERSION) {
		    		System.out.println("Unsupported Kart Racing Pro Memory Map plugin (v" + pluginVersion + ")");
		    		close();
		    	}
		    	output = true;
		    }
	    }
	    return output;
	}
	
	public void close() {
		if (ptr != null) {
			Kernel32.INSTANCE.UnmapViewOfFile(ptr);
		}
		if (fileHandle != null) {
			Kernel32.INSTANCE.CloseHandle(fileHandle);
		}
	}
	
	public void addEventDataListener(KrpEventDataListener listener) {
		if (listener != null) eventListeners.add(listener);
	}

	public void addSessionDataListener(KrpSessionDataListener listener) {
		if (listener != null) sessionListeners.add(listener);
	}

	public void addTelemetryDataListener(KrpTelemetryDataListener listener) {
		if (listener != null) telemetryListeners.add(listener);
	}

	public void addLapDataListener(KrpLapDataListener listener) {
		if (listener != null) lapListeners.add(listener);
	}

	public void addSplitDataListener(KrpSplitDataListener listener) {
		if (listener != null) splitListeners.add(listener);
	}
	
	public void process() throws Exception {
		if (!eventListeners.isEmpty() && isNewEventDataAvailable()) {
			updateEventData();
			for (KrpEventDataListener listener : eventListeners) {
				listener.onKrpEventData(eventData);
			}
		}
		if (!sessionListeners.isEmpty() && isNewSessionDataAvailable()) {
			updateSessionData();
			for (KrpSessionDataListener listener : sessionListeners) {
				listener.onKrpSessionData(sessionData);
			}
		}
		if (!telemetryListeners.isEmpty() && isNewTelemetryDataAvailable()) {
			updateTelemetryData();
			for (KrpTelemetryDataListener listener : telemetryListeners) {
				listener.onKrpTelemetryData(telemetryData);
			}
		}
		if (!lapListeners.isEmpty() && isNewLapDataAvailable()) {
			updateLapData();
			for (KrpLapDataListener listener : lapListeners) {
				listener.onKrpLapDataListener(lapData);
			}
		}
		if (!splitListeners.isEmpty() && isNewSplitDataAvailable()) {
			updateSplitData();
			for (KrpSplitDataListener listener : splitListeners) {
				listener.onKrpSplitData(splitData);
			}
		}
	}
	
	public int getState() {
		return (int)ptr.getByte(0);
	}
	
	private boolean isNewEventDataAvailable() {
		int newId = ptr.getInt(OFFSET_EVENT_ID);
		return (newId != lastEventId);
	}
	
	private void updateEventData() throws UnsupportedEncodingException {
    	if (eventData == null) eventData = new KrpEventData();
    	lastEventId = ptr.getInt(OFFSET_EVENT_ID);
    	long offset = OFFSET_STRUCT_EVENT; // the starting offset of the corresponding structure in the memory map
	    eventData.driverName = readStringFromPointer(ptr, offset, 100);
	    eventData.kartName = readStringFromPointer(ptr, offset += 100, 100);
	    eventData.driveType = (byte)ptr.getInt(offset += 100);
	    eventData.gearCount = (byte)ptr.getInt(offset += 4);
	    eventData.maxRpm = ptr.getFloat(offset += 4);
		eventData.limiter = ptr.getFloat(offset += 4);
	    eventData.shiftRpm = ptr.getFloat(offset += 4);
		eventData.engineOptimalTemperature = ptr.getFloat(offset += 4);
		eventData.engineTemperatureAlarms = new float[2];
		eventData.engineTemperatureAlarms[0] = ptr.getFloat(offset += 4); /* degrees Celsius. Lower and upper limits */
		eventData.engineTemperatureAlarms[1] = ptr.getFloat(offset += 4);
		eventData.maxFuel = ptr.getFloat(offset += 4);
		eventData.category = readStringFromPointer(ptr, offset += 4, 100);
		eventData.dash = readStringFromPointer(ptr, offset += 100, 100);
		eventData.trackName = readStringFromPointer(ptr, offset += 100, 100);
		eventData.trackLength = ptr.getFloat(offset += 100);
	}
	
	private boolean isNewSessionDataAvailable() {
		int newId = ptr.getInt(OFFSET_SESSION_ID);
		return (newId != lastSessionId);
	}
	
	private void updateSessionData() {
		if (sessionData == null) sessionData = new KrpSessionData();
		lastSessionId = ptr.getInt(OFFSET_SESSION_ID);
		long offset = OFFSET_STRUCT_SESSION;
		sessionData.session = (byte)ptr.getInt(offset); /* 0 = testing; 1 = practice; 2 = qualify; 3 = pre-final; 4 = final */
		sessionData.conditions = (byte)ptr.getInt(offset += 4); /* 0 = sunny; 1 = cloudy; 2 = rainy */
		sessionData.airTemperature = ptr.getFloat(offset += 4); /* degrees Celsius */
		sessionData.trackTemperature = ptr.getFloat(offset += 4); /* degrees Celsius */
	}

	private boolean isNewTelemetryDataAvailable() {
		int newId = ptr.getInt(OFFSET_TELEMETRY_ID);
		return (newId != lastTelemetryId);
	}
	
	private void updateTelemetryData() {
		if (telemetryData == null) telemetryData = new KrpTelemetryData();
		lastTelemetryId = ptr.getInt(OFFSET_TELEMETRY_ID);
		long offset = OFFSET_STRUCT_TELEMETRY; // the starting offset of the corresponding structure in the memory map
		telemetryData.onTrackTime = ptr.getFloat(offset);
		telemetryData.trackPositionPct = ptr.getFloat(offset += 4);
		telemetryData.rpm = ptr.getFloat(offset += 4); /* engine rpm */
		telemetryData.engineTemperature = ptr.getFloat(offset += 4); /* degrees Celsius */
		telemetryData.waterTemperature = ptr.getFloat(offset += 4); /* degrees Celsius */
		telemetryData.gear = (byte)ptr.getInt(offset += 4); /* 0 = Neutral */
		telemetryData.fuel = ptr.getFloat(offset += 4); /* liters */
		telemetryData.speedometer = ptr.getFloat(offset += 4); /* meters/second */
		telemetryData.posX = ptr.getFloat(offset += 4); /* world position of a reference point attached to chassis ( not CG ) */
		telemetryData.posY = ptr.getFloat(offset += 4);
		telemetryData.posZ = ptr.getFloat(offset += 4);
		telemetryData.velocityX = ptr.getFloat(offset += 4); /* velocity of CG in world coordinates. meters/second */
		telemetryData.velocityY = ptr.getFloat(offset += 4);
		telemetryData.velocityZ = ptr.getFloat(offset += 4);
		telemetryData.accelerationX = ptr.getFloat(offset += 4); /* acceleration of CG local to chassis rotation, expressed in G ( 9.81 m/s2 ) and averaged over the latest 10ms */
		telemetryData.accelerationY = ptr.getFloat(offset += 4);
		telemetryData.accelerationZ = ptr.getFloat(offset += 4);
		telemetryData.rotationMatrix = new float[3][3]; /* [3][3] rotation matrix of the chassis */
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				telemetryData.rotationMatrix[i][j] = ptr.getFloat(offset += 4);
			}
		}
		telemetryData.yaw = ptr.getFloat(offset += 4); /* degrees, -180 to 180 */
		telemetryData.pitch = ptr.getFloat(offset += 4); /* degrees, -180 to 180 */
		telemetryData.roll = ptr.getFloat(offset += 4); /* degrees, -180 to 180 */
		telemetryData.yawVelocity = ptr.getFloat(offset += 4); /* degress / second */
		telemetryData.pitchVelocity = ptr.getFloat(offset += 4); /* degress / second */
		telemetryData.rollVelocity = ptr.getFloat(offset += 4); /* degress / second */
		telemetryData.inputSteer = ptr.getFloat(offset += 4); /* degrees. Negative = left */
		telemetryData.inputThrottle = ptr.getFloat(offset += 4); /* 0 to 1 */
		telemetryData.inputBrake = ptr.getFloat(offset += 4); /* 0 to 1 */
		telemetryData.inputFrontBrakes = ptr.getFloat(offset += 4); /* 0 to 1 */
		telemetryData.inputClutch = ptr.getFloat(offset += 4); /* 0 to 1. 0 = Fully engaged */
		telemetryData.wheelSpeed = new float[4]; /* meters/second. 0 = front-left; 1 = front-right; 2 = rear-left; 3 = rear-right */
		for (int i = 0; i < 4; i++) {
			telemetryData.wheelSpeed[i] = ptr.getFloat(offset += 4);
		}
	}

	private boolean isNewLapDataAvailable() {
		int newId = ptr.getInt(OFFSET_LAP_ID);
		return (newId != lastLapId);
	}
	
	private void updateLapData() {
		if (lapData == null) lapData = new KrpLapData();
		lastLapId = ptr.getInt(OFFSET_LAP_ID);
		long offset = OFFSET_STRUCT_LAP;
		lapData.lapTime = ptr.getInt(offset);
		lapData.best = (ptr.getInt(offset += 4) == 1);
		lapData.lapNum = ptr.getInt(offset += 4);
	}

	private boolean isNewSplitDataAvailable() {
		int newId = ptr.getInt(OFFSET_SPLIT_ID);
		return (newId != lastSplitId);
	}
	
	private void updateSplitData() {
		if (splitData == null) splitData = new KrpSplitData();
		lastSplitId = ptr.getInt(OFFSET_SPLIT_ID);
		long offset = OFFSET_STRUCT_SPLIT;
		splitData.split = ptr.getInt(offset);
		splitData.splitTime = ptr.getInt(offset += 4);
		splitData.bestDiff = ptr.getInt(offset += 4);
	}
	
	private String readStringFromPointer(Pointer ptr, long offset, int length) throws UnsupportedEncodingException {
		byte[] rawBytes = ptr.getByteArray(offset, length);
	    byte[] ba = new byte[rawBytes.length];
	    for (int i = 0; i < rawBytes.length; i++) {
	    	if (rawBytes[i] == 0) break;
	    	else ba[i] = rawBytes[i];
	    }
	    return new String(ba, "UTF8");
	}
	
}