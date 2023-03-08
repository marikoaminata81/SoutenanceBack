package com.bezkoder.spring.login;

import com.bezkoder.spring.login.models.ERole;
import com.bezkoder.spring.login.models.Role;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.repository.RoleRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import com.bezkoder.spring.login.security.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;


@SpringBootApplication
@EnableGlobalMethodSecurity(
prePostEnabled = true,
securedEnabled = true)

public class SpringBootLoginExampleApplication {
	//static PasswordEncoder encoder;


		public static void main(String[] args) {


		SpringApplication.run(SpringBootLoginExampleApplication.class, args);




		/*ApplicationContext ctx =SpringApplication.run(SpringBootLoginExampleApplication.class, args);
		UserRepository userRepository = ctx.getBean(UserRepository.class);
		RoleRepository roleRepository = ctx.getBean(RoleRepository.class);
		Role r1 = new Role(ERole.ROLE_USER);
		roleRepository.save(r1);
*/

		//User user1 = new User();
		/*User user1 = new User();
		user1.setId(1L);
		user1.setUsername("Sekou");
		user1.setEmail("dsekou166@gmail.com");
		user1.setPassword("dsekou166");
		user1.getRoles().add(r1);

		userRepository.save(user1);
*/
		//Role r1 = new Role(1L, ERole.ROLE_USER);



	}

	@Bean
	CommandLineRunner start(RoleRepository roleRepository, UserService userService) {
		if (roleRepository.findAll().size()==0){
			return args -> {
				roleRepository.save(new Role(null, ERole.ROLE_ADMIN));
				roleRepository.save(new Role(null, ERole.ROLE_USER));

				User user = new User();
				user.setNom("ADIAWIAKOYE");
				user.setPrenom("Ahmadou");
				user.setUsername("Diadie");
				user.setNumero("+22375468913");
				user.setEmail("adiawiakoye.le10@gmail.com");
				user.setIntro("ques ce que tu veux dire par intro");
				user.setPassword("Aa1010aA2000");
				user.setAdresse("yirimadio 1008 logts");
				user.setFollowerCount(0);
				user.setFollowingCount(0);
                userService.creeradmin(user);

			};
		}else {
			return null;
		}
	}

}

