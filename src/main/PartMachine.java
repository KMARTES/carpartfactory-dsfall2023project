package main;

import data_structures.ListQueue;
import interfaces.Queue;

/**
 * 
 */
public class PartMachine {
	private int ID;
	private CarPart part;
	private int period;
	private double weightError;
	private int chance;
	
	private Queue<Integer> timer;
	private Queue<CarPart> conveyor;
	private int totalProduction;
	private int defectiveQty;

	// Constructor   
    public PartMachine(int id, CarPart p1, int period, double weightError, int chanceOfDefective) {
        this.ID = id;
        this.part = p1;
        this.period = period;
        this.weightError = weightError;
        this.chance = chanceOfDefective;
        this.timer = initTimer(period);
        this.conveyor = initConveyor();
        this.totalProduction = 0;
        this.defectiveQty = 0;
    }
    
    // Getters
    public int getId() {
    	return this.ID;
    }
    
    public Queue<Integer> getTimer() {
       return this.timer;
    }

    public CarPart getPart() {
    	return this.part;
    }
    
    public int getPeriod() {
    	return this.period;
    }
    
    public Queue<CarPart> getConveyorBelt() {
        return this.conveyor;
    }

    public double getPartWeightError() {
        return this.weightError;
    }

    public int getChanceOfDefective() {
        return this.chance;
    }
    
    public int getTotalPartsProduced() {
        return this.totalProduction;
    }
    
    public int getTotalDefectives() {
    	return this.defectiveQty;
    }
    
    // Setters
    public void setId(int id) {
        this.ID = id;
    }
    
    public void setPart(CarPart part1) {
        this.part = part1;
    }
    
    public void setTimer(Queue<Integer> timer) {
        this.timer = timer;
    }
    
    public void setConveyorBelt(Queue<CarPart> conveyorBelt) {
    	this.conveyor = conveyorBelt;
    }
    
    public void setTotalPartsProduced(int count) {
    	this.totalProduction = count;
    }
    
    public void setPartWeightError(double partWeightError) {
        this.weightError = partWeightError;
    }
    
    public void setChanceOfDefective(int chanceOfDefective) {
        this.chance = chanceOfDefective;
    }
    
    public void setTotalDefectives( int partsDefective ) {
    	this.defectiveQty = partsDefective;
    }
    // Methods

    /**
     * Returns string representation of a Part Machine in the following format:
     * Machine {id} Produced: {part name} {total parts produced}
     */
    @Override
    public String toString() {
        return "Machine " + this.getId() + " Produced: " + this.getPart().getName() + " " + this.getTotalPartsProduced();
    }
    /**
     * Prints the content of the conveyor belt. 
     * The machine is shown as |Machine {id}|.
     * If the is a part it is presented as |P| and an empty space as _.
     */
    public void printConveyorBelt() {
        // String we will print
        String str = "";
        // Iterate through the conveyor belt
        for(int i = 0; i < this.getConveyorBelt().size(); i++){
            // When the current position is empty
            if (this.getConveyorBelt().front() == null) {
                str = "_" + str;
            }
            // When there is a CarPart
            else {
                str = "|P|" + str;
            }
            // Rotate the values
            this.getConveyorBelt().enqueue(this.getConveyorBelt().dequeue());
        }
        System.out.println("|Machine " + this.getId() + "|" + str);
    }
    
    /**
     * Initializes a unique timer for each unique machine. 
     * @param period - Time machine needs to create a part.
     * @return A queue of numbers. First in is period - 1.
     */
    public Queue<Integer> initTimer(int period) {
    	Queue<Integer> timer = new ListQueue<>();
        
        for ( int i = period - 1; i >= 0; i-- ) {
     	   timer.enqueue(i);
        }
        return timer;
    }
    
    /**
     * Initializes the conveyor to be used by a unique machine.
     * @return Ten spaces all initailized as NULL.
     */
    public Queue<CarPart> initConveyor() {
    	Queue<CarPart> conveyor = new ListQueue<>();
        
        for ( int i = 0; i < 10; i++ ) {
     	   conveyor.enqueue(null);
        }
        return conveyor;
    }
    
    public void resetConveyorBelt() {
    	for ( int i = 0; i < 10; i++ ) {
    		conveyor.dequeue();
      	   	conveyor.enqueue(null);
         }
    }
    
    /**
     * this method is intended to simulate the actions of a timer.
     * @return The next number in the countdown.
     */
    public int tickTimer() {
    	int firstIn = timer.dequeue();
    	timer.enqueue(firstIn);
    	
    	return firstIn;
    }
    
    /**
     * This method creates a CarPart depending on the timer that each unique machine has.
     * @return The CarPart that has been on queue the longest.
     */
    public CarPart produceCarPart() {
    	double randomNum = Math.random() * 2 * getPartWeightError() - getPartWeightError();
    	
		if ( this.timer.front() != 0) {
			conveyor.enqueue(null);
			conveyor.dequeue();
		} else {
			int partId = getPart().getId();
			String partName = getPart().getName();
			double partWeight = getPart().getWeight() + randomNum;
			boolean isDefective = (totalProduction % getChanceOfDefective() == 0) ? true : false;
			
			CarPart carPart = new CarPart(partId, partName, partWeight, isDefective);
			conveyor.enqueue(carPart);
			conveyor.dequeue();
			
			this.totalProduction++;
			
			if ( isDefective ) {
				defectiveQty++;
			}
			tickTimer();
			return carPart;
		}
		tickTimer();
		return conveyor.front();
    }
    
}
