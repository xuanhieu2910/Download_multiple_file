package compedia.vn.tickmi.download_multiple_file_tickmi.repository;

import compedia.vn.tickmi.download_multiple_file_tickmi.entity.HandleDownloadDetailsHis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HandleDownloadDetailsHisRepository extends JpaRepository<HandleDownloadDetailsHis,Long> , HandleDownloadDetailsHisRepositoryCustom {
}
