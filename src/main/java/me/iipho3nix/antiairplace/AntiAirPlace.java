package me.iipho3nix.antiairplace;

import me.iipho3nix.datamanager.Data;
import me.iipho3nix.datamanager.DataManager;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
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
        if (!configFolder.exists()) {
            configFolder.mkdir();
            createConfig = true;
        }
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            createConfig = true;
        }

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
            List<Data> list = new ArrayList<>();
            list.add(new Data("0", UUID.randomUUID().toString()));
            list.add(new Data("1", UUID.randomUUID().toString()));
            list.add(new Data("2", UUID.randomUUID().toString()));
            list.add(new Data("3", UUID.randomUUID().toString()));
            dataManager.addData(new Data("Admins", list));
            dataManager.saveData();
        }
        dataManager.loadData();

        if(!warningsFile.exists()) {
            try {
                warningsFile.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        warningsDataManager = new DataManager(warningsFile);
        warningsDataManager.loadData();

        warningsManager = new WarningsManager(warningsDataManager);

        logger.info("AntiAirPlace has been initialized!");
    }
}
