package gcv.test;

import static org.junit.Assert.*;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.embeddable.EJBContainer;

import org.junit.Test;

import gcv.beans.Person;
import gcv.dao.ISpecificDao;
import gcv.util.PasswordHash;

public class TestPasswordHash {
	
	@Test
	public void testRandomSalt() throws Exception {
		String password = "myPassword", password2 = "myPassword";
		
		String hash = PasswordHash.createHash(password);
		String hash2 = PasswordHash.createHash(password2);
		
		assertNotEquals(hash, hash2);
	}
	
	@Test
	public void testWrongPassword() throws Exception {
		String password = "myPassword", password2 = "notMyPassword";
		
		String hash = PasswordHash.createHash(password);
		
		assertFalse(PasswordHash.validatePassword(password2, hash));
	}
	
	@Test
	public void testGoodPassword() throws Exception {
		String password = "myPassword", password2 = "myPassword";
		
		String hash = PasswordHash.createHash(password);
		
		assertTrue(PasswordHash.validatePassword(password2, hash));
	}

}
