package main;

public class CarPart {
    private int ID;
    private String name;
    private double weight;
    private boolean isDefective;
	
	// Constructor
    public CarPart(int id, String name, double weight, boolean isDefective) {
        this.ID = id;
        this.name = name;
        this.weight = weight;
        this.isDefective = isDefective;
    }
    
    // Getter
    public int getId() {
        return this.ID;
    }
    
    public String getName() {
        return this.name;
    }

    public double getWeight() {
    	return this.weight;
    }

    public boolean isDefective() {
    	return this.isDefective;
    }

    
    // Setters
    public void setId(int id) {
        this.ID = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setWeight(double weight) {
        this.weight = weight;
    }
    public void setDefective(boolean isDefective) {
        this.isDefective = isDefective;
    }
    
    // Methods
    
    /**
     * Returns the parts name as its string representation
     * @return (String) The part name
     */
    public String toString() {
        return this.getName();
    }
}