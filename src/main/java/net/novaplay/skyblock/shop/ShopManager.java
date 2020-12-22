package net.novaplay.skyblock.shop;

import net.novaplay.core.lang.Lang;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.addons.AddonData;
import ru.ragnok123.menuAPI.inventory.InventoryCategory;
import ru.ragnok123.menuAPI.inventory.InventoryMenu;
import ru.ragnok123.menuAPI.inventory.item.ItemClick;
import ru.ragnok123.menuAPI.inventory.item.ItemData;

import java.util.ArrayList;
import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;
import cn.nukkit.utils.Config;

public class ShopManager {
	
	private Config shops = null;
	private Config sellShops = null;
	private Main plugin = null;
	
	private HashMap<Vector3,Shop> shopList = new HashMap<Vector3,Shop>();
	
	public ShopManager(Main main) {
		plugin = main;
		shops = new Config(plugin.getDataFolder()+"/shops.yml", Config.YAML);
		sellShops = new Config(plugin.getDataFolder()+"/sellshops.yml",Config.YAML);
		loadShops();
	}
	
	public void loadShops() {
		for(String key : shops.getKeys(false)) {
			double x = shops.getDouble(key+".x");
			double y = shops.getDouble(key+".y");
			double z = shops.getDouble(key+".z");
			Vector3 vec = new Vector3(x,y,z);
			HashMap<String,Integer> map = new HashMap<String,Integer>();
			map.put("id",(int)shops.get(key+".data.id"));
			map.put("damage",(int)shops.get(key+".data.damage"));
			map.put("count",(int)shops.get(key+".data.count"));
			map.put("price",(int)shops.get(key+".data.price"));
			Shop shop = new Shop(key,vec,map,true);
			if(!shopList.containsKey(vec)) {
				shopList.put(vec,shop);
			}
		}
		for(String key : sellShops.getKeys(false)) {
			double x = sellShops.getDouble(key+".x");
			double y = sellShops.getDouble(key+".y");
			double z = sellShops.getDouble(key+".z");
			Vector3 vec = new Vector3(x,y,z);
			HashMap<String,Integer> map = new HashMap<String,Integer>();
			map.put("id",(int)sellShops.get(key+".data.id"));
			map.put("damage",(int)sellShops.get(key+".data.damage"));
			map.put("count",(int)sellShops.get(key+".data.count"));
			map.put("price",(int)sellShops.get(key+".data.price"));
			Shop shop = new Shop(key,vec,map,false);
			if(!shopList.containsKey(vec)) {
				shopList.put(vec,shop);
			}
		}
	}
	
	public void deleteShop(Vector3 vec) {
		Shop shop = getShop(vec);
		String key = shop.getName();
		if(shops.get(key) != null) {
			shopList.remove(vec);
			shops.remove(key);
			shops.save();
		}
	}
	
	public void deleteSellShop(Vector3 vec) {
		Shop shop = getShop(vec);
		String key = shop.getName();
		if(sellShops.get(key) != null) {
			shopList.remove(vec);
			sellShops.remove(key);
			sellShops.save();
		}
	}
	
	public Shop getShop(Vector3 vec) {
		Shop sh = null;
		if(shopList.get(vec) != null) {
			sh = shopList.get(vec);
		}
		return sh;
	}
	
	public Shop createNewBuyShop(Vector3 vec,String name, HashMap<String,Integer> data) {
		String key = name;
		shops.set(key+".x", vec.getX());
		shops.set(key+".y", vec.getY());
		shops.set(key+".z", vec.getZ());
		shops.set(key+".data.id", data.get("id"));
		shops.set(key+".data.damage", data.get("damage"));
		shops.set(key+".data.count", data.get("count"));
		shops.set(key+".data.price", data.get("price"));
		shops.save();
		Shop shop = new Shop(name,vec, data,true);
		if(!shopList.containsKey(vec)) {
			shopList.put(vec,shop);
		}
		return shop;
	}
	
	public Shop createNewSellShop(Vector3 vec,String name, HashMap<String,Integer> data) {
		String key = name;
		sellShops.set(key+".x", vec.getX());
		sellShops.set(key+".y", vec.getY());
		sellShops.set(key+".z", vec.getZ());
		sellShops.set(key+".data.id", data.get("id"));
		sellShops.set(key+".data.damage", data.get("damage"));
		sellShops.set(key+".data.count", data.get("count"));
		sellShops.set(key+".data.price", data.get("price"));
		sellShops.save();
		Shop shop = new Shop(name,vec, data,false);
		if(!shopList.containsKey(vec)) {
			shopList.put(vec,shop);
		}
		return shop;
	}
	
	
	public void processBuy(Player p, Vector3 v) {
		AddonData data = Main.getPlayer(p);
		if(shopList.get(v) != null) {
			Shop shops = shopList.get(v);
			if(data.getEmeralds() >= shops.getPrice()) {
				data.addEmeralds(-shops.getPrice());
				p.getInventory().addItem(shops.getItem());
				p.sendMessage(Lang.send(p, new String[] {
						"&eSB> &aSuccessfuly bought an item",
						"&eSB> &aУспешно куплен предмет"
				}));
			} else {
				p.sendMessage(Lang.send(p, new String[] {
						"&eSB> &cAn error occured",
						"&eSB> &aПроизощла ошибка"
				}));
			}	
		}
	}
	
