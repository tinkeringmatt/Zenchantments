package zedly.zenchantments.arrows.admin;

import org.bukkit.*;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.LivingEntity;
import zedly.zenchantments.Config;
import zedly.zenchantments.Storage;
import zedly.zenchantments.Utilities;
import zedly.zenchantments.arrows.EnchantedArrow;

import java.util.List;

import static org.bukkit.Material.AIR;

public class MissileArrow extends EnchantedArrow {

	public MissileArrow(Arrow entity) {
		super(entity);
	}

	public void onLaunch(LivingEntity player, List<String> lore) {
		final Config config = Config.get(player.getWorld());
		Location playLoc = player.getLocation();
		final Location target = Utilities.getCenter(player.getTargetBlock(null, 220));
		target.setY(target.getY() + .5);
		final Location c = playLoc;
		c.setY(c.getY() + 1.1);
		final double d = target.distance(c);
		for (int i = 9; i <= ((int) (d * 5) + 9); i++) {
			final int i1 = i;
			Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(Storage.zenchantments, () -> {
				Location loc = target.clone();
				loc.setX(c.getX() + (i1 * ((target.getX() - c.getX()) / (d * 5))));
				loc.setY(c.getY() + (i1 * ((target.getY() - c.getY()) / (d * 5))));
				loc.setZ(c.getZ() + (i1 * ((target.getZ() - c.getZ()) / (d * 5))));
				Location loc2 = target.clone();
				loc2.setX(c.getX() + ((i1 + 10) * ((target.getX() - c.getX()) / (d * 5))));
				loc2.setY(c.getY() + ((i1 + 10) * ((target.getY() - c.getY()) / (d * 5))));
				loc2.setZ(c.getZ() + ((i1 + 10) * ((target.getZ() - c.getZ()) / (d * 5))));
				Utilities.display(loc, Particle.FLAME, 10, .001f, 0, 0, 0);
				Utilities.display(loc, Particle.FLAME, 1, .1f, 0, 0, 0);
				if (i1 % 50 == 0) {
					target.getWorld().playSound(loc, Sound.ENTITY_WITHER_SPAWN, 10f, .1f);
				}
				if (i1 >= ((int) (d * 5) + 9) || loc2.getBlock().getType() != AIR) {
					Utilities.display(loc2, Particle.EXPLOSION_HUGE, 10, 0.1f, 0, 0, 0);
					Utilities.display(loc, Particle.FLAME, 175, 1f, 0, 0, 0);
					loc2.setY(loc2.getY() + 5);
					loc2.getWorld().createExplosion(loc2.getX(), loc2.getY(), loc2.getZ(), 10,
						config.explosionBlockBreak(), config.explosionBlockBreak());
				}
			}, i / 7);
		}
	}
}
