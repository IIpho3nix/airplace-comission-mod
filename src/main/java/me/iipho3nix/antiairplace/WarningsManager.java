package me.iipho3nix.antiairplace;

import me.iipho3nix.datamanager.Data;
import me.iipho3nix.datamanager.DataManager;

import java.util.ArrayList;
import java.util.List;

public class WarningsManager {
    DataManager warningsDataManager;
    List<Data> warnings;

    public WarningsManager(DataManager dataManager) {
        this.warningsDataManager = dataManager;
        Data temp = this.warningsDataManager.getData("Warnings");
        if (temp == null) {
            warnings = new ArrayList<>();
            this.warningsDataManager.addData(new Data("Warnings", warnings));
            this.warningsDataManager.saveData();
        }

        this.warnings = (List<Data>) this.warningsDataManager.getData("Warnings").getData();
    }

    public List<Data> getWarnings() {
        return this.warnings;
    }

    public int getPlayerWarnings(String playerUUID) {
        for (Data data : this.warnings) {
            if (data.getKey().equals(playerUUID)) {
                return ((Number) data.getData()).intValue();
            }
        }

        this.warnings.add(new Data(playerUUID, 0));
        this.warningsDataManager.getData("Warnings").setData(this.warnings);
        this.warningsDataManager.saveData();

        return 0;
    }

    public void removeWarning(String playerUUID) {
        for (Data data : this.warnings) {
            if (data.getKey().equals(playerUUID)) {
                this.warnings.remove(data);
                this.warningsDataManager.getData("Warnings").setData(this.warnings);
                this.warningsDataManager.saveData();
                break;
            }
        }
    }

    public void addWarning(String playerUUID) {
        for (Data data : this.warnings) {
            if (data.getKey().equals(playerUUID)) {
                data.setData(((Number) data.getData()).intValue() + 1);
                this.warningsDataManager.getData("Warnings").setData(this.warnings);
                this.warningsDataManager.saveData();
                break;
            }
        }
    }

    public void resetWarnings(String playerUUID) {
        for (Data data : this.warnings) {
            if (data.getKey().equals(playerUUID)) {
                data.setData(0);
                this.warningsDataManager.getData("Warnings").setData(this.warnings);
                this.warningsDataManager.saveData();
                break;
            }
        }
    }

    public void resetAllWarnings() {
        this.warnings.clear();
        this.warningsDataManager.getData("Warnings").setData(this.warnings);
        this.warningsDataManager.saveData();
    }
}
