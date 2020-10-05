package it.polito.oop.futsal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static java.util.stream.Collectors.*;

/**
 * Represents a infrastructure with a set of playgrounds, it allows teams
 * to book, use, and  leave fields.
 *
 */
public class Fields { 
	
	private ArrayList<Features> campi = new ArrayList<>();
	private Orario openingTime;
    private Orario closingTime;
    private Map<Integer,Associate> associates = new HashMap<>();
    private ArrayList<Prenotazione> prenotazioni = new ArrayList<>();
    
    public static class Features {
        public final boolean indoor; // otherwise outdoor
        public final boolean heating;
        public final boolean ac;
        
        public int numCampo;
        
        public Features(boolean i, boolean h, boolean a) {
            this.indoor=i; this.heating=h; this.ac = a;
        }
        
        public int getN() {
        	return numCampo;
        }
        
    }
    
    public void defineFields(Features... features) throws FutsalException {
        for (Features feature : features) {
        	if (!feature.indoor && (feature.ac || feature.heating))
        		throw new FutsalException();
        	campi.add(new Features(feature.indoor, feature.heating, feature.ac));
        	campi.get(campi.size()-1).numCampo = campi.size();
        }
    }
    
    public long countFields() {
        return campi.size();
    }

    public long countIndoor() {
    	return campi.stream().filter(c -> c.indoor).count();
    }
    
    public String getOpeningTime() {
        return openingTime.toString();
    }
    
    public void setOpeningTime(String time) {
    	this.openingTime = new Orario(time);
    }
    
    public String getClosingTime() {
        return closingTime.toString();
    }
    
    public void setClosingTime(String time) {
    	this.closingTime = new Orario(time);
    }

    public int newAssociate(String first, String last, String mobile) {
    	associates.put(associates.size(), new Associate(first, last, mobile, associates.size()));
        return associates.size()-1;
    }
    
    public String getFirst(int partyId) throws FutsalException {
    	if (!associates.containsKey(partyId)) throw new FutsalException();
        return associates.get(partyId).getFirst();
    }
    
    public String getLast(int associate) throws FutsalException {
    	if (!associates.containsKey(associate)) throw new FutsalException();
        return associates.get(associate).getLast();
    }
    
    public String getPhone(int associate) throws FutsalException {
    	if (!associates.containsKey(associate)) throw new FutsalException();
        return associates.get(associate).getMobile();
    }
    
    public int countAssociates() {
        return associates.size();
    }
    
    public void bookField(int field, int associate, String time) throws FutsalException {
    	Orario pren = new Orario(time);
    	if (((pren.getMinutiTotali() - openingTime.getMinutiTotali())%60)!=0 || (field > campi.size()) || field<0 || !associates.containsKey(associate)) 
    		throw new FutsalException();
    
    	prenotazioni.add(new Prenotazione(field,associates.get(associate),pren));
    }

    public boolean isBooked(int field, String time) {
    	Orario pren = new Orario(time);
        return prenotazioni.stream().anyMatch(p -> p.getField() == field && p.getTime().equals(pren));
    }
    
    public int getOccupation(int field) {
    	return (int)prenotazioni.stream().filter(p -> p.getField() == field).count();
    }
    
    
    public List<FieldOption> findOptions(String time, Features required) {
    	Orario temp = new Orario(time);
    	ArrayList<FieldOption> lista = new ArrayList<>();
    	int aggiunto=0;
    	for (Features campo : campi) {
    		if (required.indoor) {
    			if (campo.indoor) {
	    			if (required.heating && required.ac) {
	    				if (campo.heating && campo.ac) {
	    					lista.add(new Option(campo.numCampo, getOccupation(campo.numCampo)));
	    					aggiunto=1;
	    				}
	    			}
	    			else if (required.heating && !required.ac) {
	    				if (campo.heating) {
	    					lista.add(new Option(campo.numCampo, getOccupation(campo.numCampo)));
	    					aggiunto=1;
	    				}
	    			}
	    			else if (!required.heating && required.ac) {
	    				if (campo.ac) {
	    					lista.add(new Option(campo.numCampo, getOccupation(campo.numCampo)));
	    					aggiunto=1;
	    				}
	    			}
	    			else {
	    				lista.add(new Option(campo.numCampo, getOccupation(campo.numCampo)));
	    				aggiunto=1;
	    			}
    			}
    		}
    		else {
    			lista.add(new Option(campo.numCampo, getOccupation(campo.numCampo)));
    			aggiunto=1;
    		}
    		if (aggiunto == 1)	
	    		for (Prenotazione p : prenotazioni) {
	    			if (p.getField() == campo.numCampo && p.getTime().equals(temp))
	    				lista.remove(lista.size()-1);
	    		}
    	}
        return lista.stream().sorted((a, b) -> {
        	if (a.getOccupation() == b.getOccupation())
        		return a.getField() - b.getField();
        	else return a.getOccupation() - b.getOccupation();
        }
        ).collect(toList());
    }
    
    public long countServedAssociates() {
        return prenotazioni.stream().map(Prenotazione::getAssociate).distinct().count();
    }
    
    public Map<Integer,Long> fieldTurnover() {
        return campi.stream().collect(toMap(c -> c.getN(), c -> (long)getOccupation(c.getN())));
    }
    
    public double occupation() {
    	double numBlocchi = ((closingTime.getMinutiTotali() - openingTime.getMinutiTotali())/60)*campi.size();
    	double numPrenotazioni = campi.stream().collect(summingInt(c -> getOccupation(c.numCampo)));
        return numPrenotazioni/numBlocchi;
    }
    
}
