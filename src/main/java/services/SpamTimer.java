package services;

import logger.Globals;

public class SpamTimer implements Runnable{
    @Override
    public void run() {
        try {
            Thread.sleep(30000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Globals.spamDefender = false;
    }
}
