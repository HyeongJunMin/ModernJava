package com.mj.modernjava.common;

import com.mj.modernjava.review.domain.Person;
import com.mj.modernjava.review.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

@Slf4j
@SpringBootTest
@RequiredArgsConstructor
public class JpaConnectionTest {

    private final PersonRepository personRepository;

    @Test
    @Rollback(false)
    public void personTest() {
        Person a = Person.builder().name("name").build();
        log.debug("a : {}", a);

        personRepository.save(a);

    }
}
