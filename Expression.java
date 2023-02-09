import java.util.ArrayList;

public class Expression extends Token implements Check {
    int typenum;
    String id;
    String charstr;
    int intlit;
    float floatlit;
    Name name;
    boolean boollit;
    Expression exp[];
    BinaryOp binop;
    String op;
    String convertType;
    ArrayList<Expression> args;

    public Expression(int typenum, String id, String charstr, int intlit, float floatlit, Name name, boolean boollit, Expression exp[], BinaryOp binop, String op, String convertType, ArrayList<Expression> args) {
        this.typenum = typenum;
        this.id = id; 
        this.charstr = charstr;
        this.intlit = intlit;
        this.floatlit = floatlit;
        this.name = name;
        this.boollit = boollit;
        this.exp = exp;
        this.binop = binop;
        this.op = op;
        this.convertType = convertType;
        this.args = args;
    }

    public String asString() {
        switch (this.typenum) {
          case 0: case 12:
            return charstr;
          case 1:
            return ""+intlit;
          case 2:
            return ""+floatlit;
          case 3:
            return name.toString();
          case 4:
            return boollit ? "true" : "false";
          case 5:
            return exp[0].toString();
          case 6:
            return "(" + op + " " + exp[0].toString() + ")";
          case 7:
            return "(" + convertType + ")" + exp[0].toString();
          case 8:
            return "(" + exp[0].toString() + " " + binop.toString() + " " + exp[1].toString() + ")";
          case 9:
            return "( " + exp[0].toString() + " ? " + exp[1].toString() + " : " + exp[2].toString() + " )";
          case 10:
            String ret = "";
            for (Expression e: args) {
              ret += e.toString() + ", ";
            } ret = ret.substring(0, ret.length() > 0 ? ret.length() - 2 : 0 );
            return id + "(" + ret + ")";
          case 11:
            return id +"()";
          default:
            return "";
        }
    }

    public boolean ofArray() throws CheckException {
        return name != null && name.ofArray() || typenum == 5 && exp[0].ofArray();
    }

    public boolean ofFinal() throws CheckException {
        return name != null && name.ofFinal() || typenum == 5 && exp[0].ofFinal();
    }

    public boolean ofFunction() throws CheckException {
        return name != null && name.ofFunction() || typenum == 5 && exp[0].ofFunction();
    }

    public Type check() throws CheckException {
        switch (this.typenum) {
          case 0: 
            return AllType.STR;
          case 12: 
            return AllType.CHAR;
          case 1: 
            return AllType.INT;
          case 2: 
            return AllType.FLOAT;
          case 3: 
            return name.check();
          case 4: 
            return AllType.BOOL;
          case 5: 
            return exp[0].check();
          case 6: 
            if (op.equals("~")) {
              if (!canBeBool(exp[0]))
                throw CheckException.fromError("Error: incorrect unary logic");
              return AllType.BOOL;
            } else {
              if (!canBeFloat(exp[0]))
                throw CheckException.fromError("Error: wrong usage of sign");
              return exp[0].check();
            }        
          case 7: // 
            return getType(convertType); 
          case 8: 
            if (exp[0].check().equals(AllType.STR) || exp[1].check().equals(AllType.STR)) {
              if (!binop.op.equals("+"))
                throw CheckException.fromError("Error: cannot perform operation on str");
              return AllType.STR;
            }
            if (binop.ofMath()) {
              if ( !canBeFloat(exp[0]) && !canBeFloat(exp[1]) )
                throw CheckException.fromError("Error: cannot perform operation on str");
              return exp[0].check().equals(AllType.FLOAT) || exp[1].check().equals(AllType.FLOAT)
                ? AllType.FLOAT
                : AllType.INT;
            }
            if (binop.ofRelation()) {
              if ( !canBeFloat(exp[0]) && !canBeFloat(exp[1]) )
                throw CheckException.fromError("Error: cannot perform operation on str");
              return AllType.BOOL;
            }
            if (binop.ofLogic()) {
              if ( !canBeBool(exp[0]) && !canBeBool(exp[1]) )
                throw CheckException.fromError("Error: cannot perform operation on str");
              return AllType.BOOL;
            }
          case 9: 
            if ( !canBeBool(exp[0]) )
              throw CheckException.fromError("Error: cannot convert types");
            if ( !exp[1].check().equals(exp[2].check()))
              throw CheckException.fromError("Error: tern expressions must be the same");
            return exp[1].check(); 
          case 10: 
            Variable fun = symtable.getVar(id);
            if (!fun.ofFunction())
              throw CheckException.fromError("Error: function is undefined");
            if (fun.args.size() != args.size())
              throw CheckException.fromError("Error: wrong args");
            for (int i = 0; i < args.size(); i++) {
              if (!canAssign(args.get(i), fun.args.get(i)))
                throw CheckException.fromError("Error: wrong args");
            }
            return symtable.getVar(id).check();
          case 11: 
            if (!symtable.getVar(id).ofFunction())
              throw CheckException.fromError("Error: function is undefined");
            return symtable.getVar(id).check();
        }
        return null;
      }

    
}
