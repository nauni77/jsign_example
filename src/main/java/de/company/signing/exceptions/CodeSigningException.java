package de.company.signing.exceptions;

/**
 * Runtime exception which is thrown if something goes wrong during code signing!
 */
public class CodeSigningException extends Exception {

  public CodeSigningException(String message, Throwable cause) {
    super(message, cause);
  }

  public CodeSigningException(String message) {
    super(message);
  }


}
