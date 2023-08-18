package compedia.vn.tickmi.download_multiple_file_tickmi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CaptureImageResponse {

    @JsonProperty("result")
    private boolean result;

    @JsonProperty("otherHtml")
    private String otherHtml;

}