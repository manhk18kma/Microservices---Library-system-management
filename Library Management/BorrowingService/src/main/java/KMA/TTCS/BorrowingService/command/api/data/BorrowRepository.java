package KMA.TTCS.BorrowingService.command.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(path = "borrowing")
public interface BorrowRepository extends JpaRepository<Borrowing, String>{
	List<Borrowing> findByEmployeeIdAndReturnDateIsNull(String employeeId);
	Borrowing findByEmployeeIdAndBookIdAndReturnDateIsNull(String employeeId,String bookId);

}