import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;

public class Inventory {

    private static List<Product> productList = new ArrayList<>();
    private static List<Category> categoryList = new ArrayList<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {

        readFromFile();
        menu();
    }

    private static Path getPath() {
        String homeFolder = System.getProperty("user.home");
        return Path.of(homeFolder, "products.json");
    }

    private static void readFromFile() {
        Gson gson = new Gson();
        Path path = getPath();

        try (Reader reader = new FileReader(path.toFile())) {
            productList = gson.fromJson(reader, new TypeToken<ArrayList<Product>>() {
            }.getType());

        } catch (IOException e) {
            System.out.println("Inga produkter hittades, varulager är tomt.");
        }
    }


    private static void printMenu() {
        System.out.println("""
                HUVUDMENY
                ---------
                1. Lägg till produkt
                2. Ta bort produkt
                3. Ändra lagersaldo
                4. Visa alla produkter
                5. Sök efter produkt
                e. Avsluta
                """);
    }

    private static void menu() {
        while (true) {
            printMenu();
            String input = scanner.next();
            switch (input) {
                case "1" -> addProduct();
                case "2" -> removeProduct();
                case "3" -> updateStock();
                case "4" -> productMenu();
                case "5" -> searchMenu();
                case "e", "E" -> saveAndExit();
                default -> System.out.println("Fel vid inmatning.");
            }
        }
    }

    private static void addProduct() {
        var category = addCategory();
        System.out.println("Ange produktnamn: ");
        String productName = scanner.next();
        System.out.println("Ange pris: ");
        BigDecimal price = BigDecimal.valueOf(scanner.nextDouble());
        System.out.println("Ange ean kod: ");
        String eanCode = scanner.next();
        System.out.println("Ange antal: ");
        int stock = scanner.nextInt();

        var product = new Product(productName, price, eanCode, stock, category);
        productList.add(product);

        System.out.println("Produkten är tillagd!");
    }

    private static Category addCategory() {

        System.out.println("Ange kategori: ");
        String input = scanner.next().toLowerCase();

        for (Category category : categoryList) {
            if (category.getCategoryName().equals(input)) {
                return category;
            }
        }

        return createNewCategory(input);
    }

    private static Category createNewCategory(String input) {
        var category = new Category(input);
        categoryList.add(category);
        return category;
    }

    private static void removeProduct() {
        printProductList();
        System.out.println("Ange ean koden på den produkt du vill ta bort: ");
        String input = scanner.next();

        productList.removeIf(product -> product.getEanCode().equals(input));

        System.out.println("Produkten har tagits bort!");
    }

    private static void printProductList() {
        productList.forEach(System.out::println);
    }

    private static void updateStock() {
        printProductList();

        System.out.println("Ange eankod på den produkt du vill ändra lagersaldo på:");
        String input = scanner.next();

        for (Product product : productList) {
            if (product.getEanCode().equals(input)) {
                System.out.println("Ange nytt saldo: ");
                int stock = scanner.nextInt();
                product.setStock(stock);
                System.out.println("Produktsaldo har uppdaterats!");
            }
        }
    }

    private static void printProductMenu() {
        System.out.println("""
                Välj ett alternativ:
                ---------------------
                1. Sortera efter produktnamn
                2. Sortera efter pris
                3. Återgå till huvudmeny
                """);
    }

    private static void productMenu() {
        printProductMenu();
        String input = scanner.next();
        switch (input) {
            case "1" -> sortByName();
            case "2" -> sortByPrice();
            case "3" -> menu();
        }
    }


    private static void sortByName() {
        productList.stream()
                .sorted(Comparator.comparing(Product::getProductName))
                .forEach(System.out::println);
    }

    private static void sortByPrice() {
        productList.stream()
                .sorted(Comparator.comparing(Product::getPrice))
                .forEach(System.out::println);
    }

    private static void printSearchMenu() {
        System.out.println("""
                Välj ett alternativ:
                ---------------------
                1. Sök efter eankod
                2. Sök efter kategori
                3. Återgå till huvudmeny
                """);
    }

    private static void searchMenu() {
        printSearchMenu();
        String input = scanner.next();
        switch (input) {
            case "1" -> searchByEan();
            case "2" -> searchByCategory();
            case "3" -> menu();
        }
    }

    private static void searchByEan() {
        System.out.println("Ange eankod på produkten du söker: ");
        String input = scanner.next();

        productList.stream()
                .filter(product -> product.getEanCode().equals(input))
                .forEach(System.out::println);

    }

    private static void searchByCategory() {
        System.out.println("Ange kategorin du söker efter: ");
        String input = scanner.next().toLowerCase();

        productList.stream()
                .filter(product -> product.getCategory().getCategoryName().equals(input))
                .forEach(System.out::println);
    }

    private static void saveAndExit() {
        Gson gson = new Gson();
        Path path = getPath();

        try (FileWriter fileWriter = new FileWriter(path.toFile())) {
            gson.toJson(productList, fileWriter);
            fileWriter.close();
            System.out.println("Fil har sparats, program avslutas.");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Filer kunde inte sparas, försök igen.");
        }
    }

}
