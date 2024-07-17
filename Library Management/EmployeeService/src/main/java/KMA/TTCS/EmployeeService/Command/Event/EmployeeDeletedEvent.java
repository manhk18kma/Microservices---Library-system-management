package KMA.TTCS.EmployeeService.Command.Event;

public class EmployeeDeletedEvent {
	private String employeeId;

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}
}
