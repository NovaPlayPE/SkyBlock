package net.novaplay.skyblock.blockentity;

import cn.nukkit.block.Block;
import cn.nukkit.block.BlockAir;
import cn.nukkit.blockentity.BlockEntity;
import cn.nukkit.inventory.Inventory;
import cn.nukkit.inventory.InventoryHolder;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemBlock;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.BoneMealParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockEntityAutoFarm extends BlockEntity {
      private Map growable;
      private List blocks;
      public int harvestCooldown;
      public int blockCheckCoolDown;

      public BlockEntityAutoFarm(FullChunk chunk, CompoundTag nbt) {
            this(chunk, nbt, false);
      }

      public BlockEntityAutoFarm(FullChunk chunk, CompoundTag nbt, boolean justCreated) {
            super(chunk, nbt);
            this.growable = new HashMap() {
                  {
                        this.put(103, BlockEntityAutoFarm.BlockGrowData.MELON_BLOCK);
                        this.put(86, BlockEntityAutoFarm.BlockGrowData.PUMPKIN);
                        this.put(115, BlockEntityAutoFarm.BlockGrowData.NETHER_WART_BLOCK);
                        this.put(81, BlockEntityAutoFarm.BlockGrowData.CACTUS);
                        this.put(59, BlockEntityAutoFarm.BlockGrowData.WHEAT_BLOCK);
                        this.put(244, BlockEntityAutoFarm.BlockGrowData.BEETROOT_BLOCK);
                        this.put(141, BlockEntityAutoFarm.BlockGrowData.CARROT_BLOCK);
                        this.put(142, BlockEntityAutoFarm.BlockGrowData.POTATO_BLOCK);
                        this.put(127, BlockEntityAutoFarm.BlockGrowData.COCOA_BLOCK);
                        this.put(83, BlockEntityAutoFarm.BlockGrowData.SUGARCANE_BLOCK);
                  }
            };
            this.blocks = new ArrayList();
            this.harvestCooldown = 0;
            this.blockCheckCoolDown = 0;
            if (!justCreated && !this.isBlockEntityValid()) {
                  this.close();
            } else {
                  this.scheduleUpdate();
            }
      }

      public boolean isBlockEntityValid() {
            return this.level.getBlockIdAt(this.getFloorX(), this.getFloorY(), this.getFloorZ()) == 133;
      }

      public boolean onUpdate() {
            if (this.closed) {
                  return false;
            } else {
                  if (this.blockCheckCoolDown <= 0) {
                        this.checkBlocksAround();
                        this.blockCheckCoolDown = 400;
                  }

                  if (this.harvestCooldown <= 0) {
                        if (!this.isBlockEntityValid()) {
                              this.close();
                              return false;
                        }

                        this.harvestBlocks();
                        this.harvestCooldown = 60;
                  }

                  --this.blockCheckCoolDown;
                  --this.harvestCooldown;
                  return true;
            }
      }

      private void checkBlocksAround() {
            this.blocks.clear();
            int y = this.getFloorY();

            for(int x = this.getFloorX() - 4; x <= this.getFloorX() + 4; ++x) {
                  for(int z = this.getFloorZ() - 4; z <= this.getFloorZ() + 4; ++z) {
                        int id = this.level.getBlockIdAt(x, y, z);
                        if (this.growable.containsKey(id)) {
                              this.blocks.add(new Vector3((double)x, (double)y, (double)z));
                        }
                  }
            }

      }

      private void harvestBlocks() {
            BlockEntity blockEntity = this.level.getBlockEntity(this.up());
            Inventory inventory = null;
            if (blockEntity instanceof InventoryHolder) {
                  inventory = ((InventoryHolder)blockEntity).getInventory();
            }

            for(int i = 0; i < this.blocks.size(); ++i) {
                  Block block = this.level.getBlock((Vector3)this.blocks.get(i));
                  if (!this.growable.containsKey(block.getId())) {
                        this.blocks.remove(i);
                  } else {
                        BlockEntityAutoFarm.BlockGrowData growState = (BlockEntityAutoFarm.BlockGrowData)this.growable.get(block.getId());
                        if (growState.isGrowed(block)) {
                              Item[] dropItems = block.getDrops(new ItemBlock(new BlockAir()));
                              if (inventory != null) {
                                    dropItems = inventory.addItem(dropItems);
                              }

                              if (dropItems.length > 0) {
                                    Item[] var7 = dropItems;
                                    int var8 = dropItems.length;

                                    for(int var9 = 0; var9 < var8; ++var9) {
                                          Item item = var7[var9];
                                          this.level.dropItem(block, item);
                                    }
                              }

                              this.level.addParticle(new BoneMealParticle(block));
                              if (growState.getDefaultState(block.getDamage()) != -1) {
                                    block.setDamage(growState.getDefaultState(block.getDamage()));
                                    this.level.setBlock(block, block, true, false);
                              } else {
                                    this.level.setBlock(block, new BlockAir(), true, true);
                              }
                        }
                  }
            }

      }

      private static enum BlockGrowData {
            MELON_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return true;
                  }

                  public int getDefaultState(int data) {
                        return -1;
                  }
            }),
            PUMPKIN(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return true;
                  }

                  public int getDefaultState(int data) {
                        return -1;
                  }
            }),
            NETHER_WART_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data >= 3;
                  }

                  public int getDefaultState(int data) {
                        return 0;
                  }
            }),
            CACTUS(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return true;
                  }

                  public int getDefaultState(int data) {
                        return -1;
                  }
            }),
            WHEAT_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data >= 7;
                  }

                  public int getDefaultState(int data) {
                        return 0;
                  }
            }),
            BEETROOT_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data >= 7;
                  }

                  public int getDefaultState(int data) {
                        return 0;
                  }
            }),
            CARROT_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data >= 7;
                  }

                  public int getDefaultState(int data) {
                        return 0;
                  }
            }),
            POTATO_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data >= 7;
                  }

                  public int getDefaultState(int data) {
                        return 0;
                  }
            }),
            COCOA_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return data / 4 >= 2;
                  }

                  public int getDefaultState(int data) {
                        return data % 4;
                  }
            }),
            SUGARCANE_BLOCK(new BlockEntityAutoFarm.BlockGrowData.Checker() {
                  public boolean isGrowed(int data) {
                        return true;
                  }

                  public int getDefaultState(int data) {
                        return -1;
                  }
            });

            private final BlockEntityAutoFarm.BlockGrowData.Checker checker;

            private BlockGrowData(BlockEntityAutoFarm.BlockGrowData.Checker checker) {
                  this.checker = checker;
            }

            public boolean isGrowed(Block block) {
                  return this.isGrowed(block.getDamage());
            }

            public boolean isGrowed(int data) {
                  return this.checker.isGrowed(data);
            }

            public int getDefaultState(int data) {
                  return this.checker.getDefaultState(data);
            }

            private interface Checker {
                  boolean isGrowed(int var1);

                  int getDefaultState(int var1);
            }
      }
}
