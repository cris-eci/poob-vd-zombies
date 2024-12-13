package domain;


public class POOBvsZombiesException extends Exception {

    public static final String INVALID_COORDINATES = "Invalid row or column. Please enter valid numbers.";
    public static final String INVALID_LAWNMOWER = "Cannot remove lawnmower.";
    public static final String NO_PLANT_TO_REMOVE = "No plant to remove in the selected cell.";
    public static final String INVALID_INPUTS = "Invalid input, please enter valid integers.";
    public static final String TIME_LIMIT = "El tiempo total no debe ser negativo y por lo menos debe haber una horda.";    

    // Constructor que recibe el mensaje de la excepción
    public POOBvsZombiesException(String message) {
        super(message);
        // Al crear la excepción, automáticamente se registra en el log
        Log.record(this);
    }
}

