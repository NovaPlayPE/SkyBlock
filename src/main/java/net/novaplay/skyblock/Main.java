package net.novaplay.skyblock;

import cn.nukkit.Player;
import cn.nukkit.plugin.PluginBase;
import lombok.Getter;
import net.novaplay.skyblock.addons.AddonData;
import net.novaplay.skyblock.chat.ChatHandler;
import net.novaplay.skyblock.command.FlyCommand;
import net.novaplay.skyblock.command.SkyBlockCommand;
import net.novaplay.skyblock.command.SpawnCommand;
import net.novaplay.skyblock.economy.NovaHandler;
import net.novaplay.skyblock.generator.SkyBlockGeneratorManager;
import net.novaplay.skyblock.invitation.InvitationHandler;
import net.novaplay.skyblock.island.IslandManager;
import net.novaplay.skyblock.mine.MineManager;
import net.novaplay.skyblock.reset.ResetHandler;
import net.novaplay.skyblock.shop.ShopManager;
import net.novaplay.skyblock.skyblock.SkyBlockManager;
import ru.ragnok123.gttrade.GTTrade;

import java.io.File;
import java.util.HashMap;

public class Main extends PluginBase {

    public static boolean isRunning = true;
    
    public static HashMap<String,AddonData> players = new HashMap<String,AddonData>();

    @Getter
    private static Main instance = null;

    @Getter
    private SkyBlockGeneratorManager skyBlockGeneratorManager;

    @Getter
    private SkyBlockManager skyBlockManager;

    @Getter
    private IslandManager islandManager;
    
    @Getter
    private MineManager mineManager;
    
    @Getter
    private ShopManager shopManager;

    @Getter
    private InvitationHandler invitationHandler;

    @Getter
    private ResetHandler resetHandler;

    @Getter
    private ChatHandler chatHandler;

    @Getter
    private EventListener eventListener;

    public void onLoad() {
        instance = this;
    }
    
    public static void addPlayer(String key, AddonData value) {
    	players.put(key,value);
    }
    
    public static void removePlayer(String key) {
    	players.remove(key);
    }
    
    public static AddonData getPlayer(Player p) {
    	return getPlayer(p.getName());
    }
    
    public static AddonData getPlayer(String p) {
    	return players.get(p);
    }

    public void onEnable() {
        this.initialize();
        this.setSkyBlockGeneratorManager();
        this.setSkyBlockManager();
        this.setIslandManager();
        this.setShopManager();
        this.setEventListener();
        this.setInvitationHandler();
        this.setChatHandler();
        this.setResetHandler();
        this.setPluginHearbeat();
        this.registerCommand();
        this.setMineManager();
        this.getLogger().info("skyBlock by @GiantAmethyst was enabled.");
        isRunning = true;
    }

    public void onDisable() {
        isRunning = false;
        for (PlayerData data : this.skyBlockManager.playerData.values()) {
            data.save(false);
        }

        this.getLogger().info("skyBlock by @GiantAmethyst was disabled.");
    }

    public void setMineManager() {
    	this.mineManager = new MineManager(this);
    }
    
    /**
     * Register SkyBlockGeneratorManager instance
     */
    public void setSkyBlockGeneratorManager() {
        this.skyBlockGeneratorManager = new SkyBlockGeneratorManager(this);
    }

    /**
     * Register SkyBlockManager instance
     */
    public void setSkyBlockManager() {
        this.skyBlockManager = new SkyBlockManager(this);
    }

    /**
     * Register IslandManager instance
     */
    public void setIslandManager() {
        this.islandManager = new IslandManager(this);
    }

    /**
     * Register EventListener instance
     */
    public void setEventListener() {
        this.eventListener = new EventListener(this);
    }

    /**
     * Schedule the PluginHearbeat
     */
    public void setPluginHearbeat() {
        this.getServer().getScheduler().scheduleRepeatingTask(new PluginHearbeat(this), 20);
    }

    /**
     * Register InvitationHandler instance
     */
    public void setInvitationHandler() {
        this.invitationHandler = new InvitationHandler(this);
    }

    /**
     * Register ResetHandler instance
     */
    public void setResetHandler() {
        this.resetHandler = new ResetHandler();
    }
    
    /**
     * Register ShopManager instance
     */
    
    public void setShopManager() {
    	this.shopManager = new ShopManager(this);
    }

    /**
     * Register ChatHandler instance
     */
    public void setChatHandler() {
        this.chatHandler = new ChatHandler();
    }

    /**
     * Register skyBlock command
     */
    public void registerCommand() {
        this.getServer().getCommandMap().register("skyblock", new SkyBlockCommand(this));
        this.getServer().getCommandMap().register("spawn", new SpawnCommand(this));
        this.getServer().getCommandMap().register("fly", new FlyCommand());
    }

    public void initialize() {
        saveDefaultConfig();

        File file = new File(getDataFolder() + "/islands");
        if (!file.exists()) {
            file.mkdirs();
        }

        File file2 = new File(getDataFolder() + "/users");
        if (!file2.exists()) {
            file2.mkdirs();
        }
        
        GTTrade.setEconomyHandler(new NovaHandler());
    }

}