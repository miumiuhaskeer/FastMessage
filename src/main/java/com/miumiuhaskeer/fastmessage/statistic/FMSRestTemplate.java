package com.miumiuhaskeer.fastmessage.statistic;

import com.miumiuhaskeer.fastmessage.JsonConverter;
import com.miumiuhaskeer.fastmessage.exception.FMSRequestException;
import com.miumiuhaskeer.fastmessage.model.ExtendedUserDetails;
import com.miumiuhaskeer.fastmessage.properties.config.FMSProp;
import com.miumiuhaskeer.fastmessage.service.UserService;
import com.miumiuhaskeer.fastmessage.util.JWTokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class FMSRestTemplate {

    private final RestTemplate restTemplate = new RestTemplate();
    private final JsonConverter jsonConverter;
    private final JWTokenUtil jwTokenUtil;
    private final UserService userService;
    private final FMSProp fmsProp;

    /**
     * Send query to FastMessage Statistic service for getting result
     *
     * @param request query type for FMS service
     * @param param request body for query
     * @param <R> return type of request
     * @return result of requesting FMS service
     * @throws RestClientException when status code is not 2xx
     * @throws FMSRequestException when status code is not 2xx in ResponseBody (unexpected)
     */
    public <R> R getForEntity(FMSRequest<R> request, Object param) {
        HttpEntity<Object> entity = getHttpEntity(param);
        String requestUrl = fmsProp.getUrl() + request.getRequestPath();
        ResponseEntity<R> response = restTemplate.postForEntity(requestUrl, entity, request.getReturnType());

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new FMSRequestException();
        }

        return response.getBody();
    }

    /**
     * Send query to FastMessage Statistic service for getting result
     *
     * @param request query type for FMS service
     * @param param request body for query
     * @param <R> return type of request
     * @return result of requesting FMS service
     * @throws RestClientException when status code is not 2xx
     * @throws FMSRequestException when status code is not 2xx in ResponseBody (unexpected)
     */
    public <R> R getForObject(FMSRequest<R> request, Object param) {
        HttpEntity<Object> entity = getHttpEntity(param);
        String requestUrl = fmsProp.getUrl() + request.getRequestPath();
        R response = restTemplate.postForObject(requestUrl, entity, request.getReturnType());

        if (response == null) {
            throw new FMSRequestException();
        }

        return response;
    }

    /**
     * Get HttpEntity with some headers and request body param for query
     *
     * @param param request body for query
     * @return HttpEntity object
     */
    private HttpEntity<Object> getHttpEntity(Object param) {
        HttpHeaders headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add(HttpHeaders.AUTHORIZATION, generateTokenHeader());

        return new HttpEntity<>(jsonConverter.toJsonSafe(param), headers);
    }

    /**
     * Generate token by userDetails. If user in null, return empty string
     *
     * @return generated token header (Bearer token)
     */
    private String generateTokenHeader() {
        ExtendedUserDetails user = userService.getCurrentUserSafe();

        if (user != null) {
            String token = jwTokenUtil.generateFMSToken(user.getUsername());

            return jwTokenUtil.generateHeader(token);
        }

        return "";
    }
}
