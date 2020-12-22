package net.novaplay.skyblock.command;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import net.novaplay.core.lang.Lang;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.addons.AddonData;

public class FlyCommand extends Command {

	public FlyCommand() {
		super("fly", "Fly command");
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		Player p = (Player)sender;
		AddonData d = Main.getPlayer(p.getName());
		if(d.getRank().equals(AddonData.FLUEGER)) {
			if(args.length != 0) {
				if(args[0].equals("on")) {
					p.setAllowFlight(true);
				} else if(args[0].equals("off")) {
					p.setAllowFlight(false);
				} 
			} else {
				p.sendMessage(Lang.send(p, new String[] {
					"&eSB> &cUse &b/fly on/off",
					"&eSB> &cИспользуй &b/fly on/off"
				}));
			}
		} else {
			p.sendMessage(Lang.send(p, new String[] {
					"&eSB> &cSorry, but this feature is available only for &bFLUEGER&c, and above\n" ,
					"&eSB &cизвините, но данная способность доступна от ранга &bFLUEGER\n"
			}));
			p.sendMessage(Lang.send(p, new String[] {
					"&eSB> &cYou can buy this rank on &bstore.nova-play.eu",
					"&eSB> &cВы можете приобрести ранг на &bstore.nova-play.eu"
			}));
		}
		return false;
	}

}
