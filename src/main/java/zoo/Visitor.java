package zoo;

public class Visitor {

    private String name;
    private int age;
private boolean insideZoo;
    public Visitor(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
public void enterZoo() {
    insideZoo = true;
}

public void leaveZoo() {
    insideZoo = false;
}

public boolean isInsideZoo() {
    return insideZoo;
}
}


