package net.novaplay.skyblock.mine;

import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.math.SimpleAxisAlignedBB;
import cn.nukkit.math.Vector3;

import java.util.*;

public class Mine extends SimpleAxisAlignedBB {
	
	private String name = "";
	private ArrayList<Integer> ids = new ArrayList<Integer>();
	private String levelName = "world";
	
	public Mine(String name,String level,Vector3 pos1, Vector3 pos2, ArrayList<Integer>ids) {
		super(pos1, pos2);
		setMinX(pos1.x);
		setMinY(pos1.y);
		setMinZ(pos1.z);
		setMaxX(pos2.x);
		setMaxY(pos2.y);
		setMaxZ(pos2.z);
		this.name = name;
		this.ids = ids;
		this.levelName = level;
		for(int i : ids) {
			Server.getInstance().getLogger().info("Loaded mine " + name + ", on pos1: " +pos1.toString() + " and pos2: " +pos2.toString()+" with blocks: " + String.valueOf(i));
		}
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getLevelName() {
		return this.levelName;
	}
	
	public void reset() {
		if(!Server.getInstance().isLevelLoaded(levelName)) {
			Server.getInstance().loadLevel(levelName);
		}
		for(double x = getMinX(); x <= getMaxX(); x++) {
			for(double y = getMinY(); y <= getMaxY(); y++) {
				for(double z = getMinZ(); z <= getMaxZ(); z++) {
					int random = ids.get(new Random().nextInt(ids.size()));
					Server.getInstance().getLevelByName(levelName).setBlock(new Vector3(x,y,z), Block.get(random));
					Server.getInstance().getLogger().info("X:" + String.valueOf(x));
					Server.getInstance().getLogger().info("Y:" + String.valueOf(y));
					Server.getInstance().getLogger().info("Z:" + String.valueOf(z));
					Server.getInstance().getLogger().info("ID:" + String.valueOf(random));
				}
			}
		}
	}

}
