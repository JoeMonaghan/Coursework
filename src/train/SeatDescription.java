package train;

public class SeatDescription {

	private char classType;
	// window, aisle or single
	private char type;
	private char facing;
	private char table;
	private char easeOfAccess;

	public SeatDescription() {
	}

	public SeatDescription(char classType, char type, char facing, char table,
			char easeOfAceess) {
		this.classType = classType;
		this.type = type;
		this.facing = facing;
		this.table = table;
		this.easeOfAccess = easeOfAceess;
	}

	public char getClassType() {
		return classType;
	}

	public void setClassType(char classType) {
		this.classType = classType;
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public char getFacing() {
		return facing;
	}

	public void setFacing(char facingForward) {
		this.facing = facingForward;
	}

	public char getTable() {
		return table;
	}

	public void setTable(char table) {
		this.table = table;
	}

	public char getEaseOfAccess() {
		return easeOfAccess;
	}

	public void setEaseOfAccess(char easeOfAccess) {
		this.easeOfAccess = easeOfAccess;
	}

	// Check that each attribute matches if so return true.
	// Needed when for reservation option one.
	public boolean equals(SeatDescription otherSeat) {
		return ((this.classType == otherSeat.classType)
				&& (this.type == otherSeat.type)
				&& (this.facing == otherSeat.facing)
				&& (this.table == otherSeat.table) && (this.easeOfAccess == otherSeat.easeOfAccess));
	}

	// The correct format when writing information back to file.
	public String toString() {
		return this.classType + "," + this.type + "," + this.facing + ","
				+ this.table + "," + this.easeOfAccess;
	}

}
