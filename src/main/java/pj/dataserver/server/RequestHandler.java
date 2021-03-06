package pj.dataserver.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.cookie.ServerCookieEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pj.dataserver.util.Convert;
import pj.dataserver.util.JacksonUtil;
import pj.dataserver.util.Pool;
import pj.dataserver.util.compress.GZipUtils;
import pj.dataserver.util.HttpUtil.HttpContentBean;

import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.*;

import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;
import static io.netty.handler.codec.http.HttpHeaderNames.SET_COOKIE;

public class RequestHandler implements Pool.Poolable {

    protected static final String UTF_8 = "utf-8";

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    private HttpServer server;
    protected FullHttpRequest request;
    protected ChannelHandlerContext ctx;

    private final StringBuilder sbResponseContent = new StringBuilder();
    private Map<String, List<String>> uriAttributes = null;
    private Map<String, String> uriAttributesMap = null;

    protected Map<String, Object> postAttributes = null;
    protected byte[] httpContent = null;
    protected Map<String, Object> sendMap = new LinkedHashMap<String, Object>();

    private String reqPrefix = null;

    public String getRequestPrefix() {
        return reqPrefix;
    }

    public void setRequestPrefix(String reqPrefix) {
        this.reqPrefix = reqPrefix;
    }

    private long curTime = 0;

    public void setCurTime(long curTime) {
        this.curTime = curTime;
    }

    public long getCurTime() {
        return this.curTime;
    }

    @Override
    public void reset() {
        this.sbResponseContent.setLength(0);

        if (this.uriAttributes != null) {
            this.uriAttributes.clear();
            this.uriAttributes = null;
        }
        if (this.uriAttributesMap != null) {
            this.uriAttributesMap.clear();
            this.uriAttributesMap = null;
        }
        if (this.postAttributes != null) {
            this.postAttributes.clear();
            this.postAttributes = null;
        }
        this.sendMap.clear();
        this.httpContent = null;

        this.request = null;
        this.ctx = null;
        this.server = null;

        this.reqPrefix = null;
        this.curTime = 0;
    }

    public HttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    /**
     * 获取get请求参数
     * 
     * @param name
     * @return
     */
    public String getParameter(String name) {

        return getParameter(name, null, true);
    }

    public String getParameter(String name, String defValue) {

        return getParameter(name, defValue, true);
    }

    public String getParameter(String name, String defValue, boolean strip) {

        Map<String, List<String>> uriAttrs = getQueryAttrs();
        if (uriAttrs.containsKey(name) && uriAttrs.get(name).size() > 0) {
            String val = uriAttrs.get(name).get(0);

            if (strip) {
                return val.trim();
            }
            else {
                return val;
            }
        }

        return defValue;
    }

    /**
     * List<String>表示当参数相同时，把相同的参数的值放在list中
     */
    public Map<String, List<String>> getQueryAttrs() {
        if (uriAttributes == null) {
            uriAttributes = getQueryAttrs(this.getRequest().getUri());
        }
        return uriAttributes;
    }

    /**
     * 当参数相同时，只取最前面的参数值
     */
    public Map<String, String> getFirstQueryAttrs() {
        if (uriAttributes == null) {
            uriAttributes = getQueryAttrs(this.getRequest().getUri());
        }
        if (uriAttributesMap == null && uriAttributes != null) {
            uriAttributesMap = new HashMap<>();
            Set<String> keys = uriAttributes.keySet();
            for (String key : keys) {
                uriAttributesMap.put(key, uriAttributes.get(key).get(0));
            }
        }
        return uriAttributesMap;
    }

    /**
     * List<String>表示当参数相同时，把相同的参数的值放在list中
     */
    public static Map<String, List<String>> getQueryAttrs(String params) {
        if (params.indexOf("?") < 0) {
            params = "?" + params;
        }
        QueryStringDecoder decoderQuery = new QueryStringDecoder(params);
        return decoderQuery.parameters();
    }

