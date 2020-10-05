package hydraulic;

/**
 * Represents a tap that can interrupt the flow.
 * 
 * The status of the tap is defined by the method
 * {@link #setOpen(boolean) setOpen()}.
 */

public class Tap extends Element {
	
	private boolean open;

	public Tap(String name) {
		super(name);
	}
	
	/**
	 * Defines whether the tap is open or closed.
	 * 
	 * @param open  opening level
	 */
	public void setOpen(boolean open) {
		this.open = open;
	}

	@Override
	public void simulate(double inFlow, SimulationObserver observer) {
		double outFlow = open ? inFlow : 0.0;
		observer.notifyFlow("Tap", getName(), inFlow, outFlow);
		getOutput().simulate(outFlow, observer);
	}


}
