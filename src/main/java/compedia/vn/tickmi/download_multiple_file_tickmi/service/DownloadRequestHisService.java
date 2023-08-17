package compedia.vn.tickmi.download_multiple_file_tickmi.service;

import compedia.vn.tickmi.download_multiple_file_tickmi.dto.DownloadRequestHisFinished;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;
import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequestHis;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestHisRepository;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class DownloadRequestHisService {

    @Autowired
    DownloadRequestHisRepository downloadRequestHisRepository;


    public void saveAllDownloadRequestHis(List<DownloadRequest> downloadRequests) {
        downloadRequestHisRepository.saveAll(convertToDownloadRequest(downloadRequests));
    }

    private List<DownloadRequestHis> convertToDownloadRequest(List<DownloadRequest> downloadRequests) {
        List<DownloadRequestHis> response = new ArrayList<>();
        for (DownloadRequest rq : downloadRequests) {
            DownloadRequestHis requestHis = new DownloadRequestHis();

            String filePathParent = FileUtils.createNameFile(rq.getEventId(), rq.getTicketEventId(),
                    DbConstant.TYPE_FILE_NOT_DRAFT);
            requestHis.setPathFile(filePathParent);
            requestHis.setStatus(rq.getStatus());
            requestHis.setTotalRecord(rq.getTotalRecord());
            requestHis.setEventId(rq.getEventId());
            requestHis.setFlatStatus(rq.getFlatStatus() == null ? -1 : rq.getFlatStatus());
            requestHis.setTotalRecordFinished(rq.getTotalRecordFinished());
            requestHis.setTicketEventId(rq.getTicketEventId());
            requestHis.setContentUser(rq.getContentUser());
            requestHis.setIdRequestDownload(rq.getIdRequestDownload());
            requestHis.setTimeCreated(new Timestamp(new Date().getTime()));
            requestHis.setHtml(rq.getHtml());
            requestHis.setDesignHtml(rq.getDesignHtml());
            requestHis.setWidth(rq.getWidth());
            requestHis.setHeight(rq.getHeight());
            requestHis.setHtmlReplace(rq.getHtmlReplace());
            requestHis.setJsonData(rq.getJsonData());
            requestHis.setIsNewTool(rq.getIsNewTool());
            requestHis.setPathImage(rq.getPathImage());
            response.add(requestHis);
        }
        return response;
    }

    public List<DownloadRequestHisFinished> getDownloadHisFinishedToZipFile(){
        List<DownloadRequestHisFinished> responses = downloadRequestHisRepository.getDownloadRequestHisToZipWithSizeLimit();
        responses.stream().forEach(x -> x.setTimeFinished(calculatorTime(x)));
        downloadRequestHisRepository.updateStatusDownloadRecordZipped(responses);
        return responses;
    }

    private Integer calculatorTime(DownloadRequestHisFinished re) {
        long begin = System.currentTimeMillis();
        String pathFile = FileUtils.exportToFileZip(Arrays.asList(re.getPathFileChild()),re.getEventId(),
                                                re.getTicketEventId(),DbConstant.TYPE_FILE_DRAFT);
        FileUtils.deleteFileDraw(pathFile);
        long end = System.currentTimeMillis();
        long timeFinished = (begin - end) / 60000;
        return Math.round(timeFinished * re.getTotalRecordFinished());
    }
}
