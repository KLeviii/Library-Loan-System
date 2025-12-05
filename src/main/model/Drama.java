package main.model;

public final class Drama extends Book{
    private static final int LOAN_DURATION = 28;
    private static int count = 0;

    public Drama(String id, String title, String author, String loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public Drama(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    @Override
    public int getLoanDuration() {
        return LOAN_DURATION;
    }

    @Override
    public String getCategory() {
        return "Drama";
    }

    public static int getCount() {
        return count;
    }
}
