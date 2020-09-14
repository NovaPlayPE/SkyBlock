package net.novaplay.skyblock.task;

import cn.nukkit.scheduler.AsyncTask;
import net.novaplay.skyblock.Utils;

/**
 * Created by CreeperFace on 8.5.2017.
 */
public class IslandRemoveTask extends AsyncTask {

    private String identifier;

    public IslandRemoveTask(String identifier) {
        this.identifier = identifier;
    }

    @Override
    public void onRun() {
        Utils.deleteWorld(this.identifier);
    }
}
