package KMA.TTCS.BorrowingService.query.api.projection;

import java.util.ArrayList;
import java.util.List;

import org.axonframework.messaging.responsetypes.ResponseTypes;
import org.axonframework.queryhandling.QueryGateway;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import KMA.TTCS.BorrowingService.command.api.data.BorrowRepository;
import KMA.TTCS.BorrowingService.command.api.data.Borrowing;
import KMA.TTCS.BorrowingService.query.api.model.BorrowingResponseModel;
import KMA.TTCS.BorrowingService.query.api.queries.GetAllBorrowing;
import KMA.TTCS.BorrowingService.query.api.queries.GetListBorrowingByEmployeeQuery;


import KMA.TTCS.CommonService.model.BookResponseCommonModel;
import KMA.TTCS.CommonService.model.BorrowingResponseCommonModel;
import KMA.TTCS.CommonService.model.EmployeeResponseCommonModel;
import KMA.TTCS.CommonService.query.GetDetailsBookQuery;
import KMA.TTCS.CommonService.query.GetDetailsEmployeeQuery;
import KMA.TTCS.CommonService.query.GetListBorrowingByEmployee;

@Component
public class BorrowingProjection {

	@Autowired
	private BorrowRepository borrowRepository;

	@Autowired
	private QueryGateway queryGateway;

	@QueryHandler
	public List<BorrowingResponseModel> handle(GetListBorrowingByEmployeeQuery query){
		List<BorrowingResponseModel> list  = new ArrayList<>();
		List<Borrowing> listEntity = borrowRepository.findByEmployeeIdAndReturnDateIsNull(query.getEmployeeId());
		listEntity.forEach(s ->{
			BorrowingResponseModel model = new BorrowingResponseModel();
			BeanUtils.copyProperties(s, model);


			list.add(model);
		});
		return list;
	}
	@QueryHandler
	public List<BorrowingResponseCommonModel> handle(GetListBorrowingByEmployee query){
		List<BorrowingResponseCommonModel> list  = new ArrayList<>();
		List<Borrowing> listEntity = borrowRepository.findByEmployeeIdAndReturnDateIsNull(query.getEmployeeId());
		listEntity.forEach(s ->{
			BorrowingResponseCommonModel model = new BorrowingResponseCommonModel();
			BeanUtils.copyProperties(s, model);
			list.add(model);
		});
		return list;
	}
	@QueryHandler
	public List<BorrowingResponseModel> handle(GetAllBorrowing query){
		System.out.println("Projection Get All");
		List<BorrowingResponseModel> list  = new ArrayList<>();
		List<Borrowing> listEntity = borrowRepository.findAll();
		listEntity.forEach(s ->{
			BorrowingResponseModel model = new BorrowingResponseModel();
			BeanUtils.copyProperties(s, model);
			model.setNameBook(queryGateway.query(new GetDetailsBookQuery(model.getBookId()), ResponseTypes.instanceOf(BookResponseCommonModel.class)).join().getName());
			EmployeeResponseCommonModel employee = queryGateway.query(new GetDetailsEmployeeQuery(model.getEmployeeId()), ResponseTypes.instanceOf(EmployeeResponseCommonModel.class)).join();
			model.setNameEmployee(employee.getFirstName()+" "+employee.getLastName());
			list.add(model);
		});
		return list;
	}

}
