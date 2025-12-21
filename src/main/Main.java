package main;

import main.service.*;
import main.ui.*;
import main.exception.*;
import main.model.*;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("Loading library data...\n");

            // 1. FileService inicializálás
            FileService fileService = new FileService();

            // 2. Adatok betöltése fájlokból
            List<Book> books = fileService.loadBooks();
            List<Member> members = fileService.loadMembers();
            List<Loan> loans = fileService.loadLoans();
            List<Fine> fines = fileService.loadFines();

            // 3. Kölcsönzési időtartamok javítása
            fileService.fixLoanDurations(loans, books);

            // 4. Service-k inicializálása
            BookService bookService = new BookService(books);
            MemberService memberService = new MemberService(members);
            LoanService loanService = new LoanService(loans, fines, bookService, memberService);

            System.out.println("Library system initialized successfully!\n");

            // 5. UI indítása
            MenuUI menu = new MenuUI(bookService, memberService, loanService, fileService);
            menu.start();

            // 6. Kilépéskor mentés
            System.out.println("\nSaving data...");
            fileService.saveAll(books, members, loans, fines);
            System.out.println("Data saved. Goodbye!");

        } catch (FileOperationException e) {
            System.err.println("Fatal error: " + e.getMessage());
            System.err.println("The program will now exit.");
            System.exit(1);
        }
    }
}
