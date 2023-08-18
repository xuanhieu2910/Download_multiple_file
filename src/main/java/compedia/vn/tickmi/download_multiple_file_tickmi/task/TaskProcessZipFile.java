package compedia.vn.tickmi.download_multiple_file_tickmi.task;

import com.antkorwin.xsync.XSync;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.DownloadRequestHisFinished;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.DownloadRequestHisService;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.FileUtils;
import org.springframework.transaction.annotation.Transactional;

public class TaskProcessZipFile implements Runnable {


    private DownloadRequestHisService downloadRequestHisService;
    private DownloadRequestHisFinished requestHisFinished;
    private XSync<Long> xSync;
    public TaskProcessZipFile(DownloadRequestHisService downloadRequestHisService,
                              DownloadRequestHisFinished requestHisFinished,
                              XSync<Long> xSync) {
        this.downloadRequestHisService = downloadRequestHisService;
        this.requestHisFinished = requestHisFinished;
        this.xSync = xSync;
    }


    @Override
    public void run() {
        String pathFileZip = null;
        try {
            processZipFiles(requestHisFinished,pathFileZip);
        }catch (Exception e){
            processZipFilesFalse(requestHisFinished);
            deleteFileZipFalse(pathFileZip);
        }
    }

    private void deleteFileZipFalse(String pathFileZip) {
        FileUtils.deleteFileDraw(pathFileZip);
    }

    private void processZipFilesFalse(DownloadRequestHisFinished requestHisFinished) {
        downloadRequestHisService.updateAmountRetryWhenZipFile(requestHisFinished.getTicketEventId());
    }


    @Transactional
    void processZipFiles(DownloadRequestHisFinished requestHisFinished, String pathFileZip) {
            if (requestHisFinished.getRetry() <= DbConstant.RETRY_HANDLE) {
                String sourceFile = requestHisFinished.getPathFileParent();
                String desFile = requestHisFinished.getPathFileParent() + ".zip";
                pathFileZip = FileUtils.exportFileZipAfterHandleFinished(sourceFile, desFile);
                downloadRequestHisService.updateStatusFinishedToZipFile(requestHisFinished.getTicketEventId(), pathFileZip);
            } else {
                downloadRequestHisService.updateStatusWhenZipFile(requestHisFinished.getTicketEventId(), DbConstant.STATUS_HANDLE_ZIP_FILE_FALSE);
            }
    }





}
