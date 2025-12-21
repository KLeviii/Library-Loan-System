package main.ui;

import main.exception.InvalidBookIdException;
import main.exception.InvalidMemberException;
import main.model.Book;
import main.model.Fine;
import main.model.Loan;
import main.model.Member;
import main.service.*;

import static main.ui.AnsiColors.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Scanner;

public class MenuUI {
    private Scanner scanner;
    private BookService bookService;
    private MemberService memberService;
    private LoanService loanService;
    private FileService fileService;

    public MenuUI(BookService bookService, MemberService memberService, LoanService loanService, FileService fileService) {
        this.scanner = new Scanner(System.in);
        this.bookService = bookService;
        this.memberService = memberService;
        this.loanService = loanService;
        this.fileService = fileService;
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
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                if (input.isEmpty()) {
                    return -1;
                }
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Invalid number. " + prompt);
            }
        }
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private boolean confirm(String prompt) {
        System.out.print(prompt);
        String input = scanner.nextLine().trim();
        return input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("I");
    }

    private void waitForEnter(String prompt) {
        System.out.print(prompt);
        scanner.nextLine();
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
            System.out.println("[" + book.getId() + "] " + book.getTitle() + " by " + book.getAuthor() + " - [" + getBookStatus(book) + "]\n");
        }
        waitForEnter("Press Enter to return to menu...");
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
        } else if (!memberService.canMemberLoan(memberIdInput)) {
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
                try {
                    Loan loan = loanService.loanBook(bookIdInput, memberIdInput);
                    System.out.println("\nBook successfully loaned!");
                    System.out.println("Due date: " + loan.getDueDate());
                } catch (InvalidBookIdException | InvalidMemberException e) {
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                System.out.println();
                System.err.println("Loan process terminated!\n");
            }
            waitForEnter("Press Enter to return to menu...");
        }
    }

    private void returnBook() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   RETURN A BOOK                      ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));
        String memberIdInput = readString("Enter Member ID: ");
        System.out.println();
        System.out.println("Active loans for " + memberService.findMemberById(memberIdInput).getName() + ":\n");

        if (!memberService.memberExists(memberIdInput)) {
            System.out.println();
            System.err.println("Invalid member ID!");
            returnBook();
        } else if (memberService.getMemberLoanCount(memberIdInput) == 0) {
            System.out.println();
            System.err.println("This member does not have any books loaned!");
            loanBook();
        } else {
            Member member = memberService.findMemberById(memberIdInput);
            try {
                System.out.println();
                int index = 0;
                for (Book loanedBook : memberService.getMemberLoanedBooks(memberIdInput, bookService)) {
                    Loan currentLoan = loanService.getMemberLoans(memberIdInput).get(index);
                    System.out.println("[" + loanedBook.getId() + "] " + loanedBook.getTitle());
                    System.out.println("         Loaned: " + currentLoan.getLoanDate() + " | Due: " + currentLoan.getDueDate());
                    if (currentLoan.isOverdue()) {
                        System.out.println("         ⚠ OVERDUE: " + currentLoan.getDaysOverdue() + " days | Fine: " + currentLoan.calculateFine() + " HUF\n");
                    } else {
                        System.out.println("         ✓ On time (" + ChronoUnit.DAYS.between(LocalDate.now(), currentLoan.getDueDate()) + " days remaining)\n");
                    }
                    index++;
                }
            } catch (InvalidMemberException e) {
                System.err.println("Error: " + e.getMessage());
            }

            String bookToReturn;
            do {
                bookToReturn = readString("Enter a valid Book ID to return: ");
            } while (!member.hasLoanedBook(bookToReturn));
            Fine fine = loanService.returnBook(bookToReturn, memberIdInput);
            System.out.println();
            if (fine != null) {
                System.out.println("⚠ FINE NOTICE");
                System.out.println("Book is " + ChronoUnit.DAYS.between(fine.getOverdueDate(), LocalDate.now()) + " days overdue.");
                System.out.println("Total fine: " + fine.getAmount() + " HUF (" + ChronoUnit.DAYS.between(fine.getOverdueDate(), LocalDate.now()) + " days × 50 HUF/day)");
            }
            System.out.println();
            if (confirm("Confirm return? (Y/N): ")) {
                System.out.println();
                System.out.println("✓ Book returned successfully.");
                if (fine != null) {
                    System.out.println("✓ Fine recorded: " + fine.getAmount() + " HUF");
                }
            } else {
                System.out.println();
                System.err.println("Return process terminated!\n");
            }
            waitForEnter("Press Enter to return to menu...");

        }
    }

    private void searchBooks() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   SEARCH BOOKS                       ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));

        System.out.println("Search by:");
        System.out.println("1. Book ID");
        System.out.println("2. Title");
        System.out.println("3. Author");
        System.out.println("4. Back to Main Menu\n");

        int choice = readInt("Select search type (1-4): ");
        System.out.println();

        switch (choice) {
            case 1:
                searchById();
                break;
            case 2:
                searchByTitle();
                break;
            case 3:
                searchByAuthor();
                break;
            case 4:
                return;
            default:
                System.out.println(red("Invalid choice!"));
                waitForEnter("Press Enter to try again...");
                searchBooks();
        }
    }

    private void searchById() {
        scanner.nextLine();
        String bookId = readString("Enter Book ID (e.g., SF-042): ");
        System.out.println();

        try {
            Book book = bookService.findBookById(bookId);
            displaySearchResult(List.of(book));
        } catch (InvalidBookIdException e) {
            System.out.println(red("Book not found: " + bookId));
            System.out.println();
            waitForEnter("Press Enter to return to menu...");
        }
    }

    private void searchByTitle() {
        scanner.nextLine();
        System.out.print("Enter title to search: ");
        String title = scanner.nextLine().trim();
        System.out.println();

        List<Book> results = bookService.searchByTitle(title);

        if (results.isEmpty()) {
            System.out.println(yellow("No books found with title containing: \"" + title + "\""));
        } else {
            displaySearchResult(results);
        }

        System.out.println();
        waitForEnter("Press Enter to return to menu...");
    }

    private void searchByAuthor() {
        scanner.nextLine();
        System.out.print("Enter author name to search: ");
        String author = scanner.nextLine().trim();
        System.out.println();

        List<Book> results = bookService.searchByAuthor(author);

        if (results.isEmpty()) {
            System.out.println(yellow("No books found by author: \"" + author + "\""));
        } else {
            displaySearchResult(results);
        }

        System.out.println();
        waitForEnter("Press Enter to return to menu...");
    }

    private void displaySearchResult(List<Book> books) {
        System.out.println(cyan("Search Results:"));
        System.out.println(cyan("─────────────────────────────────────────\n"));

        for (Book book : books) {
            String status;
            if (book.isAvailable()) {
                status = green("[AVAILABLE]");
            } else {
                try {
                    LocalDate returnDate = loanService.getExpectedReturnDate(book.getId());
                    status = yellow("[LOANED until " + returnDate + "]");
                } catch (InvalidBookIdException e) {
                    status = yellow("[LOANED]");
                }
            }

            System.out.println("[" + book.getId() + "] " + book.getTitle() + " by " + book.getAuthor());
            System.out.println("         Category: " + book.getCategory() + " | Status: " + status);
            System.out.println();
        }
    }

    private void listFines() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   ACTIVE FINES                       ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));

        List<Fine> allFines = loanService.getAllFines();

        if (allFines.isEmpty()) {
            System.out.println(green("No active fines! Everyone is on time! 🎉\n"));
        } else {
            System.out.printf("%-12s | %-20s | %-10s | %-10s | %-12s%n",
                    "Member ID", "Member Name", "Book ID", "Days Late", "Fine Amount");
            System.out.println("─────────────────────────────────────────────────────────────────────────");

            int totalFines = 0;


            allFines.sort((f1, f2) -> Integer.compare(f2.getAmount(), f1.getAmount()));

            for (Fine fine : allFines) {
                try {
                    Member member = memberService.findMemberById(fine.getMemberId());
                    long daysLate = ChronoUnit.DAYS.between(fine.getOverdueDate(), LocalDate.now());

                    String fineAmount = fine.getAmount() + " HUF";

                    // Piros színnel, ha nagy a büntetés (500+ HUF)
                    if (fine.getAmount() >= 500) {
                        System.out.printf("%-12s | %-20s | %-10s | %-10d | %s%n",
                                fine.getMemberId(),
                                truncate(member.getName(), 20),
                                fine.getBookId(),
                                daysLate,
                                red(fineAmount));
                    } else if (fine.getAmount() >= 250) {
                        System.out.printf("%-12s | %-20s | %-10s | %-10d | %s%n",
                                fine.getMemberId(),
                                truncate(member.getName(), 20),
                                fine.getBookId(),
                                daysLate,
                                yellow(fineAmount));
                    } else {
                        System.out.printf("%-12s | %-20s | %-10s | %-10d | %-12s%n",
                                fine.getMemberId(),
                                truncate(member.getName(), 20),
                                fine.getBookId(),
                                daysLate,
                                fineAmount);
                    }

                    totalFines += fine.getAmount();

                } catch (InvalidMemberException e) {
                    System.err.println("Warning: Member not found for fine: " + fine.getMemberId());
                }
            }

            System.out.println("─────────────────────────────────────────────────────────────────────────");
            System.out.printf("%67s | %s%n", "TOTAL", red(bold(totalFines + " HUF")));
            System.out.println();
        }

        waitForEnter("Press Enter to return to menu...");
    }

    private void showStatistics() {
        clearScreen();
        System.out.println(cyan("╔══════════════════════════════════════╗"));
        System.out.println(cyan("║   LIBRARY STATISTICS                 ║"));
        System.out.println(cyan("╚══════════════════════════════════════╝\n"));


        var popularCategory = loanService.getMostPopularCategory();
        if (popularCategory != null) {
            System.out.println("📊 " + bold("Most Popular Category:"));
            System.out.println("   " + green(popularCategory.getKey() + " (" + popularCategory.getValue() + " loans)"));
            System.out.println();
        } else {
            System.out.println("📊 " + bold("Most Popular Category:"));
            System.out.println("   " + yellow("No loans recorded yet"));
            System.out.println();
        }


        var lateMember = loanService.getMostFrequentlyLateMember();
        if (lateMember != null) {
            try {
                Member member = memberService.findMemberById(lateMember.getKey());
                System.out.println("⏰ " + bold("Most Frequently Late Member:"));
                System.out.println("   " + red(member.getName() + " (ID: " + member.getMemberId() + ")"));
                System.out.println("   " + lateMember.getValue() + " late returns");
                System.out.println();
            } catch (InvalidMemberException e) {
                System.out.println("⏰ " + bold("Most Frequently Late Member:"));
                System.out.println("   " + yellow("Data unavailable"));
                System.out.println();
            }
        } else {
            System.out.println("⏰ " + bold("Most Frequently Late Member:"));
            System.out.println("   " + green("No late returns! Everyone is on time! 🎉"));
            System.out.println();
        }

        System.out.println(cyan("━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"));
        System.out.println();
        System.out.println(bold("Additional Information:"));

        int totalBooks = bookService.getAllBooks().size();
        System.out.println("• Total books in inventory: " + totalBooks);

        int activeLoans = loanService.getAllLoans().size();
        System.out.println("• Currently loaned books: " + activeLoans);

        int totalMembers = memberService.getAllMembers().size();
        System.out.println("• Total registered members: " + totalMembers);

        int totalFines = loanService.getTotalFinesAmount();
        if (totalFines > 0) {
            System.out.println("• Total outstanding fines: " + red(totalFines + " HUF"));
        } else {
            System.out.println("• Total outstanding fines: " + green("0 HUF"));
        }

        System.out.println();
        System.out.println(bold("Loans by Category:"));
        String[] categories = {"Sci-fi", "Drama", "History", "Children", "Technical"};
        for (String category : categories) {
            int count = loanService.getCategoryLoanCount(category);
            System.out.printf("  %-12s: %d loans%n", category, count);
        }

        System.out.println();
        waitForEnter("Press Enter to return to menu...");
    }

    private String truncate(String text, int maxLength) {
        if (text.length() <= maxLength) {
            return text;
        }
        return text.substring(0, maxLength - 3) + "...";
    }

    private String bold(String text) {
        return BOLD + text + RESET;
    }

    private String getBookStatus(Book book) {
        if (book.isAvailable()) {
            return green("AVAILABLE");
        } else {
            try {
                LocalDate returnDate = loanService.getExpectedReturnDate(book.getId());
                return yellow("LOANED until " + returnDate);
            } catch (InvalidBookIdException e) {
                return yellow("LOANED");
            }
        }
    }
}
