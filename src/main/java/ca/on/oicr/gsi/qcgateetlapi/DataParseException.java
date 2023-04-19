package ca.on.oicr.gsi.qcgateetlapi;

public class DataParseException extends Exception {

  public DataParseException(String message) {
    super(message);
  }

  public DataParseException(String message, Throwable cause) {
    super(message, cause);
  }
}
