package com.mccraftaholics.warpportals.helpers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.bukkit.Location;

import com.mccraftaholics.warpportals.objects.Coords;
import com.mccraftaholics.warpportals.objects.CoordsPY;

public class Utils {
	
	public static boolean arrayContains(Object[] array, Object key) {
		for (Object object : array) {
			if (object.equals(key))
				return true;
		}
		return false;
	}

	public static String readFile(String path, Charset encoding) throws IOException {
		byte[] buffer = new byte[(int) new File(path).length()];
		BufferedInputStream f = null;
		try {
			f = new BufferedInputStream(new FileInputStream(path));
			f.read(buffer);
		} finally {
			if (f != null)
				try {
					f.close();
				} catch (IOException ignored) {
				}
		}
		return new String(buffer);
	}

	public static void copy(InputStream in, File file) throws IOException {
		OutputStream out = new FileOutputStream(file);
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		in.close();
	}

	public static String[] ymlLevelCleanup(String[] orig, String startComb) {
		String[] comb = new String[orig.length];
		int i = 0;
		boolean fRun = true;
		for (String line : orig) {
			if (fRun) {
				comb[i] = line;
				fRun = false;
			} else {
				if (!line.startsWith(startComb))
					i++;
				comb[i] = (comb[i] != null ? comb[i] + "\n" : "") + line;
			}
		}
		return comb;
	}

	public static void coordsToLoc(Coords coords, Location loc) {
		loc.setWorld(coords.world);
		loc.setX(coords.x);
		loc.setY(coords.y);
		loc.setZ(coords.z);
	}

	public static void coordsToLoc(CoordsPY coords, Location loc) {
		loc.setWorld(coords.world);
		loc.setX(coords.x);
		loc.setY(coords.y);
		loc.setZ(coords.z);
		loc.setPitch(coords.pitch);
		loc.setYaw(coords.yaw);
	}

}
