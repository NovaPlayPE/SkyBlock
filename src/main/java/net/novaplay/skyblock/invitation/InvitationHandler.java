package net.novaplay.skyblock.invitation;

import cn.nukkit.Player;
import lombok.Getter;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.island.Island;

import java.util.HashMap;
import java.util.Map;

public class InvitationHandler {

    @Getter
    private Main plugin;

    @Getter
    private Map<String, Invitation> invitations = new HashMap<>();

    public InvitationHandler(Main plugin) {
        this.plugin = plugin;
    }

    public Invitation getInvitation(Player player) {
        return this.invitations.get(player.getName().toLowerCase());
    }

    public void addInvitation(Player sender, Player receiver, Island island) {
        this.invitations.put(sender.getName().toLowerCase(), new Invitation(this, sender, receiver, island));
    }

    public void removeInvitation(Invitation invitation) {
        this.invitations.remove(invitation.getSender().getName().toLowerCase());
    }

    public void tick() {
        for (Invitation invitation : this.invitations.values()) {
            invitation.tick();
        }
    }

}