import java.util.Objects;

public class History extends Book{
    private static int loanDuration = 21;
    private static int count = 0;

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

    @Override
    public void checkId(String id) throws IllegalArgumentException {
        if ((id.length() != 6) || (!Objects.equals(id.split("-")[0], "HS")) || id.split("-")[1].isEmpty()) {
            throw new IllegalArgumentException("Wrong ID!");
        }
    }
}
