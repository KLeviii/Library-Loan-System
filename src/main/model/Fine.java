package main.model;

import java.time.LocalDate;

public class Fine {
    private final String memberId;
    private final String bookId;
    private final LocalDate overdueDate;
    private int amount;

    public Fine(String memberId, String bookId, LocalDate overdueDate, int amount) {
        this.memberId = memberId;
        this.bookId = bookId;
        this.overdueDate = overdueDate;
        setAmount(amount);
    }

    public String getMemberId() {
        return memberId;
    }
    public String getBookId() {
        return bookId;
    }
    public LocalDate getOverdueDate() {
        return overdueDate;
    }
    public int getAmount() {
        return amount;
    }
    public void setAmount(int amount) {
        this.amount = amount;
    }
}
