package com.tencent.dingdang.tvs.ui;

import java.util.Locale;

/**
 * Controls behavior for changing Locale dynamically.
 */
public interface LocaleUIHandler {

    /**
     * When the locale is updated, call this method with the new locale.
     *
     * @param locale
     *         Updated locale.
     */
    void handleLocaleChange(Locale locale);
}
