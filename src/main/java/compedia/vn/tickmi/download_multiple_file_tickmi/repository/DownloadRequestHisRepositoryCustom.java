package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public interface DownloadRequestHisRepositoryCustom {


    void updateStatusDownloadRequestWhenFinishedProcess(Long id);

    void updateAmountByExportRecordSuccess(Long requestDownloadsId);
}
