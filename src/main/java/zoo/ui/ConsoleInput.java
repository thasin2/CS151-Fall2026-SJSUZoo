package zoo.ui;

import java.util.Scanner;

/* The only place the program reads user input
    every read checks for EXIT (any upper or lower case) and ends the program if it is typed
    numbers and yes/no answers are checked, and the user is asked again until the input is valid
 */
public final class ConsoleInput {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleInput() {
        // only static methods, so no objects are needed
    }

    // reads one line, trimmed
    public static String readLine(String prompt) {
        System.out.print(prompt);
        if (!SCANNER.hasNextLine()) {
            throw new ExitRequestedException(); // input ended, for example with Ctrl+D
        }
        String line = SCANNER.nextLine().trim();
        if (line.equalsIgnoreCase("EXIT")) {
            throw new ExitRequestedException();
        }
        return line;
    }

    public static String readNonEmptyLine(String prompt) {
        while (true) {
            String line = readLine(prompt);
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Please type something.");
        }
    }

    // reads a whole number from min to max
    public static int readInt(String prompt, int min, int max) {
        while (true) {
            String line = readLine(prompt);
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
            } catch (NumberFormatException e) {
                // not a number, so fall through and ask again
            }
            System.out.println("Please enter a whole number from " + min + " to " + max + ".");
        }
    }

    // reads y, yes, n, or no
    public static boolean readYesNo(String prompt) {
        while (true) {
            String line = readLine(prompt).toLowerCase();
            if (line.equals("y") || line.equals("yes")) {
                return true;
            }
            if (line.equals("n") || line.equals("no")) {
                return false;
            }
            System.out.println("Please enter y or n.");
        }
    }
}
