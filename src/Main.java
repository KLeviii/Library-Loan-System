import java.util.Scanner;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class Main {
    public static void main(String[] args) {

        String RESET = "\u001B[0m";
        String RED = "\u001B[31m";
        String YELLOW = "\u001B[33m";
        String CYAN = "\u001B[36m";
        String BLUE = "\u001B[34m";
        String GREEN = "\u001B[32m";

        Display display = new Display();
        display.println("╔══════════════════════════════╗");
        display.println("║"+YELLOW+"LIBRARY LOAN SYSTEM"+RESET+"           ║");
        display.println("╚══════════════════════════════╝");
        boolean run = true;
        Scanner sc = new Scanner(System.in);

        while (run) {
            try {
                display.println("┋");
                display.println("╔══════════════════════════════╗");
                display.println("║"+YELLOW+"OPTIONS:"+RESET+"                      ║");
                display.println("║"+GREEN+ "1. Loan a book"+RESET+"                ║");
                display.println("║"+BLUE+ "2. Give back a book"+RESET+"           ║");
                display.println("║"+CYAN+ "3. Statistics"+RESET+"                 ║");
                display.println("║"+RED+ "0. Exit"+RESET+"                       ║");
                display.println("╚══════════════════════════════╝");
                display.print("┋ Action: ");
                int action = Integer.parseInt(sc.nextLine());
                switch (action) {
                    case 0:
                        run = false;
                        break;
                    case 1:
                        display.println("╔══════════════════════════════╗");
                        display.println("║"+YELLOW+"BOOK LOANING"+RESET+"                  ║");
                        display.println("╚══════════════════════════════╝");
                        display.println("┋");
                        display.println("╔══════════════════════════════╗");
                        display.println("║"+YELLOW+" PLEASE ENTER YOUR MEMBER ID"+RESET+  "  ║");
                        display.println("║"+YELLOW+" ENTER \"0\" to go back..."+RESET+"      ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    case 2:
                        display.println("╔══════════════════════════════╗");
                        display.println("║"+YELLOW+"GIVE BACK A BOOK"+RESET+"              ║");
                        display.println("╚══════════════════════════════╝");
                        display.println("┋");
                        display.println("╔══════════════════════════════╗");
                        display.println("║"+YELLOW+" PLEASE ENTER YOUR MEMBER ID"+RESET+  "  ║");
                        display.println("║"+YELLOW+" ENTER \"0\" to go back..."+RESET+"      ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    case 3:
                        display.println("╔══════════════════════════════╗");
                        display.println("║"+YELLOW+"STATISTICS"+RESET+"                  ║");
                        display.println("╚══════════════════════════════╝");
                        break;
                    default:
                        throw new InvalidNumberInputExceptionAtUserInterface("Invalid number, try  again with 1,2 or 3");
                }
            } catch (InvalidNumberInputExceptionAtUserInterface inieaui) {
                display.println("╔═══════════════════════════════════════════════════╗");
                display.println("║"+RED+"ERROR: "+inieaui.getMessage()+RESET+"    ║");
                display.println("╚═══════════════════════════════════════════════════╝");
            } catch (NumberFormatException nfe) {
                display.println(RED+"ERROR: " + nfe.getMessage() +RESET);
            }

            display.print("┋ Action: ");



        }
    }
}

