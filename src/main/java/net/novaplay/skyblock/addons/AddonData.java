package net.novaplay.skyblock.addons;

import java.util.HashMap;

import cn.nukkit.Player;
import net.novaplay.callback.MySQLCallback;
import net.novaplay.core.NovaCore;
import net.novaplay.core.player.NPlayer;
import net.novaplay.core.player.name.DisplayName;
import net.novaplay.skyblock.addons.quest.Quest;
import net.novaplay.skyblock.addons.quest.QuestRegistry;
import ru.ragnok123.sqlNukkitLib.utils.Pair;

public class AddonData {
	
	private Player player = null;
	
	public static String BASIC = "basic";
	public static String FLUEGER = "flueger";
	
	public int brokenLeaves = 0;
	
	private int emeralds = 0;
	
	private int count = 0;
	private int questIndex = 0;
	
	private int smallKeys = 0;
	private int bigKeys = 0;
	private String rank = "";
	
	private Quest currentQuest = null;
	private boolean isRegistered;
	
	public AddonData(Player p) {
		this.player = p;
		
		NovaCore.getDatabase().select("skyblock", "nickname", player.getName().toLowerCase(), map -> {
			if(!map.isEmpty()) {
				isRegistered = true;
				emeralds = (int)map.get("emeralds");
				count = (int)map.get("amount");
				questIndex = (int)map.get("quest");
				smallKeys = (int)map.get("small_keys");
				bigKeys = (int)map.get("big_keys");
				rank = (String)map.get("rank");
			} else {
				isRegistered = false;
			}
		});
		
	}
	
	public void createData() {
		isRegistered = true;
		NovaCore.getDatabase().insert("skyblock", new Pair[] {
				new Pair("nickname", player.getName().toLowerCase())
		});
	}
	
	public int getEmeralds() {
		return this.emeralds;
	}
	
	public void addEmeralds(int amount) {
		this.emeralds += amount;
	}
	
	public void addKeys(int keys, Keys type) {
		if(type == Keys.SMALL) {
			this.smallKeys += keys;
		} else {
			this.bigKeys += keys;
		}
	}
	
	public int getKeys(Keys keys) {
		if(keys == Keys.SMALL) {
			return smallKeys;
		} else {
			return bigKeys;
		}
	}
	
	public void setupDisplayName() {
		NPlayer d = NovaCore.getPlayer(this.player.getName());
		DisplayName name = d.getDisplayManager();
		name.setSuffix(getSuffix());
		name.setColorVisible(false);
		name.setLevelVisible(true);
		name.setRankVisible(d.getFakeManager().checkRank());
		name.setName(d.getName());
		d.getNickManager().updateNametag();
	}
	
	public String getRank() {
		return this.rank;
	}
	
	public String getSuffix() {
		String suffix = "";
		switch(this.getRank()) {
		case "basic":
			suffix = "";
			break;
		case "flueger":
			suffix = "§f[§bFLUEGER§f] ";
			break;
		}
		return suffix;
	}
	
	public int getCount() {
		return this.count;
	}
	
	public Quest getCurrentQuest(){
		return this.currentQuest;
	}
	
	public Player getPlayer() {
		return this.player;
	}
	
	public boolean isRegistered()
	{
		return isRegistered;
	}
	
	public void save() {
		NovaCore.getDatabase().update("skyblock", "nickname", getPlayer().getName().toLowerCase(), new Pair[] {
				new Pair("emeralds", emeralds),
				new Pair("amount", count),
				new Pair("quest", questIndex),
				new Pair("small_keys", smallKeys),
				new Pair("big_keys", bigKeys),
				new Pair("rank", rank)
		});
	}
	
	public enum Keys{
		SMALL,
		BIG;
	}
	
	
}
