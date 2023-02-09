public interface Check {
    public boolean ofFinal() throws CheckException;
    public boolean ofArray() throws CheckException;
    public boolean ofFunction() throws CheckException;
    public Type check() throws CheckException;
}
