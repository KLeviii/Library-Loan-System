import java.util.Objects;

public class Children extends Book {
    private static final int loanDuration = 14;
    private static int count = 0;

    public Children(String id, String title, String author, int loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public Children(String id, String title, String author) {
        super(id, title, author);
        count++;
    }

    public static String getLoanDuration() {
        return loanDuration + " days";
    }

    public static int getCount() {
        return count;
    }

    @Override
    public void checkBookId(String id) throws IllegalArgumentException {
        if ((id.length() != 6) || (!Objects.equals(id.split("-")[0], "CH")) || id.split("-")[1].isEmpty()) {
            throw new IllegalArgumentException("Invalid ID!");
        }
    }

    @Override
    public void checkMember(int member) throws IllegalArgumentException {

    }
}
