package main.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Loan {
    private final String bookId;
    private final String memberId;
    private final LocalDate loanDate;
    private LocalDate dueDate;

    public Loan(String bookId, String memberId, LocalDate loanDate, int loanDuration) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.dueDate = loanDate.plusDays(loanDuration);
    }

    public int getDaysOverdue() {
        LocalDate today = LocalDate.now();
        return today.isAfter(dueDate) ? (int) ChronoUnit.DAYS.between(dueDate, today) : 0;
    }

    public int calculateFine() {
        return getDaysOverdue() * 50;
    }

    public boolean isOverdue() {
        return getDaysOverdue() > 0;
    }

    public String getBookId() {
        return bookId;
    }

    public String getMemberId() {
        return memberId;
    }

    public LocalDate getLoanDate() {
        return loanDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void recalculateDueDate(int correctLoanDuration) {
        this.dueDate = this.loanDate.plusDays(correctLoanDuration);
    }
}
