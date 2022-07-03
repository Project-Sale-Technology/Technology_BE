package com.technology_be.controller;

import com.technology_be.model.User;
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
import java.time.LocalDate;
import java.time.LocalDateTime;

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
        messageHelper.setFrom("gridshopvn@gmail.com", "GridShop - Technology Park");
        messageHelper.setTo(email);

        String subject = "Here's the link to reset your password";
        String content = "<h3>Dear Customer,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Click the link below to change your password:</p>"
                + "<p><a href=\"" + linkResetPassword + "\">Change my password</a></p>"
                + "<br>"
                + "<p>Ignore this email if you do remember your password, "
                + "or you have not made the request.</p>" +
                "Thank you for using our service";

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
            String linkResetPassword = "http://localhost:4200/customer/reset-password/" + token;

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

    /* Handle reset password */
    @RequestMapping(value = "/reset-password" , method = RequestMethod.PATCH)
    public ResponseEntity<User> handleResetPassword(@RequestParam(value = "token" , required = false) String resetToken ,
                                                    @RequestParam(value = "password" , required = false) String password) {
        String message = "";
        String token = resetToken;
        String newPassword = password;

        /* Get user with token */
        User user = userService.getUserByPasswordToken(token);

        /* Set date update */
        user.setUpdateAt(LocalDateTime.now());

        if(user == null) {
            message = "Cannot find token reset password of user";
            return new ResponseEntity(message , HttpStatus.BAD_REQUEST);
        } else {
            userService.updatePassword(user , newPassword);
        }

        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    /* Check page reset password by token */
    @RequestMapping(value = "/reset-password" , method = RequestMethod.GET)
    public ResponseEntity<User> checkResetPassword(@RequestParam(value = "token" , required = false) String token) {
        User user = userService.getUserByPasswordToken(token);
        if(user == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user , HttpStatus.OK);
    }

    /* Get previously used password by token reset pwd */
    @RequestMapping(value = "/reset-password/check-password" , method = RequestMethod.GET)
    public ResponseEntity<User> checkPasswordUsedBefore(@RequestParam(value = "token" , required = false) String token
    ,@RequestParam(value = "password" , required = false) String newPassword) {
        User user = userService.checkPsdUsed(token , newPassword);
        String message = "";

        if(user == null) {
            message = "You used this password recently. Please choose a different one.";
            return new ResponseEntity(message , HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(user , HttpStatus.OK);
    }
}
