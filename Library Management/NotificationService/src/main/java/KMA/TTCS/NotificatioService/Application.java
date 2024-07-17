package KMA.TTCS.NotificatioService;

import com.fasterxml.jackson.annotation.JsonMerge;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}


	@Bean
	JsonMessageConverter jsonMessageConverter(){
		return new JsonMessageConverter();
	}
}
