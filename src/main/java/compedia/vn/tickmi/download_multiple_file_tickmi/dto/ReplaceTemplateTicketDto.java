package compedia.vn.tickmi.download_multiple_file_tickmi.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReplaceTemplateTicketDto {
    private String contentHtml;
    private String htmlReplace;
    private String qr;
    private String url;
    private String email;
    private String nameGuest;
    private String phoneGuest;
    private String noteGuest;
    private String ticketName;
    private String avatarPath;

    private Integer width;
    private Integer height;
    private Integer isNewTool;
    private String jsonData;
    private Integer isPackageFree;
}
