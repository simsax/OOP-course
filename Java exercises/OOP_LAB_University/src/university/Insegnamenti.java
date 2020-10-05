package university;

public class Insegnamenti {
	private int codice;
	private String nomeCorso;
	private String titolare;
	private Studente[] iscritti = new Studente[MAX_NUM_ISCRITTI];
	private int numStudenti;
	
	private static final int MAX_NUM_ISCRITTI = 100;
	
	public Insegnamenti(int codice, String nomeCorso, String titolare) {
		this.codice = codice;
		this.nomeCorso = nomeCorso;
		this.titolare = titolare;
	}

	public int getCodice() {
		return codice;
	}

	public String getNomeCorso() {
		return nomeCorso;
	}

	public String getTitolare() {
		return titolare;
	}
	
	@Override
	public String toString() {
		return getCodice() + "," + getNomeCorso() + "," + getTitolare();
	}

	public void aggiungiStudente(Studente stud) {
		if (numStudenti < MAX_NUM_ISCRITTI) {
			iscritti[numStudenti] = stud;
			numStudenti++;
		}
		else 
			System.out.println("Massimo numero di studenti per questo corso raggiunto");
	}
	
	public String elencoIscritti() {
		String temp = "";
		for (int i=0; i < numStudenti; i++) {
			temp += iscritti[i].toString() + "\n";
		}
		return temp;
	}
}
