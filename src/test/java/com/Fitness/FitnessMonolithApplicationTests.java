package com.Fitness;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        "spring.datasource.driver-class-name=org.h2.Driver",
        "spring.datasource.username=sa",
        "spring.datasource.password=",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.sql.init.mode=always",
        "spring.app.jwtSecret=secret",
        "spring.app.jwtExpirationMS=8600",
        "spring.app.jwtCookie=cookie"
})
class FitnessMonolithApplicationTests {

    @Test
    void contextLoads() {
    }

}
