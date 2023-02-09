import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.HashMap;

public class SymbolTable {
    LinkedList<HashMap<String, Variable>> scopes;

    public SymbolTable() {
        scopes = new LinkedList<HashMap<String, Variable>>();
    }

    public boolean add(String varname, String ref, Type type, ArrayList args) {
        if (scopes.getFirst().containsKey(varname)) {
            return false;
        }
        scopes.getFirst().put(varname, new Variable(varname, ref, type, args));
        return true;
    }

    public Variable getVar(String varname) throws CheckException {
        for (HashMap<String, Variable> scope: scopes) {
            if (scope.containsKey(varname)) {
                return scope.get(varname);
            }
        }
    throw CheckException.fromError("Error: " + varname+" is not defined");
    }

    public void scope() {
        scopes.addFirst(new HashMap<String, Variable>());
    }

    public void removeScope() {
        scopes.removeFirst();
    }

    public Arg createArg(Type type, boolean a) {
        return new Arg(type, a);
    }
}
