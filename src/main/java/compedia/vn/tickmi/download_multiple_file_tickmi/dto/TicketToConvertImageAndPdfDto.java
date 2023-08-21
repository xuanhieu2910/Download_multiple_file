package compedia.vn.tickmi.download_multiple_file_tickmi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketToConvertImageAndPdfDto {

    private Long ticketId;
    private String pathQr;
    private String html;
    private String htmlReplace;
    private Integer width;
    private Integer height;
    private String nameGuest;
    private String emailGuest;
    private String phoneGuest;
    private String noteGuest;
    private Integer isDesign;
    private String jsonData;
    private Integer isFree;
    private Long eventId;
    private Long ticketEventId;
}