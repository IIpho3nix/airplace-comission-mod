package me.iipho3nix.antiairplace;

import eu.pb4.banhammer.api.BanHammer;
import eu.pb4.banhammer.api.PunishmentData;
import eu.pb4.banhammer.api.PunishmentType;
import static eu.pb4.banhammer.impl.BHUtils.parseDuration;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;

public class BanManager {
    public static void banPlayer(PlayerEntity player, String reason, String duration, boolean silent) {
        PunishmentData data = PunishmentData.create((ServerPlayerEntity) player, player.getServer().getCommandSource(), reason, parseDuration(duration), PunishmentType.BAN);
        BanHammer.punish(data, silent);
    }
}
