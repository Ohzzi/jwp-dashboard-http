package http.request;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import http.exception.QueryStringFormatException;
import org.junit.jupiter.api.Test;

class QueryParamsTest {

    @Test
    void query_string_파싱_테스트() {
        // given
        String queryString = "account=gugu&password=password";

        // when
        QueryParams queryParams = QueryParams.parse(queryString);

        // then
        assertAll(
                () -> assertThat(queryParams.get("account")).isEqualTo("gugu"),
                () -> assertThat(queryParams.get("password")).isEqualTo("password")
        );
    }

    @Test
    void 빈_query_string을_받으면_query_params가_없다() {
        // given
        String queryString = "";

        // when
        QueryParams actual = QueryParams.parse(queryString);

        // then
        assertThat(actual.exists()).isFalse();
    }

    @Test
    void 형식에_맞지_않으면_예외를_반환한다() {
        // given
        String queryString = "a%b$c^d";

        // when, then
        assertThatThrownBy(() -> QueryParams.parse(queryString))
                .isExactlyInstanceOf(QueryStringFormatException.class);
    }
}
