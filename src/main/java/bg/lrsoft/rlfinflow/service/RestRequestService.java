package bg.lrsoft.rlfinflow.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static java.net.URI.create;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.PUT;

@RequiredArgsConstructor
public class RestRequestService implements RestService {

    private final RestTemplate restTemplate;

    @Override
    public <T> ResponseEntity<T> getForEntity(String url, Class<T> entityType) {
        return restTemplate.getForEntity(url, entityType);
    }


    public <T> ResponseEntity<T> asd(Object requestBody, String url, Class<T> entityType) {
        RequestEntity<Object> updateAuthorDtoRequestEntity = new RequestEntity<>(requestBody, GET, create(url));
        return restTemplate.exchange(url, PUT, updateAuthorDtoRequestEntity, entityType);
    }

}
