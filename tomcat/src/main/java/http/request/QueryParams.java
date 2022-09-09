package http.request;

import http.exception.QueryStringFormatException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QueryParams {

    public static final String QUERY_PARAMS_DELIMITER = "&";
    public static final String KEY_AND_VALUE_DELIMITER = "=";
    public static final int KEY_INDEX = 0;
    public static final int VALUE_INDEX = 1;
    public static final int QUERY_PARAMS_FORMAT_SIZE = 2;
    private final Map<String, String> params;

    private QueryParams(final Map<String, String> params) {
        this.params = params;
    }

    public static QueryParams parse(final String queryString) {
        if (queryString == null || queryString.isBlank()) {
            return new QueryParams(new HashMap<>());
        }
        String[] queries = queryString.split(QUERY_PARAMS_DELIMITER);
        Map<String, String> params = new HashMap<>();
        for (String query : queries) {
            String[] keyAndValue = query.split(KEY_AND_VALUE_DELIMITER);
            validateFormat(keyAndValue);
            params.put(keyAndValue[KEY_INDEX], keyAndValue[VALUE_INDEX]);
        }
        return new QueryParams(params);
    }

    private static void validateFormat(final String[] keyAndValue) {
        if (keyAndValue.length != QUERY_PARAMS_FORMAT_SIZE) {
            throw new QueryStringFormatException();
        }
    }

    public String get(final String key) {
        return params.get(key);
    }

    public boolean exists() {
        return params.size() > 0;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QueryParams that = (QueryParams) o;
        return Objects.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        return Objects.hash(params);
    }
}
