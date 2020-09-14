package net.novaplay.skyblock.forms;

import cn.nukkit.Player;
import net.novaplay.core.Lang;
import net.novaplay.core.player.NPlayer;

import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.PlayerData;
import ru.ragnok123.menuAPI.form.impl.CustomFormMenu;
import ru.ragnok123.menuAPI.form.impl.SimpleFormMenu;
import ru.ragnok123.menuAPI.form.impl.elements.Button;
import ru.ragnok123.menuAPI.form.impl.elements.Input;
import ru.ragnok123.menuAPI.form.impl.response.ButtonResponse;
import ru.ragnok123.menuAPI.form.impl.response.InputResponse;

public class FormSender {
	
	public static void openCreateIslandForm(Player player) {
		PlayerData data = Main.getInstance().getSkyBlockManager().getPlayerIsland(player);
		if(data.hasIsland()) {
			player.sendMessage(Lang.send(player, new String[] {
					"&eSB> &cSorry, but you already have an island",
					"&eSB> &cИзвините, но у вас уже есть остров"
			}));
		} else {
			SimpleFormMenu menu = new SimpleFormMenu(Lang.send(player, new String[] {"&bISLAND CREATION", "&bСОХДАНИЕ ОСТРОВА"}),
					Lang.send(player, new String[] {"&aSelect a generator for an island","&aВыберете генератор для создания острова"}));
			menu.addButton(Lang.send(player, new String[] {"&dCLASSIC", "&dЛАССИЧЕСКИЙ"}), new ButtonResponse() {
				public void onResponse(Player p, Button but) {
					Main.getInstance().getSkyBlockManager().generateIsland(player, "basic");
					player.sendMessage(Lang.send(player, new String[] {
							"&eSB> &aSuccessfully created a new island",
							"&eSB> &aНовый остров был успешно создан"
					}));
				}
			});
			menu.show(player);
		}
		
	}
	
}