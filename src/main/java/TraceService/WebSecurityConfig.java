package TraceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("frank").password("cobra").roles("ADMIN");
        auth.inMemoryAuthentication().withUser("maxi").password("cobra").roles("USER");
        auth.inMemoryAuthentication().withUser("fabian").password("cobra").roles("USER");
        auth.inMemoryAuthentication().withUser("christine").password("cobra").roles("USER");
        auth.inMemoryAuthentication().withUser("benny").password("cobra").roles("USER");
        auth.inMemoryAuthentication().withUser("jochen").password("cobra").roles("USER");
        auth.inMemoryAuthentication().withUser("evgeniy").password("cobra").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        http.csrf().disable();
    }
}