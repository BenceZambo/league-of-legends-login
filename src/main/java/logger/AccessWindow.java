package logger;

import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.ptr.PointerByReference;

import java.util.concurrent.TimeUnit;

public class AccessWindow {
    private static final int MAX_TITLE_LENGTH = 1024;


    public AccessWindow() {
    }

    public String getActiveWindowTitle()
    {
        char[] buffer = new char[MAX_TITLE_LENGTH * 2];
        HWND foregroundWindow = User32DLL.GetForegroundWindow();
        User32DLL.GetWindowTextW(foregroundWindow, buffer, MAX_TITLE_LENGTH);
        return Native.toString(buffer);
    }

    public boolean checkIfRunning(String process) {
        HWND hwnd = User32.INSTANCE.FindWindow
                (null, process);
        return hwnd != null;
    }


    static class User32DLL
    {
        static
        {
            Native.register("user32");
        }

        public static native int GetWindowThreadProcessId(HWND hWnd, PointerByReference pref);
        public static native HWND GetForegroundWindow();
        public static native int GetWindowTextW(HWND hWnd, char[] lpString, int nMaxCount);
    }
}