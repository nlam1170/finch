public class ArgDecl extends Token {
    String type;
    String id;
    boolean ofArray;

    public ArgDecl(String type, String id, boolean ofArray) {
        this.type = type;
        this.id = id;
        this.ofArray = ofArray;
    }

    public String asString() {
        String s =  type + " " + id;
        s += ( ofArray ? "[]" : "" );
        return s;
    }

    public void check() throws CheckException {
        if (!symtable.add(id, (ofArray ? "array" : ""), getType(type), null)) {
            throw CheckException.fromError("Error: redefined variable Exception");
        }
    }
}
