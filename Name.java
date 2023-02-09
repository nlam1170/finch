public class Name extends Token implements Check {
    String id;
    Expression exp;

    public Name(String id, Expression exp) {
        this.id = id;
        this.exp = exp;
    }

    public boolean hasExp() {
        return exp != null;
    }

    public String asString() {
        return id + ( hasExp() ? "[" + exp.toString() + "]" : "" );
    }

    public boolean ofArray() throws CheckException {
        return symtable.getVar(id).ofArray() && !hasExp();
    }

    public boolean ofFinal() throws CheckException {
        return symtable.getVar(id).ofFinal();
    }

    public boolean ofFunction() throws CheckException {
        return symtable.getVar(id).ofFunction();
    }

    public Type check() throws CheckException {
        if (!hasExp() || exp.check().equals(AllType.INT)) {
            return symtable.getVar(id).check();
        }
        else {
            throw CheckException.fromError("Error: The index of an array must be an int");
        }
    }
}
