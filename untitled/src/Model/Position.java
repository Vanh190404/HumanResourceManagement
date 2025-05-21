package Model;

public class Position {
    private int id;
    private String title;

    public Position() {}

    public Position(int id, String title) {
        this.id = id;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return title;
    }

    public Object getName() {
        return title;
    }

    public void setId(int positionId) {
        this.id = positionId;
    }

    public void setName(String positionName) {
        this.title = positionName;
    }
    // Getters and Setters
}
