public class Member {
    private String name;
    private int id;

    public Member(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }


    // TODO: Customize toString() method
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
