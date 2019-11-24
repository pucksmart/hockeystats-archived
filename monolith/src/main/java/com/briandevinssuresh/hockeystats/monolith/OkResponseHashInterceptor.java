package com.briandevinssuresh.hockeystats.monolith;

import brave.Tracing;
import brave.propagation.ExtraFieldPropagation;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Options;
import okio.Sink;
import okio.Timeout;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class OkResponseHashInterceptor implements Interceptor {

  private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

  private static String bytesToHex(byte[] bytes) {
    char[] hexChars = new char[bytes.length * 2];
    for (int j = 0; j < bytes.length; j++) {
      int v = bytes[j] & 0xFF;
      hexChars[j * 2] = HEX_ARRAY[v >>> 4];
      hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
    }
    return new String(hexChars);
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    if (response.body() == null || !response.isSuccessful() || response.isRedirect()) {
      return response;
    } else {
      try {
        return response.newBuilder()
            .body(new HashingResponseBody(response.body()))
            .build();
      } catch (NoSuchAlgorithmException e) {
        return response;
      }
    }
  }

  public static class HashingResponseBody extends ResponseBody {
    final ResponseBody delegate;
    final HashingBufferedSource source;

    HashingResponseBody(ResponseBody delegate) throws NoSuchAlgorithmException {
      this.delegate = delegate;
      this.source = new HashingBufferedSource(this.delegate.source());
    }

    @Override public MediaType contentType() {
      return delegate.contentType();
    }

    @Override public long contentLength() {
      return delegate.contentLength();
    }

    @Override public BufferedSource source() {
      return source;
    }

    @Override public void close() {
      ExtraFieldPropagation.set("retrofit-md5", getHash());
      delegate.close();
    }

    private String getHash() {
      return source.getHash();
    }
  }

  static class HashingBufferedSource implements BufferedSource {
    final BufferedSource delegate;
    final MessageDigest messageDigest;

    HashingBufferedSource(BufferedSource delegate) throws NoSuchAlgorithmException {
      this.delegate = delegate;
      this.messageDigest = MessageDigest.getInstance("MD5");
    }

    @Override public long read(Buffer sink, long byteCount) throws IOException {
      Buffer temp = new Buffer();
      long num = delegate.read(temp, byteCount);

      byte[] body = new byte[(int) num];
      temp.read(body);
      temp.write(body);
      messageDigest.update(body);

      return temp.read(sink, byteCount);
    }

    String getHash() {
      byte[] digest = messageDigest.digest();
      return bytesToHex(digest);
    }



    @Override public Buffer buffer() {
      return delegate.buffer();
    }

    @Override public Buffer getBuffer() {
      return delegate.getBuffer();
    }

    @Override public boolean exhausted() throws IOException {
      return delegate.exhausted();
    }

    @Override public void require(long byteCount) throws IOException {
      delegate.require(byteCount);
    }

    @Override public boolean request(long byteCount) throws IOException {
      return delegate.request(byteCount);
    }

    @Override public byte readByte() throws IOException {
      return delegate.readByte();
    }

    @Override public short readShort() throws IOException {
      return delegate.readShort();
    }

    @Override public short readShortLe() throws IOException {
      return delegate.readShortLe();
    }

    @Override public int readInt() throws IOException {
      return delegate.readInt();
    }

    @Override public int readIntLe() throws IOException {
      return delegate.readIntLe();
    }

    @Override public long readLong() throws IOException {
      return delegate.readLong();
    }

    @Override public long readLongLe() throws IOException {
      return delegate.readLongLe();
    }

    @Override public long readDecimalLong() throws IOException {
      return delegate.readDecimalLong();
    }

    @Override public long readHexadecimalUnsignedLong() throws IOException {
      return delegate.readHexadecimalUnsignedLong();
    }

    @Override public void skip(long byteCount) throws IOException {
      delegate.skip(byteCount);
    }

    @Override public ByteString readByteString() throws IOException {
      return delegate.readByteString();
    }

    @Override public ByteString readByteString(long byteCount) throws IOException {
      return delegate.readByteString(byteCount);
    }

    @Override public int select(Options options) throws IOException {
      return delegate.select(options);
    }

    @Override public byte[] readByteArray() throws IOException {
      return delegate.readByteArray();
    }

    @Override public byte[] readByteArray(long byteCount) throws IOException {
      return delegate.readByteArray(byteCount);
    }

    @Override public int read(byte[] sink) throws IOException {
      return delegate.read(sink);
    }

    @Override public void readFully(byte[] sink) throws IOException {
      delegate.readFully(sink);
    }

    @Override public int read(byte[] sink, int offset, int byteCount) throws IOException {
      return delegate.read(sink, offset, byteCount);
    }

    @Override public void readFully(Buffer sink, long byteCount) throws IOException {
      delegate.readFully(sink, byteCount);
    }

    @Override public long readAll(Sink sink) throws IOException {
      return delegate.readAll(sink);
    }

    @Override public String readUtf8() throws IOException {
      return delegate.readUtf8();
    }

    @Override public String readUtf8(long byteCount) throws IOException {
      return delegate.readUtf8(byteCount);
    }

    @Override public String readUtf8Line() throws IOException {
      return delegate.readUtf8Line();
    }

    @Override public String readUtf8LineStrict() throws IOException {
      return delegate.readUtf8LineStrict();
    }

    @Override public String readUtf8LineStrict(long limit) throws IOException {
      return delegate.readUtf8LineStrict(limit);
    }

    @Override public int readUtf8CodePoint() throws IOException {
      return delegate.readUtf8CodePoint();
    }

    @Override public String readString(Charset charset) throws IOException {
      return delegate.readString(charset);
    }

    @Override public String readString(long byteCount, Charset charset) throws IOException {
      return delegate.readString(byteCount, charset);
    }

    @Override public long indexOf(byte b) throws IOException {
      return delegate.indexOf(b);
    }

    @Override public long indexOf(byte b, long fromIndex) throws IOException {
      return delegate.indexOf(b, fromIndex);
    }

    @Override public long indexOf(byte b, long fromIndex, long toIndex) throws IOException {
      return delegate.indexOf(b, fromIndex, toIndex);
    }

    @Override public long indexOf(ByteString bytes) throws IOException {
      return delegate.indexOf(bytes);
    }

    @Override public long indexOf(ByteString bytes, long fromIndex) throws IOException {
      return delegate.indexOf(bytes, fromIndex);
    }

    @Override public long indexOfElement(ByteString targetBytes) throws IOException {
      return delegate.indexOfElement(targetBytes);
    }

    @Override public long indexOfElement(ByteString targetBytes, long fromIndex)
        throws IOException {
      return delegate.indexOfElement(targetBytes, fromIndex);
    }

    @Override public boolean rangeEquals(long offset, ByteString bytes) throws IOException {
      return delegate.rangeEquals(offset, bytes);
    }

    @Override
    public boolean rangeEquals(long offset, ByteString bytes, int bytesOffset, int byteCount)
        throws IOException {
      return delegate.rangeEquals(offset, bytes, bytesOffset, byteCount);
    }

    @Override public BufferedSource peek() {
      return delegate.peek();
    }

    @Override public InputStream inputStream() {
      return delegate.inputStream();
    }

    @Override public int read(ByteBuffer dst) throws IOException {
      return delegate.read(dst);
    }

    @Override public boolean isOpen() {
      return delegate.isOpen();
    }

    @Override public Timeout timeout() {
      return delegate.timeout();
    }

    @Override public void close() throws IOException {
      delegate.close();
    }
  }
}
