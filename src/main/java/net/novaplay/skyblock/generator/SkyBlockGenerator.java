package net.novaplay.skyblock.generator;

import cn.nukkit.level.generator.Generator;

public abstract class SkyBlockGenerator extends Generator {

    /**
     * @var string
     */
    protected String islandName;

    /**
     * Return island name
     *
     * @return string
     */
    public String getIslandName() {
        return this.islandName;
    }

    /**
     * Set island name
     */
    public void setIslandName(String name) {
        this.islandName = name;
    }

}