package university;

public class Studente {
	private String nome;
	private String cognome;
	private int matricola;
	private int numCorsi;
	private Insegnamenti[] corsi = new Insegnamenti[MAX_NUM_CORSI];
	
	private static final int MAX_NUM_CORSI = 25;
	
	public Studente(String nome, String cognome, int matricola) {
		this.nome = nome;
		this.cognome = cognome;
		this.matricola = matricola;
	}

	public String getNome() {
		return nome;
	}

	public String getCognome() {
		return cognome;
	}

	public int getMatricola() {
		return matricola;
	}
	
	@Override
	public String toString() {
		return getMatricola() + " " + getNome() + " " + getCognome();
	}

	public void iscrivi(Insegnamenti corso) {
		if (numCorsi < MAX_NUM_CORSI) {
			corsi[numCorsi] = corso;
			numCorsi++;
		}
		else 
			System.out.println("Massimo numero di corsi raggiunto");
	}
	
	public String elencoInsegnamenti() {
		String temp = "";
		for (int i=0; i < numCorsi; i++) {
			temp += corsi[i].toString() + "\n";
		}
		return temp;
	}
}
