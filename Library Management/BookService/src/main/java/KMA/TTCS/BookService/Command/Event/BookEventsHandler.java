package KMA.TTCS.BookService.Command.Event;

import KMA.TTCS.BookService.Command.Data.Book;
import KMA.TTCS.BookService.Command.Data.BookRepository;
import KMA.TTCS.CommonService.event.BookRollBackStatusEvent;
import KMA.TTCS.CommonService.event.BookUpdateStatusEvent;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BookEventsHandler {

	@Autowired
	private BookRepository bookRepository;
	
	@EventHandler
    public void on(BookCreatedEvent event) {
       Book book = new Book();
        BeanUtils.copyProperties(event,book);
        bookRepository.save(book);
    }
	@EventHandler
    public void on(BookUpdatedEvent event) {
		 Book book = bookRepository.getById(event.getBookId());
	       book.setAuthor(event.getAuthor());
	       book.setName(event.getName());
	       book.setIsReady(event.getIsReady());
	        bookRepository.save(book);
    }
	@EventHandler
	public void on(BookDeletedEvent event) {
		String bookId = event.getId();
		if (bookId != null) {
			bookRepository.deleteById(bookId);
		} else {
			System.out.println("Book ID is null. Unable to delete.");
		}
	}

	@EventHandler
	public void on(BookUpdateStatusEvent event) {
		Book book = bookRepository.getById(event.getBookId());
		book.setIsReady(event.getIsReady());
//		book.setIsReady(true);
		System.out.println(bookRepository.save(book).getIsReady());
	}
	@EventHandler
	public void on(BookRollBackStatusEvent event) {
		Book book = bookRepository.getById(event.getBookId());
		book.setIsReady(event.getIsReady());
		bookRepository.save(book);
	}
}
