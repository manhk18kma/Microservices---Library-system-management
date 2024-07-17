package KMA.TTCS.BookService.Command.Aggregate;

import KMA.TTCS.BookService.Command.Command.CreateBookCommand;
import KMA.TTCS.BookService.Command.Command.DeleteBookCommand;
import KMA.TTCS.BookService.Command.Command.UpdateBookCommand;
import KMA.TTCS.BookService.Command.Event.BookCreatedEvent;
import KMA.TTCS.BookService.Command.Event.BookDeletedEvent;
import KMA.TTCS.BookService.Command.Event.BookUpdatedEvent;
import KMA.TTCS.CommonService.command.RollBackStatusBookCommand;
import KMA.TTCS.CommonService.command.UpdateStatusBookCommand;
import KMA.TTCS.CommonService.event.BookRollBackStatusEvent;
import KMA.TTCS.CommonService.event.BookUpdateStatusEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;
import org.springframework.beans.BeanUtils;

import java.util.UUID;

@Aggregate
public class BookAggregate {

    @AggregateIdentifier
    private String bookId;
    private String name;
    private String author;
    private Boolean isReady;

    public BookAggregate() {
    }

    @CommandHandler
    public BookAggregate(CreateBookCommand createBookCommand) {

        BookCreatedEvent bookCreatedEvent
                = new BookCreatedEvent();
        BeanUtils.copyProperties(createBookCommand,
                bookCreatedEvent);
        AggregateLifecycle.apply(bookCreatedEvent);
    }
    @CommandHandler
    public void handle(UpdateBookCommand updateBookCommand) {
        BookUpdatedEvent event
                = new BookUpdatedEvent();
        BeanUtils.copyProperties(updateBookCommand,
                event);
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(BookUpdatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.name = event.getName();
        this.isReady = event.getIsReady();
    }
    @CommandHandler
    public void handle(DeleteBookCommand deleteBookCommand) {
        BookDeletedEvent event
                = new BookDeletedEvent();
        BeanUtils.copyProperties(deleteBookCommand,
                event);

        AggregateLifecycle.apply(event);
    }
    @CommandHandler
    public void handle(UpdateStatusBookCommand command) {
        BookUpdateStatusEvent event = new BookUpdateStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }
    @CommandHandler
    public void handle(RollBackStatusBookCommand command) {
        BookRollBackStatusEvent event = new BookRollBackStatusEvent();
        BeanUtils.copyProperties(command, event);
        AggregateLifecycle.apply(event);
    }
    @EventSourcingHandler
    public void on(BookUpdateStatusEvent event) {
        this.bookId = event.getBookId();
        this.isReady = event.getIsReady();
    }
    @EventSourcingHandler
    public void on(BookRollBackStatusEvent event) {
        this.bookId = event.getBookId();
        this.isReady = event.getIsReady();
    }
    @EventSourcingHandler
    public void on(BookCreatedEvent event) {
        this.bookId = event.getBookId();
        this.author = event.getAuthor();
        this.isReady = event.getIsReady();
        this.name = event.getName();
    }

    @EventSourcingHandler
    public void on(BookDeletedEvent event) {
        this.bookId = event.getId();
    }

}
