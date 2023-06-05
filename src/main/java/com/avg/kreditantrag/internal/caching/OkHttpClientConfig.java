package com.avg.kreditantrag.internal.caching;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;

/**
 * A configuration class for {@code OkHttpClient} with bean methods.
 */
@Configuration
public class OkHttpClientConfig {
    public static final String CACHE_PATH = ".cache";
    private static final int MAX_SIZE = 128 * 1024; // 128 KiB

    /**
     * A bean method that creates a singleton instance of {@code OkHttpClient} class
     * with and binds it to the cache.
     *
     * @return new singleton instance of {@code OkHttpClient} class
     */
    @Bean
    @Scope("singleton")
    public OkHttpClient okHttpClient() {
        File httpCacheDir = new File(CACHE_PATH);

        Cache cache = new Cache(httpCacheDir, MAX_SIZE);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(new CacheInterceptor());
        httpClient.setCache(cache);

        return httpClient;
    }
}
