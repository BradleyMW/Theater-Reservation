//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This is the main function for reserving theater seats. It runs three separate auditoriums,    *
 * stored in text files named A1.txt, A2.txt, and A3.txt respectively. It also supports a        *
 * basic login system with usernames and passwords stored in a file called userdb.dat.           *
 * After signing in, users can make separate orders and reserve seats in any auditorium that     *
 * they wish, along with updating orders after they are made. They can also see a receipt for    *
 * their current orders and price, or cancel entire orders if they wish.                         *
 * The program ends once the administrator signs in (the username is "admin" and the password    *
 * is "password") and chooses to close the system, which saves all changes to the auditoriums    *
 * to their respective text files.                                                               *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

import java.util.Scanner;
import java.io.*;
import java.util.HashMap;

public class Main 
{
    public static void main(String[] args)  throws IOException
    {
        HashMap users = new HashMap();
        String username;
        
        Auditorium A1 = new Auditorium(),
                   A2 = new Auditorium(),
                   A3 = new Auditorium();
        
        //Attempts to open the user database file.
        File userFile = new File("userdb.dat");
        if(userFile.exists())
        {
            //If the file was found, then it is used to fill out the user login information.
            Scanner userData = new Scanner(userFile);
            users = fillMap(users, userData);
            userData.close();
        }

        //Attempts to create the first auditorium.
        File auditoriumFile = new File("A1.txt");
        if(auditoriumFile.exists())
            A1 = new Auditorium(new Scanner(auditoriumFile), 1);
        else
            System.out.println("Error: The file \"A1.txt\" was not found!");
        
        //Attempts to create the second auditorium.
        auditoriumFile = new File("A2.txt");
        if(auditoriumFile.exists())
            A2 = new Auditorium(new Scanner(auditoriumFile), 2);
        else
            System.out.println("Error: The file \"A2.txt\" was not found!");
        
        //Attempts to create the third auditorium.
        auditoriumFile = new File("A3.txt");
        if(auditoriumFile.exists())
            A3 = new Auditorium(new Scanner(auditoriumFile), 3);
        else
            System.out.println("Error: The file \"A3.txt\" was not found!");
        
        //Begins the main loop of input. Users can continuously log in and perform actions.
        int menuChoice;
        do 
        {
            //Prompts the user to enter their username and log in.
            Scanner input = new Scanner(System.in);
            username = LogIn(input, users);
            User curUser = (User)users.get(username);
            
            //Checks to see if it was the admin who logged in.
            if(username.equals("admin"))
            {
                //Prompts the admin to take an action.
                do
                {
                    //Displays the possible actions to take.
                    System.out.println("\nADMINISTRATOR MENU:");
                    System.out.println("1. Print Report");
                    System.out.println("2. Log Out");
                    System.out.println("3. Exit");
                    System.out.print("Please enter a choice from the menu: ");
                    
                    //Attempts to convert the given input into an integer to determine validity.
                    try {
                        menuChoice = Integer.parseInt(input.next());
                    } 
                    catch (NumberFormatException e) {
                        //An error message will be displayed in the following switch statement.
                        menuChoice = -1;
                    }
                    
                    //Determines which action should be carried out.
                    switch(menuChoice)
                    {
                        //Displays the Report
                        case 1:
                            DisplayReport(A1, A2, A3);
                            break;
                            
                        //Log Out
                        case 2:
                            System.out.println("You are now logged out.");
                            break;
                            
                        //Exit Program
                        case 3:
                            //If the admin chose to log out or exit, nothing else happens.
                            break;
                            
                        //Invalid Input
                        default:
                            System.out.println("Error: Please enter a valid number from the menu.");
                    }
                } while (menuChoice != 2 && menuChoice != 3);
            }
            else
            {
                //If the admin did not log in, then it must be a normal customer.
                do
                {
                    //Displays the menu for normal customers.
                    System.out.println("\nMAIN MENU:");
                    System.out.println("1. Reserve Seats");
                    System.out.println("2. View Orders");
                    System.out.println("3. Update Order");
                    System.out.println("4. Display Receipt");
                    System.out.println("5. Log Out");
                    System.out.print("Please enter a choice from the menu: ");

                    //Attempts to convert the given input into an integer to determine validity.
                    try {
                        menuChoice = Integer.parseInt(input.next());
                    } 
                    catch (NumberFormatException e) {
                        //An error message will be displayed in the following switch statement.
                        menuChoice = -1;
                    }

                    //Determines which functions should be carried out.
                    switch(menuChoice)
                    {
                        //Reserve Seats
                        case 1:
                        {
                            //Prompts the user to choose an auditorium.
                            Auditorium temp = new Auditorium();
                            switch(SelectAuditorium(input))
                            {
                                //Chose the first auditorium.
                                case 1:
                                    temp = A1;
                                    break;
                                //Chose the second auditorium.
                                case 2:
                                    temp = A2;
                                    break;
                                //Chose the third auditorium.
                                case 3:
                                    temp = A3;
                                    break;
                            }
                            
                            //Displays the auditorium and lets the user make changes.
                            DisplayAuditorium(temp, temp.getNumSeats());
                            SelectSeat(temp, input, curUser, true);
                            DisplayAuditorium(temp, temp.getNumSeats());
                            break;
                        }
                        
                        //View Orders
                        case 2:
                            curUser.displayOrders();
                            break;
                            
                        //Update Order
                        case 3:
                            //Displays the orders and makes sure that the user has some to be changed.
                            curUser.displayOrders();
                            if(!curUser.isEmpty())
                            {
                                //Loops until the user chooses a valid order for their account.
                                int orderNumber;
                                do
                                {
                                    //Prompts the user to choose which order should be updated.
                                    System.out.print("\nPlease select an order to update: ");
                                    String tempInput = input.next();

                                    //Attempts to convert the user's input into a valid number.
                                    try {
                                        orderNumber = Integer.parseInt(tempInput);
                                    }
                                    catch (NumberFormatException e) {
                                        //If the conversion failed, the loop is automatically set to repeat.
                                        orderNumber = -1;
                                    } 
                                        
                                    //Notifies the user that their input was invalid.
                                    if(orderNumber < 1 || orderNumber > curUser.getNumOrders())
                                        System.out.println("Error: Please enter a valid order number.");
                                    //Loop ends once a valid number has been entered.
                                } while(orderNumber > curUser.getNumOrders() || orderNumber < 1);
                                
                                //Decrements the current order to account for the array list index.
                                orderNumber--;
                                curUser.setCurrentOrder(orderNumber);
                                Auditorium temp = curUser.getCurrentOrder().getAuditorium();
                                
                                //Loops until the user makes a valid choice for what to do with the order.
                                int action;
                                do
                                {
                                    //Prompts the user to make a choice from the menu.
                                    System.out.println("What action would you like to perform on ORDER " + (orderNumber + 1) + "?");
                                    System.out.println("1. Add tickets to order");
                                    System.out.println("2. Delete tickets from order");
                                    System.out.println("3. Cancel Order");
                                    System.out.print("Enter your choice: ");
                                    String tempInput = input.next();
                                    
                                    //Attempts to convert the given input into a valid number.
                                    try {
                                        action = Integer.parseInt(tempInput);
                                    }
                                    catch (NumberFormatException e) {
                                        //If the conversion failed, the loop is automatically set to repeat.
                                        action = -1;
                                    } //Loop ends once a valid choice has been made.
                                    
                                    //Notifies the user that they entered something invalid.
                                    if(action < 0 || action > 3)
                                        System.out.println("Error: Please enter a valid number from the menu.");
                                } while(action < 0 || action > 3);
                                
                                //Follows the user's desired action.
                                switch(action)
                                {
                                    //Add Tickets
                                    case 1:
                                        DisplayAuditorium(temp, temp.getNumSeats());
                                        SelectSeat(curUser.getCurrentOrder().getAuditorium(), input, curUser, false);
                                        break;
                                        
                                    //Delete Individual Seats
                                    case 2:
                                        //Calls the function to find seats to delete within this auditorium and user's account.
                                        findSeatAndDelete(temp, curUser, input);
                                        break;
                                        
                                    //Delete Entire Order
                                    case 3:
                                        curUser.deleteOrder(orderNumber);
                                        break;
                                }
                            }
                            break;
                            
                        //Display Receipt
                        case 4:
                            curUser.displayReceipt();
                            break;
                            
                        //Log Out
                        case 5:
                            //If the user decided to log out, then nothing else happens.
                            System.out.println("You are now logged out.");
                            break;
                            
                        //An invalid number was entered.
                        default:
                            //Notifies the user that an invalid choice was made.
                            System.out.println("Error: Please enter a valid number on the menu.");
                    } //The loop ends when the user chooses to log out.
                } while(menuChoice != 5);
            } 
            
            //Spaces out the output more to improve legibility.
            System.out.println();
            
            //The entire loop ends when the admin chose to close the program.
            if(menuChoice == 3 && username.equals("admin"))
                input.close();
        } while(!username.equals("admin") || menuChoice != 3);
            
        //Opens the files again for output.
        PrintWriter output1 = new PrintWriter(new File("A1.txt"));
        PrintWriter output2 = new PrintWriter(new File("A2.txt"));
        PrintWriter output3 = new PrintWriter(new File("A3.txt"));
        
        //Writes out the changes to the auditoriums back to their files.
        WriteToFile(A1, output1);
        WriteToFile(A2, output2);
        WriteToFile(A3, output3);

        //Closes the remaining files and terminates the program.
        output1.close();
        output2.close();
        output3.close();
        System.out.println("Now exiting the program. Your changes have been saved.");
    }
    
