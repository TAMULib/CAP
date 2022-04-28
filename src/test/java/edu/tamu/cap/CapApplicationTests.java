package edu.tamu.cap;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = { CapApplication.class }, webEnvironment = WebEnvironment.DEFINED_PORT)
public final class CapApplicationTests {

    @Test
    public void contextLoads() {

    }

}
