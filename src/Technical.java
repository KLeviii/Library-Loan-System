import java.util.Objects;

public class Technical extends Book{
    private static final int loanDuration = 7;
    private static int count = 0;

    public Technical(String id, String title, String author, Member loanedTo) {
        super(id, title, author, loanedTo);
        count++;
    }

    public Technical(String id, String title, String author) {
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
        if ((id.length() != 6) || (!Objects.equals(id.split("-")[0], "TC")) || id.split("-")[1].isEmpty()) {
            throw new IllegalArgumentException("Invalid ID!");
        }
    }

    @Override
    public void checkMember(Member member) throws IllegalArgumentException {

    }
}
