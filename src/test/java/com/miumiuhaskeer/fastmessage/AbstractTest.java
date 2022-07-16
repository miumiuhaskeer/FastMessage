package com.miumiuhaskeer.fastmessage;

import com.miumiuhaskeer.fastmessage.config.TestPersistenceConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.embedded.EmbeddedMongoAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.bind.annotation.RequestMethod;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EnableKafka
@SpringBootTest
@AutoConfigureMockMvc
@Import({TestPersistenceConfig.class, EmbeddedMongoAutoConfiguration.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(
        partitions = 1,
        brokerProperties = {
                "listeners=PLAINTEXT://localhost:9092",
                "port=9092"
        }
)
public abstract class AbstractTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected JsonConverter jsonConverter;

    /**
     * Perform ok request with returning result
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param returnType returning class type (result of request)
     * @param <T> type for request
     * @param <U> type for returnType
     * @return result of request
     * @throws Exception throwing by mockMvc
     */
    protected <T, U> U performOkRequest(MockMvcQuery query, T request, Class<U> returnType) throws Exception {
        MvcResult mvcResult = mockMvc.perform(createRequest(query, request))
                .andExpect(status().isOk())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return jsonConverter.fromJson(content, returnType);
    }

    /**
     * Perform ok request without returning result
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param <T> type for request
     * @throws Exception throwing by mockMvc
     */
    protected <T> void performOkRequest(MockMvcQuery query, T request) throws Exception {
        mockMvc.perform(createRequest(query, request))
                .andExpect(status().isOk());
    }

    /**
     * Perform bad request with returning result
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param returnType returning class type (result of request)
     * @param <T> type for request
     * @param <U> type for returnType
     * @return result of request
     * @throws Exception throwing by mockMvc
     */
    protected <T, U> U performBadRequest(MockMvcQuery query, T request, Class<U> returnType) throws Exception {
        MvcResult mvcResult = mockMvc.perform(createRequest(query, request))
                .andExpect(status().isBadRequest())
                .andReturn();
        String content = mvcResult.getResponse().getContentAsString();

        return jsonConverter.fromJson(content, returnType);
    }

    /**
     * Perform bad request without returning result
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param <T> type for request
     * @throws Exception throwing by mockMvc
     */
    protected <T> void performBadRequest(MockMvcQuery query, T request) throws Exception {
        mockMvc.perform(createRequest(query, request))
                .andExpect(status().isBadRequest());
    }

    /**
     * Perform unauthorized request without returning result
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param <T> type for request
     * @throws Exception throwing by mockMvc
     */
    protected <T> void performUnauthorizedRequest(MockMvcQuery query, T request) throws Exception {
        mockMvc.perform(createRequest(query, request))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Create servlet request builder with parameters that are provided by query param.
     *
     * @param query servlet request parameters. Cannot be null
     * @param request parameter for servlet request. Can be null
     * @param <T> type for request
     * @return servlet request builder
     */
    protected <T> MockHttpServletRequestBuilder createRequest(MockMvcQuery query, T request) {
        final MockHttpServletRequestBuilder builder;

        if (query.getMethod() == RequestMethod.POST) {
            builder = post(query.getPath());
        } else {
            builder = get(query.getPath());
        }

        if (query.getAuthToken() != null) {
            builder.header(HttpHeaders.AUTHORIZATION, query.getAuthToken());
        }

        if (request != null) {
            builder.contentType(MediaType.APPLICATION_JSON)
                    .content(jsonConverter.toJsonSafe(request));
        }

        return builder;
    }
}
