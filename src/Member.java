public class Member {
    private String name;
    private int id;
    private int currentlyLoanedBooks;

    public Member(String name, int id, int currentlyLoanedBooks) {
        this.name = name;
        this.id = id;
        this.currentlyLoanedBooks =currentlyLoanedBooks;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setCurrentlyLoanedBooks(int currentlyLoanedBooks) throws LoanLimitReachedException {
        if (currentlyLoanedBooks < 3){
        throw new LoanLimitReachedException("WARNING: You've reached the maximum books at once");
        }
        this.currentlyLoanedBooks=currentlyLoanedBooks;
    }

    public int getCurrentlyLoanedBooks() {
        return currentlyLoanedBooks;
    }

    @Override
    public String toString() {
        return  "╔══════════════════════════════╗\n" +
                "║           MEMBER             ║\n" +
                "╠══════════════════════════════╣\n" +
                "║ Name: " + name + "\n" +
                "║ ID:   " + id + "\n" +
                "╚══════════════════════════════╝";
    }
}
