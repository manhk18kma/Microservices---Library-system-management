package KMA.TTCS.EmployeeService.Query.Projection;

import java.util.ArrayList;
import java.util.List;

import KMA.TTCS.CommonService.model.EmployeeResponseCommonModel;
import KMA.TTCS.CommonService.query.GetDetailsEmployeeQuery;
import KMA.TTCS.EmployeeService.Command.Data.Employee;
import KMA.TTCS.EmployeeService.Command.Data.EmployeeRepository;
import KMA.TTCS.EmployeeService.Query.Model.EmployeeReponseModel;
import KMA.TTCS.EmployeeService.Query.Queries.GetAllEmployeeQuery;
import KMA.TTCS.EmployeeService.Query.Queries.GetEmployeesQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class EmployeeProjection {
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@QueryHandler
    public EmployeeReponseModel handle(GetEmployeesQuery getEmployeesQuery) {
		EmployeeReponseModel model = new EmployeeReponseModel();
	 Employee employee = employeeRepository.getById(getEmployeesQuery.getEmployeeId());
      BeanUtils.copyProperties(employee, model);

        return model;
    }
	@QueryHandler
	public List<EmployeeReponseModel> handle(GetAllEmployeeQuery getAllEmployeeQuery){
		List<EmployeeReponseModel> listModel = new ArrayList<>();
		List<Employee> listEntity = employeeRepository.findAll();
		listEntity.stream().forEach(s -> {
			EmployeeReponseModel model = new EmployeeReponseModel();
			BeanUtils.copyProperties(s, model);
			listModel.add(model);
		});
		return listModel;
	}
	@QueryHandler
    public EmployeeResponseCommonModel handle(GetDetailsEmployeeQuery getDetailsEmployeeQuery) {
		EmployeeResponseCommonModel model = new EmployeeResponseCommonModel();
	 Employee employee = employeeRepository.getById(getDetailsEmployeeQuery.getEmployeeId());
      BeanUtils.copyProperties(employee, model);
        return model;
    }
}
