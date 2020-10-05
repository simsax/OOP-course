package clinic;

public class Patient {
	
	private String first;
	private String last;
	private String ssn;
	private Doctor doctor;
	
	public Patient(String first, String last, String ssn) {
		this.first = first;
		this.last = last;
		this.ssn = ssn;
	}
	
	public void setDoctor(Doctor d) {
		this.doctor = d;
	}

	public Doctor getDoctor() {
		return doctor;
	}

	public String getFirst() {
		return first;
	}

	public String getLast() {
		return last;
	}

	public String getSsn() {
		return ssn;
	}

	@Override
	public String toString() {
		return last + " " + first + " (" + ssn + ")";
	}
	
}
