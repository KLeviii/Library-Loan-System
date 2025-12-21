package main.service;

import main.model.*;
import main.exception.*;
import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;

public class FileService {
    private static final String DATA_DIR = "data/";
    private static final String LOANABLE_BOOKS_FILE = DATA_DIR + "loanableBooks.txt";
    private static final String MEMBERS_FILE = DATA_DIR + "members.txt";
    private static final String LOANED_BOOKS_FILE = DATA_DIR + "loanedBooks.txt";
    private static final String FINES_FILE = DATA_DIR + "fines.txt";

    public FileService() throws FileOperationException {
        try {
            Path dataPath = Paths.get(DATA_DIR);
            if (!Files.exists(dataPath)) {
                Files.createDirectories(dataPath);
            }
        } catch (IOException e) {
            throw new FileOperationException("Failed to create data directory: " + e.getMessage());
        }
    }

    public void saveAll(List<Book> books, List<Member> members, List<Loan> loans, List<Fine> fines)
            throws FileOperationException {
        try {
            saveBooks(books);
            saveMembers(members);
            saveLoans(loans);
            saveFines(fines);
            System.out.println("All data saved successfully.");
        } catch (FileOperationException e) {
            System.err.println("Error saving data: " + e.getMessage());
            throw e;
        }
    }

    public void saveBooks(List<Book> books) throws FileOperationException {
        try {
            List<String> lines = new ArrayList<>();

            for (Book book : books) {
                String line = String.join(";",
                        book.getId(),
                        book.getTitle(),
                        book.getAuthor()
                );
                lines.add(line);
            }

            atomicWrite(LOANABLE_BOOKS_FILE, lines);

        } catch (IOException e) {
            throw new FileOperationException("Error saving books: " + e.getMessage());
        }
    }

    public void saveMembers(List<Member> members) throws FileOperationException {
        try {
            List<String> lines = new ArrayList<>();

            for (Member member : members) {
                String line = String.join(";",
                        member.getMemberId(),
                        member.getName()
                );
                lines.add(line);
            }

            atomicWrite(MEMBERS_FILE, lines);

        } catch (IOException e) {
            throw new FileOperationException("Error saving members: " + e.getMessage());
        }
    }

    public void saveLoans(List<Loan> loans) throws FileOperationException {
        try {
            List<String> lines = new ArrayList<>();

            for (Loan loan : loans) {
                String line = String.join(";",
                        loan.getBookId(),
                        loan.getMemberId(),
                        loan.getLoanDate().toString() // ISO-8601 formátum (YYYY-MM-DD)
                );
                lines.add(line);
            }

            atomicWrite(LOANED_BOOKS_FILE, lines);

        } catch (IOException e) {
            throw new FileOperationException("Error saving loans: " + e.getMessage());
        }
    }

    public void saveFines(List<Fine> fines) throws FileOperationException {
        try {
            List<String> lines = new ArrayList<>();

            for (Fine fine : fines) {
                String line = String.join(";",
                        fine.getMemberId(),
                        fine.getBookId(),
                        fine.getOverdueDate().toString(),
                        String.valueOf(fine.getAmount())
                );
                lines.add(line);
            }

            atomicWrite(FINES_FILE, lines);

        } catch (IOException e) {
            throw new FileOperationException("Error saving fines: " + e.getMessage());
        }
    }

