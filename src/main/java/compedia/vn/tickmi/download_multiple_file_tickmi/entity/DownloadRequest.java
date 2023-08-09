package compedia.vn.tickmi.download_multiple_file_tickmi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;


@Getter
@Setter
@Table(name = "DOWNLOAD_REQUEST")
@Entity
@NoArgsConstructor
public class DownloadRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REQUEST_DOWNLOAD")
    private Long idRequestDownload;
    @Column(name = "CONTENT_USER")
    private String contentUser;
    @Column(name = "PATH_FILE")
    private String pathFile;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "TOTAL_RECORD")
    private Integer totalRecord;
    @Column(name = "TOTAL_RECORD_FINISHED")
    private Integer totalRecordFinished;
    @Column(name = "EVENT_ID")
    private Long eventId;
    @Column(name = "CONTENT_TEMPLATE_TICKET")
    private String contentTemplateTicket;
    @Column(name = "FLAT_STATUS")
    private  Integer flatStatus;
    @Column(name = "TICKET_EVENT_ID")
    private Long ticketEventId;
}
