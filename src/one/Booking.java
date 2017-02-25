package one;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;
import javax.swing.JOptionPane;

import train.Carriage;
import train.Customer;

//The class contains the main method and the flow of the program.
public class Booking {

	static final Scanner S = new Scanner(System.in);

	static Carriage carriage = Carriage.getInstance();

	static boolean QuitProgram = false;

	// Program introduction header.
	static final String HEADER = "**************************\n" + " SEAT RESERVATION SYSTEM "
			+ "\n**************************";

	// The main menu.
	static final String MAIN_MENU = "--MAIN MENU--\n\n1 : Make a Reservation\n2 : Cancel a "
			+ "Reservation\n3 : View Customers Reservations\n4 "
			+ ": View Customers in Waiting list\nQ : Quit Application\n\nEnter Option : ";

	// The reservation menu.
	static final String RESERVATION_MENU = "--RESERVATION MENU--\n\n1 : Reserve single seat based "
			+ "on preference (Inculdes waiting list and \"Next Best Match\" features)\n2 : Reserve seat/s by checking availability list\n3 : View Carriage Layout\nR "
			+ ": Return to Main Menu\n\nEnter Option : ";

	// The help instructions for reservation option 1.
	static final String HELP_INFORMATION_FOR_RESERVATION_OP_1 = "--HELP INSTRUCTIONS--\n\n1. Email must be a valid.\n2. Enter '1' for First class "
			+ "and '2' for Standard\n"
			+ "3. Enter 'W' for window or 'A' for Aisle or 'S' for Single Seat(Single only available in first class).\n"
			+ "4. Enter 'F' for forward Facing or 'B' for Backward facing seat.\n"
			+ "5. Enter 'Y' for YES a Seat with a table or 'N' for NO Seat with out table.\n"
			+ "6. Enter 'Y' for YES an Ease of Access Seat or 'N' for NO normal Seat.\n";

	// The help instructions for reservations option 2.
	static final String HELP_INFORMATION_FOR_RESERVATION_OP_2 = "\n--HELP INSTRUCTIONS--\n1."
			+ " Must be valid email.\n2. Check list above for available seats\n "
			+ "  and enter the number when promted.\n   If you want multiple seats "
			+ "separte each\n   seat number with a ',' without any spaces.\n";

	// Program entry point.
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// All vectors will be initialised when a carriage object is
		// initialised.

		// Not used but if removed static OptionPanes will not display in front
		// of eclipse.
		JOptionPane pane = new JOptionPane();

		System.out.println(HEADER);

		// Main Menu input
		String mainMenuInput;

