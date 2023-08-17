package compedia.vn.tickmi.download_multiple_file_tickmi.utils;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;
import java.util.regex.Pattern;

@Log4j2
public class StringUtil {
    private static final String PATTERN_FULL_NAME = "^[A-Z]+ [A-Z]+$";
    public static final String PATTERN_PASSWORD = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    public static final String PATTERN_EMAIL = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static boolean validatePassword(String password) {
        if (StringUtils.isBlank(password)) {
            return false;
        }
        return password.matches(PATTERN_PASSWORD);
    }

    public static boolean validateUser(String cardName) {
        if (StringUtils.isBlank(cardName)) {
            return false;
        }
        return cardName.matches(PATTERN_FULL_NAME);
    }

    public static boolean validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return email.matches(PATTERN_EMAIL);
    }

    public static String convertFullNameToFirst(String fullName) {
        String firstName = "";
        if (fullName == null) {
            firstName = "";
        } else {
            if (fullName.split("\\w+").length > 1) {
                firstName = fullName.substring(0, fullName.lastIndexOf(' '));
            } else {
                firstName = fullName;
            }
        }

        return firstName;
    }

    public static String convertFullNameToLast(String fullName) {
        String lastName = "";
        if (fullName == null) {
            lastName = "";
        } else {
            if (fullName.split("\\w+").length > 1) {
                lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
            }
        }
        return lastName;
    }

    public static String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(10);
    }

    public static String generatePasswordRandom(Integer len) {
        final String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }

        return sb.toString();
    }

    public static String generateCodeOrder() {
        return RandomStringUtils.randomNumeric(7) + RandomStringUtils.randomAlphabetic(8).toUpperCase(Locale.ROOT);
    }

    public static String generateTokenForgetPassword() {
        return RandomStringUtils.randomAlphanumeric(50);
    }

    public static String buildURI(String staticContext, String filePath) {
        if (StringUtils.isBlank(filePath)) {
            return filePath;
        }
        return staticContext + filePath;
    }

    public static String base64Encode(String originalInput) {
        return new String(Base64.encodeBase64(originalInput.getBytes()));
    }

    public static String base64Decode(String encodedString) {
        return new String(Base64.decodeBase64(encodedString));
    }

    public static String encryptPassword(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-256");
            crypt.reset();
            crypt.update(password.getBytes(StandardCharsets.UTF_8));
            sha1 = byteToHex(crypt.digest());
        } catch (NoSuchAlgorithmException e) {
            log.error("[encryptPassword|NoSuchAlgorithmException] cause error", e);
            e.printStackTrace();
        }
        return sha1;
    }

    public static String encryptPassword(String password, String salt) {
        return encryptPassword(password + salt);
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    public static long convertToSecond(String duration) {
        if (StringUtils.isBlank(duration)) {
            return 0;
        }
        String[] time = duration.split(":");
        String replaceMinute = time[0];
        String replaceSecond = time[1];
        return ((Long.parseLong(replaceMinute) * 60) + Long.parseLong(replaceSecond));
    }

    public static long convertToMinute(String duration) {
        if (StringUtils.isBlank(duration)) {
            return 0;
        }
        String[] time = duration.split(":");
        String replaceHour = time[0];
        String replaceMinute = time[1];
        return ((Long.parseLong(replaceHour) * 60 * 60) + Long.parseLong(replaceMinute) * 60);
    }

    public static String generateUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }

    public static String buildToStr(String str) {
        return "%" + str + "%";
    }

    public static String showInfoLuckyDraw(String name, String phone, String email, String code, String note) {
        List<String> list = new ArrayList<>(Arrays.asList(name, phone, email, code, note));
        list.removeIf(StringUtils::isBlank);
        return String.join("- ", list);
    }

    public static String showAddressCustom(String addressCustom, String address, String nationName) {
        List<String> list = new ArrayList<>(Arrays.asList(addressCustom, address, nationName));
        list.removeIf(StringUtils::isBlank);
        return String.join(", ", list);
    }

    public static String showAddress(String address, String communeName, String districtName, String provinceName) {
        List<String> list = new ArrayList<>(Arrays.asList(address, communeName, districtName, provinceName));
        list.removeIf(StringUtils::isBlank);
        return String.join(", ", list);
    }

    public static String showFullAddress(String address, String communeName, String districtName, String provinceName, String nationName) {
        List<String> list = new ArrayList<>(Arrays.asList(address, communeName, districtName, provinceName, nationName));
        list.removeIf(StringUtils::isBlank);
        return String.join(", ", list);
    }

    public static String createDuplicateImportGuest(String name, String phone, String email) {
        List<String> list = new ArrayList<>(Arrays.asList(name, phone, email));
        list.removeIf(StringUtils::isBlank);
        return String.join("--", list);
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            log.error("[isValidEmailAddress] cause error", ex);
            result = false;
        }
        return !result;
    }

    public static boolean isValidPhoneNumber(String phone) {
        if (StringUtils.isBlank(phone)) {
            return false;
        }
        return phone.matches("\\d+");
    }

    public static String removeSigned(String str) {
        str = str.trim().replaceAll("[-,<>{}/*#$%&@!?|:;\"\'\\[\\]]", "");
        str = convertAccent(str);
        return str.replaceAll("( )+", "-").trim() + "-" + new Date().getTime();
    }

    public static String getByCodeVnpay(String code) {
        Map<String, String> maps = new HashMap<>();
        maps.put("00", "Confirm Success");
        maps.put("01", "Order Not Found");
        maps.put("02", "Order already confirmed");
        maps.put("03", "Dữ liệu gửi sang không đúng định dạng");
        maps.put("04", "Invalid amount");
        maps.put("07", "Trừ tiền thành công. Giao dịch bị nghi ngờ (liên quan tới lừa đảo, giao dịch bất thường).");
        maps.put("09", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng.");
        maps.put("10", "Giao dịch không thành công do: Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần");
        maps.put("11", "Giao dịch không thành công do: Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch.");
        maps.put("12", "Giao dịch không thành công do: Thẻ/Tài khoản của khách hàng bị khóa.");
        maps.put("24", "Giao dịch không thành công do: Khách hàng hủy giao dịch.");
        maps.put("51", "Giao dịch không thành công do: Tài khoản của quý khách không đủ số dư để thực hiện giao dịch.");
        maps.put("65", "Giao dịch không thành công do: Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày.");
        maps.put("75", "Ngân hàng thanh toán đang bảo trì.");
        maps.put("79", "Giao dịch không thành công do: KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch");
        maps.put("91", "Không tìm thấy giao dịch yêu cầu");
        maps.put("94", "Yêu cầu bị trùng lặp trong thời gian giới hạn của API (Giới hạn trong 5 phút)");
        maps.put("97", "Invalid Checksum");
        maps.put("99", "Các lỗi khác (lỗi còn lại, không có trong danh sách mã lỗi đã liệt kê)");
        return maps.get(code);
    }

    public static String removeTagHtml (String name) {
        return name.replaceAll("(?i)<[^>]*>?", " ").replaceAll("\\s+", " ").replaceAll("\\\\","").replaceAll("/","").trim();
    }

    public static String convertAccent(String content) {
        String temp = Normalizer.normalize(content, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replace('đ','d').replace('Đ','D');
    }
}
