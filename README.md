# RandomDrop Plugin for Minecraft (Spigot)

The RandomDrop plugin adds a feature to Minecraft servers where there's a configurable chance for diamonds to spawn in new chunks. These diamonds have custom display names, lore, and a glow effect.


## Features

- **Randomized Diamond Drops:** When a new chunk is loaded for the first time, there's a 30% chance (default) a special diamond will spawn at a random surface location within the chunk.
- **Customizable Drop Chance:** Adjust the chance for diamonds to spawn via the `config.yml`, tailoring the plugin to your needs.
- **Timed Despawn:** To keep the world clean, each spawned item despawns after 60 seconds.


## Installation

1. Download the RandomDrop plugin jar file.
2. Place the jar file into your Minecraft server's `plugins` directory.
3. Restart the server, or if your server is already running, load the plugin dynamically if your server supports it.
4. The plugin will automatically generate a `config.yml` file in its directory. Adjust the `randomdrop_chance` value as desired.


## Configuration

To adjust the chance of a diamond spawning in a new chunk, open the `config.yml` file generated in the RandomDrop plugin folder and modify the `randomdrop_chance` parameter. This value represents the percentage chance of a diamond dropping and can be set to any value between 0 and 100.

Example:
```yaml
enabled: true
randomdrop_chance: 30
```

## Logging

The plugin logs each diamond spawn with the following information:
- World name
- Diamond drop location (X, Y, Z coordinates)
- Item type

This information is logged at the `INFO` level, making it easy to track where and when diamonds are spawned by the plugin.


## Asynchronous Processing

The plugin makes use of CompletableFuture to perform some operations asynchronously, specifically for calculating the diamond drop location and spawning the diamond. This approach prevents the main thread from being blocked, ensuring smooth gameplay even when the plugin is actively spawning diamonds.

## Tested Minecraft Versions

- 1.20.4