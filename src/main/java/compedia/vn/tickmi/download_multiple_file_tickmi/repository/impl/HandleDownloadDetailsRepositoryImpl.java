package compedia.vn.tickmi.download_multiple_file_tickmi.repository.impl;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.HandleDownloadDetailsRepositoryCustom;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.ValueUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class HandleDownloadDetailsRepositoryImpl implements HandleDownloadDetailsRepositoryCustom {


    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<HandleDownloadDetails> findAllHandleDownloadDetailsLimit() throws SQLException, IOException {
        Query query = entityManager.createNativeQuery(SQL_findLimitHandleDownloadDetails);
        query.setParameter("status", DbConstant.NEW_STATUS_HANDLE_DOWNLOAD_DETAILS);
        query.setParameter("limitRow", DbConstant.LIMIT_RECORD);
        List<Object[]> result = query.getResultList();
        List<HandleDownloadDetails> responses = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result) {
                HandleDownloadDetails dto = new HandleDownloadDetails();
                dto.setIdHandleDownloadDetails(ValueUtil.getLongByObject(obj[0]));
                dto.setContentTemplateTicket(ValueUtil.getClobString((Clob) obj[1]));
                dto.setContentUser(ValueUtil.getClobString((Clob) obj[2]));
                dto.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                dto.setEventId(ValueUtil.getLongByObject(obj[4]));
                dto.setRequestDownloadId(ValueUtil.getLongByObject(obj[5]));
                dto.setRetry(ValueUtil.getIntegerByObject(obj[6]));
                dto.setTicketEventId(ValueUtil.getLongByObject(obj[7]));
                responses.add(dto);
            }
        }
        return responses;
    }

    @Transactional
    @Modifying
    @Override
    public void updateRetryIncrementByIdHandleDownloadDetailsAndRetry(Long idRecord) {
        Query query = entityManager.createNativeQuery(SQL_updateAutoIncrementRetryByIdRecordAnd);
        query.setParameter("idRecord",idRecord);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    @Override
    public void deleteByIdHandleDownloadDetails(Long idRecord) {
        Query query = entityManager.createNativeQuery(SQL_deleteRecordById);
        query.setParameter("idRecord",idRecord);
        query.executeUpdate();
    }
    private final static String SQL_findLimitHandleDownloadDetails = "SELECT ID_HANDLE_DOWNLOAD_DETAILS, " +
            "       CONTENT_TEMPLATE_TICKET, " +
            "       CONTENT_USER, " +
            "       STATUS, " +
            "       EVENT_ID, " +
            "       REQUEST_DOWNLOAD_ID, " +
            "       RETRY, " +
            "       TICKET_EVENT_ID " +
            "FROM HANDLE_DOWNLOAD_DETAILS handleDownloadDetails " +
            "WHERE handleDownloadDetails.STATUS = :status " +
            "  AND ROWNUM < :limitRow ";


    private final static String SQL_updateAutoIncrementRetryByIdRecordAnd = "UPDATE HANDLE_DOWNLOAD_DETAILS detail " +
            "SET detail.RETRY = detail.RETRY + 1  " +
            "WHERE detail.ID_HANDLE_DOWNLOAD_DETAILS = :idRecord ";

    private final static String SQL_deleteRecordById = "DELETE HANDLE_DOWNLOAD_DETAILS detail " +
            "WHERE detail.ID_HANDLE_DOWNLOAD_DETAILS = :idRecord";

}