    //Uses a Scanner to fill out a hash map with login info.
    public static HashMap fillMap(HashMap map, Scanner file)
    {
        //Runs through every line of user login information.
        while(file.hasNextLine())
        {
            //The username is the key and the password object is the value.
            String key = file.next();
            User value = new User(file.next());
            
            //Enters the given values into the hash map.
            map.put(key, value);
        }
        
        //Returns the map with all user login information entered in it.
        return map;
    }
    
    //Verifies that the user can successfully log into the account.
    public static String LogIn(Scanner input, HashMap users)
    {
        //Keeps track of the number of attempts and login information.
        String username,
               password;
        int attempts;
        
        //Loops until a valid username and password pair are entered.
        do 
        {
            //Prompts the user to enter a username.
            System.out.print("Username: ");
            username = input.next();
            
            //Checks to see if the username existed before proceeding.
            if(users.containsKey(username))
            {
                //Finds the given password associated with the username and sees if the user knows it.
                String correctPassword = ((User)users.get(username)).getPassword();
                for(attempts = 0; attempts < 3; attempts++)
                {
                    //Prompts the user to enter the password. If it is correct, the loop ends.
                    System.out.print("Password: ");
                    if(input.next().equals(correctPassword))
                        break;
                    else
                        System.out.println("Error: Your password was incorrect.");
                }

                //Notifies the user that they have exceeded the maximum number of attempts.
                if(attempts == 3)
                    System.out.println("Error: You have entered an incorrect password too many times. Please try again.");
            }
            //The username does not exist.
            else
            {
                System.out.println("Error: Your username does not exist.");
                attempts = 3;
            }
        } while(attempts == 3);
        
        //Returns the successfully logged in username for the main program.
        return username;
    }
    
