package me.iipho3nix.antiairplace;

import me.iipho3nix.datamanager.Data;
import me.iipho3nix.datamanager.DataManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class AntiAirPlace implements ModInitializer {
    Logger logger = LoggerFactory.getLogger("AntiAirPlace");
    File configFolder = new File("config");
    File configFile = new File(configFolder, "AntiAirPlace.json");
    File warningsFile = new File(configFolder, "AntiAirPlace-Warnings.json");

    public static DataManager dataManager;
    public static DataManager warningsDataManager;
    public static WarningsManager warningsManager;

    @Override
    public void onInitialize() {
        boolean createConfig = false;
        // try creating config folder
        try {
            if (configFolder.mkdir()) {
                logger.info("Created new folder '{}'", configFolder.getName());
                createConfig = true;
            }
        } catch (SecurityException e) {
            logger.error("Failed to create '{}', lacking filesystem permissions.", configFolder.getName());
        }
        // try creating config file
        try {
            if (configFile.createNewFile()) {
                logger.info("Created new file '{}'", configFile.getName());
                createConfig = true;
            }
        } catch (SecurityException e) {
            logger.error("Failed to create '{}', lacking filesystem permissions.", configFile.getName());
        } catch (IOException e) {
            logger.error("Error creating '{}': {}", configFile.getName(), e.getCause().toString());
        }

        // add data fields to config file on creation
        dataManager = new DataManager(configFile);
        if(createConfig) {
            dataManager.addData(new Data("Warnings-Enabled", true));
            dataManager.addData(new Data("Warning-Overlay", "§cAirplace Detected!"));
            dataManager.addData(new Data("Warning-Message", "§cPlease Disable Airplace as its not allowed in this server!"));
            dataManager.addData(new Data("Warning-Amount", 5));
            dataManager.addData(new Data("Detection-Command", "say test"));
            dataManager.addData(new Data("Ban-Message", "§cYou have been banned for using Airplace!"));
            dataManager.addData(new Data("Ban-Duration", "30m"));
            dataManager.addData(new Data("Silent-Ban", false));
            dataManager.addData(new Data("Notify-Admins", true));
            dataManager.addData(new Data("Admin-Message", "§c${player} has used Airplace!"));
            dataManager.addData(new Data("Notify-Admins-Of-Ban", true));
            dataManager.addData(new Data("Admin-Message-Of-Ban", "§c${player} has been banned for using Airplace!"));
            dataManager.addData(new Data(
                "Fluid-Place-Whitelist",
                List.of(
                    new Data("0", "minecraft:lily_pad"),
                    new Data("1", "minecraft:frogspawn"),
                    new Data("2", "wilderwild:small_lily_pad"),
                    new Data("3", "wilderwild:flowering_lily_pad"),
                    new Data("4", "wilderwild:water_lily")
                )
            ));
            dataManager.addData(new Data(
                "Admins",
                List.of(
                    new Data("0", UUID.randomUUID().toString()),
                    new Data("1", UUID.randomUUID().toString()),
                    new Data("2", UUID.randomUUID().toString()),
                    new Data("3", UUID.randomUUID().toString())
                )
            ));

            dataManager.saveData();
        }
        // load data into config file
        dataManager.loadData();

        // try creating warnings file
        try {
            if (warningsFile.createNewFile()) {
                logger.info("Created new file '{}'", warningsFile.getName());
            }
        } catch (SecurityException e) {
            logger.error("Failed to create '{}', lacking filesystem permissions.", warningsFile.getName());
        } catch (IOException e) {
            logger.error("Error creating '{}': {}", warningsFile.getName(), e.getCause().toString());
        }

        // load data into warnings file
        warningsDataManager = new DataManager(warningsFile);
        warningsDataManager.loadData();

        warningsManager = new WarningsManager(warningsDataManager);

        logger.info("AntiAirPlace has been initialized!");
    }
}
