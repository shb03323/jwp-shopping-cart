package cart.dto;

public class ProductResponseDto {

    private final int id;
    private final String name;
    private final String image;
    private final int price;

    public ProductResponseDto(final int id, final String name, final String image, final int price) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getPrice() {
        return price;
    }
}
