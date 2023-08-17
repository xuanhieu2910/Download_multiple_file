package compedia.vn.tickmi.download_multiple_file_tickmi.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "DOWNLOAD_REQUEST_HIS")
public class DownloadRequestHis {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_REQUEST_DOWNLOAD_HIS")
    private Long idRequestDownloadHis;
    @Column(name = "PATH_FILE")
    private String pathFile;
    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "TOTAL_RECORD")
    private Integer totalRecord;
    @Column(name = "EVENT_ID")
    private Long eventId;
    @Column(name = "FLAT_STATUS")
    private Integer flatStatus;
    @Column(name = "TOTAL_RECORD_FINISHED")
    private Integer totalRecordFinished;
    @Column(name = "TICKET_EVENT_ID")
    private Long ticketEventId;
    @Column(name = "CONTENT_USER")
    private String contentUser;
    @Column(name = "ID_REQUEST_DOWNLOAD")
    private Long idRequestDownload;
    @Column(name = "TIME_CREATED")
    private Timestamp timeCreated;
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
    @Column(name = "TIME_FINISHED")
    private Integer timeFinished;
}
