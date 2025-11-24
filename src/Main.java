import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Display display = new Display();
        display.println("╔══════════════════════════════╗");
        display.println("║     LIBRARY LOAN SYSTEM      ║");
        display.println("╚══════════════════════════════╝");
        boolean run = true;
        Scanner sc = new Scanner(System.in);

        while (run) {
            try {
                display.println("┋");
                display.println("╔══════════════════════════════╗");
                display.println("║ OPTIONS:                     ║");
                display.println("║ 1. Loan a book               ║");
                display.println("║ 2. Give back a book          ║");
                display.println("║ 3. Statistics                ║");
                display.println("╚══════════════════════════════╝");
                display.print("┋ Action: ");
                int action = Integer.parseInt(sc.nextLine());

                switch (action) {
                    case 1:
                        display.println("╔══════════════════════════════╗");
                        display.println("║ BOOK LOANING                 ║");
                        display.println("╚══════════════════════════════╝");
                        display.println("┋");
                        display.println("╔══════════════════════════════╗");
                        display.println("║ PLEASE ENTER YOUR MEMBER ID  ║");
                        display.println("║ ENTER \"0\" to go back...        ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    case 2:
                        display.println("╔══════════════════════════════╗");
                        display.println("║ GIVE BACK A BOOK             ║");
                        display.println("╚══════════════════════════════╝");
                        display.println("┋");
                        display.println("╔══════════════════════════════╗");
                        display.println("║ PLEASE ENTER YOUR MEMBER ID  ║");
                        display.println("║ ENTER \"0\" to go back...        ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    case 3:
                        display.println("╔══════════════════════════════╗");
                        display.println("║ STATISTICS                   ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    default:

                        throw new InvalidNumberInputExceptionAtUserInterface("Invalid number, try  again with 1,2 or 3");
                }
            } catch (InvalidNumberInputExceptionAtUserInterface inieaui) {
                display.println("╔═══════════════════════════════════════════════════╗");
                display.println("║ ERROR: " + inieaui.getMessage() + "   ║");
                display.println("╚═══════════════════════════════════════════════════╝");
            } catch (NumberFormatException nfe) {
                display.println("╔═══════════════════════════════════════════════════╗");
                display.println("║ ERROR: " + nfe.getMessage() + "   ║");
                display.println("╚═══════════════════════════════════════════════════╝");
            }

            display.print("┋ Action: ");

        }
    }
}

