package KMA.TTCS.BorrowingService.command.api.saga;

import KMA.TTCS.BorrowingService.command.api.command.DeleteBorrowCommand;
import KMA.TTCS.BorrowingService.command.api.command.SendMessageCommand;
import KMA.TTCS.BorrowingService.command.api.events.BorrowCreatedEvent;
import KMA.TTCS.BorrowingService.command.api.events.BorrowDeletedEvent;
import KMA.TTCS.BorrowingService.command.api.events.BorrowingUpdateBookReturnEvent;
import KMA.TTCS.CommonService.command.RollBackStatusBookCommand;
import KMA.TTCS.CommonService.command.UpdateStatusBookCommand;
import KMA.TTCS.CommonService.event.BookRollBackStatusEvent;
import KMA.TTCS.CommonService.event.BookUpdateStatusEvent;
import KMA.TTCS.CommonService.model.BookResponseCommonModel;
import KMA.TTCS.CommonService.model.EmployeeResponseCommonModel;
import KMA.TTCS.CommonService.query.GetDetailsBookQuery;
import KMA.TTCS.CommonService.query.GetDetailsEmployeeQuery;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.modelling.saga.EndSaga;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

@Saga
public class BorrowingSaga {

	@Autowired
	private transient CommandGateway commandGateway;

	@Autowired
	private transient QueryGateway queryGateway;

	@StartSaga
	@SagaEventHandler(associationProperty = "id")
	private void handle(BorrowCreatedEvent event) {
		System.out.println("BorrowCreatedEvent in Saga for BookId : "+event.getBookId()+ " : EmployeeId :  "+event.getEmployeeId());

		try {
			SagaLifecycle.associateWith("bookId", event.getBookId());

			GetDetailsBookQuery getDetailsBookQuery = new GetDetailsBookQuery(event.getBookId());

			BookResponseCommonModel bookResponseModel =
					queryGateway.query(getDetailsBookQuery,
									ResponseTypes.instanceOf(BookResponseCommonModel.class))
							.join();
			if(bookResponseModel.getIsReady() == true) {
				UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), false,event.getEmployeeId(),event.getId());
				commandGateway.sendAndWait(command);
			}
			else {

				throw new Exception("Sach Da co nguoi Muon");
			}


		} catch (Exception e) {
			rollBackBorrowRecord(event.getId());

			System.out.println(e.getMessage());
		}
	}
	@StartSaga
	@SagaEventHandler(associationProperty = "id")
	private void handle(BorrowingUpdateBookReturnEvent event) {
		System.out.println("BorrowingUpdateBookReturnEvent in Saga for borrowing Id : "+event.getId());
		try {
			UpdateStatusBookCommand command = new UpdateStatusBookCommand(event.getBookId(), true,event.getEmployee(),event.getId());
			commandGateway.sendAndWait(command);
			commandGateway.sendAndWait(new SendMessageCommand(event.getId(), event.getEmployee(), "Da tra sach thanh cong !"));
			SagaLifecycle.end();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e.getMessage());
		}
	}


	private void rollBackBorrowRecord(String id) {
		commandGateway.sendAndWait(new DeleteBorrowCommand(id));
	}
	@SagaEventHandler(associationProperty = "bookId")
	private void handle(BookUpdateStatusEvent event) {
		System.out.println("BookUpdateStatusEvent in Saga for BookId : "+event.getBookId());
		try {
			GetDetailsEmployeeQuery getDetailsEmployeeQuery = new GetDetailsEmployeeQuery(event.getEmployeeId());

			EmployeeResponseCommonModel employeeResponseCommonModel =
					queryGateway.query(getDetailsEmployeeQuery,
									ResponseTypes.instanceOf(EmployeeResponseCommonModel.class))
							.join();
			if(employeeResponseCommonModel.getIsDisciplined()==true) {
				throw new Exception("Nhan vien bi ky luat");
			}else {
				commandGateway.sendAndWait(new SendMessageCommand(event.getBorrowId(), event.getEmployeeId(), "Da muon sach thanh cong !"));
				SagaLifecycle.end();
			}
		} catch (Exception e) {

			System.out.println(e.getMessage());
			rollBackBookStatus(event.getBookId(),event.getEmployeeId(),event.getBorrowId());
		}
	}
	private void rollBackBookStatus(String bookId,String employeeId,String borrowId) {
		SagaLifecycle.associateWith("bookId", bookId);
		RollBackStatusBookCommand command = new RollBackStatusBookCommand(bookId, true,employeeId,borrowId);
		commandGateway.sendAndWait(command);
	}
	@SagaEventHandler(associationProperty = "bookId")
	public void handleRollBackBookStatus(BookRollBackStatusEvent event) {
		System.out.println("BookRollBackStatusEvent in Saga for book Id : {} " + event.getBookId());
		rollBackBorrowRecord(event.getBorrowId());
	}

	@SagaEventHandler(associationProperty = "id")
	@EndSaga
	public void handle(BorrowDeletedEvent event) {
		System.out.println("BorrowDeletedEvent in Saga for Borrowing Id : {} " +
				event.getId());
		SagaLifecycle.end();
	}
}
