package net.novaplay.skyblock.mine;

import java.util.*;

import cn.nukkit.math.Vector3;
import cn.nukkit.Player;

public class MinePlayer {
	
	public String name;
	public ArrayList<Integer> ids = new ArrayList<Integer>();
	public Vector3 pos1;
	public Vector3 pos2;
	public int state = 0; // 0 - nothing, 1 - pos1, 2 - pos2, 3 - ids, 4 - ids
	
	public Player player = null;
	
	public MinePlayer(Player player) {
		this.player = player;
	}
	
	public void addBlock(int id) {
		if(!ids.contains(id)) {
			ids.add(id);
		}
	}
	
}
