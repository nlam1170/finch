import java.util.ArrayList;

public class Statement extends Token {
    int typenum;
    boolean loop;
    boolean semi;
    String id;
    Name name;
    String op;
    Expression exp;
    Statement stm;
    Statement _else;
    ArrayList<FieldDecl> fielddecls;
    ArrayList<Statement> statements;
    ArrayList<Expression> expressions;
    ArrayList funcs;

    public Statement(int typenum, boolean loop, boolean semi, String id, Name name, String op, Expression exp, Statement stm, Statement _else, ArrayList<FieldDecl> fielddecls, ArrayList<Statement> statements, ArrayList<Expression> expressions, ArrayList funcs) {
        this.typenum = typenum;
        this.loop = loop;
        this.semi = semi;
        this.id = id;
        this.name = name;
        this.op = op;
        this.exp = exp;
        this.stm = stm;
        this._else = _else;
        this.fielddecls = fielddecls;
        this.statements = statements;
        this.expressions = expressions;
        this.funcs = funcs;
    }

    public String asString(int depth) {
        switch (this.typenum) {
          case 0:
            return tabs(depth) +
              "if (" + exp.toString() + ")\n" +
              ( stm.typenum == 2 ? stm.asString(depth) : tabs(depth) +"{\n" + stm.asString(depth+1) + "\n"+ tabs(depth) + "}" ) +
              ( _else != null ? "\n" + tabs(depth) + "else\n" + ( _else.typenum == 2 ? _else.asString(depth) : tabs(depth) +"{\n" + _else.asString(depth+1) + "\n"+ tabs(depth) + "}") : "" );    
          case 1:
            String list = "";
            for (Expression e: expressions) {
              list += e.toString() + ", ";
            } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return tabs(depth) + id + "(" + list + ");";
          case 2:
            String result = "";
            for (FieldDecl f: fielddecls) {
              result += f.asString(depth+1) + "\n";
            }
            for (Statement st: statements) {
              result += st.asString(depth+1) + "\n";
            }
            return tabs(depth) + "{\n" + result + tabs(depth) + "}";
          case 3:
            return tabs(depth) +
              "while (" + exp.toString() + ")\n" + stm.asString(stm.typenum == 2 ? depth : depth+1) + "\n";
          case 4:
            return tabs(depth) +
              name.toString() + " = " + exp.toString() + ";"; 
          case 5:
            list = "";
            for (Name n: (ArrayList<Name>)funcs) {
              list += n.toString() + ", ";
            } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return tabs(depth) + id + "(" + list + ");";
          case 6:
            list = "";
            for (Expression e: (ArrayList<Expression>)funcs) {
              list += e.toString() + ", ";
            } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return tabs(depth) + id + "(" + list + ");";
          case 7:
            list = "";
            for (Expression e: (ArrayList<Expression>)funcs) {
              list += e.toString() + ", ";
            } list = list.substring(0, list.length() > 0 ? list.length() - 2 : 0);
            return tabs(depth) + id + "(" + list + ");";
          case 8:
            return tabs(depth) + name.toString() + op + ";";
          case 9:
            return tabs(depth) + "return " + expressions.toString() + ";"; 
          case 10:
            return tabs(depth) + id + "();";
          case 11:
            return tabs(depth) + "return;";
          default:
            return "";
        }
      }
    
      public Type check() throws CheckException {
        switch (this.typenum) {
          case 0: // if 
            if (!canBeBool(exp))
            throw CheckException.fromError("Error: cannot modify final var");
            symtable.scope();
            stm.check();
            symtable.removeScope();
            if ( _else != null ) {
              symtable.scope();
              stm.check();
              symtable.removeScope();
            }
            break;
          case 1: 
            Variable fun = symtable.getVar(id);
            if (!fun.ofFunction())
              throw CheckException.fromError("Error: function is undefined");
            if (fun.args.size() != expressions.size())
              throw CheckException.fromError("Error: arg list invalid");
            for (int i = 0; i < expressions.size(); i++) {
              if (!canAssign(expressions.get(i), fun.args.get(i)))
              throw CheckException.fromError("Error: arg list invalid");
            }
            break;
          case 2: 
            symtable.scope();
            for (FieldDecl f: fielddecls)
              f.check();
            for (Statement st: statements)
              st.check();
            symtable.removeScope();
            break;          
          case 3: 
            if (!canBeBool(exp))
            CheckException.fromError("Error: cannot modify final var");
            symtable.scope();
            stm.check();
            symtable.removeScope();
            break;
          case 4: 
            if (!canAssign(this.exp, this.name))
              throw CheckException.fromError("Error: cannot mismatch assignment");
            if (symtable.getVar(name.id).ofFinal())
              throw CheckException.fromError("Error: cannot modify final var");
            break;
          case 5: 
            for (Name n: (ArrayList<Name>)funcs) {
              if (n.ofArray() || n.ofFinal())
                throw CheckException.fromError("Error: cannot modify final var");
            }
            break;
          case 6: case 7:
            for (Expression e: (ArrayList<Expression>)funcs) {
              e.check();
              if (e.ofArray() || e.ofFunction())
                throw CheckException.fromError("cannot print an array or function.");
            }
            break;
          case 8: 
            if (!canBeFloat(name))
              throw CheckException.fromError("Error: can only increase or decrease int or float");
            if (symtable.getVar(name.id).ofFinal())
              throw CheckException.fromError("Error: cannot modify final var");
            break;
          case 9: 
            return exp.check();
          case 10: 
            if (!symtable.getVar(id).ofFunction())
              throw CheckException.fromError("Error: function is undefined");
            break;
          case 11:
            return AllType.VOID;
        }
        return null;
    }

    public boolean ofReturn() {
        return this.typenum == 9 | this.typenum == 11;
      }
}
