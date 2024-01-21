package es.netmind.mypersonalbankapi;

import es.netmind.mypersonalbankapi.config.SpringConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
@EnableAutoConfiguration
public class AppTest {

    @PersistenceContext
    EntityManager em;

    @Test
    public void load() {
        assertNotNull(em);
        assertTrue(true);
    }
}

/*@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {SpringConfig.class})
public class AppTest {
    private EntityManager entityManager;

    @BeforeEach
    public void setUp() throws Exception {
        entityManager = JPAUtil.getEntityManagerFactory().createEntityManager();
//        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void tearDown() {
//        entityManager.getTransaction().rollback();
        entityManager.close();
    }

    @Test
    public void load() {
        assertTrue(true);
    }
}*/
