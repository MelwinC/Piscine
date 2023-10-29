package piscine;

import java.time.LocalDateTime;

public class Utilisation {
	
	public LocalDateTime date_utilisation;
	public Ticket ticket;
	
	public Utilisation() {
		super();
	}

	public Utilisation(LocalDateTime date_utilisation, Ticket ticket) {
		super();
		this.date_utilisation = date_utilisation;
		this.ticket = ticket;
	
	}

	public LocalDateTime getDate_utilisation() {
		return date_utilisation;
	}

	public void setDate_utilisation(LocalDateTime date_utilisation) {
		this.date_utilisation = date_utilisation;
	}

	public Ticket getTicket() {
		return ticket;
	}

	public void setTicket(Ticket ticket) {
		this.ticket = ticket;
	}

	@Override
	public String toString() {
		return "Utilisation [date_utilisation=" + date_utilisation + ", " + ticket + "]";
	}

}
