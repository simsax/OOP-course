package clinic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Represents a clinic with patients and doctors.
 * 
 */
public class Clinic {
	
	private HashMap<String,Patient> patients = new HashMap<>();
	private HashMap<Integer,Doctor> doctors = new HashMap<>();
	
	/**
	 * Add a new clinic patient.
	 * 
	 * @param first first name of the patient
	 * @param last last name of the patient
	 * @param ssn SSN number of the patient
	 */
	public void addPatient(String first, String last, String ssn) {
		patients.put(ssn, new Patient(first, last, ssn));
		
	}


	/**
	 * Retrieves a patient information
	 * 
	 * @param ssn SSN of the patient
	 * @return the object representing the patient
	 * @throws NoSuchPatient in case of no patient with matching SSN
	 */
	public String getPatient(String ssn) throws NoSuchPatient {
		Patient p = patients.get(ssn);
		if (p == null)
			throw new NoSuchPatient("Il paziente non esiste.\n");
		return p.toString();
	}

	/**
	 * Add a new doctor working at the clinic
	 * 
	 * @param first first name of the doctor
	 * @param last last name of the doctor
	 * @param ssn SSN number of the doctor
	 * @param docID unique ID of the doctor
	 * @param specialization doctor's specialization
	 */
	public void addDoctor(String first, String last, String ssn, int docID, String specialization) {
		Doctor d = new Doctor(first, last, ssn, docID, specialization);
		doctors.put(docID, d);
		patients.put(ssn, d);
	}

	/**
	 * Retrieves information about a doctor
	 * 
	 * @param docID ID of the doctor
	 * @return object with information about the doctor
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public String getDoctor(int docID) throws NoSuchDoctor {
		Doctor d = doctors.get(docID);
		if (d == null)
			throw new NoSuchDoctor("Il dottore non esiste.\n");
		return d.toString();
	}
	
	/**
	 * Assign a given doctor to a patient
	 * 
	 * @param ssn SSN of the patient
	 * @param docID ID of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor exists with a matching ID
	 */
	public void assignPatientToDoctor(String ssn, int docID) throws NoSuchPatient, NoSuchDoctor {
		Patient p = patients.get(ssn);
		if (p == null) throw new NoSuchPatient("Il paziente non esiste.\n");
		Doctor d = doctors.get(docID);
		if (d == null) throw new NoSuchDoctor("Il dottore non esiste.\n");
		p.setDoctor(d);
		d.setPatient(p);
	}
	
	/**
	 * Retrieves the id of the doctor assigned to a given patient.
	 * 
	 * @param ssn SSN of the patient
	 * @return id of the doctor
	 * @throws NoSuchPatient in case of not patient with matching SSN
	 * @throws NoSuchDoctor in case no doctor has been assigned to the patient
	 */
	public int getAssignedDoctor(String ssn) throws NoSuchPatient, NoSuchDoctor {
		Patient p = patients.get(ssn);
		if (ssn == null) throw new NoSuchPatient("Il paziente non esiste.\n");
		Doctor d = p.getDoctor();
		if (d == null) throw new NoSuchDoctor("Il dottore non esiste.\n");
		return d.getDocID();
	}
	
	/**
	 * Retrieves the patients assigned to a doctor
	 * 
	 * @param id ID of the doctor
	 * @return collection of patient SSNs
	 * @throws NoSuchDoctor in case the {@code id} does not match any doctor 
	 */
	public Collection<String> getAssignedPatients(int id) throws NoSuchDoctor {
		Doctor d = doctors.get(id);
		if (d == null) throw new NoSuchDoctor("Il dottore non esiste.\n");
		return d.getPatients().stream().map(Patient::getSsn).collect(Collectors.toList());
	}


	/**
	 * Loads data about doctors and patients from the given stream.
	 * <p>
	 * The text file is organized by rows, each row contains info about
	 * either a patient or a doctor.</p>
	 * <p>
	 * Rows containing a patient's info begin with letter {@code "P"} followed by first name,
	 * last name, and SSN. Rows containing doctor's info start with letter {@code "M"},
	 * followed by badge ID, first name, last name, SSN, and specialization.<br>
	 * The elements on a line are separated by the {@code ';'} character possibly
	 * surrounded by spaces that should be ignored.</p>
	 * <p>
	 * In case of error in the data present on a given row, the method should be able
	 * to ignore the row and skip to the next one.<br>

	 * 
	 * @param readed linked to the file to be read
	 * @throws IOException in case of IO error
	 */
	public void loadData(Reader reader) throws IOException {
		BufferedReader in = new BufferedReader(reader);
		String line; //met ag -> var req, tem sv
		Pattern p1 = Pattern.compile("(P|M).*");
		Pattern p2 = Pattern.compile("(P|M)\\s*;\\s*([a-zA-Z]+)\\s*;\\s*([a-zA-Z]+)\\s*;\\s*([a-zA-Z0-9]+)");
		Pattern p3 = Pattern.compile("(P|M)\\s*;\\s*([0-9]+)\\s*;\\s*([a-zA-Z]+)\\s*;\\s*([a-zA-Z]+)\\s*;\\s*([a-zA-Z0-9]+);\\s*([a-zA-Z]+)\\s*");
		while ((line = in.readLine()) != null) {
			Matcher m1 = p1.matcher(line);
			if (m1.matches()) {
				char tag = m1.group(1).charAt(0);
				switch (tag) {
				case 'P':
					Matcher m2 = p2.matcher(line);
					this.addPatient(m2.group(2), m2.group(3), m2.group(4));
					break;
				
				case 'M':
					Matcher m3 = p3.matcher(line);
					this.addDoctor(m3.group(3), m3.group(4), m3.group(5), Integer.parseInt(m3.group(2)), m3.group(6));
					break;
					
				default:
					break;
				}
			}
		}
	}




