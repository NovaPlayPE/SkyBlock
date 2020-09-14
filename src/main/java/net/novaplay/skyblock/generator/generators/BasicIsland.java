package net.novaplay.skyblock.generator.generators;

import cn.nukkit.block.Block;
import cn.nukkit.level.ChunkManager;
import cn.nukkit.level.format.generic.BaseFullChunk;
import cn.nukkit.math.NukkitRandom;
import cn.nukkit.math.Vector3;
import net.novaplay.skyblock.generator.SkyBlockGenerator;

import java.util.Map;

public class BasicIsland extends SkyBlockGenerator {

    /**
     * @var array
     */
    private Map<String, Object> settings;

    /**
     * @var string
     */
    private String name;

    /**
     * @var ChunkManager
     */
    private ChunkManager level;

    /**
     * @var Random
     */
    private NukkitRandom random;

    public BasicIsland(Map<String, Object> settings) {
        this.settings = settings;
    }

    @Override
    public void init(ChunkManager chunkManager, NukkitRandom nukkitRandom) {
        this.level = chunkManager;
        this.random = nukkitRandom;
        this.name = "basic";
        this.islandName = "Basic Island";
    }

    /**
     * Return generator name
     *
     * @return string
     */
    public String getName() {
        return this.name;
    }

    public Map<String, Object> getSettings() {
        return this.settings;
    }

