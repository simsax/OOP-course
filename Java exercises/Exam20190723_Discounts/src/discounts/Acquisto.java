package discounts;

import java.util.HashMap;

public class Acquisto {
	private int numCarta = 0;
	//private HashMap<String, Product> products = new HashMap<>();
	private int codAcquisto;
	private double importoFinale;
	private double sconto = 0;
	private int numUnits = 0;
	
	public Acquisto (int numCarta, double importo, int codAcquisto, double sconto, int numUnits) {
		this.numCarta = numCarta;
		this.importoFinale = importo;
		this.codAcquisto = codAcquisto;
		this.sconto = sconto;
		this.numUnits = numUnits;
	}
	
	public Acquisto (double importo, int codAcquisto, int numUnits) {
		this.importoFinale = importo;
		this.codAcquisto = codAcquisto;
		this.numUnits = numUnits;
	}

	public int getNumCarta() {
		return numCarta;
	}


	public int getCodAcquisto() {
		return codAcquisto;
	}

	public double getImportoFinale() {
		return importoFinale;
	}
	
	public double getSconto() {
		return sconto;
	}

	public int getNumUnits() {
		return numUnits;
	}
	
	

}
