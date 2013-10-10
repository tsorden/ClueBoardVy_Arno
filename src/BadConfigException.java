
public class BadConfigException extends Exception{

	public BadConfigException() {
		super("Bad Config File please check that it is correct.");
	}
	public BadConfigException(String message) { super(message);}

	public BadConfigException(String message, Throwable cause) { super(message, cause); }
	public BadConfigException(Throwable cause) { super(cause); }
}
