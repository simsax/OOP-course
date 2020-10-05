package discounts;

public class Product {
	private String categoryId;
	private String productId;
	private double price;
	
	public Product(String categoryId, String productId, double price) {
		this.categoryId = categoryId;
		this.productId = productId;
		this.price = price;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public String getProductId() {
		return productId;
	}

	public double getPrice() {
		return price;
	}
	
	
	
}
