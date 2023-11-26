package asia.ceroxe.os.detect;

import asia.ceroxe.os.windowsSystem.WindowsOperation;

public class OSDetector {
    private OSDetector() {
    }

    public static boolean isLinux() {
        String s = System.getProperty("os.name");
        return s.toLowerCase().contains("linux");
    }

    public static boolean isWindows() {
        String s = System.getProperty("os.name");
        return s.toLowerCase().contains("windows");
    }

    public static boolean isMac() {
        return !isLinux() && !isWindows();
    }

    public static int getWindowsVersion() {
        if (OSDetector.isWindows()) {
            String s = WindowsOperation.runGetString("cmd /c ver");
            return Integer.parseInt(s.split("\\.")[2]);
        } else {
            return -1;
        }
    }
}
