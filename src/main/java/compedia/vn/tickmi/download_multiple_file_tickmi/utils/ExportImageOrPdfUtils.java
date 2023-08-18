package compedia.vn.tickmi.download_multiple_file_tickmi.utils;

import com.google.gson.JsonObject;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.CaptureImageResponse;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.ReplaceTemplateTicketDto;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.RestClient;
import compedia.vn.tickmi.download_multiple_file_tickmi.dto.TicketToConvertImageAndPdfDto;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
public class ExportImageOrPdfUtils {

    public final static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("DDMMYYYY");
    public final static String FILE_TEMPLATE_TICKET = "template_ticket";
    public final static String FILE_PDF_TEMPLATE = "pdf";
    public final static String FILE_HTML_TEMPLATE = "html";
    public final static String FILE_IMAGE_TEMPLATE = "image_template";

    private String convertTicketToImageAndPdf(TicketToConvertImageAndPdfDto dtoTicketToConvert,
                                              Integer typeConvert) throws Exception {
        ReplaceTemplateTicketDto dto = createReplaceTemplateTicketDto(dtoTicketToConvert);
        String content = null;
        if ( dto.getIsNewTool() != null && dto.getIsNewTool().intValue() == DbConstant.NEW_DESIGN_TOOL && StringUtils.isNotBlank(dto.getJsonData())) {
            if (typeConvert.equals(DbConstant.CONVERT_TO_PDF)) {
                throw new Exception("System not yet support type convert to Pdf with version design there!");
            } else {
                return replaceTemplateTicketNew(dto);
            }
        } else {
            content =  replaceTemplateTicket(dto);
            String filePathConvert = null;
            if (typeConvert.equals(DbConstant.CONVERT_TO_IMAGE)) {
                filePathConvert = convertHtmlToImage(content,  dtoTicketToConvert.getTicketId(),
                        dtoTicketToConvert.getWidth(), dtoTicketToConvert.getHeight(),dtoTicketToConvert.getNameGuest(),
                        dtoTicketToConvert.getNoteGuest());
            } else if (typeConvert.equals(DbConstant.CONVERT_TO_PDF)) {
                filePathConvert = convertHtmlToPdf(content, dtoTicketToConvert.getTicketId());
            }
            return filePathConvert;
        }
    }

    private ReplaceTemplateTicketDto createReplaceTemplateTicketDto(TicketToConvertImageAndPdfDto dto) {
        ReplaceTemplateTicketDto response = new ReplaceTemplateTicketDto();
        response.setContentHtml(dto.getHtml());
        response.setHtmlReplace(dto.getHtmlReplace());
        response.setQr(dto.getPathQr());
        response.setUrl(DbConstant.STATIC_CONTEXT);
        response.setEmail(dto.getEmailGuest());
        response.setNameGuest(dto.getNameGuest());
        response.setPhoneGuest(dto.getPhoneGuest());
        response.setNoteGuest(dto.getNoteGuest());
        response.setTicketName(dto.getTicketName());
        if (StringUtils.isNotBlank(dto.getAvatarPath())) {
            response.setAvatarPath(dto.getAvatarPath());
        }
        if (StringUtils.isNotBlank(dto.getAvatarPath())) {
            response.setAvatarPath(dto.getAvatarPath());
        }
        response.setIsPackageFree(dto.getIsFree());
        response.setIsNewTool(dto.getIsDesign());
        if (null != dto.getIsDesign() && dto.getIsDesign().intValue() == DbConstant.NEW_DESIGN_TOOL) {
            response.setWidth(dto.getWidth());
            response.setHeight(dto.getHeight());
        }
        if (StringUtils.isNotBlank(dto.getJsonData())) {
            response.setJsonData(dto.getJsonData());
        }

        return response;
    }


