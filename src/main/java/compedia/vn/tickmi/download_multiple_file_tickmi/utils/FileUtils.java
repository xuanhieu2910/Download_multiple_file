package compedia.vn.tickmi.download_multiple_file_tickmi.utils;

import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Log4j2
public class FileUtils {

    public final static String FOLDER_NAME_ZIP = "zip";
    public final static String FOLDER_NAME_ZIP_DRAFT = "zip_draft";

    public static String exportToFileZip(List<String> listPathFile,Long eventId ,Long ticketEventId, int typeZip) {

        String folderFileZip = createNameFile(eventId, ticketEventId, typeZip);
        File file = new File(folderFileZip);
        if (!file.exists()) {
            String mkdirFileZip = "mkdir " + folderFileZip;
            log.info("=================> Mkdir cmd string: {}", mkdirFileZip);
            executeCmd(mkdirFileZip);
        }

        String folder = "File-QR-" + StringUtil.generatePasswordRandom(20);
        log.info("=================> Folder: {}", folder);

        String fileReturnFolder = folderFileZip + File.separator + folder;
        log.info("=================> File return folder: {}", fileReturnFolder);

        String mkdirCmd = "mkdir " + fileReturnFolder;
        log.info("=================> Mkdir cmd string: {}", mkdirCmd);
        executeCmd(mkdirCmd);

        String fileReturn = fileReturnFolder + File.separator + folder + ".zip";
        putDataToZipFile(fileReturn, listPathFile);

        log.info("=================> Export cmd string: {}", fileReturn);
        return fileReturn;
    }

    public static void executeCmd(String cmdStr) {
        try {
            Process process = Runtime.getRuntime().exec(cmdStr);
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                log.info("=================> {}: ", output.append(line));
            }

            int exitVal = process.waitFor();
            if (exitVal == 0) {
                log.info("=================> Execute cmd success: {}", cmdStr);
            } else {
                log.error("=================> Execute cmd fail: {}", cmdStr);
            }
        } catch (Exception e) {
            log.error("=================> Execute cmd error: {}", e.getMessage());
        }
    }

    public static void putDataToZipFile(String zipFile, List<String> srcFiles) {
        try {
            byte[] buffer = new byte[8192];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (String file : srcFiles) {


                File srcFile = new File(file);
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            log.error("=================> Error creating zip file: {}", ioe.getMessage());
        }
    }

    public static boolean deleteFileDraw (String pathFile) {
        try {
            boolean result = Files.deleteIfExists(Paths.get(pathFile));
            if (result) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String createNameFile(Long eventId, Long ticketEventId,Integer typeZip) {
        String rootPath = PropertiesUtil.getProperty("vn.compedia.static.location");
        String folderFileZip = rootPath + File.separator;
        if (typeZip == DbConstant.TYPE_FILE_DRAFT) {
            folderFileZip = folderFileZip + FOLDER_NAME_ZIP + File.separator + eventId + File.separator + ticketEventId;
        } else {
            folderFileZip = folderFileZip + FOLDER_NAME_ZIP_DRAFT + File.separator + eventId + File.separator + ticketEventId;
        }
        return folderFileZip;
    }


    public static String exportFileZipAfterHandleFinished(String sourceFile, String desFile) {
        try {
            File file = new File(sourceFile);
            String nameFiles[] = file.list();
            FileOutputStream fos = new FileOutputStream(desFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            byte[] buffer = new byte[8192];
            for(int i = 0; i< nameFiles.length; i++) {
                String nameFileChild = sourceFile + File.separator + nameFiles[i];
                File srcFile = new File(nameFileChild);
                FileInputStream fis = new FileInputStream(srcFile);
                zos.putNextEntry(new ZipEntry(srcFile.getName()));

                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
            return desFile;
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return desFile;
    }




}
