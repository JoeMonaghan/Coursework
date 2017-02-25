package train;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.Vector;

import javax.swing.JOptionPane;

public class Carriage {
	
	/*
	 * The Carriage class contains most of the programs logic (i.e Reserve seat cancelling seat).
	 * 
	 */

	// Contains all seats that are not yet reserved.
	private Vector<Seat> unreservedSeatingList;

	// Contains list of customers and preferred seat description.
	private Vector<Customer> waitingList;

	// Contains all customers that has a reservation.
	private Vector<Customer> customerList;

	// Contains all reserved seats.
	private Vector<Seat> reservedSeatingList;

	// Contains all seats.
	private Vector<Seat> seatingList;

	private static Carriage _me = null;

	private Carriage() {
		// Initialise all vectors for program.
		waitingList = new Vector<Customer>();
		reservedSeatingList = new Vector<Seat>();
		unreservedSeatingList = new Vector<Seat>();
		seatingList = new Vector<Seat>();
		customerList = new Vector<Customer>();
		this.initSeatingLists();
		this.initWaitingList();
		this.initCustomerList();
	}

	// There is only one carriage object in this program.
	public static Carriage getInstance() {
		if (_me == null) {
			_me = new Carriage();
		}
		return _me;
	}

	/**
	 * Checks if the customer has already used the program before. If so add all
	 * previous reservations to customer.
	 * 
	 * @param customer
	 * @return 
	 */
	private Customer getPastReservations(Customer customer) {

		if (customerList.size() != 0) {
			for (Customer existingCustomer : new Vector<Customer>(customerList)) {
				if (existingCustomer.equals(customer)) {
					if (existingCustomer.getReservations().size() != 0) {
						for (Seat seat : existingCustomer.getReservations()) {
							customer.getReservations().add(seat);
						}
					}
					// Remove existing customer or there will duplicates of the
					// same customer.
					customerList.remove(existingCustomer);
					break;
				}
			}

		}
		return customer;
	}

	/**
	 * 
	 * Reserves a single seat by looping through unreservedSeatingList and
	 * checks does each seat description and customer's seat description match.
	 * If so that seat is reserved by the customer. Give the user the option to
	 * reserve a seat with one missing requirement if no exact match has been
	 * found. Add customer to waiting list if no match found or refuse next best
	 * match.
	 * 
	 * 
	 * @param customer
	 * @return message based on success of booking.
	 */

