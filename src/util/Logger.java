package util;

public class Logger {

    public static enum MessageType { DEBUG, WARN, ERROR };
    public static boolean displayMessage = false;
    
    public static void log(MessageType logType, String message) {
        if (displayMessage) {
            System.out.println("[" + logType + "]: " + message);
        }
    }
    
    public static void newline() {
        if (displayMessage) {
            System.out.println();
        }
    }
}