    public void generateChunk(int chunkX, int chunkZ) {
        BaseFullChunk chunk = this.level.getChunk(chunkX, chunkZ);
        chunk.setGenerated();
        if (chunkX % 20 == 0 && chunkZ % 20 == 0) {
            for (int x = 2; x < 5; x++) {
                for (int z = 2; z < 5; z++) {
                    //  $chunk->setBlock($x, 0, $z, Block::BEDROCK);
                    for (int y = 1; y <= 3; y++) {
                        chunk.setBlock(x, 50, z, Block.DIRT);
                    }
                    chunk.setBlock(x, 51, z, Block.DIRT);
                    chunk.setBlock(x, 52, z, Block.GRASS);
                    for (int x1 = 2; x1 <= 7; x1++) {
                        for (int z1 = 5; z1 <= 7; z1++) {
                            chunk.setBlock(x1, 50, z1, Block.DIRT);
                            chunk.setBlock(x1, 51, z1, Block.DIRT);
                            chunk.setBlock(x1, 52, z1, Block.GRASS);
                        }
                    }
                    //strom
                    for (int sy = 53; sy <= 59; sy++) {
                        chunk.setBlock(7, sy, 6, Block.WOOD);
                    }
                    //před
                    chunk.setBlock(5, 56, 5, Block.LEAVES);
                    chunk.setBlock(5, 56, 6, Block.LEAVES);
                    chunk.setBlock(5, 56, 7, Block.LEAVES);
                    chunk.setBlock(6, 56, 5, Block.LEAVES);
                    chunk.setBlock(6, 56, 6, Block.LEAVES);
                    chunk.setBlock(6, 56, 7, Block.LEAVES);
                    chunk.setBlock(5, 57, 5, Block.LEAVES);
                    chunk.setBlock(5, 57, 6, Block.LEAVES);
                    chunk.setBlock(5, 57, 7, Block.LEAVES);
                    chunk.setBlock(6, 57, 5, Block.LEAVES);
                    chunk.setBlock(6, 57, 6, Block.LEAVES);
                    chunk.setBlock(6, 57, 7, Block.LEAVES);
                    //vpravo
                    chunk.setBlock(6, 56, 7, Block.LEAVES);
                    chunk.setBlock(7, 56, 7, Block.LEAVES);
                    chunk.setBlock(8, 56, 7, Block.LEAVES);
                    chunk.setBlock(6, 56, 8, Block.LEAVES);
                    chunk.setBlock(7, 56, 8, Block.LEAVES);
                    chunk.setBlock(8, 56, 8, Block.LEAVES);
                    chunk.setBlock(6, 57, 7, Block.LEAVES);
                    chunk.setBlock(7, 57, 7, Block.LEAVES);
                    chunk.setBlock(8, 57, 7, Block.LEAVES);
                    chunk.setBlock(6, 57, 8, Block.LEAVES);
                    chunk.setBlock(7, 57, 8, Block.LEAVES);
                    chunk.setBlock(8, 57, 8, Block.LEAVES);
                    //vlevo
                    chunk.setBlock(6, 56, 5, Block.LEAVES);
                    chunk.setBlock(7, 56, 5, Block.LEAVES);
                    chunk.setBlock(8, 56, 5, Block.LEAVES);
                    chunk.setBlock(6, 56, 4, Block.LEAVES);
                    chunk.setBlock(7, 56, 4, Block.LEAVES);
                    chunk.setBlock(8, 56, 4, Block.LEAVES);
                    chunk.setBlock(6, 57, 5, Block.LEAVES);
                    chunk.setBlock(7, 57, 5, Block.LEAVES);
                    chunk.setBlock(8, 57, 5, Block.LEAVES);
                    chunk.setBlock(6, 57, 4, Block.LEAVES);
                    chunk.setBlock(7, 57, 4, Block.LEAVES);
                    chunk.setBlock(8, 57, 4, Block.LEAVES);
                    //za
                    chunk.setBlock(8, 56, 5, Block.LEAVES);
                    chunk.setBlock(8, 56, 6, Block.LEAVES);
                    chunk.setBlock(8, 56, 7, Block.LEAVES);
                    chunk.setBlock(9, 56, 5, Block.LEAVES);
                    chunk.setBlock(9, 56, 6, Block.LEAVES);
                    chunk.setBlock(9, 56, 7, Block.LEAVES);
                    chunk.setBlock(8, 57, 5, Block.LEAVES);
                    chunk.setBlock(8, 57, 6, Block.LEAVES);
                    chunk.setBlock(8, 57, 7, Block.LEAVES);
                    chunk.setBlock(9, 57, 5, Block.LEAVES);
                    chunk.setBlock(9, 57, 6, Block.LEAVES);
                    chunk.setBlock(9, 57, 7, Block.LEAVES);
                    //na
                    chunk.setBlock(7, 58, 7, Block.LEAVES);
                    chunk.setBlock(7, 58, 5, Block.LEAVES);
                    chunk.setBlock(6, 58, 6, Block.LEAVES);
                    chunk.setBlock(8, 58, 6, Block.LEAVES);
                    chunk.setBlock(7, 59, 7, Block.LEAVES);
                    chunk.setBlock(7, 59, 5, Block.LEAVES);
                    chunk.setBlock(6, 59, 6, Block.LEAVES);
                    chunk.setBlock(8, 59, 6, Block.LEAVES);
                    

                }
                // Tree::growTree($this->level, $chunkX * 16 + 8, 6, $chunkZ * 16 + 8, $this->random, 0);
                //OakTree::growTree($this->level, $chunkX * 5 + 8, 4, $chunkZ * 5 + 8, $this->random, 0);
            }
            chunk.setX(chunkX);
            chunk.setZ(chunkZ);
            this.level.setChunk(chunkX, chunkZ, chunk);
        }
    }

    @Override
    public void populateChunk(int chunkX, int chunkZ) {
        for (int x = 0; x < 5; x++) {
            for (int z = 0; z < 5; z++) {
                // $this->level->getChunk($chunkX, $chunkZ)->setBiomeColor($x, $z, 133, 188, 86);
            }
        }
    }

    /**
     * Return BasicIsland spawn
     *
     * @return Vector3
     */
    public Vector3 getSpawn() {
        return new Vector3(2, 7, 2);
    }

    @Override
    public ChunkManager getChunkManager() {
        return this.level;
    }

    @Override
    public int getId() {
        return 0; //TODO
    }
}