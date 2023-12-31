package compedia.vn.tickmi.download_multiple_file_tickmi.repository.impl;


import compedia.vn.tickmi.download_multiple_file_tickmi.entity.DownloadRequest;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.DownloadRequestRepositoryCustom;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.ValueUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.IOException;
import java.sql.Clob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Log4j2
public class DownloadRequestRepositoryImpl implements DownloadRequestRepositoryCustom {

    @Autowired
    EntityManager entityManager;

    @Override
    public List<DownloadRequest> findAllRequestDownloadsLimit() throws IOException, SQLException {
        Query query = entityManager.createNativeQuery(SQL_findRequestDownLoadsLimit);
        query.setParameter("status", DbConstant.NEW_STATUS_REQUEST_DOWNLOAD);
        query.setParameter("limitRow", DbConstant.LIMIT_RECORD);
        List<Object[]> result = query.getResultList();
        List<DownloadRequest> response = new ArrayList<>();
        if (!CollectionUtils.isEmpty(result)) {
            for (Object[] obj: result) {
                DownloadRequest dto = new DownloadRequest();
                dto.setIdRequestDownload(ValueUtil.getLongByObject(obj[0]));
                dto.setContentUser(ValueUtil.getClobString((Clob) obj[1]));
                dto.setPathFile(ValueUtil.getStringByObject(obj[2]));
                dto.setStatus(ValueUtil.getIntegerByObject(obj[3]));
                dto.setTotalRecord(ValueUtil.getIntegerByObject(obj[4]));
                dto.setTotalRecordFinished(ValueUtil.getIntegerByObject(obj[5]));
                dto.setEventId(ValueUtil.getLongByObject(obj[6]));
                dto.setFlatStatus(ValueUtil.getIntegerByObject(obj[7]));
                dto.setTicketEventId(ValueUtil.getLongByObject(obj[8]));
                if (null != obj[9]) {
                    dto.setHtml(ValueUtil.getClobString((Clob) obj[9]) == null ? "" : ValueUtil.getClobString((Clob) obj[9]));
                }
                if (null != obj[10]) {
                    dto.setDesignHtml(ValueUtil.getClobString((Clob) obj[10]) == null ? "" : ValueUtil.getClobString((Clob) obj[10]));
                }
                dto.setWidth(ValueUtil.getIntegerByObject(obj[11]) == null ? 0 : ValueUtil.getIntegerByObject(obj[11]));
                dto.setHeight(ValueUtil.getIntegerByObject(obj[12]) == null ? 0 : ValueUtil.getIntegerByObject(obj[12]));
                if (null != obj[13]) {
                    dto.setHtmlReplace(ValueUtil.getClobString((Clob) obj[13]) == null ? "" : ValueUtil.getClobString((Clob) obj[13]));
                }
                if (null != obj[14]) {
                    dto.setJsonData(ValueUtil.getClobString((Clob) obj[14]));
                }
                dto.setIsNewTool(ValueUtil.getIntegerByObject(obj[15]));
                dto.setPathImage(ValueUtil.getStringByObject(obj[16]) == null ? "" : ValueUtil.getStringByObject(obj[16]));
                dto.setIsFree(ValueUtil.getIntegerByObject(obj[17]));
                dto.setTypeDownload(ValueUtil.getIntegerByObject(obj[18]));
                response.add(dto);
            }
        }
        return response;
    }

    @Transactional
    @Modifying
    @Override
    public void updateAmountByExportRecordSuccess(Long requestDownloadsId) {
        Query query = entityManager.createNativeQuery(SQL_updateAmountByExportRecordSuccess);
        query.setParameter("idRequestDownload", requestDownloadsId);
        query.executeUpdate();
    }


    @Transactional
    @Modifying
    @Override
    public void flatStatusByIdParent(Long parentId) {
        Query query = entityManager.createNativeQuery(SQL_flatStatusByIdParent);
        query.setParameter("flatStatus",DbConstant.FLAT_STATUS_TO_ROLL_BACK);
        query.setParameter("idRequestDownload",parentId);
        query.executeUpdate();
    }

    @Transactional
    @Modifying
    @Override
    public boolean updateStatusDownloadRequestWhenFinishedProcess(Long requestDownloadId) {
        Query query = entityManager.createNativeQuery(SQL_updateStatusWhenDone);
        query.setParameter("statusDone",DbConstant.FINISHED_STATUS_REQUEST_DOWNLOAD);
        query.setParameter("idRequestDownload",requestDownloadId);
        if (query.executeUpdate() > 0) {
            return true;
        }
        return false;
    }


    private static String SQL_findRequestDownLoadsLimit = "SELECT requestDownload.ID_REQUEST_DOWNLOAD, " +
            "       requestDownload.CONTENT_USER, " +
            "       requestDownload.PATH_FILE, " +
            "       requestDownload.STATUS, " +
            "       requestDownload.TOTAL_RECORD, " +
            "       requestDownload.TOTAL_RECORD_FINISHED, " +
            "       requestDownload.EVENT_ID, " +
            "       requestDownload.FLAT_STATUS, " +
            "       requestDownload.TICKET_EVENT_ID, " +
            "       requestDownload.HTML, " +
            "       requestDownload.DESIGN_HTML, " +
            "       requestDownload.WIDTH, " +
            "       requestDownload.HEIGHT, " +
            "       requestDownload.HTML_REPLACE, " +
            "       requestDownload.JSON_DATA, " +
            "       requestDownload.IS_NEW_TOOL, " +
            "       requestDownload.PATH_IMAGE," +
            "       requestDownload.IS_FREE, " +
            "       requestDownload.TYPE_DOWNLOAD " +
            " FROM DOWNLOAD_REQUEST requestDownload " +
            " WHERE requestDownload.STATUS = :status " +
            "  AND ROWNUM < :limitRow ";

    private static String SQL_updateAmountByExportRecordSuccess = "UPDATE DOWNLOAD_REQUEST re " +
            " SET re.TOTAL_RECORD_FINISHED = re.TOTAL_RECORD_FINISHED + 1 " +
            " WHERE re.ID_REQUEST_DOWNLOAD = :idRequestDownload ";

    private static String SQL_flatStatusByIdParent = "UPDATE DOWNLOAD_REQUEST re " +
            " SET re.FLAT_STATUS = :flatStatus " +
            " WHERE re.ID_REQUEST_DOWNLOAD = :idRequestDownload ";

    private static String SQL_updateStatusWhenDone = "UPDATE DOWNLOAD_REQUEST downloadRequest " +
            " SET downloadRequest.STATUS = :statusDone " +
            " WHERE downloadRequest.ID_REQUEST_DOWNLOAD = :idRequestDownload " +
            " AND downloadRequest.TOTAL_RECORD_FINISHED = downloadRequest.TOTAL_RECORD ";
}
