package de.company.signing;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@SpringBootTest(properties = {
  "spring.config.additional-location=file:/Users/oliver.wagner/.config/spring-boot/codeSignService/"
})
@ActiveProfiles({"test", "yubikey"})
@Slf4j
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CodeSigningTestYUBIKEY extends CodeSigningTest {

}
