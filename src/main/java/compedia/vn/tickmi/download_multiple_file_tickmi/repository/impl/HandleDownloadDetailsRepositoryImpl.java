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
                dto.setContentUser(ValueUtil.getClobString((Clob) obj[1]));
                dto.setStatus(ValueUtil.getIntegerByObject(obj[2]));
                dto.setEventId(ValueUtil.getLongByObject(obj[3]));
                dto.setRequestDownloadId(ValueUtil.getLongByObject(obj[4]));
                dto.setRetry(ValueUtil.getIntegerByObject(obj[5]));
                dto.setTicketEventId(ValueUtil.getLongByObject(obj[6]));
                if (null != obj[7]) {
                    dto.setHtml(ValueUtil.getClobString((Clob) obj[7]) == null ? "" : ValueUtil.getClobString((Clob) obj[7]));
                }
                if (null != obj[8]) {
                    dto.setDesignHtml(ValueUtil.getClobString((Clob) obj[8]) == null ? "" : ValueUtil.getClobString((Clob) obj[8]));
                }
                dto.setWidth(ValueUtil.getIntegerByObject(obj[9]) == null ? 0 : ValueUtil.getIntegerByObject(obj[9]));
                dto.setHeight(ValueUtil.getIntegerByObject(obj[10]) == null ? 0 : ValueUtil.getIntegerByObject(obj[10]));
                if (null != obj[11]) {
                    dto.setHtmlReplace(ValueUtil.getClobString((Clob) obj[11]) == null ? "" : ValueUtil.getClobString((Clob) obj[11]));
                }
                if (null != obj[12]) {
                    dto.setJsonData(ValueUtil.getClobString((Clob) obj[12]) == null ? "" : ValueUtil.getClobString((Clob) obj[12]));
                }
                dto.setIsNewTool(ValueUtil.getIntegerByObject(obj[13]));
                dto.setPathImage(ValueUtil.getStringByObject(obj[14]));
                dto.setIsFree(ValueUtil.getIntegerByObject(obj[15]));
                dto.setTypeDownload(ValueUtil.getIntegerByObject(obj[16]));
                responses.add(dto);
            }
        }
        return responses;
    }

    @Transactional
    @Modifying
    @Override
    public void updateRetryAndStatusIncrementByIdHandleDownloadDetailsAndRetry(Long idRecord) {
        Query query = entityManager.createNativeQuery(SQL_updateAutoIncrementRetryByIdRecordAnd);
        query.setParameter("idRecord",idRecord);
        query.setParameter("status", DbConstant.NEW_STATUS_HANDLE_DOWNLOAD_DETAILS);
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
            "       CONTENT_USER, " +
            "       STATUS, " +
            "       EVENT_ID, " +
            "       REQUEST_DOWNLOAD_ID, " +
            "       RETRY, " +
            "       TICKET_EVENT_ID, " +
            "       HTML,  " +
            "       DESIGN_HTML, " +
            "       WIDTH,  " +
            "       HEIGHT,  " +
            "       HTML_REPLACE,  " +
            "       JSON_DATA,  " +
            "       IS_NEW_TOOL,  " +
            "       PATH_IMAGE," +
            "       IS_FREE, " +
            "       TYPE_DOWNLOAD " +
            "FROM HANDLE_DOWNLOAD_DETAILS handleDownloadDetails " +
            "WHERE handleDownloadDetails.STATUS = :status " +
            "  AND ROWNUM < :limitRow ";


    private final static String SQL_updateAutoIncrementRetryByIdRecordAnd = "UPDATE HANDLE_DOWNLOAD_DETAILS detail " +
            "SET detail.RETRY = detail.RETRY + 1  AND detail.STATUS = :status " +
            "WHERE detail.ID_HANDLE_DOWNLOAD_DETAILS = :idRecord ";

    private final static String SQL_deleteRecordById = "DELETE HANDLE_DOWNLOAD_DETAILS detail " +
            "WHERE detail.ID_HANDLE_DOWNLOAD_DETAILS = :idRecord";

}
