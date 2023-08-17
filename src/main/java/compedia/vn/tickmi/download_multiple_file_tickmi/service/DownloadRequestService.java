package compedia.vn.tickmi.download_multiple_file_tickmi.service;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestHisRepository;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class DownloadRequestService {

    @Autowired
    private DownloadRequestRepository downloadRequestRepository;

    @Autowired
    private DownloadRequestHisRepository downloadRequestHisRepository;

    public List<DownloadRequest> getRequestDownloadWithRecordLimit() throws SQLException, IOException {
        return downloadRequestRepository.findAllRequestDownloadsLimit();
    }

    public List<DownloadRequest> updateStatusDownloadRequest(List<DownloadRequest> downloadRequests, Integer status) {
        downloadRequests.stream().forEach(x->x.setStatus(status));
        return downloadRequestRepository.saveAll(downloadRequests);
    }

    public void updateAmountByExportRecordSuccess(Long requestDownloadId) {
        downloadRequestRepository.updateAmountByExportRecordSuccess(requestDownloadId);
        downloadRequestHisRepository.updateAmountByExportRecordSuccess(requestDownloadId);
        if (downloadRequestRepository.updateStatusDownloadRequestWhenFinishedProcess(requestDownloadId)) {
            downloadRequestHisRepository.updateStatusDownloadRequestWhenFinishedProcess(requestDownloadId);
        }
    }

    public void flatStatusByIdParent(Long parentId) {
        downloadRequestRepository.flatStatusByIdParent(parentId);
    }
}
