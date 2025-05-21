package Model;

public class Department {
    private int id;
    private String name;

    public Department() {
    }

    public Department(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }

    public Object getName() {
        return name;
    }

    public void setId(int departmentId) {
        this.id = departmentId;
    }

    public void setName(String departmentName) {
        this.name = departmentName;
    }
}