	/**
	 * Retrieves the collection of doctors that have no patient at all.
	 * The doctors are returned sorted in alphabetical order
	 * 
	 * @return the collection of doctors' ids
	 */
	public Collection<Integer> idleDoctors() {
//		return doctors.values().stream().filter(d -> d.getPatients().size()==0).sorted(Comparator.comparing(Doctor::getLast))
//				.sorted(Comparator.comparing(Doctor::getFirst)).map(Doctor::getDocID).collect(Collectors.toList());
		return doctors.values().stream().filter(d -> d.getPatients().size()==0)
				.sorted((a, b) -> {
					if (a.getLast().equals(b.getLast()))
						return a.getFirst().compareTo(b.getFirst());
					return a.getLast().compareTo(b.getLast());
				}).map(Doctor::getDocID).collect(Collectors.toList());
	}

	/**
	 * Retrieves the collection of doctors having a number of patients larger than the average.
	 * 
	 * @return  the collection of doctors' ids
	 */
	public Collection<Integer> busyDoctors() {
//		float media = 0;
//		LinkedList<Integer> busy = new LinkedList<>();
//		for (Doctor d : doctors.values()) {
//			media += d.getPatients().size();
//		}
//		media = media/doctors.size();
//		for (Doctor d : doctors.values()) {
//			if (d.getPatients().size() > media)
//				busy.add(d.getDocID());
//		}
//		return busy;
		
		Double avg = doctors.values().stream().collect(Collectors.averagingDouble(d -> d.getPatients().size()));
		return doctors.values().stream().filter(d -> d.getPatients().size() > avg).map(Doctor::getDocID).collect(Collectors.toList());
	}

	/**
	 * Retrieves the information about doctors and relative number of assigned patients.
	 * <p>
	 * The method returns list of strings formatted as "{@code ### : ID SURNAME NAME}" where {@code ###}
	 * represent the number of patients (printed on three characters).
	 * <p>
	 * The list is sorted by decreasing number of patients.
	 * 
	 * @return the collection of strings with information about doctors and patients count
	 */
	public Collection<String> doctorsByNumPatients() {
//		LinkedList<Doctor> orderedDoc = new LinkedList<>();
//		LinkedList<String> result = new LinkedList<>();
//		orderedDoc = (LinkedList<Doctor>)doctors.values();
//		orderedDoc.sort(Comparator.comparing(d -> d.getPatients().size(), Comparator.reverseOrder()));
//		for (Doctor d : orderedDoc) {
//			result.add(String.format("%3d : %d %s %s", d.getPatients().size(), d.getDocID(), d.getLast(), d.getFirst()));
//		}
//		return result;
		
		return doctors.values().stream()
				.sorted((a,b) ->  b.getPatients().size() - a.getPatients().size()) //ordinamento decrescente
				.map(d -> String.format("%3d : %d %s %s", d.getPatients().size(), d.getDocID(), d.getLast(), d.getFirst())).collect(Collectors.toList());
	}
	
	/**
	 * Retrieves the number of patients per (their doctor's)  speciality
	 * <p>
	 * The information is a collections of strings structured as {@code ### - SPECIALITY}
	 * where {@code SPECIALITY} is the name of the speciality and 
	 * {@code ###} is the number of patients cured by doctors with such speciality (printed on three characters).
	 * <p>
	 * The elements are sorted first by decreasing count and then by alphabetic speciality.
	 * 
	 * @return the collection of strings with speciality and patient count information.
	 */
	public Collection<String> countPatientsPerSpecialization() {
//		LinkedList<String> result = new LinkedList<>();
//		TreeMap<String, Integer> specializations = new TreeMap<>();
//		for(Doctor d : doctors.values()) {
//			if (!specializations.containsKey(d.getSpecialization()))
//				specializations.put(d.getSpecialization(), d.getPatients().size());
//			else {
//				int old = specializations.get(d.getSpecialization());
//				specializations.put(d.getSpecialization(), old + d.getPatients().size());
//			}
//		}
//		LinkedList<Integer> orderedNumPatients = new LinkedList<Integer>(specializations.values());
//		Collections.sort(orderedNumPatients, Collections.reverseOrder());
//		for (Integer i : orderedNumPatients) {
//			for (Map.Entry<String,Integer> entry : specializations.entrySet()) {
//				if (i.equals(entry.getValue())) {
//					result.add(String.format("%3d", i) + " - " + entry.getKey());
//					specializations.put(entry.getKey(), -1); //evito di riconsiderare la specializzazione nel caso ce ne fossero 2 con gli stessi num di pazienti
//				}
//			}
//		}
//		return result;
		
		return patients.values().stream().filter(p -> p.getDoctor() != null)
				.map(Patient::getDoctor)
				.collect(Collectors.groupingBy(Doctor::getSpecialization, Collectors.counting()))
				.entrySet().stream()
				.sorted((a,b) -> {
					if (a.getValue().equals(b.getValue()))
						return a.getKey().compareTo(b.getKey());
					return b.getValue().compareTo(a.getValue());
				})
				.map(e -> String.format("%3d - %s", e.getValue(), e.getKey()))
				.collect(Collectors.toList());
	}
	
}
