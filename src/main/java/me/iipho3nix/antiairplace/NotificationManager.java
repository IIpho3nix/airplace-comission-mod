package me.iipho3nix.antiairplace;

import me.iipho3nix.datamanager.Data;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class NotificationManager {
    private static CopyOnWriteArrayList<BiObject> queue = new CopyOnWriteArrayList<>();

    public static void notifyAdmins(MinecraftServer server, String message) {
        List<Data> admins = (List<Data>) AntiAirPlace.dataManager.getData("Admins").getData();
        for (Data admin : admins) {
            PlayerEntity adminPlayer = server.getPlayerManager().getPlayer(UUID.fromString((String) admin.getData()));
            if (adminPlayer != null) {
                adminPlayer.sendMessage(Text.literal(message), false);
            }else{
                queue.add(new BiObject((String) admin.getData(), message));
            }
        }
    }
}
