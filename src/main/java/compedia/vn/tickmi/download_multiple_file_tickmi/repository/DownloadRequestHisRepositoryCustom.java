package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

public interface DownloadRequestHisRepositoryCustom {


    void updateStatusDownloadRequestWhenFinishedProcess(Long id);

    void updateAmountByExportRecordSuccess(Long requestDownloadsId);
}
