package net.novaplay.skyblock;

import cn.nukkit.Player;
import cn.nukkit.block.Block;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntitySign;
import cn.nukkit.command.CommandSender;
import cn.nukkit.entity.Entity;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.block.SignChangeEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.EntityLevelChangeEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.math.Vector3;
import cn.nukkit.potion.Effect;
import cn.nukkit.utils.TextFormat;
import net.novaplay.core.NovaCore;
import net.novaplay.core.Settings;
import net.novaplay.core.entity.BasicNPC;
import net.novaplay.core.lang.Lang;
import net.novaplay.core.player.NPlayer;
import net.novaplay.core.player.name.DisplayName;
import net.novaplay.core.utils.ChanceAPI;
import net.novaplay.core.utils.Chance;

import net.novaplay.skyblock.addons.AddonData;
import net.novaplay.skyblock.chat.Chat;
import net.novaplay.skyblock.forms.FormSender;
import net.novaplay.skyblock.island.Island;
import net.novaplay.skyblock.mine.MinePlayer;
import net.novaplay.skyblock.shop.Shop;

import ru.ragnok123.gtscoreboard.GTScoreboard;
import ru.ragnok123.gtscoreboard.scoreboard.Criteria;
import ru.ragnok123.gtscoreboard.scoreboard.DisplaySlot;
import ru.ragnok123.gtscoreboard.scoreboard.Scoreboard;
import ru.ragnok123.gtscoreboard.scoreboard.ScoreboardObjective;
import ru.ragnok123.menuAPI.form.impl.SimpleFormMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class EventListener implements Listener {

    private Main plugin;
    private List<Integer> ids = new ArrayList<Integer>();

    @SuppressWarnings("deprecation")
	public EventListener(Main plugin) {
        this.plugin = plugin;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);

    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        this.plugin.getSkyBlockManager().tryRegisterUser(event.getPlayer());
    	Player p = event.getPlayer();
    	AddonData addon = new AddonData(p);
    	plugin.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
    		public void run() {
    			Main.addPlayer(p.getName(), addon);
    	    	if(!addon.isRegistered()) {
    	    		addon.createData();
    	    	}
    		}
    	}, 10);
    	
    }
    
    @EventHandler
    public void onCmd(PlayerCommandPreprocessEvent e) {
    	Player p = e.getPlayer();
    	AddonData d = Main.getPlayer(p.getName());
    	String cmd = e.getMessage();
    	if(cmd.startsWith("/help")) {
    		e.setCancelled();
    		StringBuilder builder = new StringBuilder();
    		builder.append("§e---== SkyBlock help ===---\n");
    		builder.append("§a - /sb - "+Lang.send(p, new String[] {"main skyblock command","главная команла skyblock"}) + "\n");
    		builder.append("§a - /spawn - "+Lang.send(p, new String[] {"teleport to spawn", "телепортироваться на спавн"}) + "\n");
    		builder.append("§a - /trade <nickname> - " +Lang.send(p, new String[] {"trade with other people", "торгуйтесь с остальными игроками"}) + "\n");
    		builder.append(hasColor(d, AddonData.FLUEGER)+" - /fly - "+Lang.send(p, new String[] {"enable/disable fly", "включить/выключить флай"}) + "\n");
    		p.sendMessage(builder.toString());
    	}
    }
    
    private String hasColor(AddonData d, String rank) {
    	return hasRank(d,rank) ? "§a" : "§c";
    }
    
    private boolean hasRank(AddonData d, String rank) {
    	return d.getRank().equals(rank);
    }
    
    @SuppressWarnings("deprecation")
	@EventHandler
    public void onJoin(PlayerJoinEvent event) {
    	Player player = event.getPlayer();
    	NPlayer data = NovaCore.getPlayer(player.getName());
    	AddonData addon = Main.getPlayer(player);
    	
    	
        this.plugin.getIslandManager().checkPlayerIsland(event.getPlayer());
        this.plugin.getServer().getScheduler().scheduleDelayedTask(new Runnable() {
        	public void run() {
        		player.setGamemode(0);
        		data.setLobby(true);
        		data.getNickManager().setupLobbyDisplayName();
        		addon.setupDisplayName();
        		Scoreboard board = new Scoreboard();
        		ScoreboardObjective o = board.registerNewObjective("skyblock",Criteria.DUMMY);
        		o.setDisplaySlot(DisplaySlot.SIDEBAR);
        		o.setDisplayName("§l§6> §eSkyBlock §6<");
        		o.registerScore("scoreboard_empty_1", " ",5);
        		o.registerScore("scoreboard_server", Lang.send(player, new String[] {"&7>> Server: &f", "&7>> Сервер: &f"}) + 
        				Settings.SERVER_ID
        				,5);
        		o.registerScore("scoreboard_empty_2", "  ",4);
        		o.registerScore("scoreboard_rank", Lang.send(player, new String[] {
        			"&7>> Rank: "+addon.getSuffix(),
        			"&7>> Ранг: "+addon.getSuffix()
        		}),3);
        		o.registerScore("scoreboard_emeralds", Lang.send(player, new String[] {
        			"&7>> Emeralds in bank: &a"+String.valueOf(addon.getEmeralds()),
        			"&7>> Изумрудов в банке: &a"+String.valueOf(addon.getEmeralds())
        		}),2);
        		o.registerScore("scoreboard_empty_3", "   ",1);
        		o.registerScore("scoreboard_web", "§e  www.nova-play.eu",0);
        		
        		board.addUpdater(b -> {
        			o.setScoreText("scoreboard_rank", Lang.send(player, new String[] {
                			"&7>> Rank: "+addon.getSuffix(),
                			"&7>> Ранг: "+addon.getSuffix()
                		}));
        			o.setScoreText("scoreboard_emeralds", Lang.send(player, new String[] {
        			"&7>> Emeralds in bank: &a"+String.valueOf(addon.getEmeralds()),
        			"&7>> Изумрудов в банке: &a"+String.valueOf(addon.getEmeralds())
        			}));
        		});
        		GTScoreboard.sendScoreboard(player,board);
        	}
        },20);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        this.plugin.getIslandManager().unloadByPlayer(event.getPlayer());
        AddonData d = Main.getPlayer(event.getPlayer().getName());
        d.save();
        Main.removePlayer(event.getPlayer().getName());
        PlayerData data = this.plugin.getSkyBlockManager().playerData.remove(event.getPlayer().getName().toLowerCase());

        if (data != null) {
            data.save(false);
        }
    }
    
    

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block b = event.getBlock();
        Island island = this.plugin.getIslandManager().getOnlineIsland(event.getPlayer().getLevel().getName());
        if (island != null) {
            if (!event.getPlayer().isOp() && !island.getAllMembers().contains(event.getPlayer().getName().toLowerCase())) {
                event.getPlayer().sendPopup(TextFormat.RED + "You must be part of this island to break here!");
                event.setCancelled();
            } else {
            	if(b.getId() == Block.LEAVES) {
            		AddonData a = Main.getPlayer(p.getName());
            		a.brokenLeaves++;
            		if(a.brokenLeaves == 16) {
            			a.brokenLeaves = 0;
            			ChanceAPI api = ChanceAPI.init();
            			api.add(new Chance[] {new Chance(1,92), new Chance(2,8)});
            			int amount = (int)api.generateRandom();
            			event.setDrops(new Item[] {Item.get(Item.SAPLING,0,amount)});
            		}
            	}
            }
        }
        if(p.hasPermission("novaplay.dev")) {
        	if(b.getId() == Block.SIGN_POST || b.getId() == Block.WALL_SIGN) {
        		if(this.plugin.getShopManager().isShop(b)) {
                	Shop shop = this.plugin.getShopManager().getShop(b);
                	if(shop.isBuyShop()) {
                		this.plugin.getShopManager().deleteShop(b);
                	} else {
                		this.plugin.getShopManager().deleteSellShop(b);
                	}
                }
        	}
        }
        
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        Island island = this.plugin.getIslandManager().getOnlineIsland(event.getPlayer().getLevel().getName());
        if (island != null) {
            if (!event.getPlayer().isOp() && !island.getAllMembers().contains(event.getPlayer().getName().toLowerCase())) {
                event.getPlayer().sendPopup(TextFormat.RED + "You must be part of this island to place here!");
                event.setCancelled();
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
    	Player p = event.getPlayer();
    	NPlayer d = NovaCore.getPlayer(event.getPlayer().getName());
        Island island = this.plugin.getIslandManager().getOnlineIsland(event.getPlayer().getLevel().getName());
        if(d.isInLobby()) {
        	if(!event.getPlayer().hasPermission("novaplay.owner")) {
        		event.setCancelled();
        	}
        }
        if (island != null) {
            if (!event.getPlayer().isOp() && !island.getAllMembers().contains(event.getPlayer().getName().toLowerCase())) {
                event.getPlayer().sendPopup(TextFormat.RED + "You must be part of this island to place here!");
                event.setCancelled();
            }
        }

        if(event.getBlock().getId() == Block.SIGN_POST || event.getBlock().getId() == Block.WALL_SIGN) {
        	if(this.plugin.getShopManager().isShop(event.getBlock())) {
        		Shop shop = this.plugin.getShopManager().getShop(event.getBlock());
        		if(shop.isBuyShop()) {
        			this.plugin.getShopManager().processBuy(event.getPlayer(),event.getBlock());
        		} else {
        			this.plugin.getShopManager().processSell(event.getPlayer(), event.getBlock());
        		}
        	}
        }
    }
    
    @EventHandler
    public void onShopCreate(SignChangeEvent event) {
    	Player p = event.getPlayer();
    	String[] lines = event.getLines();
    	if(p.hasPermission("novaplay.dev")) {
    		if(lines[3].equals("shop")) {
    			if(lines[0].isEmpty() && lines[1].isEmpty() && lines[2].isEmpty() && lines[3].isEmpty()) {
    				return;
    			}
    			String firstLine = lines[0];
    			String id = firstLine.split(":")[0];
    			String dmg = firstLine.split(":")[1];
    			String count = firstLine.split(":")[2];
    			String price = lines[1];
    			String name = lines[2];
    			HashMap<String,Integer> map = new HashMap<String,Integer>();
    			map.put("id", Integer.valueOf(id));
    			map.put("damage", Integer.valueOf(dmg));
    			map.put("count",Integer.valueOf(count));
    			map.put("price", Integer.valueOf(price));
    			this.plugin.getShopManager().createNewBuyShop(event.getBlock(),name,map);
    			event.setLine(0,"§e[BUY]");
    			event.setLine(1,"§e"+name);
    			event.setLine(2,"§eCount: §f"+count);
    			event.setLine(3,"§ePrice: §f"+price + " §aemeralds");
    			//sign.setText("§e[SHOP]", "§e"+name,"§eCount: §f"+count,"§ePrice: §f"+price + " §aemeralds");
    		} else if(lines[3].equals("sell")) {
    			if(lines[0].isEmpty() && lines[1].isEmpty() && lines[2].isEmpty() && lines[3].isEmpty()) {
    				return;
    			}
    			String firstLine = lines[0];
    			String id = firstLine.split(":")[0];
    			String dmg = firstLine.split(":")[1];
    			String count = firstLine.split(":")[2];
    			String price = lines[1];
    			String name = lines[2];
    			HashMap<String,Integer> map = new HashMap<String,Integer>();
    			map.put("id", Integer.valueOf(id));
    			map.put("damage", Integer.valueOf(dmg));
    			map.put("count",Integer.valueOf(count));
    			map.put("price", Integer.valueOf(price));
    			this.plugin.getShopManager().createNewSellShop(event.getBlock(),name,map);
    			event.setLine(0,"§e[SELL]");
    			event.setLine(1,"§e"+name);
    			event.setLine(2,"§eCount: §f"+count);
    			event.setLine(3,"§eReward: §f"+price + " §aemeralds");
    			//sign.setText("§e[SELL]","§e"+name, "§eCount: §f"+count, "§ePrice: §f"+price + " §aemeralds");
    		}
    	}
    }

    @EventHandler
    public void onLevelChange(EntityLevelChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
        	NPlayer data = NovaCore.getPlayer(((Player)entity).getName());
            if (this.plugin.getIslandManager().isOnlineIsland(event.getOrigin().getName())) {
                this.plugin.getIslandManager().getOnlineIsland(event.getOrigin().getName()).tryRemovePlayer((Player) entity);
                data.setLobby(true);
            } else if (this.plugin.getIslandManager().isOnlineIsland(event.getTarget().getName())) {
                this.plugin.getIslandManager().getOnlineIsland(event.getTarget().getName()).addPlayer((Player) entity);
                data.setLobby(false);
            }
        }
    }

    @EventHandler
    public void onChat(PlayerChatEvent event) {
        Chat chat = this.plugin.getChatHandler().getPlayerChat(event.getPlayer());
        Set<CommandSender> recipients = event.getRecipients();
        Player p = event.getPlayer();
        NPlayer d = NovaCore.getPlayer(p.getName());
        if(!d.isInLobby()) {
        	for(Player pla : p.getServer().getOnlinePlayers().values()) {
        		pla.sendMessage(p.getDisplayName() + "§7> " + event.getMessage());
        	}
        }
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent e) {
    	Player p = e.getPlayer();
    	if(this.plugin.getIslandManager().isOnlineIsland(p.getLevel().getName())){
    		Island is = this.plugin.getIslandManager().getOnlineIsland(p.getLevel().getName());
    		if(p.getY() <= 5) {
    			p.getInventory().clearAll();
    			p.teleport(is.getHomePosition());
    			p.addEffect(Effect.getEffect(Effect.BLINDNESS).setDuration(100));
    			p.addEffect(Effect.getEffect(Effect.SLOWNESS).setDuration(100));
    			p.sendTitle(Lang.send(p, new String[] {
    				"&l&cYOU DIED!!",
    				"&l&cТЫ УМЕР!!"
    			}));
    		}
    	}
    }
    
    @EventHandler
    public void onHurt(EntityDamageEvent event) {
        if (event instanceof EntityDamageByEntityEvent) {
            Entity entity = event.getEntity();
            if (entity instanceof Player) {
                if (this.plugin.getIslandManager().isOnlineIsland(entity.getLevel().getName())) {
                    event.setCancelled();
                }
            } else if(entity instanceof BasicNPC && ((EntityDamageByEntityEvent) event).getDamager() instanceof Player) {
            	event.setCancelled();
            	Player p = (Player) ((EntityDamageByEntityEvent) event).getDamager();
            	PlayerData data = this.plugin.getSkyBlockManager().getPlayerIsland(p);
            	BasicNPC vlg = (BasicNPC)entity;
            	if(vlg.getNpcId().equals("sb_create")) {
            		FormSender.openCreateIslandForm(p);
            	} else if(vlg.getNpcId().equals("sb_tp")) {
            		if (!data.hasIsland()) {
                        this.sendMessage(p, "You haven't a island!");
                    } else {
                        Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                        if (data.can) {
                            if (island != null) {
                                island.addPlayer(p);
                                data.can = false;
                                Level level = this.plugin.getServer().getLevelByName(island.getIdentifier());
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
                                inventory.addItem(Item.get(Item.CHEST,0,1));
                                //p.teleport(new Position(4, 54, 6, level));
                                p.teleport(island.getHomePosition());
                                this.sendMessage(p, "§eSB> §aYou were teleported to your island home");
                            } else {
                                this.sendMessage(p, "§eSB> §cYou haven't a island!!");
                            }
                        } else {
                            Level level = island != null ? this.plugin.getServer().getLevelByName(island.getIdentifier()) : null;

                            if(level == null) {
                                p.sendMessage(TextFormat.RED+"Exception happened while removing island, please try again");
                                data.island = null;
                                data.can = true;
                            }

                            //p.teleport(new Position(4, 54, 6, level));
                            p.teleport(island.getHomePosition());
                            this.sendMessage(p, "§eSB> §aYou were teleported to your island home");
                        }
                    }
            	} else if(vlg.getNpcId().equals("shop_food")) {
            		Main.getInstance().getShopManager().openFoodShop(p);
            	} else if(vlg.getNpcId().equals("shop_farming")) {
            		Main.getInstance().getShopManager().openFarmingShop(p);
            	} else if(vlg.getNpcId().equals("shop_minerals")) {
            		Main.getInstance().getShopManager().openMineralsShop(p);
            	} else if(vlg.getNpcId().equals("shop_items")) {
            		Main.getInstance().getShopManager().openItemsShop(p);
            	} else if(vlg.getNpcId().equals("skyblock")) {
            		event.setCancelled();
           			p.sendMessage("§e[SkyBlock] §aUse /sb help");
            	}
            }
        }
    }
    
    public void sendMessage(Player sender, String message) {
        sender.sendMessage(TextFormat.GREEN + "- " + TextFormat.WHITE + message);
    }

    @EventHandler
    public void onDropItem(PlayerDropItemEvent event) {
        Island island = this.plugin.getIslandManager().getOnlineIsland(event.getPlayer().getLevel().getName());
        if (island != null) {
            if (!event.getPlayer().isOp() && !island.getAllMembers().contains(event.getPlayer().getName().toLowerCase())) {
                event.getPlayer().sendPopup(TextFormat.RED + "You must be part of this island to drop items here!");
                event.setCancelled();
            }
        }
    }

    /*@EventHandler
    public void onUnloadLevel(LevelUnloadEvent event) {
        foreach(event.getLevel().getPlayers() as player) {
            player.teleportImmediate(this.plugin.getServer().getDefaultLevel().getSafeSpawn());
        }
    }*/

}