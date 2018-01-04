package services;

import environment.AccessWindow;
import environment.TaskKiller;
import logger.Globals;

import java.io.IOException;

public class WindowWatcher implements Runnable {

    private void watchGame(){
        boolean isWatchableWindowThere;
        AccessWindow accessWindow = new AccessWindow();
        isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        while (isWatchableWindowThere){
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        }
        try {
            TaskKiller.closeRunningClient(Globals.lolClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        watchGame();
    }
}
