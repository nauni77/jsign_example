package de.company.signing.sign;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import de.company.signing.exceptions.CodeSigningException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Component(CodeSigningIF.NAME)
@Primary
@Slf4j
public class CodeSigningBean implements CodeSigningIF {

  CodeSigningWithAPI codeSigningWithAPIInstance;

  @Autowired
  public CodeSigningBean(CodeSigningWithAPI codeSigningWithAPIInstance) {
    this.codeSigningWithAPIInstance = codeSigningWithAPIInstance;
  }

  @PostConstruct
  public void initBean() {
  }

  /**
   *
   * @param fileDTO
   */
  public void signFile(File fileDTO) throws CodeSigningException {
    try {
      codeSigningWithAPIInstance.signFile(fileDTO);
    } catch (CodeSigningException cse) {
      log.error("error signing file " + fileDTO.getAbsolutePath(), cse);
      throw cse;
    }
  }

}
