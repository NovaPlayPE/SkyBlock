package net.novaplay.skyblock.addons.quest;

import java.util.*;
import cn.nukkit.item.Item;
import cn.nukkit.block.Block;

public class Quest {

	public int id;
	public final Set<Integer> blockId;
	public final Set<Integer> blockDamage;
	public final Item[] reward;
	public final QuestType type;
	public final String info;
	public final String finishInfo;
	public final int requiredAmount;
	
	public Quest(int id, Set<Integer> blockId, Set<Integer> blockDamage, List<String> info, List<String> finishInfo, Item[] reward, int requiredAmount, QuestType typ) {
		this.id = id;
		this.reward = reward;
		this.type = typ;
		this.blockId = blockId;
		this.blockDamage = blockDamage;
		this.info = String.join("\n",info).replaceAll("&","§");
		this.finishInfo = String.join("\n",finishInfo).replaceAll("&","§");
		this.requiredAmount = requiredAmount;
	}
	
	public boolean accept(Object o) {
		return o instanceof Block ? this.accept((Block)o) : o instanceof Item && this.accept((Item)o);
	}
	
	public boolean accept(Item i) {
		return this.blockId.contains(i.getId()) && this.blockDamage.contains(i.getDamage());
	}
	
	public boolean accept(Block i) {
		return this.blockId.contains(i.getId()) && this.blockDamage.contains(i.getDamage());
	}
	
	public static enum QuestType{
		CRAFT,
		PLACE,
		DESTROY,
		FISH,
		KILL,
		SHEAR,
		ENCHANT,
		CLICKENTITY;
	}
	
}
