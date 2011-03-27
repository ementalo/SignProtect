package com.ementalo.signprotect;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.BlockDamageLevel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockRightClickEvent;

public class SignProtectBlockListener extends BlockListener {

	private SignProtect parent;
	private SignProtectData spData = new SignProtectData();

	public SignProtectBlockListener(SignProtect parent) {
		this.parent = parent;

	}

	public void onBlockRightClick(BlockRightClickEvent event) {
		Player player = event.getPlayer();
		String ownerName = spData.getBlockOwner(player.getName(),
				event.getBlock());

		if (SignProtect.canUseCommand(player)) {
			if (ownerName != null) {
				player.sendMessage(ChatColor.GOLD
						+ "[SignProtect] Protection owner: " + ownerName);
			}
		}
	}

	public void onBlockBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Block block = event.getBlock();

		if (SignProtect.canUseCommand(player)) {
			spData.RemoveProtectionFromDB(block);
			return;
		} else {

			boolean canDestroyThePreciousSigns = spData.canDestroySign(
					player.getName(), block);

			if (canDestroyThePreciousSigns) {
				spData.RemoveProtectionFromDB(block);
			} else {
				event.setCancelled(true);
				return;
			}
		}
	}
}
