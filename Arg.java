public class Arg implements Check {
    Type type;
    boolean ofArray;

    public Arg(Type type, boolean ofArray) {
        this.type = type;
        this.ofArray = ofArray;
    }

    public boolean ofArray() {
        return ofArray;
    }

    public boolean ofFinal() {
        return false;
    }

    public boolean ofFunction() {
        return false;
    }

    public Type check() {
        return type;
    }
    
}
