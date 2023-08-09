package compedia.vn.tickmi.download_multiple_file_tickmi.service;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetailsHis;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.HandleDownloadDetailsHisRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HandleDownloadDetailsHisService {

    @Autowired
    HandleDownloadDetailsHisRepository handleDownloadDetailsHisRepository;

    public void saveHandleDownloadDetailsHis(HandleDownloadDetailsHis handleDownloadDetailsHis) {
        handleDownloadDetailsHisRepository.save(handleDownloadDetailsHis);
    }

}
