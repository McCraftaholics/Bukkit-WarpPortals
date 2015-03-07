package com.mccraftaholics.warpportals.objects;

public class NullWorldException extends Exception {

	String worldName;

	public NullWorldException() {
		super();
	}

	public NullWorldException(String worldName, String message) {
		super(message);
		this.worldName = worldName;
	}
	
	public String getWorldName() {
		return worldName;
	}

	public static NullWorldException createForWorldName(String worldName) {
		NullWorldException e = new NullWorldException(worldName, "\"" + worldName + "\" does not exist.");
		return e;
	}
}
