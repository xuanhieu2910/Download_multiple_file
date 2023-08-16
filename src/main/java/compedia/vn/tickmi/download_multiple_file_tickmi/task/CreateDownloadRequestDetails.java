package compedia.vn.tickmi.download_multiple_file_tickmi.task;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.HandleDownloadDetailsService;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import lombok.extern.log4j.Log4j2;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Log4j2
public class CreateDownloadRequestDetails implements  Runnable{


    private HandleDownloadDetailsService handleDownloadDetailsService;
    private DownloadRequest downloadRequest;

    public CreateDownloadRequestDetails(DownloadRequest downloadRequest, HandleDownloadDetailsService handleDownloadDetailsService) {
        this.downloadRequest = downloadRequest;
        this.handleDownloadDetailsService = handleDownloadDetailsService;
    }


    @Override
    public void run() {
        try {
            List<HandleDownloadDetails> responses = new ArrayList<>();
            List<String> recordsDownload = Arrays.asList(downloadRequest.getContentUser().split(DbConstant.SEPERATE_RECORDS_DOWNLOAD));
            if (!CollectionUtils.isEmpty(recordsDownload) && recordsDownload.size() == downloadRequest.getTotalRecord()) {
                int totalRecord = downloadRequest.getTotalRecord();
                for (int i = 0; i < totalRecord; i++) {
                    HandleDownloadDetails detail = new HandleDownloadDetails();
                    detail.setContentUser(recordsDownload.get(i));
                    detail.setStatus(DbConstant.NEW_STATUS_HANDLE_DOWNLOAD_DETAILS);
                    detail.setEventId(downloadRequest.getEventId());
                    detail.setRequestDownloadId(downloadRequest.getIdRequestDownload());
                    detail.setRetry(DbConstant.RETRY_HANDLE);
                    detail.setTicketEventId(downloadRequest.getTicketEventId());
                    detail.setHtml(downloadRequest.getHtml());
                    detail.setDesignHtml(downloadRequest.getDesignHtml());
                    detail.setWidth(downloadRequest.getWidth());
                    detail.setHeight(downloadRequest.getHeight());
                    detail.setHtmlReplace(downloadRequest.getHtmlReplace());
                    detail.setJsonData(downloadRequest.getJsonData());
                    detail.setIsNewTool(downloadRequest.getIsNewTool());
                    detail.setPathImage(downloadRequest.getPathImage());
                    responses.add(detail);
                }
                handleDownloadDetailsService.saveAllHandleDownloadDetails(responses);
            }
        } catch (Exception e) {
            log.error("[CREATE SUB DETAIL] Error when handle create sub details by reason: ", e.getMessage());
        }
    }
}
