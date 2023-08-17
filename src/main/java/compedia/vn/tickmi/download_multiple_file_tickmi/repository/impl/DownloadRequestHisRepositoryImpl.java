package compedia.vn.tickmi.download_multiple_file_tickmi.repository.impl;

import compedia.vn.tickmi.download_multiple_file_tickmi.dto.DownloadRequestHisFinished;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestHisRepositoryCustom;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.ValueUtil;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public List<DownloadRequestHisFinished> getDownloadRequestHisToZipWithSizeLimit() {
        Query query = entityManager.createNativeQuery(SQL_getDownloadRequestHis);
        query.setParameter("statusSuccess", DbConstant.FINISHED_STATUS_REQUEST_DOWNLOAD);
        query.setParameter("limitRecord", DbConstant.LIMIT_RECORD_TO_HANDLE_ZIP_FILE);
        List<Object[]> result = query.getResultList();
        List<DownloadRequestHisFinished> response = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj : result) {
                DownloadRequestHisFinished his = new DownloadRequestHisFinished();
                his.setEventId(ValueUtil.getLongByObject(obj[0]));
                his.setTicketEventId(ValueUtil.getLongByObject(obj[1]));
                his.setPathFileChild(ValueUtil.getStringByObject(obj[2]));
                his.setTotalRecordFinished(ValueUtil.getIntegerByObject(obj[3]));
                his.setPathFileParent(ValueUtil.getStringByObject(obj[4]));
                response.add(his);
            }
        }
        return response;
    }
    @Transactional
    @Modifying
    @Override
    public void updateStatusDownloadRecordZipped(List<DownloadRequestHisFinished> responses) {
        for(DownloadRequestHisFinished dto : responses) {
            Query query = entityManager.createNativeQuery(SQL_updateStatusHandleZip);
            query.setParameter("status", DbConstant.STATUS_HANDLE_ZIPPING_FILE);
            query.setParameter("ticketEventIds", dto.getTicketEventId());
            query.setParameter("timeFinshed", dto.getTimeFinished());
            query.executeUpdate();
        }
    }


    private static final String SQL_updateStatusWhenDone = "update DOWNLOAD_REQUEST_HIS his " +
            "set his.STATUS = :status " +
            "where his.ID_REQUEST_DOWNLOAD = :idRequestDownload " +
            "and his.TOTAL_RECORD = his.TOTAL_RECORD_FINISHED ";

    private static final String SQL_updateAmountByExportRecordSuccess = "UPDATE DOWNLOAD_REQUEST_HIS re  " +
            "    SET re.TOTAL_RECORD_FINISHED = re.TOTAL_RECORD_FINISHED + 1  " +
            "    WHERE re.ID_REQUEST_DOWNLOAD = :idRequestDownload ";

    private static final String SQL_getDownloadRequestHis = "WITH ROOT as (SELECT his.EVENT_ID, " +
            "                     his.TICKET_EVENT_ID, " +
            "                     min(detailHis.PATH_FILE) keep ( dense_rank first order by detailHis.PATH_FILE) pathFileChild, " +
            "                     his.TOTAL_RECORD_FINISHED, " +
            "                     his.PATH_FILE pathFileParent " +
            "              FROM DOWNLOAD_REQUEST_HIS his " +
            "                       inner join HANDLE_DOWNLOAD_DETAILS_HIS detailHis " +
            "                                  on his.TICKET_EVENT_ID = detailHis.TICKET_EVENT_ID " +
            "              WHERE his.STATUS = :statusSuccess " +
            "              group by his.EVENT_ID, his.TICKET_EVENT_ID, his.TOTAL_RECORD_FINISHED, his.PATH_FILE) " +
            "SELECT root.EVENT_ID, root.TICKET_EVENT_ID, root.pathFileChild, root.TOTAL_RECORD_FINISHED, root.pathFileParent " +
            "FROM ROOT root " +
            "where ROWNUM <= : limitRecord ";

    private static final String SQL_updateStatusHandleZip = "UPDATE DOWNLOAD_REQUEST_HIS his " +
            "SET his.STATUS = :status and his.TIME_FINISHED = :timeFinshed " +
            "WHERE his.TICKET_EVENT_ID in (:ticketEventIds) ";

}
