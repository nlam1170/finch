import java.util.ArrayList;

public class MemberDecl extends Token {
    ArrayList<FieldDecl> fielddecls;
    ArrayList<MethodDecl> methoddecls;

    public MemberDecl(FieldDecl f, MemberDecl m) {
        m.fielddecls.add(0, f);
        this.fielddecls = m.fielddecls;
        this.methoddecls = m.methoddecls;
    }

    public MemberDecl(ArrayList<FieldDecl> f, ArrayList<MethodDecl> m) {
        fielddecls = f;
        methoddecls = m;
    }

    public String asString(int i) {
        String s = "";
        for (FieldDecl f: fielddecls) {
            s += f.asString(i) + "\n";
        }

        for (MethodDecl m: methoddecls) {
            s += m.asString(i) + "\n";
        }
        return s;
    }

    public void check() throws CheckException {
        symtable.scope();
        for (FieldDecl f: fielddecls) {
            f.check();
        }

        for (MethodDecl m: methoddecls) {
            m.check();;
        }
        symtable.removeScope();
    }
}