    public static String replaceTemplateTicket(ReplaceTemplateTicketDto dto) {
        int mod = 4;
        log.info("===================== REPLACE START ===============================");
        String contentHtml = dto.getContentHtml();
        String htmlReplace = dto.getHtmlReplace();
        String qr = dto.getQr();
        String url = dto.getUrl();
        if (null != contentHtml && null != htmlReplace) {
            try {
                String[] pathQR = qr.split(";");
                int quantity = pathQR.length;
                contentHtml = contentHtml.replace("\\n", "").replace("\\", "").trim();
                StringBuilder htmlOutPut = new StringBuilder();
                System.out.println("SIZE PATH : " + quantity);
                if (quantity < mod) {
                    Document document = Jsoup.parse(contentHtml);
                    Element element = document.getElementById("QR_LOCATE");
                    if (null != element) {
                        String childHtml = element.html();
                        for (int i = 0; i < quantity; i++) {
                            String strReplace = childHtml;
                            strReplace = strReplace.replace("{QR_HERE}", url + pathQR[i]);
                            htmlOutPut.append(strReplace);
                        }
                        element.html(htmlOutPut.toString());
                    }
                } else {
                    int loop = quantity / mod;
                    int remainder = quantity % mod;
                    if (remainder != 0) {
                        ++loop;
                    }
                    int stt = 0;
                    for (int i = 0; i < loop; ++i) {
                        StringBuilder tmpReplace = new StringBuilder();
                        Document document = Jsoup.parse(contentHtml);
                        Element elementTmp = document.getElementById("QR_LOCATE");
                        if (null != elementTmp) {
                            String content = elementTmp.html();
                            int loop_2 = mod;
                            if (i == loop - 1 && remainder != 0) {
                                loop_2 = quantity - remainder * mod;
                            }
                            for (int j = 0; j < loop_2; ++j) {
                                String strReplace = content;
                                strReplace = replaceQR(content, url + pathQR[stt]);
                                tmpReplace.append(strReplace);
                                ++stt;
                            }
                            elementTmp.html(tmpReplace.toString());
                            htmlOutPut.append(elementTmp.toString());
                        }
                    }
                }
                htmlReplace = htmlReplace.replace("\\n", "").replace("\\", "").trim();
                htmlReplace = htmlReplace.replace("{QR_HERE}", htmlOutPut.toString());
                return replaceContent(htmlReplace, dto);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
        } else if (null != contentHtml && null == htmlReplace) {
            return replaceContent(contentHtml, dto);
        }
        log.info("=================================== REPLACE END ================================= ");
        return " ";
    }

    public static String convertHtmlToPdf(String contentHtml, Long idTemplateTicket) throws FileNotFoundException {
        String pathReturnRoot = PropertiesUtil.getProperty("vn.compedia.static.location");
        String random = generateFileId();
        String todayFolder = SIMPLE_DATE_FORMAT.format(new Date());
        String filePdfFolder = pathReturnRoot + File.separator + FILE_PDF_TEMPLATE + File.separator + todayFolder;
        String filePdf = filePdfFolder + File.separator + random + "_" + idTemplateTicket + ".pdf";
        File fileFolder = new File(filePdfFolder);
        if (!fileFolder.exists() && !fileFolder.mkdirs()) {
            log.error("Create folder pdf false!");
        } else {
            FileOutputStream fileOutputStream = new FileOutputStream(filePdf);
            log.info("Create file pdf success!");
        }
        String pathReturn = File.separator + FILE_PDF_TEMPLATE + File.separator + todayFolder + File.separator + random + "_" + idTemplateTicket + ".pdf";
        String fileHtml = createFileHtml(contentHtml);
        String cmdStr = "google-chrome-stable --headless " + "--disable-gpu " + "--no-sandbox " + "--print-to-pdf-no-header " + "--print-to-pdf=" + "\"" + filePdf + "\" " + "\"" + fileHtml + "\"";
        ;
        try {
            log.info("Start convert Pdf with cmdStr : " + cmdStr);
            Process process = new ProcessBuilder().command("bash", "-c", cmdStr).start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            reader.close();
            int exitVal = process.waitFor();
            process.destroy();
            if (exitVal == 0) {
                log.info("Success convert to Pdf: " + output);             // Convert success
            } else {
                log.error("Fail convert to Pdf: " + fileHtml);              // Convert fail
            }
            File file = new File(fileHtml);
            if (file.delete()) {
                log.info("Delete file Html: " + fileHtml + " success!");
            } else {
                log.info("Delete file html: " + fileHtml + "false!");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return pathReturn;
    }

    private static String generateFileId() {
        return DateUtil.getCurrentDateStr() + RandomStringUtils.randomAlphanumeric(16);
    }

    public static String replaceTemplateTicketNew(ReplaceTemplateTicketDto dto) {
        log.info("=================================== REPLACE START ===================================");
        String jsonData = dto.getContentHtml();

        log.info("==========> Json: {}", jsonData);

        if (StringUtils.isNotBlank(jsonData)) {
            jsonData = jsonData.replace("{QR_HERE}", "http://tickmi.compedia.vn/assets/images/design-tool/QR.png");
            jsonData = replaceContent(jsonData, dto);

            // Call api generate image ticket
            List<String> responses = callApiCaptureImage(jsonData, dto.getWidth(), dto.getHeight());
            if (CollectionUtils.isEmpty(responses)) {
                return null;
            }
            String imagePath = responses.get(0);
            String otherHtml = responses.get(1);

            imagePath = PropertiesUtil.getProperty("vnp.cpa.url") + imagePath;

            return PropertiesUtil.getProperty("design.tool.template.replace").replace("{CANVAS_WIDTH}", dto.getWidth() + "").replace("{CANVAS_HEIGHT}", dto.getHeight() + "").replace("{IMAGE_PATH}", imagePath).replace("{OTHER_HTML}", otherHtml);
        }
        log.info("=================================== REPLACE END ===================================");
        return " ";
    }

    private static String replaceContent(String content, ReplaceTemplateTicketDto dto) {
        if (StringUtils.isNotBlank(dto.getNameGuest())) {
            content = content.replace("{NAME_HERE}", dto.getNameGuest());
        } else {
            content = content.replace("{NAME_HERE}", " ");
        }
        if (StringUtils.isNotBlank(dto.getEmail())) {
            content = content.replace("{EMAIL_HERE}", dto.getEmail());
        } else {
            content = content.replace("{EMAIL_HERE}", " ");
        }
        if (StringUtils.isNotBlank(dto.getPhoneGuest())) {
            content = content.replace("{PHONE_HERE}", dto.getPhoneGuest());
        } else {
            content = content.replace("{PHONE_HERE}", " ");
        }
        if (StringUtils.isNotBlank(dto.getNoteGuest())) {
            content = content.replace("{NOTE_HERE}", dto.getNoteGuest());
        } else {
            content = content.replace("{NOTE_HERE}", " ");
        }
        if (StringUtils.isNotBlank(dto.getTicketName())) {
            content = content.replace("{CODE_QR_HERE}", dto.getTicketName());
        } else {
            content = content.replace("{CODE_QR_HERE}", " ");
        }
        if (StringUtils.isNotBlank(dto.getAvatarPath())) {
            content = content.replace("{AVATAR_PATH}", DbConstant.DOMAIN_FILE + dto.getAvatarPath());
        } else {
            content = content.replace("{AVATAR_PATH}"," ");
        }
        return content;
    }

    public static List<String> callApiCaptureImage(String jsonData, Integer width, Integer height) {
        List<String> results = new ArrayList<>();

        RestClient restClient = new RestClient();

        String todayFolder = SIMPLE_DATE_FORMAT.format(new Date());
        String rootPath = PropertiesUtil.getProperty("vn.compedia.static.location");
        String targetFolder = rootPath + File.separator + FILE_TEMPLATE_TICKET + File.separator + todayFolder;
        String fileName = DateUtil.getCurrentDateStr() + RandomStringUtils.randomAlphanumeric(16) + ".png";

        String imagePath = File.separator + FILE_TEMPLATE_TICKET + File.separator + todayFolder + File.separator + fileName;

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("fileName", fileName);
        jsonObject.addProperty("folderPath", targetFolder);
        jsonObject.addProperty("canvasJson", jsonData);
        jsonObject.addProperty("width", width);
        jsonObject.addProperty("height", height);
        jsonObject.addProperty("needOtherHtml", true);

        String apiUrl = PropertiesUtil.getProperty("api.url.capture.image");

        CaptureImageResponse response = restClient.postRequestBody(apiUrl, jsonObject, CaptureImageResponse.class);
        if (response.isResult()) {
            log.info("=========> Image path: {}", imagePath);
            log.info("=========> Other html: {}", response.getOtherHtml());
            results.add(imagePath);
            results.add(response.getOtherHtml());
            return results;
        }

        return new ArrayList<>();
    }

    public static String replaceQR(String htmlContent, String pathQr) {
        htmlContent = htmlContent.replace("\\n", "").replace("\\", "").trim();
        return htmlContent.replace("{QR_HERE}", pathQr);
    }

    public static String createFileHtml(String contentHtml) {
        String todayFolder = SIMPLE_DATE_FORMAT.format(new Date());
        String random = generateFileId();
        String rootPath = PropertiesUtil.getProperty("vn.cpa.static.location.upload") + File.separator + FILE_HTML_TEMPLATE + File.separator + todayFolder;
        String fileHtml = rootPath + File.separator + random + ".html";
        byte[] content = contentHtml.getBytes();
        File file = new File(rootPath);
        if (!file.exists() && !file.mkdirs()) {
            log.error("Create folder file html false!");
        } else {
            log.info("Path file html: " + fileHtml);
            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(fileHtml);
                outputStream.write(content);
                outputStream.close();
                log.info("Create file html success!");
            } catch (IOException e) {
                log.error("Create file html false!");
                e.printStackTrace();
            }
        }
        return fileHtml;
    }

    public static String convertHtmlToImage(String contentHtml, Long idTemplateTicket, int width, int height,
                                            String nameGuest, String note) throws FileNotFoundException {
        nameGuest = StringUtil.removeTagHtml(nameGuest);
        note = StringUtil.removeTagHtml(note);
        String pathReturnRoot = PropertiesUtil.getProperty("vn.compedia.static.location");
        String todayFolder = SIMPLE_DATE_FORMAT.format(new Date());
        String fileImageFolder = pathReturnRoot + File.separator + FILE_IMAGE_TEMPLATE + File.separator + todayFolder;
        String fileImage = fileImageFolder + File.separator + nameGuest + "_" + note + "_" + idTemplateTicket + ".png";
        File fileFolder = new File(fileImageFolder);
        if (!fileFolder.exists() && !fileFolder.mkdirs()) {
            log.error("Create folder image false!");
        } else {
            FileOutputStream fileOutputStream = new FileOutputStream(fileImage);
            log.info("Create file image success!");
        }
        String pathReturn = File.separator + FILE_IMAGE_TEMPLATE + File.separator + todayFolder + File.separator + nameGuest + "_" + note + "_" + idTemplateTicket + ".png";
        String fileHtml = createFileHtml(contentHtml);
        String cmdStr = "google-chrome-stable --headless --disable-gpu  --screenshot=" + "\"" + fileImage + "\" --window-size=" + width + "," + height + " --no-sandbox " + fileHtml;
        try {
            log.info("Start convert Image with cmdStr : " + cmdStr);
            Process process = new ProcessBuilder().command("bash", "-c", cmdStr).start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            log.info("start read from cmd");
            while ((line = reader.readLine()) != null) {
                output.append(line + "\n");
            }
            log.info("end read from cmd");
            process.waitFor();
            log.info("process wait result");
            int exitVal = process.exitValue();
            process.destroy();
            log.info("Destroy!");
            if (exitVal == 0) {
                log.info("Success convert to Image: " + output);               // Convert success
            } else {
                log.error("Fail convert to Image: " + fileHtml);                // Convert fail
            }
            File file = new File(fileHtml);
            if (file.delete()) {
                log.info("Delete file Html: " + fileHtml + " success!");
            } else {
                log.info("Delete file html: " + fileHtml + "false!");
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return pathReturn;
    }
}
