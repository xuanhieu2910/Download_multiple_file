package compedia.vn.tickmi.download_multiple_file_tickmi.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

@Configuration
public class DbConstant {

    @Autowired
    private Environment env;


    @PostConstruct
    public void setUpConfigData() {
        URL = env.getProperty("vn.compedia.location.upload");
    }

    public static String URL;

    // Seperate of records download
    public final static String SEPERATE_RECORDS_DOWNLOAD = ";";

    // POOL_THREAD
    public final static Integer SIZE_POOL_THREAD = 20;

    // Limit to restore db
    public final static Integer LIMIT_STORE_DB = 50;

    // [GET] Limit records
    public final static Integer LIMIT_RECORD = 50;

    // RETRY
    public final static Integer RETRY_HANDLE = 3;

    // [STATUS]
    public final static Integer IS_NOT_CHANGE = 1;
    public final static Integer IS_CHANGED = -1;

    // [FLAT] Run Job
    public  static boolean IS_FLAT_RUN_JOB = true;
    public static Integer IS_FLAT_DISPLAY_LOGO = 1;
    public static Integer IS_FLAT_DISPLAY_NAME_TICKET = 1;



    // [STATUS] Table Handle download details
    public final static Integer NEW_STATUS_HANDLE_DOWNLOAD_DETAILS = -1;
    public final static Integer FALSE_STATUS_HANDLE_DOWNLOAD_DETAILS = 0;
    public final static Integer HANDLING_STATUS_HANDLE_DOWNLOAD_DETAILS = 1;

    // [STATUS] Request download
    public final static Integer NEW_STATUS_REQUEST_DOWNLOAD = -1;
    public final static Integer NOT_CHANGE_STATUS_REQUEST_DOWNLOAD = 0;
    public final static Integer CHANGE_STATUS_REQUEST_DOWNLOAD = 1;
    public final static Integer PROCESSING_STATUS_REQUEST_DOWNLOAD = 2;
    public final static Integer FINISHED_STATUS_REQUEST_DOWNLOAD = 3;
    public final static Integer FLAT_STATUS_TO_ROLL_BACK = 1;




    // [STATUS] Table Handle download details his
    public final static Integer STATUS_DETAIL_HIS_SUCCESS = 1;
    public final static Integer STATUS_DETAIL_HIS_FALSE = -1;
    public final static Integer STATUS_CHANGED = 2;
    public final static String ERROR_CONTENT = "Có lỗi xảy ra!";










}