    //Ensures that the user chooses a valid integer between 1 and 3 for input validation.
    public static int SelectAuditorium(Scanner input)
    {
        //Loops until a valid auditorium is chosen.
        int chosenAuditorium;
        do
        {
            //Displays the possible auditoriums for the user to choose.
            System.out.println("\n1. Auditorium 1");
            System.out.println("2. Auditorium 2");
            System.out.println("3. Auditorium 3");
            System.out.print("Please select an auditorium: ");
                                
            try {
                chosenAuditorium = Integer.parseInt(input.next());
            }
            catch (NumberFormatException e) {
                chosenAuditorium = -1;
            }
                                
            if(chosenAuditorium > 3 || chosenAuditorium < 1)
                System.out.println("Error: Please enter a valid number between 1 and 3.");
        } while(chosenAuditorium > 3 || chosenAuditorium < 1);
        
        //Once a valid selection is made, it is returned to the main function.
        return chosenAuditorium;
    }
    
    //Prints out the current theater seating arrangement.
    static void DisplayAuditorium(Auditorium theater, int numSeats)
    {
        //The column header.
        String Columns = "  ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        
        //Informs the user what they are seeing.
        System.out.println("\nHere is the current auditorium: ");
        
        //Only displays the letters for columns currently in the theater.
        for(int i = 0; i < numSeats + 2; i++)
        {
            //Displays the correct letter.
            System.out.print(Columns.charAt(i));
        }
        
        //Moves on to the next line for the seats.
        System.out.print("\n");
        
        //Starts at the first row's first seat and runs through the rest.
        int row = 0;
        TheaterSeat cur = theater.getFirst();
        while(cur != null)
        {
            //Prints out the current row number.
            System.out.print((row + 1) + " ");
            
            //Keeps track of the next row in the auditorium.
            TheaterSeat firstInNextRow = cur.getDown();

            //Runs through every seat in the current row.
            while(cur != null)
            {
                //Prints out whether the seat is reserved or not.
                if(cur.getReserved())
                    System.out.print("#");
                else
                    System.out.print('.');

                //Moves to the next pointer in the row.
                cur = cur.getRight();
            }
            
            //Moves on to the next row of seats.
            cur = firstInNextRow;
            row++;
            System.out.print("\n");
        }
    }
    