	public String reserveSeat(Customer customer) {

		// Get past reservations
		customer = getPastReservations(customer);
		// Loop through copy as an element may need to be removed.
		for (Seat seat : new Vector<Seat>(unreservedSeatingList)) {
			if (seat.getSeatConfig().equals(customer.getPreferredSeat())) {
				seat.setReserved(customer);

				// Add seat to customer reservation list.
				customer.getReservations().add(seat);

				// Seat must be added to the reserved seating list as it is now
				// reserved and removed from unreserved seating list.
				reservedSeatingList.add(seat);
				unreservedSeatingList.remove(seat);
				customerList.add(customer);

				// Return successful message to the user with seat number they
				// reserved.
				return customer.getEmail() + " you have reserved seat number "
						+ seat.getNumber();
			}
		}

		/*
		 * Check for a next best match as no exact match was found. Check each
		 * attribute and if 4 out 5 are matched give the user the option to book
		 * that seat. Inform user of the missing attribute to help them decide.
		 */

		for (Seat seat : new Vector<Seat>(unreservedSeatingList)) {

			String missingRequirement = "";

			int matchCounter = 0;

			if (!(seat.getSeatConfig().getClassType() == customer
					.getPreferredSeat().getClassType())) {

				if (seat.getSeatConfig().getClassType() == '1')
					missingRequirement = " is in first class";
				else
					missingRequirement = " is in standard class";
			} else {
				matchCounter++;
			}

			if (!(seat.getSeatConfig().getType() == customer.getPreferredSeat()
					.getType())) {
				char temp = seat.getSeatConfig().getType();
				switch (temp) {
				case 'W':
					missingRequirement = " is a window seat";
					break;
				case 'A':
					missingRequirement = " is an aisle seat";
					break;
				case 'S':
					missingRequirement = " is a single seat";
					break;
				default:

				}
			} else {
				matchCounter++;
			}
			if (!(seat.getSeatConfig().getFacing() == customer
					.getPreferredSeat().getFacing())) {
				if (seat.getSeatConfig().getFacing() == 'B') {
					missingRequirement = " is backward facing";
				} else {
					missingRequirement = " is forward facing";
				}
			} else {
				matchCounter++;
			}
			if (!(seat.getSeatConfig().getTable() == customer
					.getPreferredSeat().getTable())) {
				if (seat.getSeatConfig().getTable() == 'N') {
					missingRequirement = " has no table";
				} else {
					missingRequirement = " has a table";
				}
			} else {
				matchCounter++;
			}
			if (!(seat.getSeatConfig().getEaseOfAccess() == customer
					.getPreferredSeat().getEaseOfAccess())) {
				if (seat.getSeatConfig().getEaseOfAccess() == 'N') {
					missingRequirement = " is not an ease of accees seat";
				} else {
					missingRequirement = " is an ease of access seat";
				}
			} else {
				matchCounter++;
			}

			if (matchCounter == 4) {
				// There has been a 4 out of 5 match.
				// Display message to user and get answer.
				int reply = JOptionPane.showConfirmDialog(null,
						"No exact match found but there is a seat available but it"
								+ missingRequirement
								+ ".\nWould you like to book this seat?",
						"Next Best Match", JOptionPane.YES_NO_OPTION);
				// If the user clicked 'Yes' reserve that seat.
				// Update vectors with changes.
				if (reply == 0) {
					seat.setReserved(customer);
					reservedSeatingList.add(seat);
					unreservedSeatingList.remove(seat);
					customer.getReservations().add(seat);
					customerList.add(customer);
					return "Reservation Successful!\nYou have booked seat number "
							+ seat.getNumber();
				} else {
					// only allow the chance to reserve the first found next
					// best match seat.
					break;
				}

			}

		}
		// Add the customer to the waiting list as they were unable to find a
		// match.
		waitingList.add(customer);
		return customer.getEmail()
				+ " as no match was found you have been added to waiting list.";

	}

	/**
	 * Reserves multiple seats based on the seat numbers correctly entered.
	 * Return any seats numbers that could not be reserved. There is opportunity
	 * to join waiting list or Next best match feature as option clearly
	 * instructs user to reserve based on seats in list.
	 * 
	 * @param customer
	 * @param seats
	 * @return Message for multiple seats.
	 */
	public String reserveMultipleSeats(Customer customer, String[] seats) {

		// get past reservations
		customer = getPastReservations(customer);
		

		// Remove duplicates values in case user entered any.
		Vector<String> seatsV = new Vector<String>(Arrays.asList(seats));
		Set<String> set = new HashSet<String>();
		set.addAll(seatsV);
		seatsV.clear();
		seatsV.addAll(set);
		

		String seatNumbersReserved = "";
		String seatNumbersNotReserved = "";
		boolean seatRerserved = false;

		for (int i = 0; i < seatsV.size(); i++) {
			for (Seat seat : new Vector<Seat>(unreservedSeatingList)) {
				seatRerserved = false;
				if (seat.getNumber().equals(seatsV.elementAt(i))) {
					customer.getReservations().add(seat);
					seat.setReserved(customer);
					seatNumbersReserved += seat.getNumber() + " - ";
					reservedSeatingList.add(seat);
					unreservedSeatingList.remove(seat);
					seatRerserved = true;
					break;
				}
			}
			if (seatRerserved == false) {
				seatNumbersNotReserved += seatsV.elementAt(i) + " - ";
			}
		}

		// Inform the user of any seats that could not be reserved.
		if (!(seatNumbersNotReserved.equals(""))) {
			seatNumbersNotReserved = "Seat : " + seatNumbersNotReserved
					+ " could not be reserved. As it may already be reserved.";
		}
		// Inform the user of any successful reservations
		if(!(seatNumbersReserved.equals(""))){
			seatNumbersReserved = customer.getEmail()+" you have reserved seat : "+seatNumbersReserved;
		}

		// Add customer to customerList only if they managed to reserve one or
		// more seats.
		if (customer.getReservations().size() != 0) {
			customerList.add(customer);
		}
		
		return  seatNumbersReserved + "\n" + seatNumbersNotReserved;
	}

