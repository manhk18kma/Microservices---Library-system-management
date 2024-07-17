package KMA.TTCS.BorrowingService.command.api.service;

import KMA.TTCS.BorrowingService.command.api.data.BorrowRepository;
import KMA.TTCS.BorrowingService.command.api.data.Borrowing;
import KMA.TTCS.BorrowingService.command.api.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
@Service
public class BorrowService {
	@Autowired
	private BorrowRepository repository;
	@Autowired
	private KafkaTemplate<String , Object> kafkaTemplate;
	public void sendMessage(Message message) {
		System.out.println("Kafka Send "+message.toString());
		kafkaTemplate.send("ttcs",message);
	}
//	@KafkaListener(id = "ttcs", topics = "ttcs")
//	private void listener(String messageDTO){
//		System.out.println("Received " + messageDTO);
//	}


	public Borrowing findIdBorrowing(String employeeId , String bookId) {
	return	repository.findByEmployeeIdAndBookIdAndReturnDateIsNull(employeeId , bookId);
	}
}
