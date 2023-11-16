package main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import data_structures.ArrayList;
import data_structures.HashTableSC;
import data_structures.LinkedStack;
import data_structures.BasicHashFunction;
import interfaces.HashFunction;
import interfaces.List;
import interfaces.Map;
import interfaces.Stack;

public class CarPartFactory {
	private String orderPath;
	private String partsPath;
	
	private List<PartMachine> machine;
	private List<Order> orders;
	private Map<Integer, CarPart> partCatalog;
	private Map<Integer, List<CarPart>> inventory;
	private Map<Integer, Integer> defectives;
	private Stack<CarPart> productBin;
	
    public HashFunction<Integer> hashFunction = new BasicHashFunction();
    
	// Constructor
    public CarPartFactory(String orderPath, String partsPath) throws IOException {
    	this.orderPath = orderPath;
    	this.partsPath = partsPath;
    	this.machine = new ArrayList<>();
    	this.orders = new ArrayList<>();
    	this.productBin = new LinkedStack<>();
    	
    	try {
    		setupMachines(partsPath);
        	setupOrders(orderPath);
        	setupInventory();
    	} catch (IOException e) {
    		e.printStackTrace();
    	}
    }
    
    // Getters
    public List<PartMachine> getMachines() {
    	return this.machine;
    }

    public Stack<CarPart> getProductionBin() {
      return this.productBin;
    }

    public Map<Integer, CarPart> getPartCatalog() {
    	partCatalog = new HashTableSC<>(20, hashFunction);
        
        for ( PartMachine x : getMachines() ) {
        	partCatalog.put(x.getId(), x.getPart());
        }
        return partCatalog;
    }

