public class Program extends Token {
    String name;
    MemberDecl memberdecl;
    
    public Program(String n, MemberDecl m) {
        name = n;
        memberdecl = m;
        symtable = new SymbolTable();
    }

    public String asString(int i) {
        return "class " + name + " {\n" + memberdecl.asString(i+1) + "}";
    }

    public void check() throws CheckException {
        memberdecl.check();
    }
    
}
