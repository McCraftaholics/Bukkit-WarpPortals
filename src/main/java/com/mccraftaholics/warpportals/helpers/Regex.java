package com.mccraftaholics.warpportals.helpers;

public class Regex {

	public static final String ALPHANUMERIC_NS_TEXT = "[a-zA-Z0-9]+";
	
	public static final String IS_INTEGER = "[0-9]+";

    public static final String IS_UUID_STRICT = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[34][0-9a-fA-F]{3}-[89ab][0-9a-fA-F]{3}-[0-9a-fA-F]{12}";
    public static final String IS_UUID = "[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}";

}
