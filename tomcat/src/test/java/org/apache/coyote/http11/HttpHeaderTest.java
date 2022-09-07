package org.apache.coyote.http11;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import org.apache.coyote.exception.InvalidHttpRequestFormatException;
import org.junit.jupiter.api.Test;

class HttpHeaderTest {

    @Test
    void 헤더_파싱_테스트() {
        // given
        String headerString = "Name: value";

        // when
        HttpHeader httpHeader = HttpHeader.parse(headerString);

        // then
        assertAll(
                () -> assertThat(httpHeader.getName()).isEqualTo("Name"),
                () -> assertThat(httpHeader.getValue()).isEqualTo("value")
        );
    }

    @Test
    void 헤더_문자열의_형식이_올바르지_않으면_예외를_반환한다() {
        // given
        String headerString = "invalid";

        // when, then
        assertThatThrownBy(() -> HttpHeader.parse(headerString))
                .isExactlyInstanceOf(InvalidHttpRequestFormatException.class);
    }
}
