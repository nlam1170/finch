public class BinaryOp extends Token {
    String op;

    public BinaryOp(String op) {
        this.op = op;
    }

    public String asString() {
        return op;
    }

    public boolean ofMath() {
        return op.equals("+") || op.equals("-") || op.equals("*") || op.equals("/");
    }

    public boolean ofLogic() {
        return op.equals("||") || op.equals("&&");
    }

    public boolean ofRelation() {
        return op.equals("<") || op.equals("<=") || op.equals(">") || op.equals(">=") || op.equals("==") || op.equals("<>");
    }
}
