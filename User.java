//Bradley Wersterfer
//bmw170030
//Submitted on 12/4/2018

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * This is a basic class defining user accounts. They are stored in a hash map in the main       *
 * function, and each contains an identifying password (the username is used to hash them into   *
 * the table) and list of orders, which are objects containing the various reservations that     *
 * they have made. Functions to display individual orders (or the entire list) and add new       *
 * orders are also included.                                                                     *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package Tickets;

import java.util.ArrayList;

public class User 
{
    //Keeps track of the identifying password and all orders.
    private String password;
    private ArrayList<Order> orders = new ArrayList<>();
    
    //Keeps track of what order should be updated.
    private Order currentOrder;
    
    //Main Constructor.
    User(String password)
    {
        //Sets the password to be
        this.password = password;
    }
    
    //This function runs through and displays all orders.
    public void displayOrders()
    {
        //Checks to make sure that there's at least one order.
        if(isEmpty())
        {
            //If there isn't an order, the user is notified.
            System.out.println("There are no current orders for this account.");
        }
        //Runs through each of this user's orders.
        for(int i = 0; i < orders.size(); i++)
        {
            //Displays each piece of info for the current order.
            System.out.println("\nORDER " + (i + 1) + ":");
            System.out.println(orders.get(i).getOrder());
        }
    }
    
    //This function displays this user's holistic receipt.
    public void displayReceipt()
    {
        //Keeps a runing total of the price of each order.
        float totalPrice = 0;
        
        //Checks to make sure that there's at least one order.
        if(isEmpty())
        {
            //If there isn't an order, the user is notified.
            System.out.println("There are no current orders for this account.");
        }
        //Runs through each of this user's orders.
        for(int i = 0; i < orders.size(); i++)
        {
            //Displays each piece of info for the current order.
            System.out.println("\nORDER " + (i + 1) + ":");
            System.out.println(orders.get(i).getReceipt());
            totalPrice += orders.get(i).calculatePrice();
        }
        
        //Only displays the total price if there is at lesat one order.
        if(!isEmpty())
        {
            //Displays the total price for this customer.
            String price = String.format("%.2f", totalPrice);
            System.out.format("TOTAL PRICE: %11s \n", "$" + price);
        }
    }

    //Adds an entirely new order to this user's account.
    public void NewOrder(Auditorium theater, int row, int seat, int tickets)
    {
        //Adds the given selection to the list of orders as a new entry.
        orders.add(new Order(theater, row, seat, tickets));
    }
    //Adds to an existing order for this user's account.
    public void AddOrder(int row, int seat, int tickets)
    {
        //Calls the addition function on the order identified by setCurrentOrder().
        currentOrder.AddSeats(row, seat, tickets);
    }
    
    //Determines whether the current user has made any orders or not.
    public boolean isEmpty()
    {
        //Compares the current size to determine whether there are any orders or not.
        if(orders.size() < 1)
            return true;
        else
            return false;
    }
    
    //Returns a single specific order.
    public Order getOrder(int index)
    {
        return orders.get(index);
    }
    //Sets the order that should be updated based on a given index.
    public void setCurrentOrder(int index)
    {
        currentOrder = orders.get(index);
    }
    //Accessor for the current order being focused on.
    public Order getCurrentOrder()
    {
        return currentOrder;
    }
    //Returns the current number of orders in this account.
    public int getNumOrders()
    {
        return orders.size();
    }
    
    //Removes a specific order from this account and unreserves the seats.
    public void deleteOrder(int index)
    {
        //Runs through every seat associated with this order.
        Order order = getOrder(index);
        for(int i = (order.getSeats().size() - 1); i >= 0; i--)
        {
            //Deletes all seats by unreserving and removing them.
            order.deleteSeat(i);
        }
        
        //Resets that order's auditorium and isolates it from the list.
        order.setAuditorium(null);
        orders.remove(index);
    }
    
    //Accessor for the password.
    public String getPassword()
    {
        return password;
    }
}