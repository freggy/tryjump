TryJump Unit Converter
======================

This converter can be used to convert TryJump units from the legacy JSON format into Schematic files.

Legacy Unit Format
------------------

| Field      | Type               | Description                                              |
|------------|--------------------|----------------------------------------------------------|
| blockList  | Array of BlockData | Array containing the data of all the blocks of the unit. |
| startLocX  | Integer            | Relative X coordinate of the start point.                |
| startLocY  | Integer            | Relative Y coordinate of the start point.                |
| startLocZ  | Integer            | Relative Z coordinate of the start point.                |
| endLocX    | Integer            | Relative X coordinate of the end point.                  |
| endLocY    | Integer            | Relative Y coordinate of the end point.                  |
| endLocZ    | Integer            | Relative Z coordinate of the end point.                  |
| difficulty | Integer            | Difficulty of this Module (Ranges from 1 to 4).          |

### BlockData Object

| Field    | Type    | Description                                          | 
|----------|---------|------------------------------------------------------|
| x        | Integer | X coordinate relative to the start point.            |
| y        | Integer | Y coordinate relative to the start point.            |
| z        | Integer | Z coordinate relative to the start point.            |
| material | String  | Exact name of the [`Material`](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html) enum name of the block. |
| data     | Byte    | Additional data of the block.                        |

### Example

```JSON
{
    "blockList": [
        {
            "x": 0,
            "y": 0,
            "z": 1,
            "material": "STONE",
            "data": 0
        }
    ],
    "startLocX": 0,
    "startLocY": 0,
    "startLocZ": 0,
    "endLocX": 3,
    "endLocY": 2,
    "endLocZ": 3,
    "difficulty": 3
}
```
