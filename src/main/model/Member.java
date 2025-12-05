package main.model;

import main.exception.LoanLimitReachedException;

public class Member {
    private String memberId;
    private String name;
    private int loanedBooks;

    public Member(String name, String memberId, int loanedBooks) {
        this.name = name;
        this.memberId = memberId;
        setLoanedBooks(loanedBooks);
    }

    public Member(String name, String memberId) {
        this.name = name;
        this.memberId = memberId;
        setLoanedBooks(0);
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

    public boolean canLoanMore() {
        return loanedBooks < 3;
    }

    public void incrementLoans() {
        if (canLoanMore()) loanedBooks++;
    }

    public void decrementLoans() {
        if (loanedBooks > 0) loanedBooks++;

    }


    // Hell nah     ;KLevi
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
