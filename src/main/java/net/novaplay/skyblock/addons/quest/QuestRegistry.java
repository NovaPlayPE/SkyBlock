package net.novaplay.skyblock.addons.quest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestRegistry {
	
	public static List<Quest> quests = new ArrayList<>();
	
	public static void init() {
		quests = Collections.unmodifiableList(quests);
	}
	
}
