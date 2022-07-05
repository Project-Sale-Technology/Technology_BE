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
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
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

        /* Get user by email */
        User user = userService.getUserByEmail(email);

        String subject = "Here's the link to reset your password";
        String content = "<body style=\"padding: 0;margin: 0;\">" +
                "    <div style=\"width: 600px;" +
                "    display: flex;" +
                "    justify-content: center;" +
                "    margin: auto;" +
                "    flex-direction: column;\">" +
                "        <div style=\"padding: 20px;border: 1px solid #dadada;\">" +
                "            <p style=\"font-size: 16px;color: #000;\">Greetings, \""+ user.getName() + "\"</p>" +
                "            <p style=\"font-size: 16px;color: #000;\">We received a request to reset your password.<br>Click the button" +
                "                below to setup a new password</p>" +
                "" +
                "            <a href=\"" + linkResetPassword + "\" style=\"padding: 8px;display: inline-block;cursor: pointer; border-radius: 3px;" +
                "            font-size: 15px;text-decoration: none;background-color: #0167f3;color: #fff;font-weight: 500;\">Change" +
                "                my password</a>" +
                "" +
                "            <p>This link will expire after 10 minutes. If you didn't request a password reset , ingnore this email and continue using your current password." +
                "            <br>" +
                "            Thank you for using our service.<br>" +
                "            <br>If you have any question, Please contact us immediately at <a href=\"mailto:gridshopvn@gmail.com@gmail.com\">gridshopvn@gmail.com</a>" +
                "            </p>" +
                "            <p>Thanks you.</p>" +
                "            <p>Grid Shop Team.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>";

        messageHelper.setSubject(subject);
        messageHelper.setText(content, true);

        javaMailSender.send(message);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /* Process send to email */
    @RequestMapping(value = "/forgot-password", method = RequestMethod.POST)
    public ResponseEntity<String> processForgotPassword(@RequestBody String email , HttpServletResponse response) {
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
    public ResponseEntity<User> handleResetPassword(@RequestParam(value = "token") String resetToken ,
                                                    @RequestParam(value = "password") String password) {
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

    /* Get previously used password by token reset pwd*/
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
