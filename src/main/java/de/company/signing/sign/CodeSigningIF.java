package de.company.signing.sign;

import java.io.File;

import de.company.signing.exceptions.CodeSigningException;

public interface CodeSigningIF {

  String NAME = "CodeSigning";

  void signFile(File fileForSigning) throws CodeSigningException;

}
