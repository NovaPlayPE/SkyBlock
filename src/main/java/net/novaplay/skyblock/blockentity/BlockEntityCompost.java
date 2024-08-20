package net.novaplay.skyblock.blockentity;

import cn.nukkit.block.BlockDirt;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.TextFormat;

public class BlockEntityCompost extends BlockEntity {
      public int water;
      public int scrap;

      public BlockEntityCompost(FullChunk chunk, CompoundTag nbt) {
            this(chunk, nbt, false);
      }

      public BlockEntityCompost(FullChunk chunk, CompoundTag nbt, boolean justCreated) {
            super(chunk, nbt);
            this.water = 0;
            this.scrap = 0;
            if (!justCreated && !this.isBlockEntityValid()) {
                  this.close();
            } else {
                  if (this.namedTag.contains("water")) {
                        this.water = this.namedTag.getInt("water");
                  }

                  if (this.namedTag.contains("scrap")) {
                        this.scrap = this.namedTag.getInt("scrap");
                  }

            }
      }

      public boolean isBlockEntityValid() {
            return this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == 118;
      }

      public boolean checkDirt() {
            if (this.water >= 1000 && this.scrap >= 3000) {
                  this.water = 0;
                  this.scrap = 0;
                  Vector3 pos = this.add(0.5D, 1.5D, 0.5D);
                  this.level.dropItem(pos, new ItemBlock(new BlockDirt()));
                  this.level.addParticle(new BoneMealParticle(pos));
                  return true;
            } else {
                  return false;
            }
      }

      public void saveNBT() {
            super.saveNBT();
            this.namedTag.putInt("water", this.water);
            this.namedTag.putInt("scrap", this.scrap);
      }

      public static String getPrefix() {
            return TextFormat.GOLD + "[" + TextFormat.GREEN + "Kompost" + TextFormat.GOLD + "] " + TextFormat.WHITE;
      }
}
