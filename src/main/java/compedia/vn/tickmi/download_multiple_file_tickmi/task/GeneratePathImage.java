package compedia.vn.tickmi.download_multiple_file_tickmi.task;

import com.antkorwin.xsync.XSync;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.TicketToConvertImageAndPdfDto;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetailsHis;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.DownloadRequestService;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.HandleDownloadDetailsHisService;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.HandleDownloadDetailsService;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.ExportImageOrPdfUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Date;

@Log4j2
public class GeneratePathImage implements Runnable{


    private HandleDownloadDetails handleDownloadDetails;
    private XSync<Long> xSync;
    private DownloadRequestService requestDownloadsService;
    private HandleDownloadDetailsService handleDRDService;
    private HandleDownloadDetailsHisService handleDRDHisService;

    public GeneratePathImage(HandleDownloadDetails handleDownloadDetails,
                             XSync<Long> xSync,
                             DownloadRequestService requestDownloadsService,
                             HandleDownloadDetailsService handleDRDService,
                             HandleDownloadDetailsHisService handleDRDHisService) {
        this.handleDownloadDetails = handleDownloadDetails;
        this.xSync = xSync;
        this.requestDownloadsService = requestDownloadsService;
        this.handleDRDService = handleDRDService;
        this.handleDRDHisService = handleDRDHisService;
    }


    @Override
    public void run() {
        handleSyncRecord(this.handleDownloadDetails);
    }


    /**
     * TODO: Hàm này dùng để xử lý từng tiến trình con (1 luồng xử lý 1 tiến trình con)
     *      Các bước xử lý tiến trình con:
     *      - B1: Kiểm tra giá trị retry
     *      - B2: Nếu giá trị Retry pass thì bắt đầu xử lý thông tin ảnh của đối tượng
     *      - B3: Sau khi xử lý xong ảnh thì SAVE vào trong bảng HIS (Bảng coi là nơi chưa kết quar của quá trình process)
     *      - B4: SAVE xong thì sẽ xóa nó đi
     *      - B5: UPDATE giá trị đã xử lý xong tại bảng cha
     */
    @Transactional
    public void handleSyncRecord (HandleDownloadDetails handleDownloadDetails) {
        xSync.execute(handleDownloadDetails.getIdHandleDownloadDetails(), () -> {
            if (handleDownloadDetails.getRetry() <= DbConstant.RETRY_HANDLE) {
                try {
                    String pathImage = engineHandleImage(handleDownloadDetails);
                    saveRecordDRDHis(handleDownloadDetails, pathImage, DbConstant.STATUS_DETAIL_HIS_SUCCESS);
                }catch (Exception e) {
                    handleSyncRecordFalse(this.handleDownloadDetails);
                    log.error("[ERROR] Handle record false reason: ", e.getMessage());
                }
                deleteRecord(handleDownloadDetails.getIdHandleDownloadDetails());
                updateAmountRequestDownload(handleDownloadDetails.getRequestDownloadId());
            } else {
                log.info("[HANDLE FALSE]: False to handle process export image when retry not pass condition!");
                deleteRecord(handleDownloadDetails.getIdHandleDownloadDetails());
                flatStatusByIdParent(this.handleDownloadDetails.getRequestDownloadId());
                saveRecordDRDHis(handleDownloadDetails,DbConstant.ERROR_CONTENT,DbConstant.STATUS_DETAIL_HIS_FALSE);
            }
        });
    }



    public void handleSyncRecordFalse (HandleDownloadDetails handleDownloadDetails) {
        handleDRDService.updateRetryAndStatusHandleDownloadDetailsByIdAndRetry(handleDownloadDetails.getIdHandleDownloadDetails());
    }

