package KMA.TTCS.NotificatioService;

import KMA.TTCS.NotificatioService.Model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

	@KafkaListener( topics = "ttcs")
	private void listener(Message messageDTO){
		System.out.println("Received " + messageDTO.toString());
	}
}
