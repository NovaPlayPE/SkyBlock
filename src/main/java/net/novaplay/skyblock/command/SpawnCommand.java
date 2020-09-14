package net.novaplay.skyblock.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import net.novaplay.core.NovaCore;
import net.novaplay.skyblock.Main;

public class SpawnCommand extends Command {
	
	private Main plugin;
	
	public SpawnCommand(Main plugin) {
        super("spawn", "Telepport to spawn");
        this.plugin = plugin;
	}

	@Override
	public boolean execute(CommandSender sender, String label, String[] args) {
		if(sender.isPlayer()) {
			Player p = (Player)sender;
			p.teleport(NovaCore.LEVEL.getSafeSpawn());
		}
		return false;
	}

}
