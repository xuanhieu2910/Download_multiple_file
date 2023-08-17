package compedia.vn.tickmi.download_multiple_file_tickmi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DownloadRequestHisFinished {

    private Long eventId;
    private Long ticketEventId;
    private String pathFileChild;// abc/ticket_event_id/name_ticket.png
    private Integer totalRecordFinished;
    private Integer timeFinished;
    private String pathFileParent;
}
