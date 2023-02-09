import java.util.ArrayList;

public class MethodDecl extends Token {
    String type;
    String id;
    boolean semi;
    ArrayList<ArgDecl> argdecls;
    ArrayList<FieldDecl> fielddecls;
    ArrayList<Statement> statements;

    public MethodDecl(String type, String id, boolean semi, ArrayList<ArgDecl> argdecls, ArrayList<FieldDecl> fielddecls, ArrayList<Statement> statements) {
        this.type = type;
        this.id = id;
        this.semi = semi;
        this.argdecls = argdecls;
        this.fielddecls = fielddecls;
        this.statements = statements;
    }

    public String asString(int i) {
        String s = "";
        for (ArgDecl a: argdecls) {
            s += a.asString() + ", ";
        }
        s = s.substring(0, s.length() > 0 ? s.length()-2 : 0);

        String res = tabs(i) + type.toString() + " " + id + "(" + s + ")" + " {\n";

        for (FieldDecl f: fielddecls) {
            res += f.asString(i+1) + "\n";
        }

        for (Statement ss: statements) {
            res += ss.asString(i+1) + "\n";
        }
        res += tabs(i) + "}" + (semi ? ";" : "" );
        return res;
    } 

    public void check() throws CheckException {
        ArrayList<Arg> params = new ArrayList<Arg>();

        for (ArgDecl a: argdecls) {
            params.add(symtable.createArg(getType(a.type), a.ofArray)); 
        }

        if (!symtable.add(id, "function", getType(type), argdecls)) {
            throw CheckException.fromError("Error: Variable Redefined");
        }

        symtable.scope();
        for (ArgDecl a: argdecls) {
            a.check();
        }

        for (FieldDecl f: fielddecls) {
            f.check();
        }

        boolean mustReturn = !getType(type).equals(AllType.VOID);

        for (Statement s: statements) {
            if (!s.ofReturn()) {
                mustReturn = false;

                if (canConvert(s.check(), getType(type))) {
                    throw CheckException.fromError("Error: Cannot convert between types");
                }
            }
            else {
                s.check();
            }
            
            if (mustReturn) {
                throw CheckException.fromError("Error: return statement not found");
            }
            symtable.removeScope();
        }
    }
}
