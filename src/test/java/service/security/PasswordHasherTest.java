package service.security;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class PasswordHasherTest {

    @Test
    public void testHasher() {
        String password = "password_hasher_test";
        String passwordHash;
        boolean validation;
        try {
            passwordHash = PasswordHasher.hash(password);
            validation = PasswordHasher.validatePassword(password, passwordHash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
        Assert.assertNotEquals(password, passwordHash);
        Assert.assertTrue(validation);
    }

}
