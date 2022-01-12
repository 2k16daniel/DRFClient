package github._2k16daniel.drfclient;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.slf4j.LoggerFactory;

import github._2k16daniel.drfclient.utils.ServiceExceptions;
import github._2k16daniel.drfclient.utils.StringUtilities;

public abstract class AbstractDRFClient {
    StringUtilities utils = new StringUtilities();
    ObjectMapper mapper;
    HttpClient client;
    DRFRequestInterceptor requestInterceptor;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    // Constructor
    public static DRFClientBuilder build(){
        return new DRFClientBuilder();
    }

    protected AbstractDRFClient(DRFClientBuilder builder){
        this.mapper = builder.mapper;
        this.client = builder.client;
        this.requestInterceptor = builder.intercept;
    }
    
    protected HttpGet absGet(String path) { return new HttpGet(path); }
    protected HttpPost absPost(String path) { return new HttpPost(path); }
    protected HttpPut absHttpPut(String path) { return new HttpPut(path); }
    protected HttpDelete absDelete(String path) { return new HttpDelete(path);}

    public <T> T attachToObject(String dataSource, Class<T> thisClass)throws IOException{
        return mapper.readValue(dataSource, thisClass);
    }

    public <T> List<T> attachJsonArray(String dataSource, TypeReference<List<T>> typeRef)throws IOException{
        return mapper.readValue(dataSource, typeRef);
    }

    public <T> JsonNode jsonToArrayOBJ(List<T> object) throws IOException {
        return mapper.convertValue(object, JsonNode.class);
        }

    public JsonNode toJson(Object jsonObj) throws IOException{
        if (jsonObj instanceof JsonNode){
            return (JsonNode) jsonObj;
        }
        return mapper.valueToTree(jsonObj);
    }

    public byte[] dataAsBytes(HttpResponse dataResponse) throws IOException{
        return IOUtils.toByteArray(dataResponse.getEntity().getContent());
    }

    public HttpResponse start(DRFRequestInterceptor interceptor, HttpRequestBase request)
        throws ClientProtocolException, IOException{
            if (interceptor !=null){
                interceptor.requestInterceptor(request);
        }
        return client.execute(request);
    }

   public HttpResponse start(DRFRequestInterceptor interceptor, HttpRequestBase requestBase, int code_ok) throws ServiceExceptions{
        
        HttpResponse myresponse = null;
        String method = requestBase.getMethod();
        String uri = requestBase.getURI().toString();
        logger.info("Data Sent => " + method + uri);

        try { myresponse = start(interceptor, requestBase); }
        catch (Exception ex){throw new ServiceExceptions(ex, myresponse); }

        int requestStatus = myresponse.getStatusLine().getStatusCode();

        if (requestStatus < 400 && requestStatus >= 200) {
            logger.info("[" + requestStatus + "] HTTP request was sent succesfuly [" + method + "] =>" + uri);
            if (requestStatus == 204) {
                StringBuilder sb = new StringBuilder("HTTP ");
                sb.append("Request was sent succesfuly but no content recieved from server | Status ").append(requestStatus);
                logger.warn(sb.toString());
            }  
        }
        else {
            logger.error(method+" "+ uri + " "+ requestStatus );
            logger.error("URL not found!");
        }

        if (requestStatus > 400 && requestStatus < 500){
            StringBuilder sb = new StringBuilder("BAD REQUEST! [" + requestStatus);
            sb.append("] Expected response is ").append(code_ok);
            throw new ServiceExceptions(sb.toString(), myresponse);
        }
        return myresponse;
   }

   public String paramsCombiner(String path , Map<String, String> param){
    if (param == null){ return path; }
    return path + utils.queryBuilder(param);
    }

    <T extends HttpUriRequest> T contentTypeJson(T uriRequest) {
        uriRequest.addHeader("Content-Type", ContentType.APPLICATION_JSON.toString());
	return uriRequest;
    }
}