    private void atomicWrite(String filePath, List<String> lines) throws IOException {
        Path path = Paths.get(filePath);
        Path tempPath = Paths.get(filePath + ".tmp");

        Files.write(tempPath, lines, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING);

        // Átnevezés (atomi művelet)
        Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING,
                StandardCopyOption.ATOMIC_MOVE);
    }

    public List<Book> loadBooks() throws FileOperationException {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get(LOANABLE_BOOKS_FILE);

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return books;
            }

            List<String> lines = Files.readAllLines(path);
            int lineNumber = 0;

            for (String line : lines) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 3) {
                    System.err.println("Warning: Invalid format in loanableBooks.txt at line " + lineNumber);
                    continue;
                }

                String id = parts[0].trim();
                String title = parts[1].trim();
                String author = parts[2].trim();

                Book book = createBookByCategory(id, title, author);
                if (book != null) {
                    books.add(book);
                } else {
                    System.err.println("Warning: Unknown category for book ID " + id);
                }
            }

            System.out.println("Loaded " + books.size() + " books.");

        } catch (IOException e) {
            throw new FileOperationException("Error reading books: " + e.getMessage());
        }

        return books;
    }

    private Book createBookByCategory(String id, String title, String author) {
        if (id.startsWith("SF-")) return new SciFi(id, title, author);
        if (id.startsWith("DR-")) return new Drama(id, title, author);
        if (id.startsWith("HS-")) return new History(id, title, author);
        if (id.startsWith("CH-")) return new Children(id, title, author);
        if (id.startsWith("TC-")) return new Technical(id, title, author);
        return null;
    }

    /**
     * Tagok betöltése
     */
    public List<Member> loadMembers() throws FileOperationException {
        List<Member> members = new ArrayList<>();
        Path path = Paths.get(MEMBERS_FILE);

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return members;
            }

            List<String> lines = Files.readAllLines(path);
            int lineNumber = 0;

            for (String line : lines) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 2) {
                    System.err.println("Warning: Invalid format in members.txt at line " + lineNumber);
                    continue;
                }

                String memberId = parts[0].trim();
                String name = parts[1].trim();

                Member member = new Member(memberId, name);
                members.add(member);
            }

            System.out.println("Loaded " + members.size() + " members.");

        } catch (IOException e) {
            throw new FileOperationException("Error reading members: " + e.getMessage());
        }

        return members;
    }

    /**
     * Kölcsönzések betöltése
     */
    public List<Loan> loadLoans() throws FileOperationException {
        List<Loan> loans = new ArrayList<>();
        Path path = Paths.get(LOANED_BOOKS_FILE);

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return loans;
            }

            List<String> lines = Files.readAllLines(path);
            int lineNumber = 0;

            for (String line : lines) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 3) {
                    System.err.println("Warning: Invalid format in loanedBooks.txt at line " + lineNumber);
                    continue;
                }

                String bookId = parts[0].trim();
                String memberId = parts[1].trim();
                LocalDate loanDate = LocalDate.parse(parts[2].trim());

                // Loan létrehozásához szükség van a kölcsönzési időtartamra
                // Ezt a kategóriából kell kiolvasni, ezért később frissítjük
                // Egyelőre alapértelmezett 14 nappal hozzuk létre
                Loan loan = new Loan(bookId, memberId, loanDate, 14);
                loans.add(loan);
            }

            System.out.println("Loaded " + loans.size() + " active loans.");

        } catch (IOException e) {
            throw new FileOperationException("Error reading loans: " + e.getMessage());
        }

        return loans;
    }

    /**
     * Büntetések betöltése
     */
    public List<Fine> loadFines() throws FileOperationException {
        List<Fine> fines = new ArrayList<>();
        Path path = Paths.get(FINES_FILE);

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return fines;
            }

            List<String> lines = Files.readAllLines(path);
            int lineNumber = 0;

            for (String line : lines) {
                lineNumber++;
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 4) {
                    System.err.println("Warning: Invalid format in fines.txt at line " + lineNumber);
                    continue;
                }

                String memberId = parts[0].trim();
                String bookId = parts[1].trim();
                LocalDate overdueDate = LocalDate.parse(parts[2].trim());
                int amount = Integer.parseInt(parts[3].trim());

                Fine fine = new Fine(memberId, bookId, overdueDate, amount);
                fines.add(fine);
            }

            System.out.println("Loaded " + fines.size() + " active fines.");

        } catch (IOException e) {
            throw new FileOperationException("Error reading fines: " + e.getMessage());
        }

        return fines;
    }

    /**
     * Kölcsönzési időtartamok javítása a könyvek alapján
     * Ezt a Main-ben kell hívni a betöltés után
     */
    public void fixLoanDurations(List<Loan> loans, List<Book> books) {
        for (Loan loan : loans) {
            for (Book book : books) {
                if (book.getId().equals(loan.getBookId())) {
                    // Újraszámítjuk a határidőt a helyes időtartammal
                    int correctDuration = book.getLoanDuration();
                    loan.recalculateDueDate(correctDuration);
                    break;
                }
            }
        }
    }
}