    /**
     * Map<String>表示当参数相同时，取第一个参数值
     */
    public static Map<String, String> getFirstQueryAttrs(String params) {
        if (params.indexOf("?") < 0) {
            params = "?" + params;
        }
        QueryStringDecoder decoderQuery = new QueryStringDecoder(params);
        Map<String, List<String>> attrs = decoderQuery.parameters();

        Map<String, String> map = new HashMap<>();
        if (attrs != null && attrs.size() > 0) {
            Set<String> keys = attrs.keySet();
            for (String key : keys) {
                map.put(key, attrs.get(key).get(0));
            }
        }
        return map;
    }

    public byte[] getPostData(HttpContent chunk) {
        byte[] tmp = new byte[chunk.content().capacity()];
        chunk.content().readBytes(tmp);
        return tmp;
    }

    /**
     * 获取post请求content
     * 
     * @return
     */
    public String getPostStr() {
        try {
            return new String(getHttpContent(), UTF_8);
        }
        catch (UnsupportedEncodingException e) {
            log.error("Get PostStr error:", e);
        }
        return null;
    }

    /**
     * 获取post请求content(json格式解析为Map)
     * 
     * @return
     */
    public Map<String, Object> getPostAttrs() {
        if (postAttributes == null) {
            try {
                postAttributes = JacksonUtil.jackson.json2Obj(getPostStr());
            }
            catch (Exception e) {

                log.error("Get PostAtts error:", e);
                sendMap.put("errCode", 2);
                write(sendMap);
            }
        }

        return postAttributes;
    }

    public Object postParameter(String name, Object defValue, boolean strip) {

        Map<String, Object> postAtts = getPostAttrs();
        if (postAtts != null && postAtts.containsKey(name)) {
            if (strip) {
                return Convert.toString(postAtts.get(name)).trim();
            }
            return postAtts.get(name);
        }

        return defValue;

    }

    public void doGet() {
        this.get();
    }

    public void get() {

        String str = this.getServer().getConfig().default404Info
                     + String.format(", Request Url:%s \nMethod: get , Port: %s , ClassName: %s",
                                     this.getRequest().getUri(),
                                     this.getServer().getConfig().port,
                                     this.getClass().getSimpleName());
        writeResponse(HttpResponseStatus.OK, str);
    }

    public void doPost() throws UnsupportedEncodingException {
        this.post();
    }

    public byte[] getHttpContent() throws UnsupportedEncodingException {
        if (this.httpContent == null) {
            if (request.content().hasArray()) {
                return request.content().array();
            }
            else {
                this.httpContent = ByteBufToBytes.read(request.content());
            }
        }
        return this.httpContent;

    }

    public void post() {

        String str = this.getServer().getConfig().default404Info
                     + String.format(", Request Url:%s \nMethod: post , Port: %s , ClassName: %s",
                                     this.getRequest().getUri(),
                                     this.getServer().getConfig().port,
                                     this.getClass().getSimpleName());
        writeResponse(HttpResponseStatus.NOT_FOUND, str);
    }

    /**
     * 返回Map响应数据,解析为json字符串
     * 
     * @param sendMap
     */
    public void write(Map<String, Object> sendMap) {

        try {
            writeResponse(JacksonUtil.jackson.obj2Byte(sendMap));
        }
        catch (Exception e) {
            log.error("post response Encode error", e);
            write("{\"errCode\":2}");
        }
    }

    /**
     * 返回响应数据
     * 
     * @param content
     */
    public void write(String content) {

        write(content, false);

    }

    /**
     * 返回响应数据,needCompress(是否使用GZip压缩)
     * 
     * @param content
     * @param needCompress
     */
    public void write(final String content, final boolean needCompress) {

        sbResponseContent.setLength(0);
        sbResponseContent.append(content);
        try {
            byte[] data = sbResponseContent.toString().getBytes(UTF_8);
            writeByte(data, needCompress);
        }
        catch (UnsupportedEncodingException e) {
            log.error("UnsupportedEncoding Error:", e);

        }

    }

    public void writeByte(final byte[] data) {

        writeByte(data, false);
    }

    public void writeByte(byte[] data, final boolean needCompress) {

        if (needCompress) {
            try {
                data = GZipUtils.compress(data);
            }
            catch (Exception e) {
                log.error("GZipCompress Error:", e);

            }
        }

        writeResponse(data);

    }

