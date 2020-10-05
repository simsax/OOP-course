package hydraulic;

/**
 * Represents the generic abstract element of an hydraulics system.
 * It is the base class for all elements.
 *
 * Any element can be connect to a downstream element
 * using the method {@link #connect(Element) connect()}.
 */
public abstract class Element {
	
	private String name;
	protected Element[] outputs;
	
	/**
	 * Constructor
	 * @param name the name of the element
	 */
	public Element(String name){
		this(name,1);
	}
	
	public Element(String name, int noutputs) {
		this.name = name;
		outputs = new Element[noutputs];
	}

	/**
	 * getter method
	 * @return the name of the element
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Connects this element to a given element.
	 * The given element will be connected downstream of this element
	 * @param elem the element that will be placed downstream
	 */
	public void connect(Element elem) {
//		if (this instanceof Sink) return;
//		else this.output = elem;
//		output.flow = this.flow;
		outputs[0] = elem;
	}
	
	/**
	 * Retrieves the element connected downstream of this
	 * @return downstream element
	 */
	public Element getOutput() {
		return outputs[0];
	}

	public abstract void simulate(double inFlow, SimulationObserver observer) ;
}
