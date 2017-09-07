package broker.three.exception;

public class InvalidTransactionException extends Exception {

	public InvalidTransactionException() {
		this("This is InvalidTransactionException....");
	}
	
	public InvalidTransactionException(String message) {
		super(message);
	}
}
