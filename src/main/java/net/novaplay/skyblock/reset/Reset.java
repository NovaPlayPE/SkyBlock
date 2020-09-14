package net.novaplay.skyblock.reset;

import cn.nukkit.Player;
import lombok.Getter;

public class Reset {

    private ResetHandler handler;

    @Getter
    private Player player;

    @Getter
    private int time = 600;

    public Reset(ResetHandler handler, Player player) {
        this.handler = handler;
        this.player = player;
    }

    public void expire() {
        this.handler.removeReset(this);
    }

    public void tick() {
        if (this.time <= 0) {
            this.expire();
        } else {
            this.time--;
        }
    }

}