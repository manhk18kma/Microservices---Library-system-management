package KMA.TTCS.BorrowingService.command.api.controller;

import java.util.Date;
import java.util.UUID;

import KMA.TTCS.BorrowingService.command.api.command.CreateBorrowCommand;
import KMA.TTCS.BorrowingService.command.api.command.UpdateBookReturnCommand;
import KMA.TTCS.BorrowingService.command.api.data.Borrowing;
import KMA.TTCS.BorrowingService.command.api.model.BorrowRequestModel;
import KMA.TTCS.BorrowingService.command.api.service.BorrowService;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/borrowing")
public class BorrowCommandController {
	@Autowired
	private CommandGateway commandGateway;
	
	@Autowired
	private BorrowService borrowService;
	
	@PostMapping
	public String addBookBorrowing(@RequestBody BorrowRequestModel model) {
		try {
			CreateBorrowCommand command =
					new CreateBorrowCommand(model.getBookId(), model.getEmployeeId(), new Date(),UUID.randomUUID().toString());
				commandGateway.sendAndWait(command);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return "Book borrowing added";
	}
	@PutMapping
	public String updateBookReturn(@RequestBody BorrowRequestModel model) {

		Borrowing borrowing = borrowService.findIdBorrowing(model.getEmployeeId() , model.getBookId());
		UpdateBookReturnCommand command = new UpdateBookReturnCommand(
				borrowing.getId(),
				borrowing.getBookId(),
				borrowing.getEmployeeId(),
				new Date()
		);
		commandGateway.sendAndWait(command);
		return "Book returned";
	}
}
