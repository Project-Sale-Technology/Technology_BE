package     com.technology_be.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Qualifier("userService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
//        http
//                .authorizeRequests()
//                //Cấu hình cho các đuòng dẫn không cần xác thực
//                .antMatchers("/", "/login", "/register-account").permitAll()
//                //Cấu hình cho các đường dẫn đăng nhập bằng Role là Member, Admin
//                .antMatchers("/user/**").hasAnyRole("MEMBER", "ADMIN")
//                //cấu hình cho đường dẫn admin, chỉ có Role admin mới vào được
//                .antMatchers("/admin/**").hasRole("ADMIN")
//                .and()
//                //formlogin
//                .formLogin()
//                //Đường dẫn trả về trang authentication
//                .loginPage("/login")
//                .usernameParameter("email")
//                .passwordParameter("password")
//                //Nếu authentication thành công
//                .defaultSuccessUrl("/user")
//                //Nếu authentication thất bại
//                .failureUrl("/login?error")
//                //Nếu authentication thành công nhưng vào trang không đúng role
//                .and()
//                .exceptionHandling()
//                .accessDeniedPage("/403")
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/").permitAll()
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID")
//        ;

        http.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests().antMatchers("/home/**","/login", "/register").permitAll()
//                .antMatchers("/","/home/**").hasAnyRole("ADMIN" ,"MEMBER")
                //cấu hình cho đường dẫn admin, chỉ có Role admin mới vào được
                .antMatchers("/admin/**").hasRole("ADMIN")
                // all other requests need to be authenticated
                .anyRequest().authenticated().and();

        http.authorizeRequests().and() //
                .rememberMe().tokenRepository(this.persistentTokenRepository()) //
                .tokenValiditySeconds(1 * 24 * 60 * 60); // 24h

    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        InMemoryTokenRepositoryImpl memory = new InMemoryTokenRepositoryImpl();
        return memory;
    }
}