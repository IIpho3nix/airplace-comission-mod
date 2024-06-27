package me.iipho3nix.antiairplace.mixin;

import me.iipho3nix.antiairplace.AntiAirPlace;
import me.iipho3nix.antiairplace.BanManager;
import me.iipho3nix.antiairplace.NotificationManager;
import net.minecraft.block.BlockState;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BlockItem.class)
public abstract class BlockPlaceMixin {
    @Shadow public abstract boolean canBeNested();

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;Lnet/minecraft/block/BlockState;)Z", at = @At("HEAD"), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void placeBlock(ItemPlacementContext context, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        boolean cancelled = true;
        BlockPos pos = context.getBlockPos();
        World world = context.getWorld();
        BlockState up = world.getBlockState(pos.up());
        BlockState down = world.getBlockState(pos.down());
        BlockState north = world.getBlockState(pos.north());
        BlockState south = world.getBlockState(pos.south());
        BlockState west = world.getBlockState(pos.west());
        BlockState east = world.getBlockState(pos.east());

        if (!up.isAir() || !down.isAir() || !north.isAir() || !south.isAir() || !east.isAir() || !west.isAir()) {
            cancelled = false;
        }

        if (cancelled) {
            cir.setReturnValue(false);
            context.getPlayer().getServer().getCommandManager().executeWithPrefix(context.getPlayer().getCommandSource(), (String) AntiAirPlace.dataManager.getData("Detection-Command").getData());
            if ((Boolean) AntiAirPlace.dataManager.getData("Warnings-Enabled").getData()) {
                int warnings = AntiAirPlace.warningsManager.getPlayerWarnings(context.getPlayer().getUuid().toString());
                int maxWarnings = ((Number) AntiAirPlace.dataManager.getData("Warning-Amount").getData()).intValue();
                context.getPlayer().sendMessage(Text.literal((String) AntiAirPlace.dataManager.getData("Warning-Overlay").getData()), true);
                context.getPlayer().sendMessage(Text.literal((String) AntiAirPlace.dataManager.getData("Warning-Message").getData()), false);
                if (((Boolean) AntiAirPlace.dataManager.getData("Notify-Admins").getData())) {
                    NotificationManager.notifyAdmins(context.getPlayer().getServer(), ((String) AntiAirPlace.dataManager.getData("Admin-Message").getData()).replaceAll("\\$\\{player\\}", context.getPlayer().getName().getString()));
                }

                if (warnings == (maxWarnings - 1)) {
                    AntiAirPlace.warningsManager.resetWarnings(context.getPlayer().getUuid().toString());
                    String message = (String) AntiAirPlace.dataManager.getData("Ban-Message").getData();
                    String duration = (String) AntiAirPlace.dataManager.getData("Ban-Duration").getData();
                    boolean silent = (boolean) AntiAirPlace.dataManager.getData("Silent-Ban").getData();
                    BanManager.banPlayer(context.getPlayer(), message, duration, silent);
                    if (((Boolean) AntiAirPlace.dataManager.getData("Notify-Admins-Of-Ban").getData())) {
                        NotificationManager.notifyAdmins(context.getPlayer().getServer(), ((String) AntiAirPlace.dataManager.getData("Admin-Message-Of-Ban").getData()).replaceAll("\\$\\{player\\}", context.getPlayer().getName().getString()));
                    }
                }
                AntiAirPlace.warningsManager.addWarning(context.getPlayer().getUuid().toString());
            }else{
                AntiAirPlace.warningsManager.resetWarnings(context.getPlayer().getUuid().toString());
                String message = (String) AntiAirPlace.dataManager.getData("Ban-Message").getData();
                String duration = (String) AntiAirPlace.dataManager.getData("Ban-Duration").getData();
                boolean silent = (boolean) AntiAirPlace.dataManager.getData("Silent-Ban").getData();
                BanManager.banPlayer(context.getPlayer(), message, duration, silent);
                if (((Boolean) AntiAirPlace.dataManager.getData("Notify-Admins-Of-Ban").getData())) {
                    NotificationManager.notifyAdmins(context.getPlayer().getServer(), ((String) AntiAirPlace.dataManager.getData("Admin-Message-Of-Ban").getData()).replaceAll("\\$\\{player\\}", context.getPlayer().getName().getString()));
                }
            }
        }
    }
}
