package KMA.TTCS.BookService.Command.Event;

public class BookDeletedEvent {
	private String id;


	public BookDeletedEvent(String id) {
		this.id = id;
	}

	public BookDeletedEvent() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
