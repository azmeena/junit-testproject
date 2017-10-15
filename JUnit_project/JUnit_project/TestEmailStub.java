package test;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;

/**
 * TestEmail class, which extends abstract class Email
 * <br/><br/>
 * Since Email is abstract, we can't instantiate it, so this class
 * allows us to instantiate Email objects for testing purposes
 * 
 * @author azmee
 *
 */
public class TestEmailStub extends Email {

	@Override
	public Email setMsg(String msg) throws EmailException {		
		return null;
	}

}