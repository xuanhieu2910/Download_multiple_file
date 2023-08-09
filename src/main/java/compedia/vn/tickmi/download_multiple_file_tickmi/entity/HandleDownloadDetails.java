package compedia.vn.tickmi.download_multiple_file_tickmi.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "HANDLE_DOWNLOAD_DETAILS")
public class HandleDownloadDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HANDLE_DOWNLOAD_DETAILS")
    private Long idHandleDownloadDetails;
    @Column(name = "CONTENT_TEMPLATE_TICKET")
    private String contentTemplateTicket;
    @Column(name = "CONTENT_USER")
    private String contentUser;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "EVENT_ID")
    private Long eventId;
    @Column(name = "REQUEST_DOWNLOAD_ID")
    private Long requestDownloadId;
    @Column(name = "RETRY")
    private Integer retry;
    @Column(name = "TICKET_EVENT_ID")
    private Long ticketEventId;
}
