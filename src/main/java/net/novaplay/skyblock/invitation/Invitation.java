package net.novaplay.skyblock.invitation;

import cn.nukkit.Player;
import cn.nukkit.utils.TextFormat;
import lombok.Getter;
import net.novaplay.skyblock.PlayerData;
import net.novaplay.skyblock.island.Island;

public class Invitation {

    private InvitationHandler handler;

    @Getter
    private Player sender;

    @Getter
    private Player receiver;

    private Island island;

    private int time = 30;

    public Invitation(InvitationHandler handler, Player sender, Player receiver, Island island) {
        this.handler = handler;
        this.sender = sender;
        this.receiver = receiver;
        this.island = island;
    }

    public void accept() {
        PlayerData data = this.handler.getPlugin().getSkyBlockManager().getPlayerIsland(this.receiver);
        if (data.island == null || data.island.isEmpty()) {
            data.island = this.island.getIdentifier();
            this.island.addMember(this.receiver);
            this.sender.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + this.receiver.getName()+ "accepted your invitation!");
            this.receiver.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + "You joined "+this.sender.getName()+" island!");
        } else {
            this.sender.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + this.receiver.getName()+" is already in island!");
        }
        this.handler.removeInvitation(this);
    }

    public void deny() {
        this.sender.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + this.receiver.getName()+" denied your invitation!");
        this.receiver.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + "You denied "+this.sender.getName()+"'s invitation!");
        this.handler.removeInvitation(this);
    }

    public void expire() {
        this.sender.sendMessage(TextFormat.RED + "* " + TextFormat.YELLOW + "The invitation to "+this.receiver.getName()+" expired!");
        this.handler.removeInvitation(this);
    }

    public void tick() {
        if (this.time <= 0) {
            this.expire();
        } else {
            this.time--;
            this.sender.sendPopup(TextFormat.RED + "> " + TextFormat.YELLOW + "The invitation to "+this.receiver.getName()+" will expire in "+this.time+" seconds" + TextFormat.RED + " <");
        }
    }

}