		do {

			System.out.print(MAIN_MENU);
			mainMenuInput = S.next();

			switch (mainMenuInput) {

			/*
			 * There is three options for the user. First the user can reserve a
			 * single seat based on preference. The second options prints all
			 * available seats and then asks for email followed by a list of
			 * seats numbers or a seat number to reserve. The third option
			 * prints the carriage layout.
			 */

			case "1":
				// Reservation input
				String reserveSeatInput;

				do {

					System.out.print(RESERVATION_MENU);

					reserveSeatInput = S.next();

					switch (reserveSeatInput) {
					case "1":
						System.out.println(HELP_INFORMATION_FOR_RESERVATION_OP_1);

						/*
						 * For all input check for any invalid entry. If so the
						 * user will have to reenter input until valid input is
						 * obtained. Every time the user enters invalid input an
						 * error dialog is displayed informing them of valid
						 * input for that option.
						 * 
						 * NOTE: Same procedure for each seat description input.
						 */

						String email = getEmail();

						// String attr, String outPut,
						// String[] acptList, String errorMessage, String
						// errorHeading) {

						
						// valid inputs for class entry
						String[] classTypeValues = { "1", "2" };

						String classType = getInput("2. Enter class: ", classTypeValues,
								"Must be either '1' or '2'");

						// There is only single seats in first class if the
						// customer selects standard class(2) do not give the
						// option to select a single seat.
						String position = "";

						if (classType.equals("1")) {
							
							// valid input for standard class. 
							String[] posValidInput = {"W", "A", "S"};
							
							position = getInput("3. Specify if you like a Window, Aisle or Single Seat: ", posValidInput, "Must be either 'W', 'A' or 'S'" );


						} else {
							
							// valid input for second class.
							String[] posValidInputStandardClass = {"W", "A"};
							
							position = getInput("3. Specify if you like a Window or an Aisle Seat: ", posValidInputStandardClass, "Must be either 'W' or 'A'");
							

						}

						// valid options for direction of seat.  
						
						String[] facingValid = {"F", "B"};
						
						String facing = getInput("4. Forward or Backward facing: ", facingValid, "Must either 'F' or 'B'");
					
						// valid input for seat with table yes or no. 
						String[] validValues = {"Y", "N"};
						
						String table = getInput("5. Seat with table: ", validValues,
								"Must be either '1' or '2'");
					
						// Ease of Access valid input. 
						String eoa = getInput("6. Ease of Access of Seat: ", validValues,
								"Must be either '1' or '2'");;
					
						// get the required output based on users options and availability of seats.
						String reservationMessage = carriage.reserveSeat(new Customer(email, classType.charAt(0),
								position.charAt(0), facing.charAt(0), table.charAt(0), eoa.charAt(0)));

						// Output the returned message to the user.
						JOptionPane.showMessageDialog(null, reservationMessage, "Reservation Information",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					case "2":

						// Display all available seats to the user.
						System.out.println(carriage.printUnreservedSeats());

						/*
						 * Display help information after as it may not be
						 * visible to user in viewport once all available seats
						 * are displayed.
						 */
						System.out.println(HELP_INFORMATION_FOR_RESERVATION_OP_2);

						
						String emailOp2 = getEmail();


						// declare out side of loop
						String[] seats;

						boolean invalidInput = false;

						outer: do {
							if (invalidInput) {
								// inform user of invalid input.
								JOptionPane.showMessageDialog(null, "You must enter seat numbers between 1 and 43",
										"INVALID ENTRY", JOptionPane.ERROR_MESSAGE);

							}
							System.out.print("2. Enter the seat/s you want to reserve : ");
							String muilpleSeatReservation = S.next();

							// obtain each individual seat number
							seats = muilpleSeatReservation.split(",");

							invalidInput = false;
							for (int i = 0; i < seats.length; i++) {
								// check for any invalid input
								try {
									int temp = Integer.parseInt(seats[i]);
									if (!(temp > 0) || !(temp < 43)) {
										invalidInput = true;
										continue outer;
									}

								} catch (NumberFormatException e) {
									invalidInput = true;
									continue outer;
								}
							}

						} while (invalidInput);

						// call method once valid input has been obtained.
						String multipleReservationMessage = carriage.reserveMultipleSeats(new Customer(emailOp2),
								seats);
						// display outcome
						JOptionPane.showMessageDialog(null, multipleReservationMessage, "Reservation Information",
								JOptionPane.INFORMATION_MESSAGE);
						break;
					case "3":
						// Print carriage layout
						try {
							String carriageLayout = "";
							Scanner fileReader = new Scanner(new FileReader("carriage.txt"));
							while (fileReader.hasNextLine()) {
								carriageLayout += fileReader.nextLine() + "\n";
							}
							fileReader.close();
							System.out.print(carriageLayout);
						} catch (FileNotFoundException e) {
							e.printStackTrace();
						}
						break;

					case "R":
						// Just break to stop error message
						break;
					default:
						// check for invalid input and inform user of valid
						// input.
						JOptionPane.showMessageDialog(null, "Only '1','2','3' or 'R' is valid input", "INVALID INPUT",
								JOptionPane.ERROR_MESSAGE);

					}

				} while (!(reserveSeatInput.equals("R")));

				break;
			case "2":
				// Cancel reservation.
				System.out.println("--HELP INSTRUCTIONS--\n1. Enter the email you made the reservation with.");
				
				
				String cancelEmail = getEmail();
			
				String cancelMessage = carriage.cancelReservation(new Customer(cancelEmail));
				JOptionPane.showMessageDialog(null, cancelMessage, "Cancellation Information",
						JOptionPane.INFORMATION_MESSAGE);
				break;
			case "3":
				// Print all customers in customerList with all seats they have
				// reserved.
				System.out.println(carriage.printReservations());
				break;
			case "4":
				// Print all customers email in waitingList
				System.out.println(carriage.printWaitingList());
				break;
			case "Q":
				// User wants quit program
				QuitProgram = true;
				break;
			default:
				// Inform of invalid input
				JOptionPane.showMessageDialog(null, "INVALID ENTRY\nPlease select a valid option from the list!");

			}

		} while (!QuitProgram);

		// Write all information back to files so
		// that any reservations, new customers or new waiting customers are
		// visible for next program launch.
		carriage.writeSeatingListToFile();
		carriage.writeWaitingListToFile();
		carriage.writeCustomerListToFile();
		S.close();
		System.exit(0);
	}


	/**
	 * Utility to check seat information provided by user.
	 * 
	 * @param outPut message to inform user of required input
	 * @param validList valid array of options.
	 * @param errorMessage an error to display if invalid input is obtained.
	 * @return valid input based on option and user choice.
	 */
	private static String getInput(String outPut, String[] validList, String errorMessage) {

		boolean validInput = false;

		String str = "";

		do {

			System.out.print(outPut);
			str = S.next();
			for (int i = 0; i < validList.length; i++) {
				if (str.equals(validList[i]))
					validInput = true;
			}
			if (!validInput)
				JOptionPane.showMessageDialog(null, errorMessage, "Invalid Input", JOptionPane.ERROR_MESSAGE);

		} while (!validInput);
		
		return str;
	}

	/**
	 * Utility for obtaining a valid email address from the user.
	 * 
	 * @return a valid email address 
	 */
	private static String getEmail() {

		String email = "";

		do {

			System.out.print("1. Enter Email : ");
			email = S.next();
			if (email.indexOf("@") == -1) {
				JOptionPane.showMessageDialog(null, "Must be a valid email", "INVALID INPUT",
						JOptionPane.ERROR_MESSAGE);
			}

		} while (email.indexOf("@") == -1);

		return email;
	}

}
