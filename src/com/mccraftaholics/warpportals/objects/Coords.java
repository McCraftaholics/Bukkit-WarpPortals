package com.mccraftaholics.warpportals.objects;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

public class Coords {
	public double x, y, z;
	public World world;

	public Coords(World world, double i, double j, double k) {
		this.world = world;
		x = i;
		y = j;
		z = k;
	}

	public Coords(Block b) {
		world = b.getWorld();
		x = b.getX();
		y = b.getY();
		z = b.getZ();
	}

	public Coords(Location loc) {
		this(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
	}

	public Coords(String coordsString) throws Exception {
		String t = coordsString.trim();
		if (t.matches("\\(.+,-*[0-9]+\\.*[0-9]*,-*[0-9]+\\.*[0-9]*,-*[0-9]+\\.*[0-9]*\\)")) {
			String n = coordsString.substring(1, coordsString.length() - 1);
			String[] s = n.split(",");
			world = Bukkit.getWorld(s[0]);
			x = Double.parseDouble(s[1]);
			y = Double.parseDouble(s[2]);
			z = Double.parseDouble(s[3]);
		} else {
			throw new Exception("Invalid Coordinate String");
		}
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(17, 31).append(world.getName()).append(x).append(y).append(z).toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (!(obj instanceof Coords))
			return false;
		else {
			Coords crd = (Coords) obj;
			if (crd.world.getName().equals(this.world.getName()) && crd.x == this.x && crd.y == this.y && crd.z == this.z)
				return true;
			return false;
		}
	}

	public String toString() {
		return "(" + world.getName() + "," + String.valueOf(x) + "," + String.valueOf(y) + "," + String.valueOf(z) + ")";
	}

}