	public void processSell(Player p,Vector3 v) {
		AddonData data = Main.getPlayer(p);
		if(shopList.get(v) != null) {
			Shop shops = shopList.get(v);
			if(p.getInventory().contains(shops.getItem())) {
				data.addEmeralds(shops.getPrice());
				p.getInventory().removeItem(shops.getItem());
				p.sendMessage(Lang.send(p, new String[] {
						"&eSB> &aSuccessfuly sold an item",
						"&eSB> &aУспешно продан предмет"
				}));
			} else {
				p.sendMessage(Lang.send(p, new String[] {
						"&eSB> &cAn error occured",
						"&eSB> &aПроизощла ошибка"
				}));
			}	
		}
	}
	
	public boolean isShop(Vector3 vector) {
		if(shopList.get(vector) != null) {
			if(shopList.get(vector).getVector().equals(vector)) {
				return true;
			}
		}
		return false;
	}
	
	public void openFoodShop(Player p) {
		Item emerald = Item.get(Item.EMERALD);
		
		InventoryMenu menu = new InventoryMenu();
		menu.setName(Lang.send(p, new String[] {"&l&eFOOD","&l&eЕДА"}));
		menu.setMainCategory(new InventoryCategory() {{
			addElement(0, new ItemData(Item.RAW_CHICKEN,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl, 4, emerald,i);
				}
			});
			addElement(1, new ItemData(Item.EMERALD,0,4));
			addElement(2, new ItemData(Item.RAW_CHICKEN,0,32), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,8,emerald,i);
				}
			});
			addElement(3, new ItemData(Item.EMERALD,0,8));
			addElement(4, new ItemData(Item.COOKED_CHICKEN,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,8,emerald,i);
				}
			});
			addElement(5, new ItemData(Item.EMERALD,0,8));
			addElement(6, new ItemData(Item.COOKED_CHICKEN,0,32), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,16,emerald,i);
				}
			});
			addElement(7, new ItemData(Item.EMERALD,0,16));
			
			addElement(8, new ItemData(Item.RAW_BEEF,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,5,emerald,i);
				}
			});
			addElement(9, new ItemData(Item.EMERALD,0,5));
			addElement(10, new ItemData(Item.STEAK,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,12,emerald,i);
				}
			});
			addElement(11, new ItemData(Item.EMERALD,0,12));
			addElement(12, new ItemData(Item.BAKED_POTATO,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,10,emerald,i);
				}
			});
			addElement(13, new ItemData(Item.EMERALD,0,10));
			addElement(14, new ItemData(Item.BREAD,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,4,emerald,i);
				}
			});
			addElement(15, new ItemData(Item.EMERALD,0,4));
			addElement(16, new ItemData(Item.APPLE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,4,emerald,i);
				}
			});
			addElement(17, new ItemData(Item.EMERALD,0,4));
			addElement(18, new ItemData(Item.GOLDEN_APPLE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,16,emerald,i);
				}
			});
			addElement(19, new ItemData(Item.EMERALD,0,16));
			addElement(20, new ItemData(Item.GOLDEN_APPLE_ENCHANTED,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,32,emerald,i);
				}
			});
			addElement(21, new ItemData(Item.EMERALD,0,32));
		}});
		menu.setOnlyRead(true);
		menu.show(p);
	}
	
	public void openItemsShop(Player p) {
		Item emerald = Item.get(Item.EMERALD);
		
		InventoryMenu menu = new InventoryMenu();
		menu.setName(Lang.send(p, new String[]{"&l&cITEMS","&l&cПРЕДМЕТЫ"}));
		menu.setMainCategory(new InventoryCategory() {{
			addElement(0, new ItemData(Item.WOODEN_PLANKS,0,40), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 1, emerald,i);	
				}
			});
			addElement(1, new ItemData(Item.EMERALD,0,1));
			addElement(2, new ItemData(Item.BUCKET,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 8, emerald,i);	
				}
			});
			addElement(3, new ItemData(Item.EMERALD,0,8));
			addElement(4, new ItemData(Item.FEATHER,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 4, emerald,i);	
				}
			});
			addElement(5, new ItemData(Item.EMERALD,0,4));
			addElement(6, new ItemData(Item.LEATHER,0,5), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 5, emerald,i);	
				}
			});
			addElement(7, new ItemData(Item.EMERALD,0,5));
			addElement(8, new ItemData(Item.RED_MUSHROOM,0,2), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 1, emerald,i);	
				}
			});
			addElement(9, new ItemData(Item.EMERALD,0,1));
			addElement(10, new ItemData(Item.STRING,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 10, emerald,i);	
				}
			});
			addElement(11, new ItemData(Item.EMERALD,0,10));
			addElement(12, new ItemData(Item.COBWEB,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 20, emerald,i);	
				}
			});
			addElement(13, new ItemData(Item.EMERALD,0,20));
			addElement(14, new ItemData(Item.BONE,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 10, emerald,i);	
				}
			});
			addElement(15, new ItemData(Item.EMERALD,0,10));
			addElement(16, new ItemData(Item.DYE,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 40, emerald,i);	
				}
			});
			addElement(17, new ItemData(Item.EMERALD,0,40));
			addElement(18, new ItemData(Item.SPAWN_EGG,93,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 100, emerald,i);	
				}
			});
			addElement(19, new ItemData(Item.EMERALD,0,50));
			addElement(20, new ItemData(Item.EMERALD,0,50));
			addElement(21, new ItemData(Item.SPAWN_EGG,92,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 150, emerald,i);	
				}
			});
			addElement(22, new ItemData(Item.EMERALD,0,50));
			addElement(23, new ItemData(Item.EMERALD,0,50));
			addElement(24, new ItemData(Item.EMERALD,0,50));
		}});
		menu.setOnlyRead(true);
		menu.show(p);
	}
	
	public void openMineralsShop(Player p) {
		Item emerald = Item.get(Item.EMERALD);
		
		InventoryMenu menu = new InventoryMenu();
		menu.setName(Lang.send(p, new String[] {"&l&gMINERALD","&l&dМИНЕРАЛЫ"}));
		menu.setMainCategory(new InventoryCategory() {{
			addElement(0, new ItemData(Item.COAL_ORE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 40, emerald,i);	
				}
			});
			addElement(1, new ItemData(Item.EMERALD,0,40));
			addElement(2, new ItemData(Item.IRON_ORE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 80, emerald,i);	
				}
			});
			addElement(3, new ItemData(Item.EMERALD,0,40));
			addElement(4, new ItemData(Item.EMERALD,0,40));
			addElement(5, new ItemData(Item.GOLD_ORE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 150, emerald,i);	
				}
			});
			addElement(6, new ItemData(Item.EMERALD,0,50));
			addElement(7, new ItemData(Item.EMERALD,0,50));
			addElement(8, new ItemData(Item.EMERALD,0,50));
			addElement(9, new ItemData(Item.DIAMOND_ORE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 200, emerald,i);	
				}
			});
			addElement(10, new ItemData(Item.EMERALD,0,50));
			addElement(11, new ItemData(Item.EMERALD,0,50));
			addElement(12, new ItemData(Item.EMERALD,0,50));
			addElement(13, new ItemData(Item.EMERALD,0,50));
			
		}});
		menu.setOnlyRead(true);
		menu.show(p);
	}
	
	public void openFarmingShop(Player p) {
		Item emerald = Item.get(Item.EMERALD);
		
		InventoryMenu menu = new InventoryMenu();
		menu.setName(Lang.send(p, new String[] {"&l&cFARMING","&l&cФЕРМЕРСТВО"}));
		menu.setMainCategory(new InventoryCategory() {{
			addElement(0, new ItemData(Item.CARROT,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl, 4, emerald,i);	
				}
			});
			addElement(1, new ItemData(Item.EMERALD,0,4));
			addElement(2, new ItemData(Item.CARROT,0,10), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
					buyItem(pl,10,emerald,i);
				}
			});
			addElement(3, new ItemData(Item.EMERALD,0,10));
			addElement(4, new ItemData(Item.POTATO,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,6,emerald,i);
				}
			});
			addElement(5, new ItemData(Item.EMERALD,0,6));
			addElement(6, new ItemData(Item.POTATO,0,10), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,13,emerald,i);
				}
			});
			addElement(7, new ItemData(Item.EMERALD,0,13));
			addElement(8, new ItemData(Item.WHEAT_SEEDS,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,12,emerald,i);
				}
			});
			addElement(9, new ItemData(Item.EMERALD,0,12));
			addElement(10, new ItemData(Item.SUGARCANE,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,12,emerald,i);
				}
			});
			addElement(11, new ItemData(Item.EMERALD,0,12));
			addElement(12, new ItemData(Item.CACTUS,0,16), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,12,emerald,i);
				}
			});
			addElement(13, new ItemData(Item.EMERALD,0,12));
			addElement(14, new ItemData(Item.SAPLING,0,1), new ItemClick() {
				@Override
				public void onClick(Player pl, Item i) {
						buyItem(pl,15,emerald,i);
				}
			});
			addElement(15, new ItemData(Item.EMERALD,0,15));
		}});
		menu.setOnlyRead(true);
		menu.show(p);
	}
	
	public void buyItem(Player player, int price, Item price2, Item result) {
		Inventory inv = player.getInventory();
		Item item = new Item(price2.getId(),price2.getDamage(),price);
		if(inv.contains(item)) {
			inv.removeItem(item);
			inv.addItem(result);
		}
	}
}