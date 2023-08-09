package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandleDownloadDetailsRepository extends JpaRepository<HandleDownloadDetails, Long>, HandleDownloadDetailsRepositoryCustom {
}
