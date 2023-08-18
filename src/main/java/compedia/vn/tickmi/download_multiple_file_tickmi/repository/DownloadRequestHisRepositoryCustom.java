package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

import compedia.vn.tickmi.download_multiple_file_tickmi.dto.DownloadRequestHisFinished;

import java.util.List;

public interface DownloadRequestHisRepositoryCustom {


    void updateStatusDownloadRequestWhenFinishedProcess(Long id);

    void updateAmountByExportRecordSuccess(Long requestDownloadsId);


    List<DownloadRequestHisFinished> getDownloadRequestHisToZipWithSizeLimit ();

    void updateStatusDownloadRecordZipped(List<DownloadRequestHisFinished> responses);

    void updateStatusFinishedToZipFile(Long ticketEventId, String pathFileZip);

    void updateAmountRetryWhenZipFile(Long ticketEventId);

    void updateStatusWhenZipFile(Long ticketEventId, Integer status);
}
