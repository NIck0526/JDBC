package jdbc.exception;

public class RecordNotFoundException extends Exception{
	public RecordNotFoundException() {
		this("this RecordNotFoundException.....");
	}
	public RecordNotFoundException(String message) {
		super(message);
	}
}
