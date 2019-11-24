package hockeystats.monolith.nhl_api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;

public abstract class ApiResponse {
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
  static {
    OBJECT_MAPPER.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
  }

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  @JsonIgnore
  private byte[] json;

  @JsonIgnore
  public byte[] getJson() {
    if (json == null) {
      try {
        json = OBJECT_MAPPER.writeValueAsBytes(this);
      } catch (JsonProcessingException ignored) {
      }
    }
    return json;
  }

  @JsonIgnore
  public String getHash() {
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(getJson());
      byte[] digest = md.digest();
      return bytesToHex(digest);
    } catch (Exception e) {
      return null;
    }
  }

  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }
}