    //Verifies user input to ensure that a proper seat is found before it can be processed.
    static void SelectSeat(Auditorium theater, Scanner input, User user, boolean suggestAlternateSeats)
    {
        //Keeps track of input from the user.
        int chosenRow,
            chosenSeat,
            totalSeats,
            adults,
            kids,
            seniors;
        TheaterSeat suggestedSeat;
        
        //The column that the user chooses.
        String temp;
        
        //The number of rows and seats in the current auditorium.
        int rows = theater.getNumRows(),
            numSeats = theater.getNumSeats();
        
        //Spaces out the output to improve legibility.
        System.out.print("\n");
 
        //Verifies that the row selection is in bounds.
        do {
            //Prompts the user to choose a row number.
            System.out.print("Please select a row number: ");
            try
            {
                //Ensures that the input was a row and then decrements it to account for the index.
                chosenRow = Integer.parseInt(input.next());
                chosenRow--;
            }
            catch(NumberFormatException e)
            {
                //Sets the prompt to loop if a number wasn't entered.
                chosenRow = -1;
            }
            
            //Warns the user if the chosen row is out of bounds.
            if(chosenRow < 0 || chosenRow >= rows)
                System.out.println("Error! Please choose a row inside the auditorium.");
        } while(chosenRow < 0 || chosenRow >= rows);
        
        //Verifies that the seat selection is in bounds.
        do {
            //Prompts the user to choose a seat letter.
            System.out.print("Please select a starting seat letter: ");

            try
            {
                //Attempts to convert the input into the seat character.
                temp = input.next();
                if(temp.length() > 1)
                    throw new NumberFormatException();
                else
                {
                    //Converts the seat character into a usable integer.
                    chosenSeat = temp.toLowerCase().charAt(0) - 'a';
                }
            }
            catch(NumberFormatException e)
            {
                //Sets the loop to repeat.
                chosenSeat = -1;
            }
            
            //Warns the user if the chosen seat is out of bounds.
            if(chosenSeat < 0 || chosenSeat >= numSeats)
                System.out.println("Error! Please choose a seat letter within the auditorium.");
        } while (chosenSeat < 0 || chosenSeat >= numSeats);
        
        //Prompts the user to enter quantities for each type of tickets.
        do {
            //Prompts for the number of adult tickets.
            System.out.print("Please enter the number of adult tickets: ");
            try
            {
                //Attempts to convert the input into the number of adult tickets.
                adults = Integer.parseInt(input.next());
            }
            catch(NumberFormatException e)
            {
                //Sets the prompt to loop.
                adults = -1;
            }
            
            //Checks for any error messages to be displayed.
            if(adults < 0)
                System.out.println("Error! Please enter a positive integer.");
        } while(adults < 0);
        do {
            //Prompts for the number of child tickets.
            System.out.print("Please enter the number of child tickets: ");
            try
            {
                //Attempts to convert the input into the number of child tickets.
                kids = Integer.parseInt(input.next());
            }
            catch(NumberFormatException e)
            {
                //Sets the prompt to loop.
                kids = -1;
            }
            
            //Checks for any error messages to be displayed.
            if(kids < 0)
                System.out.println("Error! Please enter a positive integer.");
        } while(kids < 0);
        do {
            //Prompts for the number of senior tickets.
            System.out.print("Please enter the number of senior tickets: ");
            try
            {
                //Attempts to convert the input into the number of senior tickets.
                seniors = Integer.parseInt(input.next());
            }
            catch(NumberFormatException e)
            {
                //Sets the prompt to loop.
                seniors = -1;
            }
            
            //Checks for any error messages to be displayed.
            if(seniors < 0)
                System.out.println("Error! Please enter a positive integer.");
        } while(seniors < 0);
        
        //Sums the total number of seats and checks their availability.
        totalSeats = adults + kids + seniors;
      
        //Makes sure that the seats are available before reserving them.
        if(CheckAvailability(theater, chosenRow, chosenSeat, totalSeats)  && (chosenSeat + totalSeats <= numSeats) && totalSeats > 0)
        {
            //If the seats were available, then they are successfully reserved.
            ReserveSeats(theater, chosenRow, chosenSeat, adults, kids, seniors);
            System.out.println("Your seats have been reserved!");
            
            //Sees whether the function was called by a new order (when seats might be suggested) or not.
            if(suggestAlternateSeats)
                user.NewOrder(theater, chosenRow, chosenSeat, totalSeats);
            else
                user.AddOrder(chosenRow, chosenSeat, totalSeats);
        } 
        //Only tires to find the best available seats if the calling function wants it to.
        else if(suggestAlternateSeats)
        {
            //Only tries to find alternate seating if at least one ticket is trying to be purchased.
            if(totalSeats < 1)
                System.out.println("Error! You must select more than 0 tickets to reserve any seats!");
            else
            {
                //Finds an alternative seating arrangement to suggest.
                System.out.println("\nError! Some of the seats that you have chosen are not available!");
                suggestedSeat = BestSuggestion(theater, theater.getNumRows(), numSeats, totalSeats);
               
                //If an alternative was found, it is offered to the user.
                if(suggestedSeat != null)
                {
                    //Temporary holder for the user's choice.
                    String takeSeats;

                    //Informs the user of the alternative seating idea.
                    System.out.println("Here is a suggestion for alternate seating: ");
                    if(totalSeats > 1)
                        System.out.println("Row " + (suggestedSeat.getRow() + 1) + ", Seats " + suggestedSeat.getSeat() + " through " + (char)(suggestedSeat.getSeat() + totalSeats - 1) + ".");
                    else
                        System.out.println("Row " + (suggestedSeat.getRow() + 1) + ", Seat " + suggestedSeat.getSeat() + ".");
                    
                    //Loops through prompting them until a valid choice is made.
                    char yesOrNo;
                    do {
                        //Takes the next line of input from the user.
                        System.out.println("Would you like to reserve these seats? (Y/N)");
                        takeSeats = input.next();
                        try
                        {
                            //If the user entered more than one character, then it cannot be valid.
                            if(takeSeats.length() > 1)
                            {
                                throw new NumberFormatException();
                            }
                            else
                                yesOrNo = takeSeats.toLowerCase().charAt(0);
                        }
                        catch(NumberFormatException e)
                        {
                            //Notifies the user that their input was invalid.
                            System.out.println("Error! Please enter either Y or N!");
                            yesOrNo = ' ';
                        }
                    } while (yesOrNo != 'y' && yesOrNo != 'n');

                    //If the alternative is accepted, then the seats are reserved.
                    if(takeSeats.equalsIgnoreCase("y"))
                    {
                        //Alternate seats are only suggested in new orders, so they should be added to the current customer.
                        System.out.println("Your seats have been reserved!");
                        ReserveSeats(theater, suggestedSeat.getRow(), (char)(suggestedSeat.getSeat() - 'A'), adults, kids, seniors);
                        user.NewOrder(theater, suggestedSeat.getRow(), (char)(suggestedSeat.getSeat() - 'A'), totalSeats);
                    }
                    else
                        //If the seats were not accepted, the user is taken back to the main menu.
                        System.out.println("Please choose a different seating arrangement if you would still like to purchase tickets.");
                }
                else
                    //Informs the user that no alternative seating was possible on this row.
                    System.out.println("Unfortunately, a good alternative suggestion could not be found.");
            }
        }
        //Notifies the user that since they are updating an order, no alternate seats can be found.
        else
        {
            //Since alternate suggestions can only be made from the main reserve function, an error message is displayed.
            System.out.println("Error: The seats that you have selected are not available.");
        }
    }
    
