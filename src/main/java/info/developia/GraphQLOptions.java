package info.developia;

import graphql.schema.DataFetcher;

import java.util.Map;

public record GraphQLOptions(
        String path,
        String schema,
        boolean playground,
        String playgroundPath,
        String playgroundHtmlFilename,
        Map<String, DataFetcher> queries,
        Map<String, DataFetcher> mutations,
        String packageName
) {
    public static GraphQLOptionsBuilder builder() {
        return new GraphQLOptionsBuilder();
    }
}
