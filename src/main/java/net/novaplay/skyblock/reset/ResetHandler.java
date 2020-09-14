package net.novaplay.skyblock.reset;

import cn.nukkit.Player;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

public class ResetHandler {

    @Getter
    private Map<String, Reset> resets = new HashMap<>();

    public Reset getResetTimer(Player player) {
        return this.resets.get(player.getName().toLowerCase());
    }

    public void addResetTimer(Player player) {
        this.resets.put(player.getName().toLowerCase(), new Reset(this, player));
    }

    public void removeReset(Reset reset) {
        this.resets.remove(reset.getPlayer().getName().toLowerCase());
    }

    public void tick() {
        for (Reset reset : resets.values()) {
            reset.tick();
        }
    }

}