package mountainhuts;

import static java.util.stream.Collectors.toList;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Region {
	
	private String name;
	private ArrayList<Integer> minAltitudeRanges = new ArrayList<>();
	private ArrayList<Integer> maxAltitudeRanges = new ArrayList<>();
	private ArrayList<Municipality> municipalities = new ArrayList<>();
	private ArrayList<MountainHut> mountainHuts = new ArrayList<>();

	/**
	 * Create a region with the given name.
	 * 
	 * @param name
	 *            the name of the region
	 */
	public Region(String name) {
		this.name = name;
	}

	/**
	 * Return the name of the region.
	 * 
	 * @return the name of the region
	 */
	public String getName() {
		return name;
	}

	/**
	 * Create the ranges given their textual representation in the format
	 * "[minValue]-[maxValue]".
	 * 
	 * @param ranges
	 *            an array of textual ranges
	 */
	public void setAltitudeRanges(String... ranges) {
		Pattern p = Pattern.compile("([0-9]+)-([0-9]+)");
		for (String range : ranges) {
			Matcher m = p.matcher(range);
			if (m.matches()) {
				minAltitudeRanges.add(Integer.parseInt(m.group(1)));
				maxAltitudeRanges.add(Integer.parseInt(m.group(2)));
			}
		}
	}

	/**
	 * Return the textual representation in the format "[minValue]-[maxValue]" of
	 * the range including the given altitude or return the default range "0-INF".
	 * 
	 * @param altitude
	 *            the geographical altitude
	 * @return a string representing the range
	 */
	public String getAltitudeRange(Integer altitude) {
		for (int i = 0; i < minAltitudeRanges.size(); i++) {
			if (altitude >= minAltitudeRanges.get(i) && altitude <= maxAltitudeRanges.get(i)) {
				return minAltitudeRanges.get(i) + "-" + maxAltitudeRanges.get(i);
			}
		}
		return "0-INF";
	}

	/**
	 * Return all the municipalities available.
	 * 
	 * @return a collection of municipalities
	 */
	public Collection<Municipality> getMunicipalities() {
		return municipalities;
	}

	/**
	 * Return all the mountain huts available.
	 * 
	 * @return a collection of mountain huts
	 */
	public Collection<MountainHut> getMountainHuts() {
		return mountainHuts;
	}

	/**
	 * Create a new municipality if it is not already available or find it.
	 * Duplicates must be detected by comparing the municipality names.
	 * 
	 * @param name
	 *            the municipality name
	 * @param province
	 *            the municipality province
	 * @param altitude
	 *            the municipality altitude
	 * @return the municipality
	 */
	public Municipality createOrGetMunicipality(String name, String province, Integer altitude) {
//		for (Municipality m : municipalities) {
//			if (m.getName().equals(name)) {
//				return m;
//			}
//		}
//		municipalities.add(new Municipality(name, province, altitude));
//		return municipalities.get(municipalities.size()-1);
		return municipalities.stream().filter(m -> m.getName().equals(name)).findFirst()
				.orElseGet(() -> {
					Municipality m = new Municipality(name, province, altitude);
					municipalities.add(m);
					return m;
				});
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 *
	 * @param name
	 *            the mountain hut name
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return the mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, String category, Integer bedsNumber,
			Municipality municipality) {
		return createOrGetMountainHut(name, null, category, bedsNumber, municipality);
	}

	/**
	 * Create a new mountain hut if it is not already available or find it.
	 * Duplicates must be detected by comparing the mountain hut names.
	 * 
	 * @param name
	 *            the mountain hut name
	 * @param altitude
	 *            the mountain hut altitude
	 * @param category
	 *            the mountain hut category
	 * @param bedsNumber
	 *            the number of beds in the mountain hut
	 * @param municipality
	 *            the municipality in which the mountain hut is located
	 * @return a mountain hut
	 */
	public MountainHut createOrGetMountainHut(String name, Integer altitude, String category, Integer bedsNumber,
			Municipality municipality) {
//		for (MountainHut m : mountainHuts) {
//			if (m.getName().equals(name)) {
//				return m;
//			}
//		}
//		mountainHuts.add(new MountainHut(name, category, bedsNumber, municipality, Optional.ofNullable(altitude)));
//		return mountainHuts.get(mountainHuts.size()-1);
		return mountainHuts.stream().filter(m -> m.getName().equals(name)).findFirst()
				.orElseGet(() -> {
					MountainHut m = new MountainHut(name, category, bedsNumber, municipality, Optional.ofNullable(altitude));
					mountainHuts.add(m);
					return m;
				});
	}

	/**
	 * Creates a new region and loads its data from a file.
	 * 
	 * The file must be a CSV file and it must contain the following fields:
	 * <ul>
	 * <li>{@code "Province"},
	 * <li>{@code "Municipality"},
	 * <li>{@code "MunicipalityAltitude"},
	 * <li>{@code "Name"},
	 * <li>{@code "Altitude"},
	 * <li>{@code "Category"},
	 * <li>{@code "BedsNumber"}
	 * </ul>
	 * 
	 * The fields are separated by a semicolon (';'). The field {@code "Altitude"}
	 * may be empty.
	 * 
	 * @param name
	 *            the name of the region
	 * @param file
	 *            the path of the file
	 */
	public static Region fromFile(String name, String file) {
		Region r = new Region(name);
		r.readData(file);
		return r;
	}

	private void readData(String file) {
		List<String> lines = null;

		try (BufferedReader in = new BufferedReader(new FileReader(file))) {
			lines = in.lines().collect(toList());
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}

		if (lines == null)
			return;

		// Continue the implementation here
		String[] tokens;
		//anche lines.forEach((line) -> {...
		for (int i = 1; i < lines.size(); i++) {
			tokens = lines.get(i).split(";");
			if (tokens[4].equals(""))
				this.createOrGetMountainHut(tokens[3], tokens[5], Integer.parseInt(tokens[6]), this.createOrGetMunicipality(tokens[1], tokens[0], Integer.parseInt(tokens[2])));
			else
				this.createOrGetMountainHut(tokens[3], Integer.parseInt(tokens[4]), tokens[5], Integer.parseInt(tokens[6]), this.createOrGetMunicipality(tokens[1], tokens[0], Integer.parseInt(tokens[2])));
		}
	}

	/**
	 * Count the number of municipalities with at least a mountain hut per each
	 * province.
	 * 
	 * @return a map with the province as key and the number of municipalities as
	 *         value
	 */
	public Map<String, Long> countMunicipalitiesPerProvince() {
//		Map<String, Long> map = new TreeMap<>();
//		for (Municipality m : municipalities) {
//			if (map.containsKey(m.getProvince())) {
//				long value = map.get(m.getProvince());
//				map.put(m.getProvince(), ++value);
//			}
//			else
//				map.put(m.getProvince(), (long)1);
//		}
//		return map;
		return municipalities.stream().collect(Collectors.groupingBy(Municipality::getProvince, Collectors.counting()));
	}

	/**
	 * Count the number of mountain huts per each municipality within each province.
	 * 
	 * @return a map with the province as key and, as value, a map with the
	 *         municipality as key and the number of mountain huts as value
	 */
	public Map<String, Map<String, Long>> countMountainHutsPerMunicipalityPerProvince() {
//		Map<String, Map<String, Long>> map = new TreeMap<>();
//		Map<String, Long> map1 = new TreeMap<>();
//		for (Municipality mun : municipalities) {
//			for (MountainHut m : mountainHuts) {
//				if (mun.getProvince().equals(m.getMunicipality().getProvince()))
//					break;
//			}
//			if (map1.containsKey(mun.getName())) {
//				long value = map1.get(mun.getName());
//				map1.put(mun.getName(), ++value);
//			}
//			else 
//				map1.put(mun.getName(), (long)1);
//			map.put(mun.getProvince(), map1);
//		}		
//		return map;
		return mountainHuts.stream().collect(Collectors.groupingBy(x -> x.getMunicipality().getProvince(),
											Collectors.groupingBy(x -> x.getMunicipality().getName(), Collectors.counting())));
	}

	/**
	 * Count the number of mountain huts per altitude range. If the altitude of the
	 * mountain hut is not available, use the altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the number of mountain huts
	 *         as value
	 */
	public Map<String, Long> countMountainHutsPerAltitudeRange() {
		return mountainHuts.stream().collect(Collectors.groupingBy(getAltitudeRange, Collectors.counting()));
	}
	
	private Function<MountainHut, String> getAltitudeRange = m -> {
		int altitude;
		if (!m.getAltitude().isPresent()) {
			altitude = m.getMunicipality().getAltitude();
		} else {
			altitude = m.getAltitude().get();
		}
		return getAltitudeRange(altitude);
	};

	/**
	 * Compute the total number of beds available in the mountain huts per each
	 * province.
	 * 
	 * @return a map with the province as key and the total number of beds as value
	 */
	public Map<String, Integer> totalBedsNumberPerProvince() {
//		Map<String, Integer> map = new TreeMap<>();
//		for (Municipality mun : municipalities) {
//			int bedsNumber = 0;
//			for (MountainHut m : mountainHuts) {
//				if (m.getMunicipality().getProvince().equals(mun.getProvince()))
//					bedsNumber += m.getBedsNumber();
//			}
//			map.put(mun.getProvince(), bedsNumber);
//		}
//		return map;
		return mountainHuts.stream().collect(Collectors.groupingBy(x -> x.getMunicipality().getProvince(), Collectors.summingInt(MountainHut::getBedsNumber)));
	}

	/**
	 * Compute the maximum number of beds available in a single mountain hut per
	 * altitude range. If the altitude of the mountain hut is not available, use the
	 * altitude of its municipality.
	 * 
	 * @return a map with the altitude range as key and the maximum number of beds
	 *         as value
	 */
	public Map<String, Optional<Integer>> maximumBedsNumberPerAltitudeRange() {
		return mountainHuts.stream().collect(Collectors
									.groupingBy(getAltitudeRange, Collectors
									.mapping(MountainHut::getBedsNumber, Collectors
									.maxBy(Comparator.naturalOrder()))));
	}

	/**
	 * Compute the municipality names per number of mountain huts in a municipality.
	 * The lists of municipality names must be in alphabetical order.
	 * 
	 * @return a map with the number of mountain huts in a municipality as key and a
	 *         list of municipality names as value
	 */
	public Map<Long, List<String>> municipalityNamesPerCountOfMountainHuts() {
		long numRifugi;
		Map<Long, List<String>> map = new TreeMap<>();
		for (int i = 0; i < municipalities.size(); i++) {	
			numRifugi = 0;
			ArrayList<String> nomiComuni = new ArrayList<String>();
			for (MountainHut m : mountainHuts) {
				if (m.getMunicipality().equals(municipalities.get(i)))
					numRifugi++;
			}
			nomiComuni.add(municipalities.get(i).getName());
			if (!map.containsKey(numRifugi))
				map.put(numRifugi, nomiComuni);
			else {
				map.get(numRifugi).add(municipalities.get(i).getName());
				Collections.sort(map.get(numRifugi));
			}
		}
		return map;
	}

}
