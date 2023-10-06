package net.frozenorb.foxtrot.team.commands.team;

import java.util.Arrays;

import cc.fyre.proton.command.Command;
import cc.fyre.proton.command.param.Parameter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.team.Team;
import org.bukkit.ChatColor;
import net.frozenorb.foxtrot.team.claims.VisualClaim;
import net.frozenorb.foxtrot.team.claims.VisualClaimType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamBaseCommand {

    public static final ItemStack SELECTION_WAND = new ItemStack(Material.WOOD_HOE);

    static {
        ItemMeta meta = SELECTION_WAND.getItemMeta();

        meta.setDisplayName("§a§oBase Wand");
        meta.setLore(Arrays.asList(
                "",
                "§eRight/Left Click§6 Block",
                "§b- §fSelect base's corners",
                "",
                "§eRight Click §6Air",
                "§b- §fCancel current selection",
                "",
                "§9Crouch §eLeft Click §6Block/Air",
                "§b- §fPurchase current selection"
        ));

        SELECTION_WAND.setItemMeta(meta);
    }

//    @Command(names = {"base deposit", "team base deposit", "t base deposit", "f base deposit", "faction base deposit"}, permission = "")
//    public static void teamBaseDeposit(Player sender, @Parameter(name = "amount") int amount) {
//        int tokens = Foxtrot.getInstance().getBaseTokensMap().getTokens(sender.getUniqueId());
//        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
//        if (team == null) {
//            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
//            return;
//        }
//
//        if (amount <= 0) {
//            sender.sendMessage(ChatColor.RED + "You can't deposit 0 (or less)!");
//            return;
//        }

//        if (tokens < amount) {
//            sender.sendMessage(ChatColor.RED + "You don't have enough tokens to do this!");
//            return;
//        }

//        Foxtrot.getInstance().getBaseTokensMap().setTokens(sender.getUniqueId(), tokens - amount);
//
//        sender.sendMessage(ChatColor.YELLOW + "You have added " + ChatColor.LIGHT_PURPLE + amount + " base tokens " + ChatColor.YELLOW + " to the team!");
//
//        team.setBaseTokens(team.getBaseTokens() + amount);
//        team.sendMessage(ChatColor.YELLOW + sender.getName() + " deposited " + ChatColor.LIGHT_PURPLE + amount + " base tokens " + ChatColor.YELLOW + " into the team.");
//    }

    @Command(names = {"base wand", "team base wand", "t base wand", "f base wand", "faction base wand"}, permission = "")
    public static void teamBaseWand(Player sender) {
        Team team = Foxtrot.getInstance().getTeamHandler().getTeam(sender);
        if (team == null) {
            sender.sendMessage(ChatColor.GRAY + "You are not on a team!");
            return;
        }

        if (team.isOwner(sender.getUniqueId()) || team.isCoLeader(sender.getUniqueId()) || team.isCaptain(sender.getUniqueId())) {
            sender.getInventory().remove(SELECTION_WAND);

            new BukkitRunnable() {
                @Override
                public void run() {
                    sender.getInventory().addItem(SELECTION_WAND.clone());
                }
            }.runTaskLater(Foxtrot.getInstance(), 1L);

            new VisualClaim(sender, VisualClaimType.BASE, false).draw(false);

            if (!VisualClaim.getCurrentMaps().containsKey(sender.getName())) {
                new VisualClaim(sender, VisualClaimType.MAP, false).draw(true);
            }
        } else {
            sender.sendMessage(ChatColor.DARK_AQUA + "Only team captains can do this.");
        }
    }
}