    private String engineHandleImage(HandleDownloadDetails handleDownloadDetails) throws Exception {
        TicketToConvertImageAndPdfDto dtoTicketToConvert = createTicketToConvertImageAndPdfDto(handleDownloadDetails);
        return ExportImageOrPdfUtils.convertTicketToImageAndPdf(dtoTicketToConvert, handleDownloadDetails.getTypeDownload());
    }
    //              0               1               2           3               4
    // Thứ tự :  NAME_GUEST => EMAIL_GUEST => PHONE_GUEST => NOTE_GUEST => TICKET_ID
    private TicketToConvertImageAndPdfDto createTicketToConvertImageAndPdfDto(HandleDownloadDetails handleDownloadDetails) {
        TicketToConvertImageAndPdfDto dto = new TicketToConvertImageAndPdfDto();
        String[] contentUser = handleDownloadDetails.getContentUser().split(DbConstant.SEPARATOR_CONTENT_USER_TMP);
        dto.setTicketId(Long.valueOf(contentUser[4]));
        dto.setNameGuest(contentUser[0]);
        dto.setEmailGuest(contentUser[1]);
        dto.setPhoneGuest(contentUser[2]);
        dto.setNoteGuest(contentUser[3]);
        dto.setPathQr(contentUser[4]);
        dto.setHtml(handleDownloadDetails.getHtml());
        dto.setHtmlReplace(handleDownloadDetails.getHtmlReplace());
        dto.setWidth(handleDownloadDetails.getWidth());
        dto.setHeight(handleDownloadDetails.getHeight());
        dto.setIsDesign(handleDownloadDetails.getIsNewTool());
        dto.setJsonData(handleDownloadDetails.getJsonData());
        dto.setIsFree(handleDownloadDetails.getIsFree());
        dto.setEventId(handleDownloadDetails.getEventId());
        dto.setTicketEventId(handleDownloadDetails.getTicketEventId());
        return dto;
    }

    private void saveRecordDRDHis(HandleDownloadDetails handleDownloadDetails, String pathImage,Integer status) {
        if (status.equals(DbConstant.STATUS_DETAIL_HIS_SUCCESS)) {
            handleDRDHisService.saveHandleDownloadDetailsHis(
                    createHandleDRDHis(handleDownloadDetails, pathImage,status)
            );
        } else {
            handleDRDHisService.saveHandleDownloadDetailsHis(createHandleDRDHis(handleDownloadDetails,pathImage,status));
        }
    }

    private void deleteRecord (Long idRecord) {
        handleDRDService.deleteHandleDownloadDetailsById(idRecord);
    }


    private void updateAmountRequestDownload(Long requestDownloadId) {
        requestDownloadsService.updateAmountByExportRecordSuccess(requestDownloadId);
    }


    public void flatStatusByIdParent(Long parentId) {
        requestDownloadsService.flatStatusByIdParent(parentId);
    }

    private HandleDownloadDetailsHis createHandleDRDHis(HandleDownloadDetails handleDownloadDetails,
                                                        String pathImage,Integer status){
        HandleDownloadDetailsHis handleDRDHis = new HandleDownloadDetailsHis();
        handleDRDHis.setContentUser(handleDownloadDetails.getContentUser());
        handleDRDHis.setStatus(status);
        handleDRDHis.setEventId(handleDownloadDetails.getEventId());
        handleDRDHis.setRequestDownloadId(handleDownloadDetails.getRequestDownloadId());
        handleDRDHis.setRetry(0);
        handleDRDHis.setPathFile(pathImage);
        handleDRDHis.setTicketEventId(handleDownloadDetails.getTicketEventId());
        handleDRDHis.setCreateTime(new Timestamp(new Date().getTime()));
        handleDRDHis.setHtml(handleDownloadDetails.getHtml());
        handleDRDHis.setDesignHtml(handleDownloadDetails.getDesignHtml());
        handleDRDHis.setWidth(handleDownloadDetails.getWidth());
        handleDRDHis.setHeight(handleDownloadDetails.getHeight());
        handleDRDHis.setHtmlReplace(handleDownloadDetails.getHtmlReplace());
        handleDRDHis.setJsonData(handleDownloadDetails.getJsonData());
        handleDRDHis.setIsNewTool(handleDownloadDetails.getIsNewTool());
        handleDRDHis.setPathImage(handleDownloadDetails.getPathImage());
        handleDRDHis.setIsFree(handleDownloadDetails.getIsFree());
        handleDRDHis.setTypeDownload(handleDownloadDetails.getTypeDownload());
        return handleDRDHis;
    }

}
