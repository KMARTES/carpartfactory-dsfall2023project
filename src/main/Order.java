package main;

import interfaces.Map;

public class Order {
	private int ID;
	private String customerName;
	private Map<Integer,Integer> partsRequested;
	private boolean fulfilled;
	
	// Constructor 
    public Order(int id, String customerName, Map<Integer, Integer> requestedParts, boolean fulfilled) {
    	this.ID = id;
    	this.customerName = customerName;
    	this.partsRequested = requestedParts;
    	this.fulfilled = fulfilled;
    }
    
    // Getters
    public int getId() {
        return this.ID;
    }
    
    public boolean isFulfilled() {
    	return this.fulfilled;
    }
    
    public Map<Integer, Integer> getRequestedParts() {
       return this.partsRequested;
    }
    
    public String getCustomerName() {
      return this.customerName;
    }
    
    
    // Setters
    public void setId(int id) {
        this.ID = id;
    }
    
    public void setFulfilled(boolean fulfilled) {
    	this.fulfilled =  fulfilled;
    }
    
    public void setRequestedParts(Map<Integer, Integer> requestedParts) {
        this.partsRequested = requestedParts;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    // Methods
    
    /**
     * Returns the order's information in the following format: {id} {customer name} {number of parts requested} {isFulfilled}
     */
    @Override
    public String toString() {
        return String.format("%d %s %d %s", this.getId(), this.getCustomerName(), this.getRequestedParts().size(), (this.isFulfilled())? "FULFILLED": "PENDING");
    }

    
    
}
