public class CheckException extends Exception {
    String error;

    private CheckException(String error) {
        this.error = error;
    }

    public static CheckException fromError(String error) {
        return new CheckException(error);
    }

    public String asString() {
        return error;
    }
    
}