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
 * <p>Converts a {@link JsonObject} representing the legacy unit format into a Schematic file.
 *
 * @author Yannic Rieger
 */
public class UnitConverter {

  /** Class containing block data found in the JSON array. */
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

  /**
   * Creates {@link CompoundTag} compliant with the Schematic format.
   *
   * @param object JSON file with the legacy unit format.
   * @return {@link CompoundTag} compliant with the Schematic format.
   */
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
    final ByteArrayTag data = this.createDataTag(blockData, width, length, size);
    final StringTag materials = new StringTag("Materials", "ALPHA");

    compoundMap.put(blocks);
    compoundMap.put(data);
    compoundMap.put(materials);
    compoundMap.put(EMPTY_ENTITIES);
    compoundMap.put(EMPTY_TILE_ENTITIES);
    return new CompoundTag("Schematic", compoundMap);
  }

  /**
   * Creates a {@link ByteArrayTag} containing additional data about the block.
   *
   * @param blockData the {@link BlockData}.
   * @param width width of the schematic.
   * @param length length of the schematic.
   * @param size size of the schematic.
   * @return a {@link ByteArrayTag} containing additional data about the block.
   */
  private ByteArrayTag createDataTag(Set<BlockData> blockData, int width, int length, int size) {
    final byte[] array = new byte[size];
    for (BlockData data : blockData) {
      final int index = (data.y * length + data.z) * width + data.x;
      array[index] = data.data;
    }
    return new ByteArrayTag("Data", array);
  }

  /**
   * Creates a {@link ByteArrayTag} containing the block id See {@link Material}.
   *
   * @param blockData the {@link BlockData}.
   * @param width width of the schematic.
   * @param length length of the schematic.
   * @param size size of the schematic.
   * @return a {@link ByteArrayTag} containing additional data about the block.
   */
  private ByteArrayTag createBlocksTag(Set<BlockData> blockData, int width, int length, int size) {
    final byte[] array = new byte[size];
    for (BlockData data : blockData) {
      final int index = (data.y * length + data.z) * width + data.x;
      array[index] = (byte) data.id;
    }
    return new ByteArrayTag("Blocks", array);
  }

  /**
   * Creates a {@link Set} of {@link BlockData} from a {@link JsonArray}.
   *
   * @param blockList {@link JsonArray} containing a {@link BlockData} object.
   * @return a {@link Set} of {@link BlockData} from a {@link JsonArray}.
   */
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
