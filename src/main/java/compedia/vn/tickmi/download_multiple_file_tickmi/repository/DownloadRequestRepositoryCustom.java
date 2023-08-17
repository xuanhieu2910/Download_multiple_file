package compedia.vn.tickmi.download_multiple_file_tickmi.repository;


import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DownloadRequestRepositoryCustom {

    List<DownloadRequest> findAllRequestDownloadsLimit() throws IOException, SQLException;

    void updateAmountByExportRecordSuccess(Long requestDownloadId);

    void flatStatusByIdParent(Long parentId);

    boolean updateStatusDownloadRequestWhenFinishedProcess(Long requestDownloadId);
}
