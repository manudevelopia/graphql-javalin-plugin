package info.developia;

import graphql.schema.DataFetcher;

import java.util.HashMap;
import java.util.Map;

public class GraphQLOptionsBuilder {
    public String path = "/graphql";
    public String schema = "schema.graphqls";
    public boolean playground = true;
    public String playgroundPath = "/playground";
    public String playgroundHtmlFilename = "playground/index.html";
    public Map<String, DataFetcher> queries;
    private String packageName;

    public GraphQLOptionsBuilder addPackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public GraphQLOptionsBuilder registerQuery(String queryName, DataFetcher<String> dataFetcher) {
        if (queries == null) {
            queries = new HashMap<>();
        }
        queries.put(queryName, dataFetcher);
        return this;
    }

    public GraphQLOptions build() {
        return new GraphQLOptions(path, schema, playground, playgroundPath, playgroundHtmlFilename, queries, packageName);
    }
}