    public void writeGzipData(final String content) {

        try {
            byte[] data = content.getBytes(UTF_8);
            data = GZipUtils.compress(data);
            writeResponse(data);

        }
        catch (Exception e) {
            log.error("Compress Error:", e);
        }

    }

    protected void writeResponse(final byte[] data) {
        writeResponse(HttpResponseStatus.OK, data);
    }

    protected void writeResponse(final HttpResponseStatus status, final String strData) {
        byte[] data = (strData == null ? new byte[]{} : strData.getBytes());
        writeResponse(status, data);
    }

    protected void writeResponse(final HttpResponseStatus status, final byte[] data) {
        // Convert the response content to a ChannelBuffer.
        // copiedBuffer(sbResponseContent.toString().getBytes());

        final FullHttpResponse response = buildResponse(status, data);

         final Channel channel = ctx.channel();
         // Write the response.
         final ChannelFuture future = channel.writeAndFlush(response);

         // Close the connection after the write operation is done if necessary.
         future.addListener(ChannelFutureListener.CLOSE);

        ctx.channel().writeAndFlush(response);

        ctx.channel().close();

    }

    protected FullHttpResponse buildResponse(final HttpResponseStatus status, final byte[] data) {

        pj.dataserver.util.HttpUtil.HttpContentBean bean = beforeBuildResponse(data);

        final ByteBuf buf;
        if (bean != null) {
            buf = Unpooled.wrappedBuffer(bean.content, 0, bean.content.length);
        }
        else {
            buf = Unpooled.wrappedBuffer(data, 0, data.length);
        }

        // Build the response object.
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buf);

        afterBuildResponse(status, response, bean);

        return response;
    }

    protected HttpContentBean beforeBuildResponse(final byte[] data) {
        return null;
    }

    protected void afterBuildResponse(final HttpResponseStatus status,
                                      final FullHttpResponse response,
                                      final HttpContentBean bean) {

        // Decide whether to close the connection or not.
        boolean keepAlive = this.getServer().getConfig().keepAlive
                            && HttpHeaders.isKeepAlive(request);

        boolean close = request.headers()
                               .contains(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE, true)
                        || request.getProtocolVersion().equals(HttpVersion.HTTP_1_0)
                        && !keepAlive;

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, pj.dataserver.util.HttpUtil.TYPE_PLAIN);

        if (!close && keepAlive) {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        else {
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
        }

        // if (!close) {
        if (response.content().readableBytes() > 0) {
            // There's no need to add 'Content-Length' header
            // if this is the last response.
            response.headers().set(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes());
        }

        // Encode the cookie.
        String cookieString = request.headers().get(COOKIE);
        if (cookieString != null) {
            Set<Cookie> cookies = ServerCookieDecoder.LAX.decode(cookieString);
            if (!cookies.isEmpty()) {
                // Reset the cookies if necessary.
                for (Cookie cookie : cookies) {
                    response.headers().add(SET_COOKIE, ServerCookieEncoder.LAX.encode(cookie));
                }
            }
        }

        addServerHeader(response);
    }

    protected void addServerHeader(final FullHttpResponse response) {}

    public void addHttpContent(HttpContent chunk) {
        addHttpContent(getPostData(chunk));
    }

    public void addHttpContent(byte[] data) {
        if (httpContent == null) {
            httpContent = data;
            return;
        }
        byte[] newTmp = new byte[data.length + httpContent.length];
        System.arraycopy(httpContent, 0, newTmp, 0, httpContent.length);
        System.arraycopy(data, 0, newTmp, httpContent.length, data.length);
        httpContent = newTmp;
    }

    public String getClientIp() {
        String clientIP = request.headers().get("X-Forwarded-For");
        if (clientIP == null) {
            InetSocketAddress insocket = (InetSocketAddress) ctx.channel().remoteAddress();
            clientIP = insocket.getAddress().getHostAddress();
        }

        if (clientIP != null) {
            String[] strs = clientIP.split(",");
            if (strs.length > 0) {
                return strs[0].trim();
            }
        }

        return clientIP;
    }

    public String getClientUA() {
        return this.request.headers().get("user-agent");
    }

    public HttpServer getServer() {
        return server;
    }

    public void setServer(HttpServer server) {
        this.server = server;
    }
}