	/**
	 * Cancel all seats in the customer's reservation.
	 * 
	 * @param customer
	 * @return message based on success of cancellation. 
	 */
	public String cancelReservation(Customer customer) {
		String str = "";
		boolean matchFound = false;
		if (customerList.size() != 0) {
			for (Customer existingCustomer : new Vector<Customer>(customerList)) {
				if (existingCustomer.equals(customer)) {
					for (Seat seat : existingCustomer.getReservations()) {
						// Get each each seat number to inform user of the
						// seat/s they have cancelled.
						str += seat.getNumber() + " - ";
						matchFound = false;
						if (waitingList.size() != 0) {
							// check waiting list for any matching description
							// for each cancelled seat.
							for (Customer c : new Vector<Customer>(waitingList)) {
								if (c.getPreferredSeat().equals(
										seat.getSeatConfig())) {
									seat.setReserved(c);
									c.getReservations().add(seat);
									// waiting list customer has now a
									// reservation so add them to customer list.
									customerList.add(c);
									waitingList.remove(c);
									matchFound = true;
									break;
								}
							}

						}

						if (!matchFound) {
							seat.setReserved(null);
							reservedSeatingList.remove(seat);
							unreservedSeatingList.add(seat);
						}

					}
					// remove customer from the list as they will have no
					// reserved seats now.
					customerList.remove(existingCustomer);
					return existingCustomer.getEmail()
							+ " you have cancelled seat : " + str;
				}
			}
		}
		return "You do not have a reservation";

	}