    //Checks to see if the chosen seats are open or not.
    static boolean CheckAvailability(Auditorium theater, int chosenRow, int chosenSeat, int totalSeats)
    { 
        //Moves to the proper row and starting seat.
        TheaterSeat cur = theater.getFirst();
        for(int row = 0; row < chosenRow; row++)
        {
            cur = cur.getDown();
        }
        for(int col = 0; col < chosenSeat; col++)
        {
            cur = cur.getRight();
        }
        
        //Loops through every seat in the selection.
        for(int k = 0; k < totalSeats; k++)
        {
            //Checks to see if any seats were reserved.
            if(cur.getReserved())
                return false;
            
            //Checks to see if the end has been reached or if the loop should continue.
            if(cur.getRight() != null)
                cur = cur.getRight();
        }
        
        //If all seats weren't reserved, then the selection is open.
        return true;
    }
    
    //Reserves the chosen seats in the auditorium.
    static void ReserveSeats(Auditorium theater, int chosenRow, int chosenSeat, int adults, int kids, int seniors)
    {
        //Moves to the proper row and starting seat.
        TheaterSeat cur = theater.getFirst();
        for(int row = 0; row < chosenRow; row++)
        {
            cur = cur.getDown();
        }
        for(int col = 0; col < chosenSeat; col++)
        {
            cur = cur.getRight();
        }
        
        //Assigns every adult seat chosen.
        for(int seat = 0; seat < adults; seat++)
        {
            cur.setTicketType('A');
            cur.setReserved('A');
            cur = cur.getRight();
        }
        //Assigns every child seat chosen.
        for(int seat = 0; seat < kids; seat++)
        {
            cur.setTicketType('C');
            cur.setReserved('C');
            cur = cur.getRight();
        }
        //Assigns every senior seat chosen.
        for(int seat = 0; seat < seniors; seat++)
        {
            cur.setTicketType('S');
            cur.setReserved('S');
            cur = cur.getRight();
        }
    }
    
