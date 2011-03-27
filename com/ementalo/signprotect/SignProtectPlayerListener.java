package com.ementalo.signprotect;

import java.util.List;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.Material;

public class SignProtectPlayerListener extends PlayerListener {

	private SignProtect parent;
	private int signBlockX;
	private int signBlockY;
	private int signBlockZ;
	private SignProtectData spData = new SignProtectData();

	public SignProtectPlayerListener(SignProtect parent) {
		this.parent = parent;

	}
	public void onPlayerItem(PlayerItemEvent event) {

		ItemStack item = event.getItem();
		if (item.getType() == Material.SIGN) {
			Player player = event.getPlayer();

			Block blockPlaced = event.getBlockClicked();

			// if (canUseCommand("/signProtect")) {

			if (item.getTypeId() == 323) {

				signBlockX = blockPlaced.getX();
				signBlockY = blockPlaced.getY();
				signBlockZ = blockPlaced.getZ();

				spData.insertSignProtectIntoDb(player.getName(), signBlockX,
						signBlockY + 1, signBlockZ);

				if (SignProtect.protectBlockUnderneath)
				{
				spData.insertSignProtectIntoDb(player.getName(), signBlockX,
						signBlockY, signBlockZ);
				}

			}

		}
	}

}
