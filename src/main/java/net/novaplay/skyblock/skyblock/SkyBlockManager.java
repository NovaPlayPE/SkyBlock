package net.novaplay.skyblock.skyblock;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockChest;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.utils.Config;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.PlayerData;
import net.novaplay.skyblock.Utils;
import net.novaplay.skyblock.task.IslandCreateTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SkyBlockManager {

    private Main plugin;

    public Map<String, PlayerData> playerData = new HashMap<>();

    public SkyBlockManager(Main plugin) {
        this.plugin = plugin;

        plugin.getServer().getScheduler().scheduleDelayedRepeatingTask(new Runnable() {
            @Override
            public void run() {
                for (PlayerData data : playerData.values()) {
                    data.save(true);
                }
            }
        }, 600 * 20, 600 * 20);
    }

    public void generateIsland(Player player) {
        generateIsland(player, "basic");
    }

    public void generateIsland(Player player, String generatorName) {
        String id = Utils.genIslandId();
        Server.getInstance().getScheduler().scheduleAsyncTask(new IslandCreateTask(id,generatorName));
        this.plugin.getIslandManager().createIsland(player, generatorName, id);
        // this.spawnDefaultChest(island, player);
    }

    public void spawnDefaultChest(String islandName, Player p) {
        Level level = this.plugin.getServer().getLevelByName(islandName);
        level.setBlock(new Vector3(10, 6, 4), new BlockAir());
        level.loadChunk(10, 4, true);
        /** @var Chest chest */
        BlockEntityChest chest = new BlockEntityChest(level.getChunk(10 >> 4, 4 >> 4), new CompoundTag()
                .putList(new ListTag("Items"))
                .putString("id", BlockEntityChest.CHEST)
                .putInt("x", 10)
                .putInt("y", 6)
                .putInt("z", 4));

        level.setBlock(new Vector3(10, 6, 4), new BlockChest());

        PlayerInventory inventory = p.getInventory();
        inventory.addItem(Item.get(Item.WATER, 0, 2));
        inventory.addItem(Item.get(Item.LAVA, 0, 1));
        inventory.addItem(Item.get(Item.ICE, 0, 2));
        inventory.addItem(Item.get(Item.MELON_BLOCK, 0, 1));
        inventory.addItem(Item.get(Item.BONE, 0, 1));
        inventory.addItem(Item.get(Item.PUMPKIN_SEEDS, 0, 1));
        inventory.addItem(Item.get(Item.CACTUS, 0, 1));
        inventory.addItem(Item.get(Item.SUGAR_CANE, 0, 1));
        inventory.addItem(Item.get(Item.BREAD, 0, 1));
        inventory.addItem(Item.get(Item.WHEAT, 0, 1));
        inventory.addItem(Item.get(Item.LEATHER_BOOTS, 0, 1));
        inventory.addItem(Item.get(Item.LEATHER_PANTS, 0, 1));
        inventory.addItem(Item.get(Item.LEATHER_TUNIC, 0, 1));
        inventory.addItem(Item.get(Item.LEATHER_CAP, 0, 1));
    }

    public File getPlayerDataPath(Player player) {
        return new File(this.plugin.getDataFolder() + "/users/" + player.getName().toLowerCase() + ".json");
    }

    public void registerUser(Player player) {
        PlayerData data = new PlayerData(new Config(this.getPlayerDataPath(player), Config.JSON));
        playerData.put(player.getName().toLowerCase(), data);
    }

    public void tryRegisterUser(Player player) {
        if (!this.getPlayerDataPath(player).exists()) {
            this.registerUser(player);
        }
    }

    public PlayerData getPlayerIsland(String player) {
        return playerData.get(player.toLowerCase());
    }

    public PlayerData getPlayerIsland(Player player) {
        PlayerData data = playerData.get(player.getName().toLowerCase());

        if (data == null) {
            data = new PlayerData(new Config(this.getPlayerDataPath(player), Config.JSON));

            playerData.put(player.getName().toLowerCase(), data);
        }

        return data;
    }
}