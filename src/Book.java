import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public abstract class Book {
    private final String id;
    private final String title;
    private final String author;
    private int loanedTo;


    public Book(String id, String title, String author, int loanedTo) {
        this.id = id;
        this.title = title;
        this.author = author;
        setLoanedTo(loanedTo);
    }

    public Book(String id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
        setLoanedTo(0);
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getLoanedTo() {
        return loanedTo;
    }

    public void setLoanedTo(int loanedTo) {
        this.loanedTo = loanedTo;
    }

    public abstract void checkBookId(String id) throws IllegalArgumentException;

    public void checkMember(int memberId) throws IllegalArgumentException{
        boolean foundMember = false;
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = bufferedReader.readLine())!=null) {
                if (Integer.parseInt(line.split(";")[1]) == memberId){
                    foundMember = true;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file.");
        }
        if (!foundMember){
            throw new IllegalArgumentException("Nem található ilyen tag!");
        }
    }
}
