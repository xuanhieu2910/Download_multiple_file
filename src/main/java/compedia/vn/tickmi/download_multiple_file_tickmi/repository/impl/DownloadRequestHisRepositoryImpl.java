package compedia.vn.tickmi.download_multiple_file_tickmi.repository.impl;

import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestHisRepositoryCustom;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

public class DownloadRequestHisRepositoryImpl implements DownloadRequestHisRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    @Modifying
    @Override
    public void updateStatusDownloadRequestWhenFinishedProcess(Long requestDownloadId) {
        Query query = entityManager.createNativeQuery(SQL_updateStatusWhenDone);
        query.setParameter("status", DbConstant.FINISHED_STATUS_REQUEST_DOWNLOAD);
        query.setParameter("idRequestDownload",requestDownloadId);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    @Override
    public void updateAmountByExportRecordSuccess(Long requestDownloadsId) {
        Query query = entityManager.createNativeQuery(SQL_updateAmountByExportRecordSuccess);
        query.setParameter("idRequestDownload", requestDownloadsId);
        query.executeUpdate();
    }


    private static final String SQL_updateStatusWhenDone = "update DOWNLOAD_REQUEST_HIS his " +
            "set his.STATUS = :status " +
            "where his.ID_REQUEST_DOWNLOAD = :idRequestDownload " +
            "and his.TOTAL_RECORD = his.TOTAL_RECORD_FINISHED ";

    private static final String SQL_updateAmountByExportRecordSuccess = "UPDATE DOWNLOAD_REQUEST_HIS re  " +
            "    SET re.TOTAL_RECORD_FINISHED = re.TOTAL_RECORD_FINISHED + 1  " +
            "    WHERE re.ID_REQUEST_DOWNLOAD = :idRequestDownload ";
}
