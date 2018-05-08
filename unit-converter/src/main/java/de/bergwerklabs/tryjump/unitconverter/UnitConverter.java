package de.bergwerklabs.tryjump.unitconverter;

import com.flowpowered.nbt.ByteArrayTag;
import com.flowpowered.nbt.CompoundMap;
import com.flowpowered.nbt.CompoundTag;
import com.flowpowered.nbt.ListTag;
import com.flowpowered.nbt.StringTag;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Yannic Rieger on 08.05.2018.
 *
 * <p>
 *
 * @author Yannic Rieger
 */
public class UnitConverter {

  private class BlockData {
    private int x, y, z, id;
    private byte data;

    private BlockData(int id, int x, int y, int z, byte data) {
      this.id = id;
      this.x = x;
      this.y = y;
      this.z = z;
      this.data = data;
    }

    int getY() {
      return this.y;
    }

    int getX() {
      return this.x;
    }
  }

  private final ListTag<CompoundTag> EMPTY_ENTITIES =
      new ListTag<>("Entities", CompoundTag.class, new ArrayList<>());

  private final ListTag<CompoundTag> EMPTY_TILE_ENTITIES =
      new ListTag<>("Entities", CompoundTag.class, new ArrayList<>());

  public CompoundTag createSchematic(JsonObject object) {
    final CompoundMap compoundMap = new CompoundMap();

    // Sort in ascending order since the Schematic specification requires it. (bottom to top)
    final Set<BlockData> blockData =
        this.retrieveBlockData(object.get("blockList").getAsJsonArray())
            .stream()
            .sorted(Comparator.comparing(BlockData::getY).reversed())
            .collect(Collectors.toSet());

    final int height = blockData.stream().mapToInt(BlockData::getY).max().getAsInt();
    final int width = blockData.stream().mapToInt(BlockData::getX).max().getAsInt();
    final int length = object.get("endLocZ").getAsInt();
    final int size = height * width * length;

    final ByteArrayTag blocks = this.createBlocksTag(blockData, width, length, size);
    final ByteArrayTag data = this.createBlocksTag(blockData, width, length, size);
    final StringTag materials = new StringTag("Materials", "ALPHA");

    compoundMap.put(blocks);
    compoundMap.put(data);
    compoundMap.put(materials);
    compoundMap.put(EMPTY_ENTITIES);
    compoundMap.put(EMPTY_TILE_ENTITIES);
    return new CompoundTag("Schematic", compoundMap);
  }

  private ByteArrayTag createDataTag(Set<BlockData> blockData, int width, int length, int size) {
    final byte[] array = new byte[size];
    for (BlockData data : blockData) {
      final int index = (data.y * length + data.z) * width + data.x;
      array[index] = data.data;
    }
    return new ByteArrayTag("Data", array);
  }

  private ByteArrayTag createBlocksTag(Set<BlockData> blockData, int width, int length, int size) {
    final byte[] array = new byte[size];
    for (BlockData data : blockData) {
      final int index = (data.y * length + data.z) * width + data.x;
      array[index] = (byte) data.id;
    }
    return new ByteArrayTag("Blocks", array);
  }

  private Set<BlockData> retrieveBlockData(JsonArray blockList) {
    final Set<BlockData> data = new HashSet<>();

    for (JsonElement block : blockList) {
      if (!block.isJsonObject()) continue;
      final JsonObject dataObject = block.getAsJsonObject();
      final Material material = Material.valueOf(dataObject.get("material").getAsString());

      data.add(
          new BlockData(
              material.getId(),
              dataObject.get("x").getAsInt(),
              dataObject.get("y").getAsInt(),
              dataObject.get("z").getAsInt(),
              dataObject.get("data").getAsByte()));
    }
    return data;
  }
}
