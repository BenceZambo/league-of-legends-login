package services;

import environment.AccessWindow;
import environment.TaskKiller;
import logger.Globals;

import java.io.IOException;

public class WindowWatcher implements Runnable {

    private void watchGame(){
        System.out.println("ttttttttttttttttttt");
        boolean isWatchableWindowThere;
        AccessWindow accessWindow = new AccessWindow();
        isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        while (isWatchableWindowThere){
            try {
                Thread.sleep(15000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        }
        try {
            TaskKiller.closeRunningClient("LeagueClient.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        watchGame();
    }
}
