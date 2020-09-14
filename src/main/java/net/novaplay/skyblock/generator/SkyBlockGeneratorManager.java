package net.novaplay.skyblock.generator;

import cn.nukkit.level.generator.Generator;
import net.novaplay.skyblock.Main;
import net.novaplay.skyblock.generator.generators.BasicIsland;

import java.util.HashMap;
import java.util.Map;

public class SkyBlockGeneratorManager {

    private Main plugin;

    private Map<String, String> generators = new HashMap<>();

    public SkyBlockGeneratorManager(Main plugin) {
        this.plugin = plugin;
        this.registerGenerator(BasicIsland.class, "basic", "Basic Island");
        Generator.addGenerator(BasicIsland.class, "basicgen", Generator.TYPE_INFINITE);
    }

    public boolean isGenerator(String name) {
        return this.generators.containsKey(name);
    }

    public Map<String, String> getGenerators() {
        return this.generators;
    }

    public String getGeneratorIslandName(String name) {
        return this.isGenerator(name) ? this.generators.get(name) : "";
    }

    public void registerGenerator(Class<? extends Generator> generator, String name, String islandName) {
        Generator.addGenerator(generator, name, Generator.TYPE_INFINITE);
        this.generators.put(name, islandName);
    }

}