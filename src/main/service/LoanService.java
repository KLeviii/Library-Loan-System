/**
 * AI:
 * stream filterek megírása
 */

package main.service;

import main.model.*;
import main.exception.*;
import java.time.LocalDate;
import java.util.*;

public class LoanService {
    private List<Loan> loans;
    private List<Fine> fines;
    private BookService bookService;
    private MemberService memberService;

    private static final int FINE_PER_DAY = 50;

    public LoanService(List<Loan> loans, List<Fine> fines,
                       BookService bookService, MemberService memberService) {
        this.loans = loans;
        this.fines = fines;
        this.bookService = bookService;
        this.memberService = memberService;
        synchronizeState();
    }

    private void synchronizeState() {
        for (Book book : bookService.getAllBooks()) {
            book.setLoanedTo(null);
        }

        for (Member member : memberService.getAllMembers()) {
            member.resetLoanState();
        }

        for (Loan loan : loans) {
            try {
                Book book = bookService.findBookById(loan.getBookId());
                book.setLoanedTo(loan.getMemberId());

                Member member = memberService.findMemberById(loan.getMemberId());
                member.addLoanedBook(loan.getBookId());
                member.incrementLoans();
            } catch (InvalidBookIdException | InvalidMemberException e) {
                System.err.println("Warning: " + e.getMessage());
            }
        }
    }

    public Loan loanBook(String bookId, String memberId) throws InvalidBookIdException, InvalidMemberException {
        Book book = bookService.findBookById(bookId);

        if (!book.isAvailable()) {
            Loan existingLoan = findLoanByBookId(bookId);
            LocalDate dueDate = existingLoan.getDueDate();
            throw new InvalidBookIdException("Book is already loaned. Expected return: " + dueDate);
        }

        Member member = memberService.findMemberById(memberId);

        if (!member.canLoanMore()) {
            throw new InvalidMemberException("Member '" + memberId + "' has reached the maximum loan limit (3 books).");
        }

        LocalDate loanDate = LocalDate.now();
        int loanDuration = book.getLoanDuration();

        Loan loan = new Loan(bookId, memberId, loanDate, loanDuration);

        book.setLoanedTo(memberId);
        memberService.addBookToMember(memberId, bookId);
        loans.add(loan);

        return loan;
    }

    public Fine returnBook(String bookId, String memberId) throws InvalidBookIdException, InvalidMemberException {
        Loan loan = loans.stream()
                .filter(l -> l.getBookId().equals(bookId) &&
                        l.getMemberId().equals(memberId))
                .findFirst()
                .orElseThrow(() -> new InvalidBookIdException("No active loan found for book '" + bookId + "' by member '" + memberId + "'."));

        Fine fine = null;
        if (loan.isOverdue()) {
            int daysOverdue = loan.getDaysOverdue();
            int fineAmount = loan.calculateFine();

            fine = new Fine(memberId, bookId, loan.getDueDate().plusDays(1), fineAmount);
            fines.add(fine);
        }

        Book book = bookService.findBookById(bookId);
        book.setLoanedTo(null);

        Member member = memberService.findMemberById(memberId);
        memberService.removeBookFromMember(memberId, bookId);

        loans.remove(loan);

        return fine; // null ha nincs büntetés
    }

    public List<Loan> getMemberLoans(String memberId) throws InvalidMemberException {
        memberService.findMemberById(memberId);

        return loans.stream()
                .filter(l -> l.getMemberId().equals(memberId))
                .sorted(Comparator.comparing(Loan::getLoanDate))
                .toList();
    }

    private Loan findLoanByBookId(String bookId) {
        return loans.stream()
                .filter(l -> l.getBookId().equals(bookId))
                .findFirst()
                .orElse(null);
    }

    public List<Loan> getAllLoans() {
        return new ArrayList<>(loans);
    }

    public List<Fine> getAllFines() {
        return new ArrayList<>(fines);
    }

    public List<Fine> getMemberFines(String memberId) {
        return fines.stream()
                .filter(f -> f.getMemberId().equals(memberId))
                .toList();
    }

    public int getTotalFinesAmount() {
        return fines.stream()
                .mapToInt(Fine::getAmount)
                .sum();
    }

    public int getMemberTotalFines(String memberId) {
        return fines.stream()
                .filter(f -> f.getMemberId().equals(memberId))
                .mapToInt(Fine::getAmount)
                .sum();
    }

    public void clearFine(String memberId, String bookId) throws InvalidMemberException {
        fines.stream()
                .filter(f -> f.getMemberId().equals(memberId) &&
                        f.getBookId().equals(bookId))
                .findFirst().ifPresent(fine -> fines.remove(fine));

    }

    public void clearAllMemberFines(String memberId) {
        fines.removeIf(f -> f.getMemberId().equals(memberId));
    }

    /**
     * STATISZTIKA: Legnépszerűbb kategória
     */
    public Map.Entry<String, Integer> getMostPopularCategory() {
        Map<String, Integer> categoryCount = new HashMap<>();

        for (Loan loan : loans) {
            try {
                Book book = bookService.findBookById(loan.getBookId());
                String category = book.getCategory();
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            } catch (InvalidBookIdException e) {
                // Skip
            }
        }

        for (Fine fine : fines) {
            try {
                Book book = bookService.findBookById(fine.getBookId());
                String category = book.getCategory();
                categoryCount.put(category, categoryCount.getOrDefault(category, 0) + 1);
            } catch (InvalidBookIdException e) {
                // Skip
            }
        }

        return categoryCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    /**
     * STATISZTIKA: Leggyakrabban késő tag
     * Visszaadja a tag ID-t és a késések számát
     */
    public Map.Entry<String, Integer> getMostFrequentlyLateMember() {
        Map<String, Integer> memberLateCount = new HashMap<>();

        for (Fine fine : fines) {
            String memberId = fine.getMemberId();
            memberLateCount.put(memberId, memberLateCount.getOrDefault(memberId, 0) + 1);
        }

        return memberLateCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .orElse(null);
    }

    public List<Loan> getOverdueLoans() {
        return loans.stream()
                .filter(Loan::isOverdue)
                .sorted(Comparator.comparing(Loan::getDaysOverdue).reversed())
                .toList();
    }

    public int getCategoryLoanCount(String category) {
        int count = 0;

        for (Loan loan : loans) {
            try {
                Book book = bookService.findBookById(loan.getBookId());
                if (book.getCategory().equals(category)) {
                    count++;
                }
            } catch (InvalidBookIdException e) {
                // Skip
            }
        }

        for (Fine fine : fines) {
            try {
                Book book = bookService.findBookById(fine.getBookId());
                if (book.getCategory().equals(category)) {
                    count++;
                }
            } catch (InvalidBookIdException e) {
                // Skip
            }
        }

        return count;
    }

    public LocalDate getExpectedReturnDate(String bookId) throws InvalidBookIdException {
        Loan loan = findLoanByBookId(bookId);
        if (loan == null) {
            throw new InvalidBookIdException("Book is not currently loaned.");
        }
        return loan.getDueDate();
    }
}

