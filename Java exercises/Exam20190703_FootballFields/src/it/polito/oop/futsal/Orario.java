package it.polito.oop.futsal;

public class Orario {
	private int ora;
	private int minuto;
	private int minutiTotali;
	
	public Orario(String time) {
		String tokens[];
		tokens=time.split(":");
		this.ora=Integer.parseInt(tokens[0]);
		this.minuto=Integer.parseInt(tokens[1]);
		this.minutiTotali=ora*60+minuto;
	}

	public int getOra() {
		return ora;
	}

	public int getMinuto() {
		return minuto;
	}
	
	public int getMinutiTotali() {
		return minutiTotali;
	}

	@Override
	public String toString() {
		return ora + ":" + minuto;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Orario other = (Orario) obj;
		if (minutiTotali != other.minutiTotali)
			return false;
		if (minuto != other.minuto)
			return false;
		if (ora != other.ora)
			return false;
		return true;
	}
	
	
}
