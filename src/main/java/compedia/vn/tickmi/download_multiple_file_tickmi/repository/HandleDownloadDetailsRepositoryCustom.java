package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface HandleDownloadDetailsRepositoryCustom {

    List<HandleDownloadDetails> findAllHandleDownloadDetailsLimit() throws SQLException, IOException;

    void updateRetryAndStatusIncrementByIdHandleDownloadDetailsAndRetry(Long idRecord);

    void deleteByIdHandleDownloadDetails(Long idRecord);

}
