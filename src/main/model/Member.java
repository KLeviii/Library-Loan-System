package main.model;

import main.exception.LoanLimitReachedException;

import java.util.ArrayList;
import java.util.List;

public class Member {
    private String memberId;
    private String name;
    private int loanedBooks;
    private List<String> loanedBookIds;


    public Member(String memberId, String name, int loanedBooks, List<String> loanedBookIds) {
        this.memberId = memberId;
        this.name = name;
        setLoanedBooks(loanedBooks);
        this.loanedBookIds = loanedBookIds;
    }

    public Member(String memberId, String name) {
        this.memberId = memberId;
        this.name = name;
        setLoanedBooks(0);
        this.loanedBookIds = new ArrayList<>();
    }

    public List<String> getLoanedBookIds() {
        return new ArrayList<>(loanedBookIds);
    }

    public String getMemberId() {
        return memberId;
    }

    public String getName() {
        return name;
    }

    public int getLoanedBooks() {
        return loanedBooks;
    }

    public void setLoanedBooks(int count) throws LoanLimitReachedException {
        if (!canLoanMore()) {
            throw new LoanLimitReachedException("WARNING: You've reached the maximum books at once");
        }
        this.loanedBooks = count;
    }

    public void addLoanedBook(String bookId) {
        if (!hasLoanedBook(bookId) && canLoanMore()) {
            loanedBookIds.add(bookId);
        }
    }

    public void resetLoanState() {
        this.loanedBookIds.clear();
        this.loanedBooks = 0;
    }

    public void removeLoanedBook(String bookId) {
        loanedBookIds.remove(bookId);
    }

    public boolean hasLoanedBook(String bookId) {
        return loanedBookIds.contains(bookId);
    }

    public void syncLoanCount() {
        this.loanedBooks = loanedBookIds.size();
    }

    public boolean canLoanMore() {
        return loanedBooks < 3;
    }

    public void incrementLoans() {
        if (canLoanMore()) loanedBooks++;
    }

    public void decrementLoans() {
        if (loanedBooks > 0) loanedBooks--;

    }

    // Hell nah
    @Override
    public String toString() {
        return "╔══════════════════════════════╗\n" +
                "║           MEMBER             ║\n" +
                "╠══════════════════════════════╣\n" +
                "║ Name: " + name + "\n" +
                "║ ID:   " + memberId + "\n" +
                "╚══════════════════════════════╝";
    }
}
