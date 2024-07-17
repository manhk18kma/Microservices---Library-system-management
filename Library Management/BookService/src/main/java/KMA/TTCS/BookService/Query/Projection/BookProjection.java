package KMA.TTCS.BookService.Query.Projection;

import java.util.ArrayList;
import java.util.List;

import KMA.TTCS.BookService.Command.Data.Book;
import KMA.TTCS.BookService.Command.Data.BookRepository;
import KMA.TTCS.BookService.Query.Model.BookResponseModel;
import KMA.TTCS.BookService.Query.Queries.GetAllBooksQuery;
import KMA.TTCS.BookService.Query.Queries.GetBookQuery;
import KMA.TTCS.CommonService.model.BookResponseCommonModel;
import KMA.TTCS.CommonService.query.GetDetailsBookQuery;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class BookProjection {
	@Autowired
	private BookRepository bookRepository;
	
	 @QueryHandler
	    public BookResponseModel handle(GetBookQuery getBooksQuery) {
		 BookResponseModel model = new BookResponseModel();
		 Book book = bookRepository.getById(getBooksQuery.getBookId());
	      BeanUtils.copyProperties(book, model);
	        return model;
	    }
	 @QueryHandler List<BookResponseModel> handle(GetAllBooksQuery getAllBooksQuery){
		 List<Book> listEntity = bookRepository.findAll();
		 List<BookResponseModel> listbook = new ArrayList<>();
		 listEntity.forEach(s -> {
			 BookResponseModel model = new BookResponseModel();
			 BeanUtils.copyProperties(s, model);
			 listbook.add(model);
		 });
		 return listbook;
	 }
	 @QueryHandler
	    public BookResponseCommonModel handle(GetDetailsBookQuery getDetailsBookQuery) {
		 BookResponseCommonModel model = new BookResponseCommonModel();
		 Book book = bookRepository.getById(getDetailsBookQuery.getBookId());
	      BeanUtils.copyProperties(book, model);

	        return model;
	    }
}
