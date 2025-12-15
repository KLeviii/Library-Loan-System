/* AI:
java.nio.charset.StandardCharsets;
Atomic file writing
 */

package main.service;

import main.model.*;
import main.exception.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class FileService {
    private static final String DATA_DIR = "data/";

    public List<Book> loadBooks() throws FileOperationException {
        List<Book> books = new ArrayList<>();
        Path path = Paths.get(DATA_DIR + "loanableBooks.txt");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return books;
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 3) continue;

                String id = parts[0].trim();
                String title = parts[1].trim();
                String author = parts[2].trim();

                Book book = createBookByCategory(id, title, author);
                if (book != null) {
                    books.add(book);
                }
            }
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

    public List<Member> loadMembers() throws FileOperationException {
        List<Member> members = new ArrayList<>();
        Path path = Paths.get(DATA_DIR + "members.txt");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return members;
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 2) continue;

                String name = parts[0].trim();
                String id = parts[1].trim();

                Member member = createMember(name, id);
                members.add(member);
            }
        } catch (IOException e) {
            throw new FileOperationException("Error reading members: " + e.getMessage());
        }

        return members;
    }

    private Member createMember(String name, String id) {
        return new Member(name, id);
    }

    public List<Loan> loadLoans() throws FileOperationException {
        List<Loan> loans = new ArrayList<>();
        Path path = Paths.get(DATA_DIR + "loanedBooks.txt");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return loans;
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 3) continue;

                String bookId = parts[0].trim();
                String memberId = parts[1].trim();
                String loanDateStr = parts[2].trim();

                LocalDate loanDate;
                try {
                    loanDate = LocalDate.parse(loanDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Loan loan = createLoan(bookId, memberId, loanDate);
                    loans.add(loan);
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format: " + loanDateStr + " → line skipped");
                }
            }
        } catch (IOException e) {
            throw new FileOperationException("Error reading loans: " + e.getMessage());
        }

        return loans;
    }

    private Loan createLoan(String bookId, String memberId, LocalDate loanDate) {
        if (bookId.startsWith("SF-")) return new Loan(bookId, memberId, loanDate, 14);
        if (bookId.startsWith("DR-")) return new Loan(bookId, memberId, loanDate, 28);
        if (bookId.startsWith("HS-")) return new Loan(bookId, memberId, loanDate, 21);
        if (bookId.startsWith("CH-")) return new Loan(bookId, memberId, loanDate, 14);
        if (bookId.startsWith("TC-")) return new Loan(bookId, memberId, loanDate, 7);
        return null;
    }

    public List<Fine> loadFines() throws FileOperationException {
        List<Fine> fines = new ArrayList<>();
        Path path = Paths.get(DATA_DIR + "fines.txt");

        try {
            if (!Files.exists(path)) {
                Files.createFile(path);
                return fines;
            }

            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length != 4) continue;

                String memberId = parts[0].trim();
                String bookId = parts[1].trim();
                String overdueDateStr = parts[2].trim();
                int amount = Integer.parseInt(parts[3].trim());

                LocalDate overdueDate;
                try {
                    overdueDate = LocalDate.parse(overdueDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    Fine fine = createFine(memberId, bookId, overdueDate, amount);
                    fines.add(fine);
                } catch (DateTimeParseException e) {
                    System.err.println("Invalid date format: " + overdueDateStr + " → line skipped");
                }
            }
        } catch (IOException e) {
            throw new FileOperationException("Error reading fines: " + e.getMessage());
        }

        return fines;
    }

    private Fine createFine(String memberId, String bookId, LocalDate overdueDate, int amount) {
        return new Fine(memberId, bookId, overdueDate, amount);
    }

    public void saveBooks(List<Book> books) throws FileOperationException {
        Path path = Paths.get(DATA_DIR + "loanableBooks.txt");
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");

        List<String> lines = new ArrayList<>();

        for (Book book : books) {
            String line = book.getId() + ";" +
                    book.getTitle() + ";" +
                    book.getAuthor();
            lines.add(line);
        }

        try {
            Files.createDirectories(path.getParent());

            Files.write(tempPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
            }
            throw new FileOperationException("Error saving books: " + e.getMessage());
        }
    }

    public void saveMembers(List<Member> members) throws FileOperationException {
        Path path = Paths.get(DATA_DIR + "members.txt");
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");

        List<String> lines = new ArrayList<>();

        for (Member member : members) {
            String line = member.getName() + ";" +
                    member.getMemberId();
            lines.add(line);
        }

        try {
            Files.createDirectories(path.getParent());

            Files.write(tempPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
            }
            throw new FileOperationException("Error saving members: " + e.getMessage());
        }
    }

    public void saveLoans(List<Loan> loans) throws FileOperationException {
        Path path = Paths.get(DATA_DIR + "loanedBooks.txt");
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");

        List<String> lines = new ArrayList<>();

        for (Loan loan : loans) {
            String line = loan.getBookId() + ";" +
                    loan.getMemberId() + ";" +
                    loan.getLoanDate();
            lines.add(line);
        }

        try {
            Files.createDirectories(path.getParent());

            Files.write(tempPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
            }
            throw new FileOperationException("Error saving loans: " + e.getMessage());
        }
    }

    public void saveFines(List<Fine> fines) throws FileOperationException {
        Path path = Paths.get(DATA_DIR + "fines.txt");
        Path tempPath = path.resolveSibling(path.getFileName() + ".tmp");

        List<String> lines = new ArrayList<>();

        for (Fine fine : fines) {
            String line = fine.getMemberId() + ";" +
                    fine.getBookId() + ";" +
                    fine.getMemberId() + ";" +
                    fine.getOverdueDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) + ";" +
                    fine.getAmount();
            lines.add(line);
        }

        try {
            Files.createDirectories(path.getParent());

            Files.write(tempPath, lines, StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);

            Files.move(tempPath, path, StandardCopyOption.REPLACE_EXISTING,
                    StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            try {
                Files.deleteIfExists(tempPath);
            } catch (IOException ignored) {
            }
            throw new FileOperationException("Error saving fines: " + e.getMessage());
        }
    }
}