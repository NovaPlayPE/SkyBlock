package net.novaplay.skyblock.task;

import cn.nukkit.Server;
import cn.nukkit.level.generator.Generator;
import cn.nukkit.scheduler.AsyncTask;
import net.novaplay.skyblock.Utils;

/**
 * Created by CreeperFace on 8.5.2017.
 */
public class IslandCreateTask extends AsyncTask {

    private String identifier;
    private String gen = "basic";

    public IslandCreateTask(String identifier,String gen) {
        this.identifier = identifier;
        this.gen = gen;
    }

    @Override
    public void onRun() {
        //Utils.addWorld(this.identifier);
    	Server.getInstance().generateLevel(this.identifier,0,Generator.getGenerator(this.gen));
    }

    @Override
    public void onCompletion(Server server) {
        server.loadLevel(this.identifier);
    }
}
