package university;

/**
 * This class represents a university education system.
 * 
 * It manages students and courses.
 *
 */
public class University {
	
	private String nomeAteneo;
	private String rettore;
	
	private Studente[] elencoStud = new Studente[MAX_NUM_STUDENTI];
	private int numStud;
	private Insegnamenti[] elencoCor = new Insegnamenti[MAX_NUM_INSEGNAMENTI];
	private int numCor;
	
	private static final int MAX_NUM_STUDENTI = 1000;
	private static final int MAX_NUM_INSEGNAMENTI = 50;
	/**
	 * Constructor
	 * @param name name of the university
	 */
	public University(String nome) {
		this.nomeAteneo = nome;
	}
	
	/**
	 * Getter for the name of the university
	 * @return name of university
	 */
	public String getName() {
		return nomeAteneo;
	}
	
	/**
	 * Defines the rector for the university
	 * 
	 * @param first
	 * @param last
	 */
	public void setRector(String first, String last) {
		rettore = first + " " + last;
	}
	
	/**
	 * Retrieves the rector of the university
	 * 
	 * @return
	 */
	public String getRector() {
		return rettore;
	}
	
	/**
	 * Enroll a student in the university
	 * 
	 * @param first first name of the student
	 * @param last last name of the student
	 * @return
	 */
	public int enroll(String first, String last) {
		if(numStud > MAX_NUM_STUDENTI) {
			System.out.println("Numero max di studenti raggiunto.");
			return -1;
		}
		elencoStud[numStud] = new Studente(first, last, numStud+10000);
		numStud++;
		return elencoStud[numStud-1].getMatricola();
	}
	
	/**
	 * Retrieves the information for a given student
	 * 
	 * @param id the id of the student
	 * @return information about the student
	 */
	public String student(int id) {
		int index = id - 10000;
		if (index >= 0 && index < numStud)
			return elencoStud[index].toString();
		return null;
	}
	
	/**
	 * Activates a new course with the given teacher
	 * 
	 * @param title title of the course
	 * @param teacher name of the teacher
	 * @return the unique code assigned to the course
	 */
	public int activate(String title, String teacher) {
		if(numCor > MAX_NUM_INSEGNAMENTI) {
			System.out.println("Numero max di insegnamenti raggiunto.");
			return -1;
		}
		elencoCor[numCor] = new Insegnamenti(numCor+10, title, teacher);
		numCor++;
		return elencoCor[numCor-1].getCodice();
	}
	
	/**
	 * Retrieve the information for a given course
	 * 
	 * @param code unique code of the course
	 * @return information about the course
	 */
	public String course(int code) {
		int index = code - 10;
		if (index >= 0 && index < numCor)
			return elencoCor[index].toString();
		return null;
	}
	
	/**
	 * Register a student to attend a course
	 * @param studentID id of the student
	 * @param courseCode id of the course
	 */
	public void register(int studentID, int courseCode) {
		Studente stud = elencoStud[studentID - 10000];
		Insegnamenti corso = elencoCor[courseCode - 10];
	
		stud.iscrivi(corso);
		corso.aggiungiStudente(stud);
	}
	
	/**
	 * Retrieve a list of attendees
	 * 
	 * @param courseCode unique id of the course
	 * @return list of attendees separated by "\n"
	 */
	public String listAttendees(int courseCode) {
		if (courseCode>=10 && courseCode<10+numCor) {
			for (int i = 0; i < numCor; i++) {
				if (elencoCor[i].getCodice() == courseCode)
					return elencoCor[i].elencoIscritti();
			}
		}
		return null;
	}

	/**
	 * Retrieves the study plan for a student
	 * 
	 * @param studentID id of the student
	 * @return list of courses the student is registered for
	 */
	public String studyPlan(int studentID) {
		if (studentID>=10000 && studentID<10000+numStud) {
			for (int i = 0; i < numStud; i++) {
				if (elencoStud[i].getMatricola() == studentID)
					return elencoStud[i].elencoInsegnamenti();
			}
		}
		return null;
	}
}
