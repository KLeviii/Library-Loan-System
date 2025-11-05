import java.util.Objects;

public class History extends Book{
    private static final int loanDuration = 21;
    private static int count = 0;

    public History(String id, String title, String author, int loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public History(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    public static String getLoanDuration() {
        return loanDuration + " days";
    }

    public static int getCount() {
        return count;
    }
}