    public Map<Integer, List<CarPart>> getInventory() {
       return this.inventory;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public Map<Integer, Integer> getDefectives() {
    	defectives = new HashTableSC<>(20, hashFunction);
        
        for ( PartMachine x : getMachines() ) {
        	defectives.put(x.getId(), x.getTotalDefectives());
        }
        return defectives;
    }
    
    // Setters
    public void setMachines(List<PartMachine> machines) {
        this.machine = machines;
    }
    
    public void setProductionBin(Stack<CarPart> production) {
        this.productBin = production;
    }
    
    public void setPartCatalog(Map<Integer, CarPart> partCatalog) {
        this.partCatalog = partCatalog;
    }
    
    public void setInventory(Map<Integer, List<CarPart>> inventory) {
    	this.inventory = inventory;
    }
    
    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
    
    public void setDefectives(Map<Integer, Integer> defectives) {
        this.defectives = defectives;
    }
    
    // Methods
    
    /**
     * Generates a report indicating how many parts were produced per machine,
     * how many of those were defective and are still in inventory. Additionally, 
     * it also shows how many orders were successfully fulfilled. 
     */
    public void generateReport() {
        String report = "\t\t\tREPORT\n\n";
        report += "Parts Produced per Machine\n";
        for (PartMachine machine : this.getMachines()) {
            report += machine + "\t(" + 
            this.getDefectives().get(machine.getPart().getId()) +" defective)\t(" + 
            this.getInventory().get(machine.getPart().getId()).size() + " in inventory)\n";
        }
       
        report += "\nORDERS\n\n";
        for (Order transaction : this.getOrders()) {
            report += transaction + "\n";
        }
        report += "\nUpdated Inventory\n\n";
        for (List<CarPart> x : this.getInventory().getValues() ) {
        	report += x + "\n";
        }
        System.out.println(report);
    }

    /**
     * Takes the parameter and creates order map.
     * @param path
     * @throws IOException
     */
    public void setupOrders(String path) throws IOException {
    	BufferedReader input = new BufferedReader( new FileReader(path) );
		
		String line;
		boolean firstIndex = true;
		
		while( (line = input.readLine()) != null ) {
			if( firstIndex ) {
				firstIndex = false;
				continue;
			}
			
			String[] sections = line.split(",");
			
			int ID = Integer.parseInt( sections[0] );
			String customer = sections[1];
			
			Map<Integer,Integer> order =  new HashTableSC<Integer,Integer>(110, hashFunction);
			
			if ( sections.length == 3 ) {
				String[] orders = sections[2].split("-");
				
				for( String x : orders ) {
					String[] orderReqParts = x.replace("(", "").replace(")", "").split(" ");
					int key = Integer.parseInt(orderReqParts[0]);
					int value = Integer.parseInt(orderReqParts[1]);
					
					order.put(key, value);
				} 
			}
			
			Order newOrder = new Order( ID, customer, order, false);
			orders.add(newOrder);
		}
    }
    
    /**
     * Takes the parameter and creates machine list.
     * @param path
     * @throws IOException
     */
    public void setupMachines(String path) throws IOException {
    	
    	BufferedReader input = new BufferedReader( new FileReader(path) );
		
		String line;
		boolean firstIndex = true;
		
		while( ( line = input.readLine() ) != null ) {
			if( firstIndex ) {
				firstIndex = false;
				continue;
			}
			
			String[] sections = line.split(",");
			
			int ID = Integer.parseInt( sections[0] );
			String partName = sections[1];
			double weight = Double.parseDouble(sections[2]);
			double weightError = Double.parseDouble(sections[3]);
			int period = Integer.parseInt(sections[4]);
			int chanceOfDefective  = Integer.parseInt(sections[5]);
			
			CarPart carPart = new CarPart(ID,partName,weight, false);
			
			PartMachine partMachine = new PartMachine( ID, carPart, period, weightError, chanceOfDefective );
			machine.add(partMachine);
			
		}
    }

    /**
     * Inventory is setup.
     */
    public void setupInventory() {
        inventory = new HashTableSC<>(25, hashFunction);
        
        for ( PartMachine x : getMachines() ) {
        	inventory.put(x.getPart().getId(), new ArrayList<>());
        }
    }
    
    /**
     * This method empties the bins that were filled with parts and puts them in inventory.
     */
    public void storeInInventory() {
    	while ( !productBin.isEmpty() ) {
    		
    		CarPart partToMove = productBin.pop();
    		int partID = partToMove.getId();
    		
    		if ( partToMove.isDefective() ) {
    			continue;
    		}
    		
    		if ( inventory.containsKey(partID) ) {
        		inventory.get(partID).add(partToMove);
        	} else {
        		List<CarPart> newInventory = new ArrayList<>();
        		newInventory.add(partToMove);
        		
        		inventory.put(partID, newInventory);
        	}
    	}
    }
    
    /**
     * Principal method. It tells each unique machine the periods of the timers.
     * If a part is produced and not defective it goes to the bin. 
     * After the end of the day, conveyer belts are cleared and orders processed.
     * @param days
     * @param minutes
     */
    public void runFactory(int days, int minutes) {
        while ( days != 0 ) {
        	for ( int i = minutes - 1; i >= 0; i-- ) {
        		for ( PartMachine x : getMachines() ) {
        			CarPart part = x.produceCarPart();
        			
        			if ( part != null && !part.isDefective() ) {
        				productBin.push(part);
        			}
        		}
        	}
        	
        	for( PartMachine y : getMachines() ) {
        		y.resetConveyorBelt();
        		
        	}
        	
        	storeInInventory();
        	days--;
        }
        
        processOrders();
    }

    /**
     * This method verifies the quantity left in inventory of a part and checks how many does the customer need.
     * If there are not sufficient parts in inventory then order isn't fulfilled, else it is.
     */
    public void processOrders() {
    	
		for (Order x : getOrders()) {
	        Map<Integer, Integer> partsReq = x.getRequestedParts();

	        boolean orderFulfilled = true;

	        for (int y : partsReq.getKeys()) {
	            int partsReqQty = partsReq.get(y);

	            List<CarPart> stock = inventory.get(y);
	            
	            if (stock == null || stock.size() < partsReqQty) {
	                orderFulfilled = false;
	                break;  
	            }
	        }

	        if (orderFulfilled) {
	            for (int y : partsReq.getKeys()) {
	                int partsReqQty = partsReq.get(y);

	                List<CarPart> stock = inventory.get(y);

	                for (int i = 0; i < partsReqQty; i++) {
	                    stock.remove(0);  
	                }
	            }
	        }

	        x.setFulfilled(orderFulfilled);
	    }
    }   
}
