package mountainhuts;

import java.util.Optional;

public class MountainHut {
	
	private String name;
	private String category;
	private int bedsNumber;
	private Municipality municipality;
	private Optional<Integer> altitude;

	public MountainHut(String name, String category, Integer bedsNumber, Municipality municipality, Optional<Integer> altitude) {
		this.name = name;
		this.category = category;
		this.bedsNumber = bedsNumber;
		this.municipality = municipality;
		this.altitude = altitude;
	}

	public String getName() {
		return name;
	}

	public Optional<Integer> getAltitude() {
		return altitude;
	}

	public String getCategory() {
		return category;
	}

	public Integer getBedsNumber() {
		return bedsNumber;
	}

	public Municipality getMunicipality() {
		return municipality;
	}

}
