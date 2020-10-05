package clinic;

import java.util.LinkedList;

public class Doctor extends Patient {
	
	private int docID;
	private String specialization;
	private LinkedList<Patient> patients = new LinkedList<>();
	
	public Doctor(String first, String last, String ssn, int docID, String specialization) {
		super(first, last, ssn);
		this.docID = docID;
		this.specialization = specialization;
	}

	public void setPatient(Patient p) {
		patients.add(p);
	}

	public LinkedList<Patient> getPatients() {
		return patients;
	}

	public int getDocID() {
		return docID;
	}

	public String getSpecialization() {
		return specialization;
	}

	@Override
	public String toString() {
		return super.toString() + " [" + docID + "]: " + specialization;
	}
	
}
