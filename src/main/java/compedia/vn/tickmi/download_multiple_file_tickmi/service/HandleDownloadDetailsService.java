package compedia.vn.tickmi.download_multiple_file_tickmi.service;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import compedia.vn.tickmi.download_multiple_file_tickmi.repository.HandleDownloadDetailsRepository;
import compedia.vn.tickmi.download_multiple_file_tickmi.utils.DbConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class HandleDownloadDetailsService {


    @Autowired
    HandleDownloadDetailsRepository handleDownloadDetailsRepository;

    public List<HandleDownloadDetails> findAllHandleDownloadDetailLimit () throws SQLException, IOException {
        return handleDownloadDetailsRepository.findAllHandleDownloadDetailsLimit();
    }

    public void updateStatusHandleDownloadDetails(List<HandleDownloadDetails> handleDownloadDetails, Integer status){
        handleDownloadDetails.stream().forEach(x->x.setStatus(status));
        saveHandleDownloadDetailsLimit(handleDownloadDetails);
    }

    public void saveAllHandleDownloadDetails(List<HandleDownloadDetails>handleDownloadDetails) {
        saveHandleDownloadDetailsLimit(handleDownloadDetails);
    }

    public void deleteHandleDownloadDetailsById (Long idHandleDownloadDetails) {
        handleDownloadDetailsRepository.deleteByIdHandleDownloadDetails(idHandleDownloadDetails);
    }

    public void updateRetryAndStatusHandleDownloadDetailsByIdAndRetry (Long id) {
        handleDownloadDetailsRepository.updateRetryAndStatusIncrementByIdHandleDownloadDetailsAndRetry(id);
    }


    private void saveHandleDownloadDetailsLimit(List<HandleDownloadDetails>handleDownloadDetails) {
        int limitStoreDb = DbConstant.LIMIT_STORE_DB;
        if (handleDownloadDetails.size() <= limitStoreDb) {
            handleDownloadDetailsRepository.saveAll(handleDownloadDetails);
        } else {
            int sizeReduce = handleDownloadDetails.size() / limitStoreDb;
            int sizeBack = handleDownloadDetails.size() - limitStoreDb * sizeReduce;
            int startIndex = 0;
            int endIndex = limitStoreDb;
            for (int i = 1; i < sizeReduce; i++) {
                List<HandleDownloadDetails> loop_records = handleDownloadDetails.subList(startIndex, endIndex);
                startIndex = endIndex;
                endIndex += limitStoreDb;
                handleDownloadDetailsRepository.saveAll(loop_records);
            }
            List<HandleDownloadDetails> afterRecords = handleDownloadDetails.subList(startIndex, endIndex);
            handleDownloadDetailsRepository.saveAll(afterRecords);
            startIndex = endIndex;
            endIndex += sizeBack;
            List<HandleDownloadDetails> finalRecords = handleDownloadDetails.subList(startIndex, endIndex);
            handleDownloadDetailsRepository.saveAll(finalRecords);
        }
    }

}
