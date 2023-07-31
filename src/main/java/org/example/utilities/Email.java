package org.example.utilities;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
public class Email {


    public static void sendEmail(String email, String titel, String msg)
    {
        // change below lines accordingly
        String to = email;
        String from = Utilities.Email;
        String host = "mail.mailfour24.de"; // or IP address

        // Get the session object
        // Get system properties




        Properties prop = System.getProperties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "mail.mailfour24.de");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.trust", "mail.mailfour24.de");
        // Setup mail server
        prop.setProperty("mail.smtp.host", host);

        // Get the default Session object
        Session session = Session.getDefaultInstance(prop,  new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(Utilities.Email
                        , Utilities.EmailPWD);
            }
            });

        // compose the message
        try {

            // javax.mail.internet.MimeMessage class
            // is mostly used for abstraction.
            MimeMessage message = new MimeMessage(session);

            // header field of the header.
            message.setFrom(new InternetAddress(from, "Rares WG-Apply"));
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress(to));
            message.setSubject(titel);
            message.setText(msg);

            // Send message
            Transport.send(message);
            System.out.println("Email sent to " + email);
        }
        catch (MessagingException | UnsupportedEncodingException mex) {
            mex.printStackTrace();
        }
    }

}
