package bg.lrsoft.rlfinflow.service.impl;

import bg.lrsoft.rlfinflow.service.RestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
public class RestRequestService implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> entityType) {
        return restTemplate.getForEntity(url, entityType);
    }
}
