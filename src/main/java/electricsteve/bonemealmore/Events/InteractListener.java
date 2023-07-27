package electricsteve.bonemealmore.Events;

import electricsteve.bonemealmore.BonemealMore;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class InteractListener implements Listener {
    FileConfiguration config = BonemealMore.getPlugin(BonemealMore.class).getConfig();
    HashMap<UUID, Integer> cooldowns = new HashMap<UUID, Integer>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.RIGHT_CLICK_BLOCK) {
            ItemStack item = event.getItem();
            assert item != null;
            Block block = event.getClickedBlock();
            assert block != null;
            World world = Objects.requireNonNull(event.getClickedBlock()).getWorld();
            Player player = event.getPlayer();
            Block blockAbove = world.getBlockAt(block.getX(), block.getY() + 1, block.getZ());
            Block blockBelow = world.getBlockAt(block.getX(), block.getY() - 2, block.getZ());

            UUID uuid = player.getUniqueId();
            int cd = 0;
            int duration = 5;
            if (cooldowns.containsKey(uuid)) {
                cd = cooldowns.get(uuid);
            }
            if (cd > 0) {
                return;
            }



            if (item.getType() == Material.BONE_MEAL && player.getGameMode() != GameMode.SPECTATOR && event.getHand() == EquipmentSlot.HAND) {
                Ageable ageable = null;
                if (block.getBlockData() instanceof Ageable) {
                    ageable = (Ageable) block.getBlockData();
                }

                if (block.getType() == Material.SUGAR_CANE && config.getBoolean("blocks.sugar_cane") && blockAbove.getType() == Material.AIR && blockBelow.getType() != Material.SUGAR_CANE) {
                    assert ageable != null;
                    ageable.setAge(15);

                    block.setBlockData(ageable);
                    block.randomTick();
                    if (player.getGameMode() != GameMode.CREATIVE)
                        item.subtract();
                    world.spawnParticle(Particle.VILLAGER_HAPPY, block.getLocation().toCenterLocation(), 10, 0.25, 0.25, 0.25);
                }



                cooldowns.put(uuid, duration);
                BonemealMore.getPlugin(BonemealMore.class).getServer().getScheduler().scheduleSyncDelayedTask(BonemealMore.getPlugin(BonemealMore.class), new Runnable() {
                    public void run() {
                        cooldowns.remove(uuid);
                    }
                }, duration);
            }
        }
    }
}
