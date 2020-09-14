package net.novaplay.skyblock.addons;

import java.util.HashMap;

import cn.nukkit.Player;
import net.novaplay.callback.MySQLCallback;
import net.novaplay.core.NovaCore;
import net.novaplay.core.player.DisplayName;
import net.novaplay.core.player.NPlayer;
import net.novaplay.skyblock.addons.quest.Quest;
import net.novaplay.skyblock.addons.quest.QuestRegistry;

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
		NovaCore.getInstance().mysql.fetchData(new MySQLCallback<HashMap<String,Object>>(){
			public void accept(HashMap<String,Object> map) {
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
			}
			public void fail(Throwable fail) {
				
			}
		}, player.getName().toLowerCase(), "skyblock");
	}
	
	public void createData() {
		NovaCore.getInstance().mysql.updateData("INSERT INTO `skyblock` (`nickname`,`emeralds`,`amount`,`quest`,`small_keys`,`big_keys`,`rank`) VALUES ('"+player.getName().toLowerCase()+"','0','0','0','0','0','basic')");
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
		name.setRankVisible(d.hasRankWhileFake());
		name.setName(d.getPlayerName());
		this.player.setNameTag(name.getFinalFormat());
		this.player.setDisplayName(this.player.getNameTag());
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
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `emeralds`='"+emeralds+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `amount` = '"+count+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `quest` ='"+questIndex+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `small_keys` ='"+smallKeys+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `big_keys` ='"+bigKeys+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
		NovaCore.getInstance().mysql.updateData("UPDATE `skyblock` SET `rank` ='"+rank+"' WHERE `nickname`='"+getPlayer().getName().toLowerCase()+"'");
	}
	
	public enum Keys{
		SMALL,
		BIG;
	}
	
	
}
