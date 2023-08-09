package compedia.vn.tickmi.download_multiple_file_tickmi.task;


import com.antkorwin.xsync.XSync;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.DownloadRequestHisService;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.DownloadRequestService;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.HandleDownloadDetailsHisService;
import compedia.vn.tickmi.download_multiple_file_tickmi.service.HandleDownloadDetailsService;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
@EnableScheduling
@Log4j2
@EnableAsync
public class TaskHandleRequestDownload {

    @Autowired
    DownloadRequestService downloadRequestService;

    @Autowired
    XSync<Long> xSync;

    @Autowired
    HandleDownloadDetailsService handleDownloadDetailsService;

    @Autowired
    HandleDownloadDetailsHisService handleDownloadDetailsHisService;

    @Autowired
    DownloadRequestHisService downloadRequestHisService;

    private static final Queue<DownloadRequest> queueRequestDownloads = new ConcurrentLinkedQueue<>();
    private static final ExecutorService executor = Executors.newFixedThreadPool(DbConstant.SIZE_POOL_THREAD);

    private static final Queue<HandleDownloadDetails> queueHandleDownloadDetails = new ConcurrentLinkedQueue<>();

    /**
     * TODO: Process to get Data from Table: Request download -> Push: Queue to handle Request Download
     * */
    @Scheduled(fixedRate = 3000)
    public void taskGetRequestDownload(){
        if (DbConstant.IS_FLAT_RUN_JOB) {
            try {
                List<DownloadRequest> requestDownloads = downloadRequestService.getRequestDownloadWithRecordLimit();
                if (!CollectionUtils.isEmpty(requestDownloads)) {
                    downloadRequestService.updateStatusDownloadRequest(requestDownloads,DbConstant.PROCESSING_STATUS_REQUEST_DOWNLOAD);
                    queueRequestDownloads.addAll(requestDownloads);
                    downloadRequestHisService.saveAllDownloadRequestHis(requestDownloads);
                }
            } catch (Exception e){
                log.error("[ERROR - TASK] Get request download by reason: ", e);
            }
        }
    }

    /**
     * TODO: Queue to handle sub request download details
     * */
    @Async
    @Scheduled(fixedRate = 50)
    public void processToDivideRequestDownloads() {
        if (!queueRequestDownloads.isEmpty()) {
            DownloadRequest req = queueRequestDownloads.poll();
            if (null == req) {
                return;
            }
            Runnable worker = new CreateDownloadRequestDetails(req, handleDownloadDetailsService);
            executor.execute(worker);
        }
    }

    /**
     * TODO: Get data from record download details and then carry it to handle export images
     * */
    @Scheduled(fixedRate = 3000)
    public void taskHandleRecordsDownloadDetails () {
        if (DbConstant.IS_FLAT_RUN_JOB) {
            try {
                List<HandleDownloadDetails> req = handleDownloadDetailsService.findAllHandleDownloadDetailLimit();
                if (!CollectionUtils.isEmpty(req)) {
                    handleDownloadDetailsService.updateStatusHandleDownloadDetails(req,DbConstant.HANDLING_STATUS_HANDLE_DOWNLOAD_DETAILS);
                    queueHandleDownloadDetails.addAll(req);
                }
            } catch (Exception e){
                log.error("[ERROR - TASK] Handle get details by reason: ", e);
            }
        }
    }

    /**
     * TODO: Queue to handle sub request download details
     * */
    @Async
    @Scheduled(fixedRate = 50)
    public void processToHandleEachSub() {
        if (!queueHandleDownloadDetails.isEmpty()) {
            HandleDownloadDetails req = queueHandleDownloadDetails.poll();
            if (null == req) {
                return;
            }
            Runnable worker = new GeneratePathImage(req,xSync,downloadRequestService,handleDownloadDetailsService,handleDownloadDetailsHisService);
            executor.execute(worker);
        }
    }
}
