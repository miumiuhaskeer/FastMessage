package com.miumiuhaskeer.fastmessage.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@TestConfiguration
@RequiredArgsConstructor
@TestPropertySource(locations="classpath:application.properties")
public class TestPersistenceConfig {

    private final ResourcePatternResolver resourcePatternResolver;

    @Bean
    @Primary
    public DataSource embeddedDataSource() {
        return new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScripts(getInitSqlFiles())
                .build();
    }

    @Bean
    public LocalValidatorFactoryBean localValidatorFactoryBean() {
        return new LocalValidatorFactoryBean();
    }

    private String[] getInitSqlFiles() {
        List<String> resources = new ArrayList<>();

        try {
            List<String> schema = getFilesPathList("sql/schema", "*.sql");
            List<String> data = getFilesPathList("sql/data", "*.sql");

            resources.addAll(schema);
            resources.addAll(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        String[] paths = new String[resources.size()];
        paths = resources.toArray(paths);

        return paths;
    }

    private List<String> getFilesPathList(String path, String fileName) throws IOException {
        String resourcePath = (path.charAt(path.length() - 1) == '/') ? path : path + "/";
        String locationPattern = "classpath:" + resourcePath + fileName;
        List<Resource> resources = Arrays.asList(resourcePatternResolver.getResources(locationPattern));

        return resources.stream()
                .map(resource -> {
                    try {
                        return resourcePath + resource.getFile().getName();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return "";
                })
                .filter(filePath -> !filePath.isEmpty())
                .collect(Collectors.toList());
    }
}