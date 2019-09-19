package com.briandevinssuresh.hockeystats.scraper;

import com.briandevinssures.hockeystats.scraper.Strings;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringsTest {
    @Test
    public void testHeightParsing() {
        assertThat(Strings.heightToInches("1' 0\"")).isEqualTo(12);
        assertThat(Strings.heightToInches("1' 11\"")).isEqualTo(23);
        assertThat(Strings.heightToInches("5' 0\"")).isEqualTo(60);
        assertThat(Strings.heightToInches("5' 11\"")).isEqualTo(71);
    }
}