	/**
	 * If the seat is reserved add seat to reserved seating list. If it is not
	 * add the seat to the unreserved seating list.
	 */
	private void initSeatingLists() {
		try {
			String strTemp;
			String[] temp = new String[7];
			Scanner s = new Scanner(new FileReader("seats.txt"));

			while (s.hasNextLine()) {
				strTemp = s.nextLine();
				temp = strTemp.split(",");
				String seatNum = temp[0];
				char classnum = temp[1].charAt(0);
				char type = temp[2].charAt(0);
				char facing = temp[3].charAt(0);
				char table = temp[4].charAt(0);
				char easeOfAccess = temp[5].charAt(0);
				String customerEmail = temp[6];

				// Add seat to the correct vector.
				if (customerEmail.equals("null")) {
					customerEmail = null;
					unreservedSeatingList.add(new Seat(seatNum, classnum, type,
							facing, table, easeOfAccess, customerEmail));
				} else {
					reservedSeatingList.add(new Seat(seatNum, classnum, type,
							facing, table, easeOfAccess, customerEmail));
				}
				// A seat vector with all seats no matter if there are reserved
				// or not will allow seat information to be easily written back
				// to file on program closure.
				seatingList.add(new Seat(seatNum, classnum, type, facing,
						table, easeOfAccess, customerEmail));
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Creates a customer list based on file customerList.txt contents.
	 * 
	 */
	private void initCustomerList() {

		try {
			Scanner s = new Scanner(new FileReader("customerList.txt"));

			while (s.hasNextLine()) {
				String[] temp = s.nextLine().split(",");
				// Customer name is first element in each line.
				Customer customer = new Customer(temp[0]);
				// Every other piece of data is the seat numbers of the
				// customer's reservation.
				for (int i = 1; i < temp.length; i++) {
					for (Seat seat : reservedSeatingList) {
						if (seat.getNumber().equals(temp[i])) {
							customer.getReservations().add(seat);
							break;
						}
					}
				}
				// Add each customer the customer list.
				customerList.add(customer);
			}
			s.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates a customer list based on file customerList.txt contents.
	 * 
	 */
	private void initWaitingList() {

		try {
			String strTemp;
			String[] temp = new String[7];
			Scanner s = new Scanner(new FileReader("waitingList.txt"));
			if (s.hasNext()) {
				while (s.hasNextLine()) {
					strTemp = s.nextLine();
					temp = strTemp.split(",");
					String email = temp[0];
					char classType = temp[1].charAt(0);
					char type = temp[2].charAt(0);
					char fowardFacing = temp[3].charAt(0);
					char table = temp[4].charAt(0);
					char easeOfAccess = temp[5].charAt(0);

					waitingList.add(new Customer(email, classType, type,
							fowardFacing, table, easeOfAccess));
				}
			}
			s.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Gets the list of available seats.
	 * 
	 * @return string of available seats
	 */
	public String printUnreservedSeats() {
		String str = "--AVAILABLE SEATS--\n";
		str += "--" + unreservedSeatingList.size() + " seats are free\n";

		for (Seat seat : unreservedSeatingList) {
			str += "-Seat Number: " + seat.getNumber() + " | Class: "
					+ seat.getSeatConfig().getClassType() + " | Type: "
					+ seat.getSeatConfig().getType() + " | Facing: "
					+ seat.getSeatConfig().getFacing() + " | Table: "
					+ seat.getSeatConfig().getTable() + " | Ease Of Access: "
					+ seat.getSeatConfig().getEaseOfAccess() + "\n";
		}
		str += "--\n";
		return str;
	}

	/**
	 * A list of all the reservations.
	 * 
	 * @return
	 */
	public String printReservations() {
		String str = "--RESERVATIONS--\n";
		if (!(reservedSeatingList.isEmpty())) {
			str += "-- " + reservedSeatingList.size() + " seats are reserved\n";
			for (Customer customer : customerList) {
				str += "- " + customer.getEmail() + " has reserved seat : ";
				// output each seat number in each customer's reservation
				for (int i = 0; i < customer.getReservations().size(); i++) {
					str += customer.getReservations().elementAt(i).getNumber()
							+ " - ";
				}
				str += "\n";
			}
			str += "--\n";
		} else {
			str = "\nThere are no reservations\n";
		}

		return str;
	}

	/**
	 * A list of the waiting list.
	 * 
	 * @return
	 */
	public String printWaitingList() {
		String str = "--WAITING LIST--\n";
		if (!(waitingList.isEmpty())) {
			str += "--" + waitingList.size() + " customers waiting\n";
			for (Customer customer : waitingList) {
				str += "- " + customer + "\n";
			}
			str += "--\n";
		} else {
			str = "\nWaiting list is empty\n";
		}
		return str;
	}

	/**
	 * writes all currently seats to file.
	 * 
	 */
	public void writeSeatingListToFile() {
		try {
			PrintWriter printWriter = new PrintWriter("seats.txt");

			// update any changes to seating list
			for (Seat seat : seatingList) {

				// check for new reservations and cancelled seats
				// and update the seatingList for next program launch
				for (Seat unreservedSeat : unreservedSeatingList) {
					if (seat.equals(unreservedSeat)) {
						seat.setReserved(null);
					}
				}

				for (Seat reservedSeat : reservedSeatingList) {
					if (seat.equals(reservedSeat)) {
						seat.setReserved(reservedSeat.getReserved());
					}
				}

				printWriter.println(seat);

			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Must write waiting list back to file in the same format
	 * that it will be read in correctly next time.
	 * 
	 */
	public void writeWaitingListToFile() {

		try {
			PrintWriter printWriter = new PrintWriter("waitingList.txt");

			if (!(waitingList.isEmpty())) {
				for (Customer customer : waitingList) {
					printWriter.println(customer.getEmail() + ","
							+ customer.getPreferredSeat().getClassType() + ","
							+ customer.getPreferredSeat().getType() + ","
							+ customer.getPreferredSeat().getFacing() + ","
							+ customer.getPreferredSeat().getTable() + ","
							+ customer.getPreferredSeat().getEaseOfAccess());
				}
			} else {
				// clear any previous data in file
				// from program launch.
				printWriter.print("");
			}

			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Must write customer list back to file in the same format
	 * that it will be read in correctly next time.
	 */
	public void writeCustomerListToFile() {

		try {
			PrintWriter printWriter = new PrintWriter("customerList.txt");

			if (!(customerList.isEmpty())) {
				for (Customer customer : customerList) {
					String str = "";
					for (int i = 0; i < customer.getReservations().size(); i++) {
						str += customer.getReservations().elementAt(i)
								.getNumber();
						if (i != (customer.getReservations().size() - 1)) {
							str += ",";
						}
					}
					printWriter.println(customer.getEmail() + "," + str);
				}
			} else {
				// clear file as there may now be no customers in list between
				// program runs.
				printWriter.print("");
			}
			printWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	// Setters/getters
	public Vector<Customer> getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(Vector<Customer> waitingList) {
		this.waitingList = waitingList;
	}

	public Vector<Customer> getCustomerList() {
		return customerList;
	}

	public void setCustomerList(Vector<Customer> customerList) {
		this.customerList = customerList;
	}

	public Vector<Seat> getReservedSeatingList() {
		return reservedSeatingList;
	}

}
