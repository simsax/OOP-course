package it.polito.oop.futsal;

public class Prenotazione {
	int field;
	Associate associate;
	Orario time;
	
	public Prenotazione(int field, Associate associate, Orario time) {
		this.field = field;
		this.associate = associate;
		this.time = time;
	}

	public int getField() {
		return field;
	}

	public Associate getAssociate() {
		return associate;
	}

	public Orario getTime() {
		return time;
	}

}
