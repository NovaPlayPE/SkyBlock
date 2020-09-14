package net.novaplay.skyblock.economy;

import cn.nukkit.Player;
import net.novaplay.core.NovaCore;
import net.novaplay.core.player.NPlayer;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.addons.AddonData;
import ru.ragnok123.gttrade.economy.EconomyHandler;

public class NovaHandler implements EconomyHandler{

	@Override
	public void addMoney(Player p, int amount) {
		NPlayer data = NovaCore.getPlayer(p.getName());
		data.addMoney(amount);
	}

	@Override
	public int getMoney(Player p) {
		NPlayer data = NovaCore.getPlayer(p.getName());
		return data.getMoney();
	}

}
