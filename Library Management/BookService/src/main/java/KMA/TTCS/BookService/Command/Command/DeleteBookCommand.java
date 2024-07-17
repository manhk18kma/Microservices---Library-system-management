package KMA.TTCS.BookService.Command.Command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

public class DeleteBookCommand {
    @TargetAggregateIdentifier
    private String id;
    public DeleteBookCommand() {
    }

    public DeleteBookCommand(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "DeleteBookCommand{" +
                "id='" + id + '\'' +
                '}';
    }
}
