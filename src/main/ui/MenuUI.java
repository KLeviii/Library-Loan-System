package main.ui;

import main.model.Book;
import main.model.Member;
import main.service.*;

import static main.ui.AnsiColors.*;

import java.time.LocalDate;
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
                case 1:
                    listBooksByCategory();
                    break;
                case 2:
                    loanBook();
                    break;
                case 3:
                    returnBook();
                    break;
                case 4:
                    searchBooks();
                    break;
                case 5:
                    listFines();
                    break;
                case 6:
                    showStatistics();
                    break;
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

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.next().trim();
    }

    private boolean confirm(String prompt) {
        System.out.println(prompt);
        return scanner.next().equalsIgnoreCase("Y");
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
                listScifiBooks();
                break;
            case 2:
                listDramaBooks();
                break;
            case 3:
                listHistoryBooks();
                break;
            case 4:
                listChildBooks();
                break;
            case 5:
                listTechBooks();
                break;
            case 6:
                displayMainMenu();
        }
    }

    private void iterBooks(List<Book> bookList) {
        for (Book book : bookList) {
            System.out.println("[" + book.getId() + "] " + book.getTitle() + " by " + book.getAuthor() + " - [" + (book.isAvailable() ? "AVAILABLE" : "LOANED until ") + loanService.getExpectedReturnDate(book.getId()) + "]\n");
        }
        readInt("Press Enter to return to menu...");
        displayMainMenu();
    }

    private void listScifiBooks() {
        clearScreen();
        List<Book> sciFiList = bookService.getBooksByCategory("Sci-fi");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   SCI-FI BOOKS                       ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        iterBooks(sciFiList);
    }

    private void listDramaBooks() {
        clearScreen();
        List<Book> dramaList = bookService.getBooksByCategory("Drama");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   DRAMA BOOKS                        ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        iterBooks(dramaList);
    }

    private void listHistoryBooks() {
        clearScreen();
        List<Book> historyList = bookService.getBooksByCategory("History");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   HISTORY BOOKS                      ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        iterBooks(historyList);
    }

    private void listChildBooks() {
        clearScreen();
        List<Book> childList = bookService.getBooksByCategory("Children");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   HISTORY BOOKS                      ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        iterBooks(childList);
    }

    private void listTechBooks() {
        clearScreen();
        List<Book> techList = bookService.getBooksByCategory("Technical");
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   TECHNICAL BOOKS                    ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        iterBooks(techList);
    }

    private void loanBook() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   LOAN A BOOK                        ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        String bookIdInput = readString("Enter Book ID: ");
        String memberIdInput = readString("Enter Member ID: ");

        if (!bookService.bookExists(bookIdInput) || !memberService.memberExists(memberIdInput)) {
            System.out.println();
            System.err.println("Invalid book or member ID!");
            loanBook();
        } else if (memberService.canMemberLoan(memberIdInput)) {
            System.out.println();
            System.err.println("This member may not loan any more books!");
            loanBook();
        } else {
            Book book = bookService.findBookById(bookIdInput);
            Member member = memberService.findMemberById(memberIdInput);
            System.out.println();
            System.out.println("✓ Book: " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("✓ Member: " + member.getName() + "(ID: " + member.getMemberId() + ")");
            System.out.println("✓ Current loans: " + member.getLoanedBooks() + "/3\n");

            LocalDate localDate = LocalDate.now();
            System.out.println("Loan Date: " + localDate);
            System.out.println("Due Date: " + localDate.plusDays(book.getLoanDuration()) + " (" + book.getLoanDuration() + " days)\n");
            if (confirm("Confirm loan? (Y/N): ")) {
                loanService.loanBook(bookIdInput, memberIdInput);
                System.out.println();
                System.out.println("Book successfully loaned!\n");
            } else {
                System.out.println();
                System.err.println("Loan process terminated!\n");
            }
            readInt("Press Enter to return to menu...");
            displayMainMenu();
        }
    }

    // TODO: Implement 4 methods
}
