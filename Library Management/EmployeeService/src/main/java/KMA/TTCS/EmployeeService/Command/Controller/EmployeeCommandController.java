package KMA.TTCS.EmployeeService.Command.Controller;
import java.util.UUID;
import KMA.TTCS.EmployeeService.Command.Command.CreateEmployeeCommand;
import KMA.TTCS.EmployeeService.Command.Command.DeleteEmployeeCommand;
import KMA.TTCS.EmployeeService.Command.Command.UpdateEmployeeCommand;
import KMA.TTCS.EmployeeService.Command.Event.KafkaProducerService;
import KMA.TTCS.EmployeeService.Command.Model.EmployeeRequestModel;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeCommandController {

	@Autowired
	private  CommandGateway commandGateway;
	@Autowired
	private KafkaProducerService kafkaProducerService;


	@PostMapping
	public String addEmployee(@RequestBody EmployeeRequestModel model) {
		CreateEmployeeCommand command =
				new CreateEmployeeCommand(UUID.randomUUID().toString(),model.getFirstName(), model.getLastName(), model.getKin(), false);

		commandGateway.sendAndWait(command);

		return "emmployee added";
	}
	@PutMapping
	public String updateEmployee(@RequestBody EmployeeRequestModel model) {
		UpdateEmployeeCommand command =
				new UpdateEmployeeCommand(model.getEmployeeId(),model.getFirstName(),model.getLastName(),model.getKin(),model.getIsDisciplined());
		commandGateway.sendAndWait(command);
		return "employee updated";
	}
	@DeleteMapping("/{employeeId}")
	public String deleteEmployee(@PathVariable String employeeId) {
		DeleteEmployeeCommand command = new DeleteEmployeeCommand(employeeId);
		commandGateway.sendAndWait(command);
		return "emmployee deleted";
	}
	@PostMapping("/send-message")
	public void sendMessageToKafka(@RequestBody String message) {
		kafkaProducerService.sendMessage(message);
	}

}
