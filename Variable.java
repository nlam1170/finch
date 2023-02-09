import java.util.ArrayList;

public class Variable implements Check {
    public ArrayList<Arg> args;
    public String ref;
    public String name;
    public Type type;

    public Variable(String name, String ref, Type type, ArrayList<Arg> args) {
        this.name = name;
        this.ref = ref;
        this.type = type;
        this.args = args;
    }

    public boolean ofFinal() {
        return ref.equals("final");
    }
    
    public boolean ofArray() {
        return ref.equals("final");
    }

    public boolean ofFunction() {
        return ref.equals("final");
    }

    public Type check() {
        return type;
    }
    
}
