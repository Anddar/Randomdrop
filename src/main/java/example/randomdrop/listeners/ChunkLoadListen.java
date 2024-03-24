package example.randomdrop.listeners;

import example.randomdrop.Randomdrop;
import example.randomdrop.config.RandomdropConfig;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class ChunkLoadListen implements Listener {
    private static final Random rand = new Random();

    private static Logger logger;
    private static RandomdropConfig RDConfigInstance;

    private double diamondDropChance;
    private ItemStack diamondDrop = createDiamondDrop();

    public ChunkLoadListen(Logger logger, RandomdropConfig RDConfigInstance) {
        ChunkLoadListen.logger = logger;
        ChunkLoadListen.RDConfigInstance = RDConfigInstance;

        setDiamondDropChance();
    }

    @EventHandler
    private void onChunkLoad(ChunkLoadEvent event) {
        // Check if this is the first load = new chunk and if the random chance is met for a diamond to spawn
        if (event.isNewChunk() && rand.nextDouble() < diamondDropChance) {
            spawnDiamondDrop(event.getChunk());
        }
    }

    /**
     * Spawn a diamond drop on the surface of the chunk
     * @param chunk The chunk to spawn the diamond drop in
     */
    private void spawnDiamondDrop(Chunk chunk) {
        // Asynchronously calculate the drop location, if our calculation was more expensive we would not want
        // it blocking the main thread. That is the primary reason for using CompletableFuture in a situation like this.
        CompletableFuture.supplyAsync(() -> calculateDropLocation(chunk))
            .thenAcceptAsync(location -> {
                Bukkit.getScheduler().runTask(Randomdrop.getPlugin(Randomdrop.class), () -> {
                    // Drop a Diamond on the surface of the random location in the 16x16 chunk
                    Item droppedItem = chunk.getWorld().dropItemNaturally(location, diamondDrop);

                    // Log the Diamond drop
                    logger.info("World Name: " + chunk.getWorld().getName() +
                            " Diamond Location: X:" + location.getX() + ", Y:" + location.getY() + ", Z:" + location.getZ() +
                            " Item Type: " + diamondDrop.getType());

                    // Schedule Removal
                    scheduleDiamondDropRemoval(droppedItem);
                });
            });
    }

    /**
     * This method is used to schedule the removal of the diamond drop after 60 seconds
     * @param droppedItem The item that was dropped
     */
    private void scheduleDiamondDropRemoval(Item droppedItem) {
        Bukkit.getScheduler().runTaskLater(Randomdrop.getPlugin(Randomdrop.class), () -> {
            droppedItem.remove();
        }, 20L * 60);
    }

    /**
     * Calculate the location to drop the diamond in the chunk
     * @param chunk The chunk to calculate the drop location in
     * @return The location to drop the diamond
     */
    private Location calculateDropLocation(Chunk chunk) {
        // Randomly choose an X and Z coordinate in the chunk
        int x = rand.nextInt(16);
        int z = rand.nextInt(16);

        // Get the highest Y coordinate at the random X and Z coordinate in the chunk
        int y = chunk.getWorld().getHighestBlockYAt(chunk.getX()*16 + x, chunk.getZ()*16 + z);

        return new Location(chunk.getWorld(), chunk.getX()*16 + x, y, chunk.getZ()*16 + z);
    }

    /**
     * Set the chance of a diamond item spawning in a chunk
     */
    private void setDiamondDropChance() {
        diamondDropChance = RDConfigInstance.getDefaultConfig().getDouble("randomdrop_chance")/100;
    }

    /**
     * Create a custom diamond drop with a display name, lore, and enchantment
     * @return The custom diamond drop
     */
    public static ItemStack createDiamondDrop() {
        ItemStack diamond = new ItemStack(Material.DIAMOND);
        ItemMeta diamondMeta = diamond.getItemMeta();

        // Ensure the ItemMeta is not null, otherwise just return a normal diamond
        if (diamondMeta == null) {
            logger.warning("ItemMeta is null for diamond drop. Skipping custom diamond drop.");
            return diamond;
        }

        // Setting the display name of the diamond
        diamondMeta.setDisplayName("§6§lShiny Diamond");

        // Setting the lore of the diamond
        diamondMeta.setLore(new ArrayList<>(Arrays.asList("§7A diamond that has been", "§7shining for centuries.")));

        // Add the glowing effect to the diamond, and hide the enchantment
        diamondMeta.addEnchant(Enchantment.LUCK, 1, true);
        diamondMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        diamond.setItemMeta(diamondMeta);

        return diamond;
    }
}
