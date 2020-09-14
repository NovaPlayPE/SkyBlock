package net.novaplay.skyblock;

import cn.nukkit.utils.Config;
import lombok.Getter;

/**
 * Created by CreeperFace on 6.5.2017.
 */
public class PlayerData {

    @Getter
    public String island = null;

    public boolean can = true;

    public final Config cfg;

    public PlayerData(Config config) {
        this.cfg = config;
        this.island = cfg.getString("island");

        this.can = cfg.getBoolean("can", true);
    }

    public void save(boolean async) {
        cfg.set("island", island == null ? "" : this.island);
        cfg.set("can", this.can);
        cfg.save(async);
    }

    public boolean hasIsland() {
        return this.island != null && !this.island.isEmpty();
    }
}
