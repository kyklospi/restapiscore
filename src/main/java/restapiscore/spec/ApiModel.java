package restapiscore.spec;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Data transfer object for a parsed OpenAPI document together with its operations.
 *
 * <p>Operations are collected in a list for easy iteration, and each operation is wrapped in an {@link OperationRef}
 * that provides the HTTP method, path template, enclosing path item.
 */
public class ApiModel {
    private final OpenAPI openApi;
    private final List<OperationRef> operations;

    public ApiModel(OpenAPI openApi) {
        this.openApi = openApi;
        this.operations = collectOperations();
    }

    public OpenAPI openApi() {
        return openApi;
    }

    private List<OperationRef> collectOperations() {
        List<OperationRef> result = new ArrayList<>();
        if (openApi.getPaths() == null) {
            return result;
        }
        for (Map.Entry<String, PathItem> entry : openApi.getPaths().entrySet()) {
            PathItem pathItem = entry.getValue();
            for (Map.Entry<PathItem.HttpMethod, Operation> op : pathItem.readOperationsMap().entrySet()) {
                result.add(new OperationRef(op.getKey(), entry.getKey(), pathItem, op.getValue()));
            }
        }
        return result;
    }

    // document level

    public Info info() {
        return openApi.getInfo() == null ? new Info() : openApi.getInfo();
    }

    public String title() {
        return info().getTitle() == null ? "" : info().getTitle();
    }

    // paths and operations

    public List<String> paths() {
        return openApi.getPaths() == null ? List.of() : new ArrayList<>(openApi.getPaths().keySet());
    }

    public List<OperationRef> operations() {
        return operations;
    }

    public List<OperationRef> operations(PathItem.HttpMethod method) {
        return operations.stream().filter(op -> op.method() == method).toList();
    }
}