    //Finds the seating arrangement closest to the center of the row.
    static TheaterSeat BestSuggestion(Auditorium theater, int numRows, int numSeats, int tickets)
    {        
        //Accounts for the difference in index in the row count.
        numRows--;
        numSeats--;
        
        //Variables for keeping track of seating options.
        boolean seatsOpen;
        float verticalDistance,
            minVerticalDistance = theater.getNumRows() * 2, //DELETEME Change these to numRows //FIXME
            horizontalDistance;
        float curDistance,
              minDistance = minVerticalDistance + theater.getNumSeats() * 2;
        
        //Loops through each row of the auditorium.
        TheaterSeat cur = theater.getFirst(),
                    seat,
                    minSeat = null;
        while(cur != null)
        {
            //Keeps track of the next row and loops through each seat in the current one.
            TheaterSeat firstInRow = cur.getDown();
            while(cur != null)
            {
                //Beginning from the current seat, sees if the entire selection is open.
                seat = cur;
                seatsOpen = true;
                for(int i = 0; i < tickets; i++)
                {
                    //If one of the seats is not open, this loop ends and begins again from the next seat.
                    if(seat.getReserved())
                    {
                        //If any seat is already reserved, then the selection is invalid.
                        seatsOpen = false;
                        break;
                    }
                    seat = seat.getRight();
                    if(seat == null)
                    {
                        //If the selection would extend past the end of the row, then it is invalid.
                        if((i + 1) != tickets)
                            seatsOpen = false;
                        break;
                    }
                }
                
                if(seatsOpen)
                {
                    //Calculates the distance from the midpoint of the auditorium to the midpoint of the selection.
                    verticalDistance = Math.abs(numRows / (float)2.0 - cur.getRow());
                    horizontalDistance = Math.abs(numSeats / (float)2.0 - ((cur.getSeat() - 'A') + ((tickets - 1) / (float)2.0)));
                    curDistance = (float)Math.sqrt(Math.pow(verticalDistance, 2) + Math.pow(horizontalDistance, 2));
                    
                    //Settles any ties for the minimum distance.
                    if(minSeat != null && curDistance == minDistance)
                    {
                        //Sees which row was closer to the center.
                        if(verticalDistance < minVerticalDistance)
                        {
                            //Changes to the row closest to the center.
                            minDistance = curDistance;
                            minSeat = cur;
                            minVerticalDistance = verticalDistance;
                        }
                        //Checks if this row was lower than the other.
                        else if(cur.getRow() < minSeat.getRow())
                        {
                            //Uses the row with the smaller number.
                            minDistance = curDistance;
                            minSeat = cur;
                            minVerticalDistance = verticalDistance;
                        }
                    }
                    //If the current distance is closer than any other distance, it is accepted as the new minimum distance.
                    else if(curDistance < minDistance)
                    {
                        //Sets the permanent variables accordingly.
                        minDistance = curDistance;
                        minSeat = cur;
                        minVerticalDistance = verticalDistance;
                    }
                }
                cur = cur.getRight();
            }
            cur = firstInRow;
        }
        
        //Returns the starting seat for the closest arrangement to the center possible.
        return minSeat;
    }
    
