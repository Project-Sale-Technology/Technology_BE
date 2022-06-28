package com.technology_be.controller;

import com.technology_be.service.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@RestController
@CrossOrigin("http://localhost:4200/")
@RequestMapping(value = "/account")
public class ForgetPasswordController {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private UserService userService;

    /* Send email */
    private ResponseEntity<Void> sendEmail(String email , String linkResetPassword) throws UnsupportedEncodingException , MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message, "UTF-8");

        message.addHeader("Content-type", "text/HTML; charset=UTF-8");
        message.addHeader("format", "flowed");
        message.addHeader("Content-Transfer-Encoding", "8bit");
        /* Create form */
        messageHelper.setFrom("nguyenhanhtuan1206@gmail.com", "GridShop - Sale Technology");
        messageHelper.setTo(email);

        String subject = "Here's the link to reset your password";
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + linkResetPassword + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>";

        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        javaMailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Process send to email */
    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<String> processForgotPassword(@RequestBody String email) {
        String message = "";
        String emailCon = email;

        /* Create token */
        String token = RandomString.make(45);
        try {
            /* Check account */
            userService.setPasswordToken(token, emailCon);
            String linkResetPassword = "http://localhost:4200/customer/forgot-password/reset-password/" + token;

            sendEmail(emailCon, linkResetPassword);
            message = "We have sent a reset password link to your email. Please check.";
        } catch (UsernameNotFoundException ex) {
            message = ex.getMessage();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedEncodingException | MessagingException e) {
            message = "Error when sending to mail";
            return new ResponseEntity<>(message , HttpStatus.BAD_REQUEST);
        } catch (Exception exception) {
            exception.printStackTrace();
            return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(token , HttpStatus.OK);
    }
}
