public abstract class Token {
    public static SymbolTable symtable;

    protected String tabs(int n) {
        String tabs = "";
        for (int i = 0; i < n ; i++)
        {
            tabs += "\t";
        }
        return tabs;
    }

    protected Type getType(String t) {
        return
            t.equals("int") ? AllType.INT :
            t.equals("bool") ? AllType.BOOL :
            t.equals("char") ? AllType.CHAR :
            t.equals("float") ? AllType.FLOAT :
            t.equals("string") ? AllType.STR :
            t.equals("void") ? AllType.VOID :
            null;
    }

    protected boolean canAssign(Check start, Check end) throws CheckException {
        boolean cond1 = start.ofArray() && end.ofArray() && start.check().equals(end.check());
        boolean cond2 = !start.ofArray() && !end.ofArray() && canConvert(start.check(), end.check());
        return cond1 || cond2;
    }

    protected boolean canConvert(Type right, Type left) {
        return right.equals(AllType.INT)
          ? left.equals(AllType.BOOL) || left.equals(AllType.FLOAT) || left.equals(AllType.INT)
          : right.equals(left);
    }
        
      protected boolean canBeBool(Check expr) throws CheckException {
        return !expr.ofArray() && ( expr.check().equals(AllType.BOOL) || expr.check().equals(AllType.INT) );
      }

      protected boolean canBeFloat(Check expr) throws CheckException {
        return !expr.ofArray() && ( expr.check().equals(AllType.FLOAT) || expr.check().equals(AllType.INT) );
      }
}