    //Takes a theater and user account to prompt the user to delete a seat in their current order.
    static void findSeatAndDelete(Auditorium theater, User curUser, Scanner input)
    {
        //Loops until a valid row-seat combination within the current order is found.
        int row, 
            seat, 
            target;
        do
        {
            //Loops until a potentially valid row number is found.
            do
            {
                //Prompts the user to enter a valid row number for this auditorium.
                System.out.print("Please enter the row of the seat that you would like to delete: ");
                try {
                    row = Integer.parseInt(input.next());
                }
                catch (NumberFormatException e) {
                    System.out.println("Error: Please enter a valid row number.");
                    row = -1;
                } //Loop ends once the user has entered a valid row number.
            } while (row < 1 || row > theater.getNumRows());
            row--;
            
            //Loops until a valid seat letter in the auditorium is entered.
            do
            {
                //Prompts the user to enter a valid seat letter for this auditorium.
                System.out.print("Please enter the letter of the seat that you would like to delete: ");
                try {
                    //Attempts to convert the input into the seat character.
                    String temp = input.next();
                    if(temp.length() > 1)
                        throw new NumberFormatException();
                    else
                    {
                        //Converts the seat character into a usable integer.
                        seat = temp.toLowerCase().charAt(0) - 'a';
                    }
                }
                catch (NumberFormatException e) {
                    seat = -1;
                } //Loop ends once a valid seat within the auditorium was chosen.
            } while (seat < 0 || seat > theater.getNumSeats());
            
            //Attempts to determine the index of the desired seat.
            target = curUser.getCurrentOrder().findSeat(row, (char)(seat + 'A'));
            if(target == -1)
                System.out.println("Error: The row and seat that you chose are not in this order.");
        } while (target == -1);
        
        //Deletes the desired seat from the order.
        curUser.getCurrentOrder().deleteSeat(target);
        System.out.println("The seat has been successfully removed from this order.");
    }
    
