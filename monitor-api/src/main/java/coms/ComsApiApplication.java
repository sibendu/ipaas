package coms;

import java.util.Date;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ComsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComsApiApplication.class, args);
	}
	
	@GetMapping("/live")
	public String index() {
		System.out.println("Inside hello");
		return new Date() + "\n";
	}

}
