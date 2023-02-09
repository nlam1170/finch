import java.util.IllformedLocaleException;

public class FieldDecl extends Token {
    boolean isfinal;
    String type;
    String id;
    Expression exp;
    int length;
    int declType;

    public FieldDecl(boolean isfinal, String type, String id, Expression exp) {
        this.isfinal = isfinal;
        this.type = type;
        this.id = id;
        this.exp = exp;
        this.declType = 0;
    }

    public FieldDecl(String type, String id, int length) {
        this.type = type;
        this.id = id;
        this.length = length;
        this.declType = 1;
    }

    public String asString(int i) {
        if (declType == 0) {
            return tabs(i) + ( isfinal ? "final " : "" ) + type + " " + id  + ( exp != null ? " = "+ exp.asString() : "" ) + ";";
        }
        else if (declType == 1) {
            return tabs(i) + type +  " " + id + "[" + length + "]" + ";";
        }
        else {
            return "";
        }
    }

    public void check() throws CheckException {
        if (declType == 0) {
            if (exp != null) {
                if (!canConvert(exp.check(), getType(type))) {
                    throw CheckException.fromError("Error: type mismatch");
                }
            }

            if (!symtable.add(id, (isfinal ? "final" : ""), getType(type), null)) {
                throw CheckException.fromError("Error: redefined variable");
            }
        }

        if (declType == 1) {
            if (!symtable.add(id, "array", getType(type), null)) {
                throw CheckException.fromError("Error: redefined variable");
            }
        }
    }
}
