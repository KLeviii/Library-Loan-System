package main.model;

public final class History extends Book{
    private static final int LOAN_DURATION = 21;
    private static int count = 0;

    public History(String id, String title, String author, String loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public History(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    @Override
    public int getLoanDuration() {
        return LOAN_DURATION;
    }

    @Override
    public String getCategory() {
        return "History";
    }

    public static int getCount() {
        return count;
    }
}
