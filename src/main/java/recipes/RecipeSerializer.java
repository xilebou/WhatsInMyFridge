package recipes;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeSerializer {
    private ObjectMapper om;
    public RecipeSerializer() {
        om = new ObjectMapper();
    }
}
