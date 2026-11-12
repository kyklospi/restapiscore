package restapiscore.spec;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OperationRefTest {
    @Test
    void pointerPointsToTheOperation() {
        OperationRef ref = new OperationRef(
                PathItem.HttpMethod.GET, "/users/{id}", new PathItem(), new Operation()
        );

        assertEquals("/paths/~1users~1{id}/get", ref.pointer());
    }

    @Test
    void locationHoldsThePointer() {
        OperationRef ref = new OperationRef(
                PathItem.HttpMethod.DELETE, "/users/{id}", new PathItem(), new Operation()
        );

        assertEquals("/paths/~1users~1{id}/delete", ref.location().pointer());
    }
}
