package net.novaplay.skyblock.chat;

import cn.nukkit.Player;
import net.novaplay.skyblock.island.Island;

import java.util.HashMap;
import java.util.Map;

public class ChatHandler {

    private Map<String, Chat> chats = new HashMap<>();

    public boolean isInChat(Player player) {
        for (Chat chat : chats.values()) {
            if (chat.getMembers().containsKey(player.getName().toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public Chat getPlayerChat(Player player) {
        for (Chat chat : chats.values()) {
            if (chat.getMembers().containsKey(player.getName().toLowerCase())) {
                return chat;
            }
        }

        return null;
    }

    public void addPlayerToChat(Player player, Island island) {
        Chat chat = new Chat(island);
        chat.addMember(player);
        this.chats.put(island.getIdentifier(), chat);
    }

    public void removePlayerFromChat(Player player) {
        for (Chat chat : chats.values()) {
            chat.tryRemoveMember(player);
        }
    }

}