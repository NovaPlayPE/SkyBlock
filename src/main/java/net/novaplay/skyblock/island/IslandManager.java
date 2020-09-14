package net.novaplay.skyblock.island;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.PlayerData;
import net.novaplay.skyblock.Utils;
import net.novaplay.skyblock.task.IslandRemoveTask;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IslandManager {

    private Main plugin;

    private HashMap<String, Island> islands = new HashMap<>();

    public IslandManager(Main plugin) {
        this.plugin = plugin;
    }

    public boolean isOnlineIsland(String id) {
        return this.islands.containsKey(id);
    }

    public Map<String, Island> getOnlineIslands() {
        return this.islands;
    }

    public Island getOnlineIsland(String id) {
        return this.islands.get(id);
    }

    public Island getIslandByOwner(String ownerName) {
        for (Island island : this.islands.values()) {
            if (island.getOwnerName().equalsIgnoreCase(ownerName)) {
                return island;
            }
        }
        return null;
    }

    public void addIsland(Config config, String ownerName, String id, Set<String> members, boolean locked, String home, String generator) {
        this.islands.put(id, new Island(config, ownerName, id, members, locked, home, generator));
    }

    public void createIsland(Player owner, String generator, String id) {
        String name = owner.getName().toLowerCase();
        Config config = new Config(Utils.getIslandPath(id), Config.JSON);

        config.set("owner", name);
        config.set("members", new HashSet<String>());
        config.set("locked", false);
        config.set("home", id + ",4,54,6");
        config.set("generator", generator);

        this.addIsland(config, name, id, new HashSet<>(), false, id + ",4,54,6", generator);
        PlayerData data = this.plugin.getSkyBlockManager().getPlayerIsland(owner);
        data.island = id;
    }

    public void setIslandOffline(String id) {
        this.islands.remove(id);
    }

    public void checkPlayerIsland(Player player) {
        PlayerData data = this.plugin.getSkyBlockManager().getPlayerIsland(player);
        if (data.hasIsland()) {
            String id;
            File path = Utils.getIslandPath(id = data.island);
            if (path.exists()) {
                Server server = this.plugin.getServer();
                if(!server.isLevelGenerated(id)) {
                    data.island = null;
                    path.delete();
                    return;
                }

                Config config = new Config(path, Config.JSON);
                this.addIsland(config, config.getString("owner"), id, new HashSet<>(config.getList("members")), config.getBoolean("locked"), config.getString("home"), config.getString("generator"));
                if (!server.isLevelLoaded(id)) {
                    server.loadLevel(id);
                }
            } else {
                data.island = null;
            }
        }
    }

    public void removeIsland(Island island) {
        this.islands.remove(island.getIdentifier());
        Server server = this.plugin.getServer();
        if (server.isLevelLoaded(island.getIdentifier())) {
            Level level = server.getLevelByName(island.getIdentifier());
            Position safeSpawn = server.getDefaultLevel().getSafeSpawn();
            for (Player p : level.getPlayers().values()) {
                p.teleport(safeSpawn);
            }

            for (Entity entity : level.getEntities()) {
                entity.close();
            }
            server.unloadLevel(level);
            server.getScheduler().scheduleAsyncTask(new IslandRemoveTask(island.getIdentifier()));
        }
    }

    public void unloadByPlayer(Player player) {
        PlayerData data = this.plugin.getSkyBlockManager().getPlayerIsland(player);
        if (data.island != null && !data.island.isEmpty()) {
            String id = data.island;
            if (this.isOnlineIsland(id)) {
                plugin.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
                    @Override
                    public void run() {
                        Island island = getOnlineIsland(id);
                        boolean online = false;
                        for (String member : island.getAllMembers()) {
                            if (!member.equalsIgnoreCase(player.getName())) {
                                Player user = plugin.getServer().getPlayerExact(member);
                                if (user != null && user.isOnline()) {
                                    online = true;
                                    break;
                                }
                            }
                        }
                        if (!online) {
                            Level level = plugin.getServer().getLevelByName(id);
                            island.update();
                            setIslandOffline(id);
                            plugin.getServer().unloadLevel(level);
                        }
                    }
                }, 1);
            }
        }
    }

    public void update() {
        for (Island island : this.islands.values()) {
            island.update();
        }
    }
}