package me.iipho3nix.antiairplace;

public class ShutdownThread extends Thread {
    @Override
    public void run() {
        AntiAirPlace.dataManager.saveData();
        AntiAirPlace.warningsDataManager.saveData();
    }
}