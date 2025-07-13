package info.developia;

import graphql.schema.DataFetcher;

import java.util.HashMap;
import java.util.Map;

public class GraphQLOptions {
    public String path = "/graphql";
    public String schema = "schema.graphqls";
    public boolean playground = true;
    public String playgroundPath = "/playground";
    public String playgroundHtmlFilename = "playground/index.html";
    public Map<String, DataFetcher> queries = new HashMap<>();
    private String packageName;


    public GraphQLOptions addPackage(String packageName) {
        this.packageName = packageName;
        return this;
    }

    public GraphQLOptions registerQuery(String queryName, DataFetcher<String> dataFetcher) {
        return this;
    }
}
