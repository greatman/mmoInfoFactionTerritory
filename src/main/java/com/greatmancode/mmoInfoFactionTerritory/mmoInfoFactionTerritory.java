/*
 * This file is part of mmoInfoFactionTerritory <http://github.com/greatman/mmoInfoFactionTerritory>.
 *
 * mmoInfoFactionTerritory is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.greatmancode.mmoInfoFactionTerritory;

import java.util.HashMap;
import java.util.Map;

import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;

import mmo.Core.InfoAPI.MMOInfoEvent;
import mmo.Core.MMOPlugin;
import mmo.Core.util.EnumBitSet;
import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.player.SpoutPlayer;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public final class mmoInfoFactionTerritory extends MMOPlugin
		implements Listener
{
	private static final Map<Player, CustomLabel> widgets = new HashMap();

	public EnumBitSet mmoSupport(EnumBitSet support)
	{
		support.set(MMOPlugin.Support.MMO_AUTO_EXTRACT);
		return support;
	}

	public void onEnable()
	{
		super.onEnable();
		this.pm.registerEvents(this, this);
	}

	@EventHandler
	public void onMMOInfo(MMOInfoEvent event) {
		if (event.isToken("factionterritory")) {
			SpoutPlayer player = event.getPlayer();
			if (player.hasPermission("mmo.info.factionterritory")) {
				CustomLabel label = (CustomLabel)new CustomLabel().setResize(true).setFixed(true);
				widgets.put(player, label);
				event.setWidget(this.plugin, label);
			} else {
				event.setCancelled(true);
			}
		}
	}

	public static final class CustomLabel extends GenericLabel {
		private transient int tick = 0;

		public void onTick()
		{
			if (this.tick++ % 50 == 0) {
				Player player = getScreen().getPlayer();
				FPlayer fPlayer = FPlayers.i.get(player);
				Faction faction = Board.getFactionAt(new FLocation(player.getLocation()));
				setText(String.format(ChatColor.AQUA + "Location" + ChatColor.WHITE + ": "+ fPlayer.getColorTo(faction) + faction.getTag(), new Object[0]));
			}
		}
	}
}