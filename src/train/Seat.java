package train;

public class Seat {

	/*
	 * The class contains the fields and methods for a seat object. Each seat
	 * will have number, seat Description and a customer object if it gets
	 * reserved otherwise reserved property will be set to null.
	 */

	private String number;
	private SeatDescription seatConfig;
	private Customer reserved;

	public Seat() {
	}

	// Constructor called to initialise each seat object in all seat vectors.
	public Seat(String num, char classType, char type, char ff, char table,
			char ease, String reserved) {
		this.number = num;
		this.seatConfig = new SeatDescription(classType, type, ff, table, ease);

		if (reserved != null) {
			this.reserved = new Customer(reserved);
		} else {
			this.reserved = null;
		}

	}

	public SeatDescription getSeatConfig() {
		return seatConfig;
	}

	public void setSeatConfig(SeatDescription seatConfig) {
		this.seatConfig = seatConfig;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public Customer getReserved() {
		return reserved;
	}

	public void setReserved(Customer reserved) {
		this.reserved = reserved;
	}

	public boolean equals(Seat other) {
		return this.number.equals(other.getNumber());
	}

	// when writing seat information back to file we need all fields.
	// Comma needed to read data back into vector(line is split at ',').
	public String toString() {
		return this.number + "," + this.seatConfig + "," + this.reserved;
	}
}