    //Shows the administrator the statistics of the final auditoriums.
    static void DisplayReport(Auditorium A1, Auditorium A2, Auditorium A3)
    {
        //Title Row
        System.out.format("%1$27s", "Open Seats");
        System.out.format("%1$23s", "Total Reserved Seats");
        System.out.format("%1$14s", "Adult Seats");
        System.out.format("%1$14s", "Child Seats");
        System.out.format("%1$15s", "Senior Seats");
        System.out.format("%1$15s \n", "Ticket Sales");
        
        //First Auditorium Row
        System.out.print("Auditorium 1");
        System.out.format("%1$15s", A1.getSeatsOfType('.'));
        System.out.format("%1$23s", A1.getTotalReservedSeats());
        System.out.format("%1$14s", A1.getSeatsOfType('A'));
        System.out.format("%1$14s", A1.getSeatsOfType('C'));
        System.out.format("%1$15s", A1.getSeatsOfType('S'));
        String price = String.format("%.2f", A1.getSales());
        System.out.format("%1$15s \n", "$" + price);
        
        //Second Auditorium Row
        System.out.print("Auditorium 2");
        System.out.format("%1$15s", A2.getSeatsOfType('.'));
        System.out.format("%1$23s", A2.getTotalReservedSeats());
        System.out.format("%1$14s", A2.getSeatsOfType('A'));
        System.out.format("%1$14s", A2.getSeatsOfType('C'));
        System.out.format("%1$15s", A2.getSeatsOfType('S'));
        String price2 = String.format("%.2f", A2.getSales());
        System.out.format("%1$15s \n", "$" + price2);
        
        //Third Auditorium Row
        System.out.print("Auditorium 3");
        System.out.format("%1$15s", A3.getSeatsOfType('.'));
        System.out.format("%1$23s", A3.getTotalReservedSeats());
        System.out.format("%1$14s", A3.getSeatsOfType('A'));
        System.out.format("%1$14s", A3.getSeatsOfType('C'));
        System.out.format("%1$15s", A3.getSeatsOfType('S'));
        String price3 = String.format("%.2f", A3.getSales());
        System.out.format("%1$15s \n", "$" + price3);
        
        //Total Row
        System.out.print("Total");
        System.out.format("%1$22s", A1.getSeatsOfType('.') + A2.getSeatsOfType('.') + A3.getSeatsOfType('.'));
        System.out.format("%1$23s", A1.getTotalReservedSeats() + A2.getTotalReservedSeats() + A3.getTotalReservedSeats());
        System.out.format("%1$14s", A1.getSeatsOfType('A') + A2.getSeatsOfType('A') + A3.getSeatsOfType('A'));
        System.out.format("%1$14s", A1.getSeatsOfType('C') + A2.getSeatsOfType('C') + A3.getSeatsOfType('C'));
        System.out.format("%1$15s", A1.getSeatsOfType('S') + A2.getSeatsOfType('S') + A3.getSeatsOfType('S'));
        String totalPrice = String.format("%.2f", A1.getSales() + A2.getSales() + A3.getSales());
        System.out.format("%1$15s \n", "$" + totalPrice);
    }
    
    //Saves changes that the user made back to the file.
    static void WriteToFile(Auditorium theater, PrintWriter output)
    {
        //Starts at the beginning of the auditorium.
        TheaterSeat cur = theater.getFirst(),
                    firstInRow;
        
        //Runs through every row of the auditorium.
        while(cur != null)
        {
            //Runs through every seat in the current row.
            firstInRow = cur.getDown();
            while(cur != null)
            {
                //Outputs the current character and moves the pointer to the right.
                output.write(cur.getTicketType());
                cur = cur.getRight();
            }
            
            //Moves on to the next row.
            output.write("\n");
            cur = firstInRow;
        }
    }
}