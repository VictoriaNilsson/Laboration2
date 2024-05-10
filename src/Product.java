import java.math.BigDecimal;
import java.util.Objects;

public class Product {

    private String productName;
    private BigDecimal price;
    private final String eanCode;
    private int stock;
    private Category category;

    public Product(String productName, BigDecimal price, String eanCode, int stock, Category category) {
        this.productName = productName;
        this.price = price;
        this.eanCode = eanCode;
        this.stock = stock;
        this.category = category;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getEanCode() {
        return eanCode;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "[" +
                "produkt: " + productName +
                ", pris: " + price +
                ", eankod: " + eanCode +
                ", saldo: " + stock +
                ", kategori: " + category.getCategoryName() +
                "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return stock == product.stock && Objects.equals(productName, product.productName) && Objects.equals(price, product.price) && Objects.equals(eanCode, product.eanCode) && Objects.equals(category, product.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, price, eanCode, stock, category);
    }
}
