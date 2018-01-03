package services;

import environment.AccessWindow;
import environment.TaskKiller;
import logger.Globals;

import java.io.IOException;

public class WindowWatcher {

    public WindowWatcher(String watchableWindow) {
        if (watchableWindow == Globals.lolGame){
            watchGame();
        }else{
            watchClient();
        }
    }

    private void watchClient() {
        boolean isWatchableWindowThere;
        AccessWindow accessWindow = new AccessWindow();
        isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolClient);
        while (isWatchableWindowThere){
            isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolClient);
        }
    }

    private void watchGame(){
        boolean isWatchableWindowThere;
        AccessWindow accessWindow = new AccessWindow();
        isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        while (isWatchableWindowThere){
            isWatchableWindowThere = accessWindow.checkIfRunning(Globals.lolGame);
        }
        try {
            TaskKiller.closeRunningClient(Globals.lolClient);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
