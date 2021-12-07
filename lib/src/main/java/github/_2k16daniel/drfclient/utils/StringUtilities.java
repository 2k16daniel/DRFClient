package github._2k16daniel.drfclient.utils;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class StringUtilities {

   public String queryBuilder(Map<String, String> query) {
        if (query == null || query.isEmpty())
            return "";
        int i = 0;
        StringBuilder stringbuilder = new StringBuilder("?");
        for (Map.Entry<String, String> entry : query.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (i > 0) {
                stringbuilder.append("&");
            }
            try {
                stringbuilder.append(key).append("=").append(URLEncoder.encode(value, StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            }
            i++;
        }
        return stringbuilder.toString();
    }
}
