package train;

import java.util.Vector;

public class Customer {

	/*
	 * The class contains the fields and methods for a Customer object. Each
	 * Customer will have an email, seat Description if they select reservation
	 * option 1 and a list of seats they have reserved.
	 */
	private String email;
	private SeatDescription preferredSeat;
	private Vector<Seat> reservations;

	public Customer() {
	}

	// When cancelling a seat and reserving multiple seats this constructor is
	// called.
	public Customer(String email) {
		this.email = email;
		reservations = new Vector<Seat>();
	}

	// When reserving a seat based on preference this constructor is called.
	public Customer(String email, char classType, char type, char ff,
			char table, char ease) {
		this.email = email;
		preferredSeat = new SeatDescription(classType, type, ff, table, ease);
		reservations = new Vector<Seat>();
	}

	public boolean equals(Customer other) {
		return this.email.equals(other.email);
	}

	public Vector<Seat> getReservations() {
		return reservations;
	}

	public void setReservations(Vector<Seat> reservation) {
		this.reservations = reservation;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public SeatDescription getPreferredSeat() {
		return preferredSeat;
	}

	public void setPreferredSeat(SeatDescription preferredSeat) {
		this.preferredSeat = preferredSeat;
	}

	public String toString() {
		return this.email;
	}

}
