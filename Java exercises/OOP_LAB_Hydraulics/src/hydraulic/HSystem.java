package hydraulic;

import java.util.ArrayList;

/**
 * Main class that act as a container of the elements for
 * the simulation of an hydraulics system 
 * 
 */
public class HSystem {
	
	private ArrayList<Element> elementi = new ArrayList<Element>();
	
	/**
	 * Adds a new element to the system
	 * @param elem
	 */
	public void addElement(Element elem){
		elementi.add(elem);
	}
	
	/**
	 * returns the element added so far to the system
	 * @return an array of elements whose length is equal to the number of added elements
	 */
	public Element[] getElements() {
//		Element[] elem = new Element[elementi.size()];
//		for (int i=0; i < elementi.size(); i++)
//			elem[i] = elementi.get(i);
//		return elem;
		return elementi.toArray(new Element[elementi.size()]);
	}
	
	/**
	 * Prints the layout of the system starting at each Source
	 */
	public String layout(){
		// TODO: to be implemented
		return null;
	}
	
	/**
	 * starts the simulation of the system
	 */
	public void simulate(SimulationObserver observer) {
			for (Element e : elementi) {
				if (e instanceof Source) {
					e.simulate(SimulationObserver.NO_FLOW, observer);
				}
			}
	}
}
