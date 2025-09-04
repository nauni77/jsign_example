package de.company.signing.sign;

import java.io.File;
import java.security.KeyStore;
import java.security.KeyStoreException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import de.company.signing.exceptions.CodeSigningException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.jsign.AuthenticodeSigner;
import net.jsign.KeyStoreBuilder;
import net.jsign.KeyStoreType;
import net.jsign.Signable;

@Component
@Slf4j
public class CodeSigningWithAPI implements CodeSigningIF {

  @Value("${signsecret.keystore.type:#{null}}")
  KeyStoreType keyStoreType;

  @Value("${signsecret.keystore.keystore:#{null}}")
  String keyStorePath;

  @Value("${signsecret.keystore.alias:#{null}}")
  String keyStoreAlias;

  @Value("${signsecret.keystore.pass:#{null}}")
  String keyStorePass;
  
  @Value("${signsecret.timestampauthority:http://timestamp.sectigo.com}")
  String timeStampAuthority;

  @Value("${signsecret.programname:Noventi SE - Code Sign Service}")
  String programName;

  @Value("${signsecret.programurl:https://www.noventi.de}")
  String programUrl;

  // the keystore to use for signing
  KeyStore keystore;

  /**
   * Initialize everything what should be initialized once.
   * @throws CodeSigningException if something goes wrong initializing the KeyStore
   */
  @PostConstruct
  public void initialize() throws CodeSigningException {
    
    KeyStoreBuilder keyStoreBuilder = null;
    if (keyStorePath != null) {
      if (!(new File(keyStorePath).exists())) {
        log.error("Keystore file does not exist: {}", new File(keyStorePath).getAbsolutePath());
        throw new CodeSigningException("Keystore file does not exist: " + keyStorePath);
      }
      keyStoreBuilder = new KeyStoreBuilder().storetype(keyStoreType).keystore(keyStorePath).storepass(keyStorePass);
    } else {
      keyStoreBuilder = new KeyStoreBuilder().storetype(keyStoreType).storepass(keyStorePass);
    }
    
    log.info("using keystore type: {}", keyStoreType);
    try {
      keystore = keyStoreBuilder.build();
    } catch (KeyStoreException e) {
      throw new CodeSigningException("could not create keystore", e);
    }
  }

  /**
   *
   * @param fileDTO
   */
  public void signFile(File fileToSign) throws CodeSigningException {
    
    // Source: https://stackoverflow.com/questions/18556104/read-and-write-text-in-ansi-format
    // => The correct charset for Java encoding for Windows ANSI is Cp1252.
    // => complete list of encodings: https://docs.oracle.com/javase/8/docs/technotes/guides/intl/encoding.doc.html
    try {
      Signable fileSignable = Signable.of(fileToSign);
      
      // During creating a PKCS12 key, there are two passwords needed. KeyStore and Certificate.
      // Maybe the sign stick will require here null for keyStorePass.
      AuthenticodeSigner signer = new AuthenticodeSigner(keystore, keyStoreAlias, keyStorePass);

      signer.withProgramName(programName)
          .withProgramURL(programUrl)
          .withTimestamping(true)
          .withTimestampingAuthority(timeStampAuthority)
          .withTimestampingRetries(5)
          .withTimestampingRetryWait(10);

      signer.sign(fileSignable);
      log.info("signed file with KeyStoreType " + keyStoreType + " -> fileReference: " + fileToSign.getAbsolutePath());
    } catch (Exception e) {
      throw new CodeSigningException("something went wrong during signing the file", e);
    }
  }

}
