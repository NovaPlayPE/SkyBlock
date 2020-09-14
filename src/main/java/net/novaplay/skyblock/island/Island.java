package net.novaplay.skyblock.island;


import cn.nukkit.Player;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import lombok.Getter;
import lombok.Setter;
import net.novaplay.skyblock.Utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Island {

    @Getter
    @Setter
    private Config config;

    @Getter
    @Setter
    private String ownerName;

    @Getter
    @Setter
    private String identifier;

    @Getter
    @Setter
    private Map<String, Player> playersOnline = new HashMap<>();

    @Getter
    @Setter
    private Set<String> members = new HashSet<>();

    @Getter
    @Setter
    private boolean locked;

    @Getter
    @Setter
    private String home;

    @Getter
    @Setter
    private String generator;

    public Island(Config config, String ownerName, String identifier, Set<String> members, boolean locked, String home, String generator) {
        this.config = config;
        this.ownerName = ownerName;
        this.identifier = identifier;
        this.members = members;
        this.locked = locked;
        this.home = home;
        this.generator = generator;
    }

    public Position getHomePosition() {
        return Utils.parsePosition(this.home);
    }

    public boolean hasHome() {
        return this.getHomePosition() != null;
    }

    public Set<String> getAllMembers() {
        Set<String> members = this.members;
        members.add(ownerName);
        return members;
    }

    public void addPlayer(Player player) {
        this.playersOnline.put(player.getName().toLowerCase(), player);
        this.update();
    }

    public void addMember(Player player) {
        members.add(player.getName().toLowerCase());
        this.update();
    }

    public void setHomePosition(Position position) {
        this.home = Utils.createPositionString(position);
        this.update();
    }

    public void tryRemovePlayer(Player player) {
        this.playersOnline.remove(player.getName().toLowerCase());
        this.update();
    }

    public void removeMember(String string) {
        this.members.remove(string.toLowerCase());
        this.update();
    }

    public void update() {
        this.config.set("owner", this.getOwnerName());
        this.config.set("home", this.getHome());
        this.config.set("locked", this.isLocked());
        this.config.set("members", this.getMembers());
        this.config.save();
    }

}