package net.novaplay.skyblock.shop;

import java.util.HashMap;

import cn.nukkit.item.Item;
import cn.nukkit.math.Vector3;

public class Shop {
	
	private Vector3 vec = null;
	private HashMap<String,Integer> data = new HashMap<String,Integer>();
	private boolean isBuy = true;
	private String name;
	
	private Item item = null;
	private int price = 0;
	
	public Shop(String name, Vector3 vec, HashMap<String,Integer> data,boolean buyType) {
		this.name = name;
		this.vec = vec;
		this.data = data;
		item = Item.get(data.get("id"),data.get("damage"),data.get("count"));
		price = data.get("price");
		isBuy = buyType;
	}
	
	public String getName() {
		return this.name;
	}
	
	public boolean isBuyShop() {
		return this.isBuy;
	}
	
	public Item getItem() {
		return item;
	}
	
	public Integer getPrice() {
		return price;
	}
	
	public Vector3 getVector() {
		return this.vec;
	}

}
