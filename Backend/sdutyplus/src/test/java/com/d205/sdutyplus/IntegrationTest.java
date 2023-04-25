package com.d205.sdutyplus;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Transactional
@SpringBootTest
public class IntegrationTest {

    @PersistenceContext
    protected EntityManager em;
}
