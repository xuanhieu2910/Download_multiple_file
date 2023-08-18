package compedia.vn.tickmi.download_multiple_file_tickmi.dto;

import com.google.gson.JsonObject;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;

@Log4j2
public class RestClient extends RestTemplate implements Serializable {

    public <T> T getRequestFormDataGoogle(String url, String accessToken, Class<T> cls) {
        String restUrl = url + "?access_token=" + accessToken;
        logger.info("Send GET request restUrl: " + url);
        return this.getForObject(restUrl, cls);
    }

    public <T> T postRequestBody(String url, JsonObject jsonData, Class<T> cls) {
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        logger.info("Send POST request restUrl: " + url);
        logger.info("Send POST data: " + jsonData);
        HttpEntity<String> entity = new HttpEntity<>(jsonData.toString(), requestHeaders);
        return this.postForObject(url, entity, cls);
    }

}