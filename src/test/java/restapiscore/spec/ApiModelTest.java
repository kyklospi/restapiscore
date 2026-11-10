package restapiscore.spec;

import io.swagger.v3.oas.models.PathItem;
import org.junit.jupiter.api.Test;
import restapiscore.TestUtil;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiModelTest {

    @Test
    void operationsAreInDocumentOrder() {
        ApiModel model = TestUtil.model("spec/test-api.yaml");
        List<String> descriptions = new ArrayList<>();
        for (OperationRef op : model.operations()) {
            descriptions.add(op.describe());
        }

        assertEquals(List.of("GET /books", "POST /books", "GET /books/{bookId}",
                "DELETE /books/{bookId}", "GET /getStats"), descriptions);
    }

    @Test
    void operationsCanBeFilteredByMethod() {
        ApiModel model = TestUtil.model("spec/test-api.yaml");
        assertEquals(3, model.operations(PathItem.HttpMethod.GET).size());
        assertEquals(1, model.operations(PathItem.HttpMethod.POST).size());
    }
}
