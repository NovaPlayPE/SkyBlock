package net.novaplay.skyblock.mine;

import java.util.*;
import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;
import net.novaplay.skyblock.Main;

public class MineManager {
	
	private Config minesConfig = null;
	private Main plugin = null;
	private HashMap<String,MinePlayer> players = new HashMap<String,MinePlayer>();
	private ArrayList<Mine> mines = new ArrayList<Mine>();
	
	public MineManager(Main main) {
		plugin = main;
		minesConfig = new Config(plugin.getDataFolder()+"/mines.yml", Config.YAML);
		if(minesConfig.getKeys(false).size() != 0) {
			loadMines();
		}
	}
	
	public MinePlayer getPlayer(Player p) {
		String nick = p.getName().toLowerCase();
		if(players.containsKey(nick)) {
			return players.get(nick);
		} else {
			MinePlayer data = new MinePlayer(p);
			players.put(nick,data);
			return data;
		}
	}
	
	public void removePlayer(Player p) {
		String nick = p.getName().toLowerCase();
		if(players.containsKey(nick)) {
			players.remove(nick);
		}
	}
	
	public ArrayList<Mine> getMines(){
		return this.mines;
	}
	
	public Mine addMine(String name, String level,Vector3 pos1, Vector3 pos2, ArrayList<Integer> ids) {
		Mine mine = new Mine(name,level,pos1,pos2,ids);
		minesConfig.set(name+".level", level);
		minesConfig.set(name+".minX", String.valueOf(pos1.x));
		minesConfig.set(name+".minY", String.valueOf(pos1.y));
		minesConfig.set(name+".minZ", String.valueOf(pos1.z));
		minesConfig.set(name+".maxX", String.valueOf(pos2.x));
		minesConfig.set(name+".maxY", String.valueOf(pos2.y));
		minesConfig.set(name+".maxZ", String.valueOf(pos2.z));
		minesConfig.set(name+".ids", ids);
		minesConfig.save();
		mines.add(mine);
		mine.reset();
		return mine;
	}
	
	public void loadMines() {
		Server.getInstance().getLogger().info("Loading all mines");
		for(String names : minesConfig.getKeys(false)) {
			Server.getInstance().getLogger().info("Loading mine names: " + names);
			String level = minesConfig.getString(names+".level");
			Vector3 pos1 = new Vector3(Double.valueOf((String)minesConfig.get(names+".minX")),Double.valueOf((String)minesConfig.get(names+".minY")),Double.valueOf((String)minesConfig.get(names+".minZ")));
			Vector3 pos2 = new Vector3(Double.valueOf((String)minesConfig.get(names+".maxX")),Double.valueOf((String)minesConfig.get(names+".maxY")),Double.valueOf((String)minesConfig.get(names+".maxZ")));
			ArrayList<Integer> ids = (ArrayList<Integer>)minesConfig.getIntegerList(names+".ids"); 
			Mine mine = new Mine(names,level,pos1,pos2,ids);
			mines.add(mine);
			mine.reset();
		}
	}
	
}
