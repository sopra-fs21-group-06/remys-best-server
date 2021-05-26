package ch.uzh.ifi.hase.soprafs21.repository;

import ch.uzh.ifi.hase.soprafs21.constant.UserStatus;
import ch.uzh.ifi.hase.soprafs21.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
public class UserRepositoryIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;

    @Qualifier("userRepository")
    @Autowired
    private UserRepository userRepository;

    @Test
    public void findByUsername_success() {
        // given
        User user = new User();
        user.setUsername("MyUsername");
        user.setEmail("firstname@lastname");
        user.setPassword("12345678");
        user.setStatus(UserStatus.Offline);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername(user.getUsername());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getEmail(), user.getEmail());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findByUsername_error_usernameNotFound() {
        // given
        User user = new User();
        user.setUsername("MyUsername");
        user.setEmail("firstname@lastname");
        user.setPassword("12345678");
        user.setStatus(UserStatus.Offline);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername("MyUsername2");

        // then
        assertNull(found);
    }

    @Test
    public void findByEmail_success() {
        // given
        User user = new User();
        user.setUsername("MyUsername");
        user.setEmail("firstname@lastname");
        user.setPassword("12345678");
        user.setStatus(UserStatus.Offline);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByEmail(user.getEmail());

        // then
        assertNotNull(found.getId());
        assertEquals(found.getUsername(), user.getUsername());
        assertEquals(found.getEmail(), user.getEmail());
        assertEquals(found.getPassword(), user.getPassword());
        assertEquals(found.getToken(), user.getToken());
        assertEquals(found.getStatus(), user.getStatus());
    }

    @Test
    public void findByEmail_error_emailNotFound() {
        // given
        User user = new User();
        user.setUsername("MyUsername");
        user.setEmail("firstname@lastname");
        user.setPassword("12345678");
        user.setStatus(UserStatus.Offline);
        user.setToken("1");

        entityManager.persist(user);
        entityManager.flush();

        // when
        User found = userRepository.findByUsername("firstname2@lastname2");

        // then
        assertNull(found);
    }
}
