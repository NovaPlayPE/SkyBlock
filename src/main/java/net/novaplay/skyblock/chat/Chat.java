package net.novaplay.skyblock.chat;

import cn.nukkit.Player;
import lombok.Getter;
import net.novaplay.skyblock.island.Island;

import java.util.HashMap;
import java.util.Map;

public class Chat {

    @Getter
    private Island island;

    @Getter
    private Map<String, Player> members = new HashMap<>();

    public Chat(Island island) {
        this.island = island;
    }

    public void addMember(Player player) {
        this.members.put(player.getName().toLowerCase(), player);
    }

    public void tryRemoveMember(Player player) {
        this.members.remove(player.getName().toLowerCase());
    }
}