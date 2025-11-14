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

    public boolean checkBookId(String bookid) throws IllegalArgumentException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("books.txt"))) {
            String line;
            while ((line = bufferedReader.readLine())!=null) {
                if (line.split(";")[1].equals(bookid)){
                    return true;
                }
            }
            throw new IllegalArgumentException("Couldn't find this member!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading the books.");
        }
    }

    public boolean checkMember(int memberId) throws IllegalArgumentException{
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("members.txt"))) {
            String line;
            while ((line = bufferedReader.readLine())!=null) {
                if (Integer.parseInt(line.split(";")[1]) == memberId){
                    return true;
                }
            }
            throw new IllegalArgumentException("Couldn't find this member!");
        } catch (IOException e) {
            throw new IllegalArgumentException("Error reading the members.");
        }
    }
}
