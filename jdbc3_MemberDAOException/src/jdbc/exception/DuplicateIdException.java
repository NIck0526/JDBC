package jdbc.exception;

public class DuplicateIdException extends Exception{
	public DuplicateIdException() {
		this("this DuplicateIdException.....");
	}
	public DuplicateIdException(String message) {
		super(message);
	}
}
