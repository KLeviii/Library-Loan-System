package main.ui;

import main.model.Book;
import main.service.*;

import static main.ui.AnsiColors.*;
import java.util.List;
import java.util.Scanner;

public class MenuUI {
    private Scanner scanner;
    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;

    public MenuUI(BookService bookService, MemberService memberService, LoanService loanService) {
        this.scanner = new Scanner(System.in);
        this.bookService = bookService;
        this.memberService = memberService;
        this.loanService = loanService;
    }

    public void start() {
        while (true) {
            displayMainMenu();
            int choice = readInt("Select option (1-7): ");

            switch (choice) {
                case 1: listBooksByCategory(); break;
                case 2: loanBook(); break;
                case 3: returnBook(); break;
                case 4: searchBooks(); break;
                case 5: listFines(); break;
                case 6: showStatistics(); break;
                case 7:
                    System.out.println(green("Goodbye!"));
                    return;
                default:
                    System.out.println("Invalid choice!");

            }
        }
    }

    private void displayMainMenu() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   LIBRARY MANAGEMENT SYSTEM          ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        System.out.println("1. List Books by Category");
        System.out.println("2. Loan a Book");
        System.out.println("3. Return a Book");
        System.out.println("4. Search Books");
        System.out.println("5. List All Fines");
        System.out.println("6. View Statistics");
        System.out.println("7. Exit\n");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        try {
            return Integer.parseInt(scanner.next().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void listBooksByCategory() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   SELECT CATEGORY                    ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        System.out.println("1. Sci-fi (" + bookService.getBooksByCategory("Sci-fi").size() + " books)");
        System.out.println("2. Drama (" + bookService.getBooksByCategory("Drama").size() + " books)");
        System.out.println("3. History (" + bookService.getBooksByCategory("History").size() + " books)");
        System.out.println("4. Children (" + bookService.getBooksByCategory("Children").size() + " books)");
        System.out.println("5. Technical (" + bookService.getBooksByCategory("Technical").size() + " books)");
        System.out.println("6. Back to main menu\n");
        int choice = readInt("Select category (1-6): ");

        switch (choice) {
            case 1:
                listScifiBooks(); break;
            case 2:
                listDramaBooks(); break;
            case 3:
                listHistoryBooks(); break;
            case 4:
                listChildBooks(); break;
            case 5:
                listTechBooks(); break;
            case 6:
                displayMainMenu();
        }
    }

    private void listScifiBooks() {
        clearScreen();
        List<Book> sciFiList = bookService.getBooksByCategory("Sci-fi");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   SCI-FI BOOKS                       ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        for (Book book : sciFiList) {
            // TODO kiiírni az adatokat
        }

    }

}
