package com.avg.kreditantrag.internal.caching;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.util.Arrays;

@Configuration
public class OkHttpClientConfig {
    private static final String CACHE_DIR = ".cache";
    private static final int MAX_SIZE = 128 * 1024; // 128 KiB

    @Bean
    @Scope("singleton")
    public OkHttpClient okHttpClient() {
        File httpCacheDir = new File(CACHE_DIR);
        clearDir(httpCacheDir);
        Cache cache = new Cache(httpCacheDir, MAX_SIZE);

        OkHttpClient httpClient = new OkHttpClient();
        httpClient.networkInterceptors().add(new CacheInterceptor());
        httpClient.setCache(cache);

        return httpClient;
    }

    private void clearDir(File directory) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        Arrays.stream(files)
                .toList()
                .forEach(file -> {
            if (file.delete()) {
                return;
            }
            throw new SecurityException();
        });
    }
}
