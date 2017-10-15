package test;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailConstants;
import org.apache.commons.mail.EmailException;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.properties.EncryptableProperties;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class EmailTestSuite {

	static List<InternetAddress> toEmails;

	Email testEmail = null;
	Session mailSession = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		toEmails = new ArrayList<InternetAddress>();
		InternetAddress toEmail = new InternetAddress();
		toEmail.setAddress("azmeenautsa@gmail.com");
		toEmails.add(toEmail);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		toEmails.clear();
		toEmails = null;
		
	}

	/**
	 * Setting up the stub prior to running each individual test
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testEmail = new TestEmailStub();
		testEmail.setSubject("Test email");
		testEmail.setCharset("UTF-8");
		testEmail.setHostName("localhost");
		testEmail.updateContentType(EmailConstants.TEXT_PLAIN);
		testEmail.setHostName("smtp.gmail.com");
		testEmail.setSmtpPort(587);
		testEmail.setStartTLSEnabled(true);
		testEmail.setStartTLSRequired(true);
		//testEmail.setDebug(true);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testUpdateContentTypeWithEmptyValue() {

		try {
			testEmail.getMailSession();
			testEmail.updateContentType("");
			// void method, so nothing really to assert if method doesn't throw
			// an exception
			assertTrue(true);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateContentTypeWithTextType() {

		try {
			testEmail.getMailSession();
			testEmail.updateContentType(EmailConstants.TEXT_HTML);
			// void method, so nothing really to assert if method doesn't throw
			// an exception
			assertTrue(true);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateContentTypeWithMimeTypeCharsetInMiddle() {

		try {
			testEmail.getMailSession();
			testEmail.updateContentType("mime/html; charset=" + EmailConstants.ISO_8859_1 + " ");
			// void method, so nothing really to assert if method doesn't throw
			// an exception
			assertTrue(true);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testUpdateContentTypeWithMimeTypeCharsetAtTheEnd() {

		try {
			testEmail.getMailSession();
			testEmail.updateContentType("mime/html; charset=" + EmailConstants.ISO_8859_1);
			// void method, so nothing really to assert if method doesn't throw
			// an exception
			assertTrue(true);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testGetMailSession() {
		try {
			testEmail.getMailSession();
			assertTrue(testEmail.getMailSession() != null);
			assertTrue(testEmail.getMailSession().getProperty(Email.MAIL_HOST).equals("smtp.gmail.com"));
			assertTrue(testEmail.getMailSession().getProperty(Email.MAIL_PORT).equals(testEmail.getSmtpPort()));
			assertTrue(testEmail.getMailSession().getProperty(Email.MAIL_SMTP_CONNECTIONTIMEOUT).equals("60000"));
			assertTrue(testEmail.getMailSession().getProperty(Email.MAIL_SMTP_TIMEOUT).equals("60000"));
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testSetFromString() {
		try {
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			assertTrue(testEmail.getFromAddress().getAddress() != null);
			assertTrue(testEmail.getFromAddress().getAddress().equals("azmeenautsa@gmail.com"));
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testAddCcString() {
		try {
			testEmail.getMailSession();
			testEmail.addCc("azmeenautsa@gmail.com");
			assertTrue(testEmail.getCcAddresses() != null);

			boolean emailFound = false;

			for (InternetAddress ccEmail : testEmail.getCcAddresses()) {
				if (ccEmail.getAddress() != null && ccEmail.getAddress().equals("azmeenautsa@gmail.com")) {
					emailFound = true;
				}
			}

			assertTrue(emailFound);

		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}

	}

	@Test
	public void testAddNullBccStringArray() {

		try {
			testEmail.getMailSession();
			testEmail.addBcc();
		} catch (EmailException e) {
			assertTrue(e.toString().endsWith("Address List provided was invalid"));
		}
	}

	@Test
	public void testAddBccStringArray() {
		try {
			testEmail.getMailSession();
			testEmail.addBcc("azmeenautsa@gmail.com", "azmeena_n@yahoo.com", "azmeena_n@yahoo.com",
					"azmeena786@gmail.com");
			assertTrue(testEmail.getBccAddresses() != null);

			List<String> addedEmails = new ArrayList<String>();
			addedEmails.add("azmeenautsa@gmail.com");
			addedEmails.add("azmeena_n@yahoo.com");
			addedEmails.add("azmeena_n@yahoo.com");
			addedEmails.add("azmeena786@gmail.com");

			for (InternetAddress bccEmail : testEmail.getBccAddresses()) {

				Iterator<String> emailIterator = addedEmails.iterator();

				// Remove each matching email in addedEmails we find in the
				// bccList
				// if they're all removed, then we found all of the added bcc
				// emails
				if (bccEmail.getAddress() != null) {
					String addedEmail = emailIterator.next();
					if (addedEmail.equals(bccEmail.getAddress())) {
						emailIterator.remove();
					}
				}
			}

			assertTrue(addedEmails.isEmpty());

		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testAddReplyTo() {

		try {
			testEmail.getMailSession();
			testEmail.addReplyTo("azmeenautsa@gmail.com");
			assertTrue(testEmail.getReplyToAddresses() != null);

			boolean emailFound = false;

			for (InternetAddress replyToEmail : testEmail.getReplyToAddresses()) {
				if (replyToEmail.getAddress() != null && replyToEmail.getAddress().equals("azmeenautsa@gmail.com")) {
					emailFound = true;
				}
			}

			assertTrue(emailFound);
		} catch (Exception e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testAddHeader() {

		try {
			testEmail.getMailSession();
			testEmail.addHeader(null, "VALUE");
		} catch (IllegalArgumentException e) {
			assertTrue(e.toString().endsWith("name can not be null or empty"));
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}

		try {
			testEmail.addHeader("NAME", null);
		} catch (IllegalArgumentException e) {
			assertTrue(e.toString().endsWith("value can not be null or empty"));
		}

		testEmail.addHeader("Test Email Header", "VALUE");
	}

	@Test
	public void testBuildMimeMessageWithNoFromAddress() {
		try {
			testEmail.getMailSession();
			// testEmail.setFrom("azmeenautsa@gmail.com");
			testEmail.setTo(toEmails);
			testEmail.buildMimeMessage();
		} catch (EmailException e) {
			assertTrue(e.toString().endsWith("From address required"));
		}
	}

	@Test
	public void testBuildMimeMessageWithNoRecipients() {
		try {
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			// testEmail.setTo(toEmails);
			testEmail.buildMimeMessage();
		} catch (EmailException e) {
			assertTrue(e.toString().endsWith("At least one receiver address required"));
		}
	}

	@Test
	public void testBuildMimeMessageWithPreexistingMimeMessage() {
		try {
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			testEmail.setTo(toEmails);
			testEmail.buildMimeMessage();
			testEmail.buildMimeMessage();
		} catch (IllegalStateException e) {
			assertTrue(e.toString().endsWith("The MimeMessage is already built."));
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testBuildMimeMessage() {
		try {
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			testEmail.setTo(toEmails);
			testEmail.addCc("azmeena_n@yahoo.com");
			testEmail.addBcc("azmeena_n@yahoo.com");
			testEmail.addReplyTo("azmeena786@gmail.com");
			testEmail.setContent(new String("Test content"), EmailConstants.TEXT_PLAIN);
			testEmail.addHeader("NAME", "VALUE");
			testEmail.buildMimeMessage();
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Test
	public void testSend() {

		try {
			StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
			encryptor.setPassword("jasypt");
			Properties emailProperties = new EncryptableProperties(encryptor);
			emailProperties.load(new FileInputStream("email.properties"));
			testEmail.setAuthentication(emailProperties.getProperty("emailAddress"),
					encryptor.decrypt(emailProperties.getProperty("emailPassword")));

		} catch (IOException e1) {
			e1.printStackTrace();
		}
		try {
			Properties properties = System.getProperties();
			properties.setProperty(Email.MAIL_HOST, "smtp.gmail.com");
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			testEmail.setTo(toEmails);
			testEmail.addCc("azmeena_n@yahoo.com");
			testEmail.addBcc("azmeena_n@yahoo.com");
			testEmail.addReplyTo("azmeena786@gmail.com");
			testEmail.setContent(new String("Test content"), EmailConstants.TEXT_PLAIN);
			testEmail.addHeader("NAME", "VALUE");
			testEmail.send();
		} catch (IllegalStateException e) {
			fail(e.toString());
			e.printStackTrace();
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testSendWithInvalidHostName() {
		try {
			testEmail.setHostName("localhost");
			testEmail.getMailSession();
			testEmail.setFrom("azmeenautsa@gmail.com");
			testEmail.setTo(toEmails);
			testEmail.addCc("azmeena_n@yahoo.com");
			testEmail.addBcc("azmeena_n@yahoo.com");
			testEmail.addReplyTo("azmeena786@gmail.com");
			testEmail.setContent(new String("Test content"), EmailConstants.TEXT_PLAIN);
			testEmail.addHeader("NAME", "VALUE");
			// should trigger EmailException since hostname is still localhost
			testEmail.send();
		} catch (IllegalStateException e) {
			fail(e.toString());
			e.printStackTrace();
		} catch (EmailException e) {
			assert (true);
		}
	}

	@Test
	public void testGetSentDate() {
		try {
			testEmail.getMailSession();
			Date sentDate = testEmail.getSentDate();
			assert(sentDate != null);
			testEmail.setSentDate(new Date());
			sentDate = testEmail.getSentDate();
			assert(sentDate != null);
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

	@Test
	public void testGetHostName() {
		try {
			String hostName = testEmail.getHostName();
			testEmail.getMailSession();
			hostName = testEmail.getHostName();
			assert (hostName != null && !hostName.isEmpty());
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}

	}

	@Test
	public void testGetSocketConnectionTimeout() {
		try {
			testEmail.getMailSession();
			Integer socketConnectionTimeout = testEmail.getSocketConnectionTimeout();
			assert (socketConnectionTimeout != null);
		} catch (EmailException e) {
			fail(e.toString());
			e.printStackTrace();
		}
	}

}
