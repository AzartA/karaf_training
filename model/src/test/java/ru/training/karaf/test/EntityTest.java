package ru.training.karaf.test;

import java.util.Date;
import org.junit.jupiter.api.AfterAll;
import ru.training.karaf.model.UserDO;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.training.karaf.model.AvatarDO;
import ru.training.karaf.model.UserNameDO;

public class EntityTest {

    private UserDO user = new UserDO();
    
    @BeforeAll
    static void init() {
        System.err.println("Tests are starting...");
    }
    
    @AfterAll
    static void shutDown() {
        System.err.println("Tests are shutting down");
    }
    
    @Test
    void throwsWhenGetFromNewInstance() {
        assertAll(() -> assertThrows(NullPointerException.class, () -> user.getAvatar().getPicture()),
                () -> assertThrows(NullPointerException.class, () -> user.getUserName().getFirstName()),
                () -> assertThrows(NullPointerException.class, () -> user.getUserName().getLastName()));
    }
    
    @Nested
    @DisplayName("After injecting values")
    class AfterInjecting {
        
        @BeforeEach
        void initializeUser() {
            user.setAvatar(new AvatarDO("Avatar".getBytes()));
            user.setLibCard("LibCard");
            user.setRegDate(new Date());
            user.setUserName(new UserNameDO());
        }
        
        @Test
        void avatarNotNull() {
            assertEquals("Avatar", new String(user.getAvatar().getPicture()));
        }
        
        @Test
        void libCardNotNull() {
            assertEquals("LibCard", user.getLibCard());
        }
        
        @Test
        void regDateAndUserNameNotNull() {
            assertAll(() -> assertNotNull(user.getRegDate()), 
                    () -> assertNotNull(user.getUserName()));
        }
    }
}
