package net.novaplay.skyblock;

import cn.nukkit.scheduler.PluginTask;

public class PluginHearbeat extends PluginTask {

    private int nextUpdate = 0;

    public PluginHearbeat(Main owner) {
        super(owner);
    }

    @Override
    public void onRun(int currentTick) {
        this.nextUpdate++;

        Main owner = Main.getInstance();
        if (this.nextUpdate == 120) {
            this.nextUpdate = 0;
            owner.getIslandManager().update();
        }
        owner.getInvitationHandler().tick();
        owner.getResetHandler().tick();
    }

}