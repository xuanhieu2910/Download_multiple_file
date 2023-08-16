package compedia.vn.tickmi.download_multiple_file_tickmi.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;


@Getter
@Setter
@Entity
@Table(name = "HANDLE_DOWNLOAD_DETAILS_HIS")
public class HandleDownloadDetailsHis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_HANDLE_DOWNLOAD_DETAILS_HIS")
    private Long idHandleDownloadDetailsHis;
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
    @Column(name = "PATH_FILE")
    private String pathFile;
    @Column(name = "TICKET_EVENT_ID")
    private Long ticketEventId;
    @Column(name = "CREATE_TIME")
    private Timestamp createTime;
    @Column(name = "HTML")
    private String html;
    @Column(name = "DESIGN_HTML")
    private String designHtml;
    @Column(name = "WIDTH")
    private Integer width;
    @Column(name = "HEIGHT")
    private Integer height;
    @Column(name = "HTML_REPLACE")
    private String htmlReplace;
    @Column(name = "JSON_DATA")
    private String jsonData;
    @Column(name = "IS_NEW_TOOL")
    private Integer isNewTool;
    @Column(name = "PATH_IMAGE")
    private String pathImage;

}
