import java.util.ArrayList;
import java.util.List;

public class Ingredients {
    private final String hash;
    public List<String> ingredients = new ArrayList<>();

    public Ingredients(String hash,List<String> ingredients) {
        this.hash = hash;
        this.ingredients = ingredients;
    }
}
