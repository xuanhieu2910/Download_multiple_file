package compedia.vn.tickmi.download_multiple_file_tickmi.task;

import compedia.vn.tickmi.download_multiple_file_tickmi.dto.DownloadRequestHisFinished;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.DownloadRequestHisService;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.FileUtils;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;

public class TaskProcessZipFile implements Runnable {


    private DownloadRequestHisService downloadRequestHisService;
    private DownloadRequestHisFinished requestHisFinished;
    public TaskProcessZipFile(DownloadRequestHisService downloadRequestHisService,
                              DownloadRequestHisFinished requestHisFinished) {
        this.downloadRequestHisService = downloadRequestHisService;
        this.requestHisFinished = requestHisFinished;
    }


    @Override
    public void run() {
        try {
            processZipFiles(requestHisFinished);
        }catch (Exception e){
        }
    }


    @Transactional
    void processZipFiles(DownloadRequestHisFinished requestHisFinished) {
//        String pathFile = FileUtils.exportToFileZip();
    }





}
