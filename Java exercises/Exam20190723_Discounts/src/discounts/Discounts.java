package discounts;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;

import org.omg.CORBA.IMP_LIMIT;

import sun.nio.cs.ext.DoubleByteEncoder;
import sun.reflect.generics.tree.Tree;
import sun.swing.StringUIClientPropertyKey;

public class Discounts {
	
	private HashMap<Integer, String> cards = new HashMap<>();
	private int numCarte = 0;
	private HashMap<String, Product> products = new HashMap<>();
	private HashMap<String, Integer> sconti = new HashMap<>();
	private HashMap<Integer, Acquisto> acquisti = new HashMap<>();
	private HashMap<String, Integer> prodPerNo = new HashMap<>(); 
	private int numAcquisti = 0;
	
	//R1
	public int issueCard(String name) {
		cards.put(++numCarte, name);
	    return numCarte;
	}
	
    public String cardHolder(int cardN) {
        return cards.get(cardN);
    }
    

	public int nOfCards() {
	       return numCarte;
	}
	
	//R2
	public void addProduct(String categoryId, String productId, double price) 
			throws DiscountsException {
		if (products.containsKey(productId)) throw new DiscountsException();
		products.put(productId, new Product(categoryId, productId, price));
		sconti.put(categoryId, 0);
	}
	
	public double getPrice(String productId) 
			throws DiscountsException {
		if (!products.containsKey(productId)) throw new DiscountsException();
        return products.get(productId).getPrice();
	}

	public int getAveragePrice(String categoryId) throws DiscountsException {
		if (!products.values().stream().anyMatch(p -> p.getCategoryId().equals(categoryId))) throw new DiscountsException();	
        double v = products.values().stream().filter(p -> p.getCategoryId().equals(categoryId)).collect(Collectors.averagingDouble(Product::getPrice));
        int i = products.values().stream().filter(p -> p.getCategoryId().equals(categoryId)).collect(Collectors.averagingDouble(Product::getPrice)).intValue();
        if (v-i >= 0.5) return i+1;
        else return i;
	}
	
	//R3
	public void setDiscount(String categoryId, int percentage) throws DiscountsException {
		if (!sconti.containsKey(categoryId) || percentage<0 || percentage >50) throw new DiscountsException();
		sconti.replace(categoryId, percentage);
	}

	public int getDiscount(String categoryId) {
        return sconti.get(categoryId);
	}

	//R4
	public int addPurchase(int cardId, String... items) throws DiscountsException {
		double importo = 0;
		double scontoTot = 0;
		double importoFinale = 0;
		int numUnits = 0;
		for (String s : items) {
			String[] tokens = s.split(":");
			String productId = tokens[0];
			if (!products.containsKey(productId)) throw new DiscountsException();
			int numProdotti = Integer.parseInt(tokens[1]);
			numUnits += numProdotti;
			double sconto = (double)sconti.get(products.get(productId).getCategoryId())/100;
			scontoTot += sconto*products.get(productId).getPrice()*numProdotti;
			importo += products.get(productId).getPrice()*numProdotti;
			prodPerNo.put(productId, prodPerNo.getOrDefault(productId, 0)+numProdotti);
		}
		importoFinale = importo - scontoTot;
		acquisti.put(++numAcquisti, new Acquisto(cardId, importoFinale, numAcquisti, scontoTot, numUnits));
        return numAcquisti;
	}

	public int addPurchase(String... items) throws DiscountsException {
		double importo = 0;
		int numUnits = 0;
		for (String s : items) {
			String[] tokens = s.split(":");
			String productId = tokens[0];
			if (!products.containsKey(productId)) throw new DiscountsException();
			int numProdotti = Integer.parseInt(tokens[1]);
			numUnits += numProdotti;
			importo += products.get(productId).getPrice()*numProdotti;
			prodPerNo.put(productId, prodPerNo.getOrDefault(productId, 0)+numProdotti);
		}
		acquisti.put(++numAcquisti, new Acquisto(importo, numAcquisti, numUnits));
        return numAcquisti;
	}
	
	public double getAmount(int purchaseCode) {
        return acquisti.get(purchaseCode).getImportoFinale();
	}
	
	public double getDiscount(int purchaseCode)  {
        return acquisti.get(purchaseCode).getSconto();
	}
	
	public int getNofUnits(int purchaseCode) {
        return acquisti.get(purchaseCode).getNumUnits();
	}
	
	//R5
	public SortedMap<Integer, List<String>> productIdsPerNofUnits() {
		return prodPerNo.entrySet().stream().collect(Collectors.toMap(e -> e.getValue(), e -> {LinkedList<String> l = new LinkedList<>(); l.add(e.getKey());return l;},(l1,l2) -> {l1.addAll(l2);Collections.sort(l1);return l1;}, TreeMap::new));
	}
	
	public SortedMap<Integer, Integer> totalPurchasePerCard() {
        return acquisti.values().stream().filter(a -> a.getNumCarta()!=0 ).collect(Collectors.groupingBy(Acquisto::getNumCarta,TreeMap::new,Collectors.summingInt(a -> {
        	if ((a.getImportoFinale()-(int)a.getImportoFinale())<0.5) return (int)a.getImportoFinale();
        	else return (int)a.getImportoFinale()+1;
        })));
	}
	
	public int totalPurchaseWithoutCard() {
        return acquisti.values().stream().filter(a -> a.getNumCarta()==0).collect(Collectors.summingInt(a -> {
        	int importo = (int)a.getImportoFinale();
        	if ((a.getImportoFinale() - importo) < 0.5) return importo;
        	else return importo+1;
        }));
	}
	
	public SortedMap<Integer, Integer> totalDiscountPerCard() {
        return acquisti.values().stream().filter(a -> a.getSconto()!=0).collect(Collectors.groupingBy(Acquisto::getNumCarta,TreeMap::new,Collectors.summingInt(a -> {
        	if ((a.getSconto()-(int)a.getSconto())<0.5) return (int)a.getSconto();
        	else return (int)a.getSconto()+1;
        })));
	}


}
