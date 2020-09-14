package net.novaplay.skyblock.command;

import cn.nukkit.Player;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.blockentity.BlockEntityChest;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.inventory.PlayerInventory;
import cn.nukkit.item.Item;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import net.novaplay.core.NovaCore;
import net.novaplay.core.player.NPlayer;
import net.novaplay.skyblock.addons.AddonData;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.PlayerData;
import net.novaplay.skyblock.Utils;
import net.novaplay.skyblock.generator.SkyBlockGeneratorManager;
import net.novaplay.skyblock.invitation.Invitation;
import net.novaplay.skyblock.island.Island;
import net.novaplay.skyblock.mine.MinePlayer;
import net.novaplay.skyblock.reset.Reset;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SkyBlockCommand extends Command {

    private Main plugin;

    public SkyBlockCommand(Main plugin) {
        super("sb", "SkyBlock command", "Usage: /sb", new String[]{"is"});
        this.plugin = plugin;

        /*this.commandParameters.clear();
        this.commandParameters.put("default", new CommandParameter[]{
                new CommandParameter("action", false, "join", "create", "home", "sethome", "kick", "expel", "lock", "invite", "accept", "deny", "reject", "members", "disband", "makeleader", "leave", "remove", "tp", "reset", "help", "teamchat"),
                /*new CommandParameter("create", false),
                new CommandParameter("home", false),
                new CommandParameter("sethome", false),
                new CommandParameter("kick", false, ),
                new CommandParameter("expel", false),
                new CommandParameter("lock", false),
                new CommandParameter("invite", false),
                new CommandParameter("accept", false),
                new CommandParameter("deny", false),
                new CommandParameter("reject", false),
                new CommandParameter("members", false),
                new CommandParameter("disband", false),
                new CommandParameter("makeleader", false),
                new CommandParameter("leave", false),
                new CommandParameter("remove", false),
                new CommandParameter("tp", false),
                new CommandParameter("reset", false),
                new CommandParameter("help", false),
                new CommandParameter("teamchat", false)
                new CommandParameter("")
        });*/
    }

    public void sendMessage(Player sender, String message) {
        sender.sendMessage(TextFormat.GREEN + "- " + TextFormat.WHITE + message);
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            PlayerData data = this.plugin.getSkyBlockManager().getPlayerIsland(p);
            NPlayer coreData = NovaCore.getPlayer(p.getName());
            AddonData addon = this.plugin.getPlayer(p.getName());
            if (args.length > 0) {
                switch (args[0].toLowerCase()) {
                case "mines":
                	if(p.hasPermission("novaplay.dev")) {
                		MinePlayer pl = this.plugin.getMineManager().getPlayer(p);
                		pl.state = 1;
                		p.sendMessage("§e[Mines] §aNow tap for first pos");
                	}
                	break;
                    case "join":
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
                                    this.sendMessage(p, "§e[SkyBlock] §aYou were teleported to your island home");
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!!");
                                }
                            } else {
                                Level level = island != null ? this.plugin.getServer().getLevelByName(island.getIdentifier()) : null;

                                if(level == null) {
                                    p.sendMessage(TextFormat.RED+"Exception happened while removing island, please try again");
                                    data.island = null;
                                    data.can = true;
                                    return true;
                                }
                                p.teleport(island.getHomePosition());
                                //p.teleport(new Position(4, 54, 6, level));
                                this.sendMessage(p, "§e[SkyBlock] §aYou were teleported to your island home");
                            }
                        }
                        break;
                    case "create":
                        if (!data.hasIsland()) {
                            Reset reset = this.plugin.getResetHandler().getResetTimer(p);
                            if (reset != null) {
                                String minutes = Utils.printSeconds(reset.getTime());
                                this.sendMessage(p, "§e[SkyBlock] You'll be able to create a new island in " + minutes + " minutes");
                            } else {
                                SkyBlockGeneratorManager skyBlockManager = this.plugin.getSkyBlockGeneratorManager();
                                if (args.length > 1) {
                                    if (skyBlockManager.isGenerator(args[1])) {
                                        this.plugin.getSkyBlockManager().generateIsland(p, args[1]);
                                        this.sendMessage(p, "§e[SkyBlock] §aYou successfully created a " + skyBlockManager.getGeneratorIslandName(args[1]) + " island!");
                                    } else {
                                        this.sendMessage(p, "§e[SkyBlock] §cThat isn't a valid skyBlock generator!");
                                    }
                                } else {
                                    this.plugin.getSkyBlockManager().generateIsland(p, "basic");
                                    this.sendMessage(p, "§e[SkyBlock] §aYou successfully created a island! Now type /sb join");
                                }

                            }
                        } else {
                            this.sendMessage(p, "§e[SkyBlock] §cYou already got a skyblock island!");
                        }
                        break;
                    case "home":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                Position home = island.getHomePosition();
                                if (home != null) {
                                    p.teleport(home);
                                    this.sendMessage(p, "§e[SkyBlock] §aYou have been teleported to your island home");
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYour island haven't a home position set!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!!");
                            }
                        }
                        break;
                    case "sethome":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    if (p.getLevel().getName().equals(data.island)) {
                                    	island.setHomePosition(p);
                                        this.sendMessage(p, "§e[skyBlock] §aYou set your island home successfully!");
                                    } else {
                                        this.sendMessage(p, "§e[skyBlock] §cYou must be in your island to set home!");
                                    }
                                } else {
                                    this.sendMessage(p, "§e[skyBlock] §cYou must be the island leader to do this!");
                                }
                            } else {
                                this.sendMessage(p, "§e[skyBlock] §cYou haven't a island!!");
                            }
                        }
                        break;
                    case "kick":
                    case "expel":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "You haven't a island!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    if (args.length > 1) {
                                        Player player = this.plugin.getServer().getPlayer(args[1]);
                                        if (player != null && player.isOnline()) {
                                            if (player.getLevel().getName().equals(island.getIdentifier())) {
                                                player.teleport(this.plugin.getServer().getDefaultLevel().getSafeSpawn());
                                                this.sendMessage(p, player.getName() + " has been kicked from your island!");
                                            } else {
                                                this.sendMessage(p, "§e[SkyBlock] §cThe player isn't in your island!");
                                            }
                                        } else {
                                            this.sendMessage(p, "§e[SkyBlock] §cThat isn't a valid player");
                                        }
                                    } else {
                                        this.sendMessage(p, "§e[SkyBlock] §eUsage: /skyblock expel [name]");
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must the island owner to expel anyone");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                            }
                        }
                        break;
                    case "lock":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    island.setLocked(!island.isLocked());
                                    String locked = (island.isLocked()) ? "locked" : "unlocked";
                                    this.sendMessage(p, "§e[SkyBlock] §aYour island has been " + locked + "!");
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be the island owner to do this!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                            }
                        }
                        break;
                    case "invite":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                            	if(!addon.getRank().equals(AddonData.BASIC)) {
                            		if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                            			if (args.length > 1) {
                            				Player player = this.plugin.getServer().getPlayer(args[1]);
                            				if (player != null && player.isOnline()) {
                            					PlayerData data2 = this.plugin.getSkyBlockManager().getPlayerIsland(player);
                            					if (!data2.hasIsland()) {
                            						int members = island.getMembers().size();
                            						if(addon.getRank().equals(AddonData.FLUEGER) && members == 4) {
                            							this.sendMessage(p, "§e[SkyBlock] §cSorry, but you cannot add more than 3 members (yet)");
                            						} else if(addon.getRank().equals(AddonData.FLUEGER) && members < 4){
                           								this.plugin.getInvitationHandler().addInvitation(p, player, island);
                               							this.sendMessage(p, "§e[SkyBlock] §aYou sent a invitation to "+player.getName()+"!");
                               							this.sendMessage(player, "§e[SkyBlock] §a"+sender.getName()+" invited you to his island! Do /skyblock <accept/reject> {sender.getName()}");	
                            						}
                            					} else {
                            						this.sendMessage(p, "§e[SkyBlock] §cThis player is already in a island!");
                            					}
                            				} else {
                            					this.sendMessage(p, "§e[SkyBlock] §c"+args[1]+" isn't a valid player!");
                            				}
                            			} else {
                                        this.sendMessage(p, "Usage: /sb invite [player]");
                            			}
                            		} else {
                            			this.sendMessage(p, "§e[SkyBlock] §cYou must be the island owner to do this!");
                            		}
                            	} else {
                            		this.sendMessage(p, "§e[SkyBlock] §cSorry, but you can't invite members");
                            		this.sendMessage(p, "§e[SkyBlock] §cUpgrade your rank to unlock inviting members");
                            	}
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou haven't a island!!");
                            }
                        }
                        break;
                    case "accept":
                        if (args.length > 1) {
                            if (!data.hasIsland()) {
                                Player player = this.plugin.getServer().getPlayer(args[1]);
                                if (player != null && player.isOnline()) {
                                    Invitation invitation = this.plugin.getInvitationHandler().getInvitation(player);
                                    if (invitation != null) {
                                        if (invitation.getSender() == player) {
                                            invitation.accept();
                                        } else {
                                            this.sendMessage(p, "§e[SkyBlock] §cYou haven't a invitation from "+player.getName()+"!");
                                        }
                                    } else {
                                        this.sendMessage(p, "§e[SkyBlock] §cYou haven't a invitation from "+player.getName());
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §c"+args[1]+" is not a valid player");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou cannot be on a island if you want join another island!");
                            }
                        } else {
                            this.sendMessage(p, "Usage: /sb accept [sender name]");
                        }
                        break;
                    case "deny":
                    case "reject":
                        if (args.length > 1) {
                            if (!data.hasIsland()) {
                                Player player = this.plugin.getServer().getPlayer(args[1]);
                                if (player != null && player.isOnline()) {
                                    Invitation invitation = this.plugin.getInvitationHandler().getInvitation(player);
                                    if (invitation != null) {
                                        if (invitation.getSender() == player) {
                                            invitation.deny();
                                        } else {
                                            this.sendMessage(p, "§e[SkyBlock]  §cYou haven't a invitation from " + player.getName() + "!");
                                        }
                                    } else {
                                        this.sendMessage(p, "§e[SkyBlock]  §cYou haven't a invitation from " + player.getName());
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock]  §c" + args[1] + " is not a valid player");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock]  §cYou cannot be in a island if you want reject another island!");
                            }
                        } else {
                            this.sendMessage(p, "Usage: /sb accept [sender name]");
                        }
                        break;
                    case "members":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to use this command!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                this.sendMessage(p, "§e____| "+island.getOwnerName()+"'s Members |____");
                                int i = 1;
                                for (String member : island.getAllMembers()) {
                                    this.sendMessage(p, i + ". " + member);
                                    i++;
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to use this command!!");
                            }
                        }
                        break;
                    case "delete":
                        if (data.island == null) {
                            this.sendMessage(p, "You must be in a island to delete it!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    for (String member : island.getAllMembers()) {
                                        data.island = null;
                                        data.can = true;
                                    }
                                    this.plugin.getIslandManager().removeIsland(island);
                                    this.plugin.getResetHandler().addResetTimer(p);
                                    this.sendMessage(p, "§e[SkyBlock] §aYou successfully deleted the island!");
                                    p.getInventory().clearAll();
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be the owner to disband the island!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to disband it!!");
                            }
                        }
                        break;
                    case "makeleader":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to set a new leader!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    if (args.length > 1) {
                                        Player player = this.plugin.getServer().getPlayer(args[1]);
                                        if (player != null && player.isOnline()) {
                                            PlayerData playerConfig = this.plugin.getSkyBlockManager().getPlayerIsland(player);
                                            Island playerIsland = this.plugin.getIslandManager().getOnlineIsland(playerConfig.island);
                                            if (island == playerIsland) {
                                                island.setOwnerName(player.getName().toLowerCase());
                                                island.addPlayer(player);
                                                this.sendMessage(p, "§e[SkyBlock] §aYou sent the ownership to "+player.getName());
                                                this.sendMessage(player, "§e[SkyBlock] §aYou get your island ownership by "+sender.getName());
                                            } else {
                                                this.sendMessage(p, "§e[SkyBlock] §cThe player should be on your island!");
                                            }
                                        } else {
                                            this.sendMessage(p, "§e[SkyBlock] §c"+args[1]+" isn't a valid player!");
                                        }
                                    } else {
                                        this.sendMessage(p, "Usage: /sb makeleader [player]");
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be the island leader to do this!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to set a new leader!!");
                            }
                        }
                        break;
                    case "leave":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to leave it!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou cannot leave a island if your the owner! Maybe you can try use /skyblock disband");
                                } else {
                                    this.plugin.getChatHandler().removePlayerFromChat(p);
                                    data.island = null;
                                    island.removeMember(p.getName());
                                    this.sendMessage(p, "§e[SkyBlock] §aYou left the island!!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be on a island to leave it!!");
                            }
                        }
                        break;
                    case "remove":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou must be on a island to leave it!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    if (args.length > 1) {
                                        if (island.getAllMembers().contains(args[1].toLowerCase())) {
                                            island.removeMember(args[1].toLowerCase());
                                            Player player = this.plugin.getServer().getPlayerExact(args[1]);
                                            if (player != null && player.isOnline()) {
                                                this.plugin.getChatHandler().removePlayerFromChat(player);
                                            }
                                            this.sendMessage(p, "§e[SkyBlock] §c" + args[1] + " was removed from your team!");
                                        } else {
                                            this.sendMessage(p, "§e[SkyBlock] §c" + args[1] + " isn't a player of your island!");
                                        }
                                    } else {
                                        this.sendMessage(p, "Usage: /sb remove [player]");
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be the island owner to do this!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to leave it!!");
                            }
                        }
                        break;
                    case "tp":
                        if (args.length > 1) {
                            Island island = this.plugin.getIslandManager().getIslandByOwner(args[1]);
                            if (island != null) {
                                if (island.isLocked()) {
                                    this.sendMessage(p, "§e[SkyBlock] §aThis island is locked, you cannot join it!");
                                } else {
                                    p.teleport(new Position(15, 7, 10, this.plugin.getServer().getLevelByName(island.getIdentifier())));
                                    this.sendMessage(p, "§e[SkyBlock] §aYou joined the island successfully");
                                }
                            } else {
                                this.sendMessage(p, "At least one island member must be active if you want see the island!");
                            }
                        } else {
                            this.sendMessage(p, "Usage: /sb tp [owner name]");
                        }
                        break;
                    case "reset":
                        if (!data.hasIsland()) {
                            this.sendMessage(p, "§e[SkyBlock] §cYou must be on a island to reset it!");
                        } else {
                            Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                            if (island != null) {
                                if (island.getOwnerName().equalsIgnoreCase(p.getName())) {
                                    Reset reset = this.plugin.getResetHandler().getResetTimer(p);
                                    if (reset != null) {
                                        String minutes = Utils.printSeconds(reset.getTime());
                                        this.sendMessage(p, "§e[SkyBlock] §cYou'll be able to reset your island again in " + minutes + " minutes");
                                    } else {
                                        for (String member : island.getAllMembers()) {
                                            PlayerData memberData = plugin.getSkyBlockManager().getPlayerIsland(member);

                                            if(memberData != null) {
                                                memberData.island = null;
                                            } else {
                                                Config memberConfig = new Config(this.plugin.getDataFolder() + "users/" + member + ".json", Config.JSON);
                                                memberConfig.set("island", "");
                                                memberConfig.save();
                                            }
                                        }
                                        String generator = island.getGenerator();
                                        this.plugin.getIslandManager().removeIsland(island);
                                        this.plugin.getResetHandler().addResetTimer(p);
                                        this.plugin.getSkyBlockManager().generateIsland(p, generator);
                                        this.sendMessage(p, "§e[SkyBlock] §aYou successfully reset the island!");
                                    }
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be the owner to reset the island!");
                                }
                            } else {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to reset it!!");
                            }
                        }
                        break;
                    case "help":
                        HashMap<String, String> commands = new LinkedHashMap<String, String>() {
                            {
                                put("help", "Show skyblock command info");
                                put("create", "Create a new island");
                                put("delete", "Delete your existing island");
                                put("join", "Teleport you to your island");
                                put("expel", "Kick someone from your island");
                                put("lock", "Lock/unlock your island, then nobody/everybody will be able to join");
                                put("sethome", "Set your island home");
                                put("home", "Teleport you to your island home");
                                put("members", "Show all members of your island");
                                put("tp <ownerName>", "Teleport you to a island that isn't yours");
                                put("invite", "Invite a player to be member of your island");
                                put("accept/reject <sender name>", "Accept/reject an invitation");
                                put("leave", "Leave your island");
                                put("remove", "Remove member from your island");
                                put("makeleader", "Transfer island ownership");
                                //put("teamchat", "Change your chat to your island chat");
                            }
                        };

                        for (Entry<String, String> entry : commands.entrySet()) {
                            p.sendMessage(TextFormat.YELLOW + "/sb " + entry.getKey() + ": " + TextFormat.GREEN + entry.getValue());
                        }
                        break;/*
                    case "teamchat":
                        if (this.plugin.getChatHandler().isInChat(p)) {
                            this.plugin.getChatHandler().removePlayerFromChat(p);
                            this.sendMessage(p, "§e[SkyBlock] §aYou successfully left your team chat!");
                        } else {
                            if (!data.hasIsland()) {
                                this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to use this command!");
                            } else {
                                Island island = this.plugin.getIslandManager().getOnlineIsland(data.island);
                                if (island != null) {
                                    this.plugin.getChatHandler().addPlayerToChat(p, island);
                                    this.sendMessage(p, "§e[SkyBlock] §aYou joined your team chat room");
                                } else {
                                    this.sendMessage(p, "§e[SkyBlock] §cYou must be in a island to use this command!!");
                                }
                            }
                        }
                        break;*/
                    default:
                        this.sendMessage(p, "§e[SkyBlock] §aUse /sb help if you don't know how to use the command!");
                        break;
                }
            } else {
                this.sendMessage(p, "§e[SkyBlock] §aUse /sb help if you don't know how to use the command!");
            }

        }

        return true;
    }
}