package github._2k16daniel.drfclient;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import github._2k16daniel.drfclient.utils.ServiceExceptions;

import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.commons.io.IOUtils;

public class DRFClient extends AbstractDRFClient {
    private DRFClient(DRFClientBuilder drfbuilder) {
        super(drfbuilder);
    }
    /*  Start of private methods */
    private String retrieve(DRFRequestInterceptor interceptor, String uri, Map<String, String> queryParams, int httpStatus)
            throws IOException, ServiceExceptions {
        HttpGet get = absGet(paramsCombiner(uri, queryParams));
        HttpResponse response = start(interceptor, get, httpStatus);
        String document = null;
        try {
            document = toString(response);
        } catch (IOException e) {
            consume(response);
        }
        return document;
    }

    private void consume(HttpResponse response) throws IOException {
        if (response != null && response.getEntity() != null) {
            EntityUtils.consume(response.getEntity());
        }
    }

    private <T> T retrieve(DRFRequestInterceptor interceptor, String uri, Map<String, String> queryParams,
            Class<T> entityClass, int httpStatus) throws ServiceExceptions, IOException {
        String doc = retrieve(interceptor, uri, queryParams, httpStatus);
        if (doc != null) {
            return attachToObject(doc, entityClass);
        } else {
            return null;
        }
    }

    private <T> List<T> retrieve(DRFRequestInterceptor interceptor, String uri, Map<String, String> queryParams,
            TypeReference<List<T>> type, int httpStatus) throws ServiceExceptions, IOException {
        String content = retrieve(interceptor, uri, queryParams, httpStatus);
        if (content != null) {
            return attachJsonArray(content, type);
        } else {
            return null;
        }
    }
    
    private Header send(DRFRequestInterceptor interceptor, String uri, Object object, int httpStatus)
    throws ServiceExceptions, IOException {
        HttpPost post = contentTypeJson(absPost(uri));
        HttpEntity entity = new StringEntity(toJson(object).toString(), StandardCharsets.UTF_8);
        post.setEntity(entity);
        HttpResponse response = start(interceptor, post, httpStatus);
        consume(response);
        return response.getFirstHeader("Location");
        }

    private void update(DRFRequestInterceptor interceptor, String uri, Object obj, int httpStatus)
    throws ServiceExceptions, IOException {
        HttpPut put = contentTypeJson(absHttpPut(uri));
        HttpEntity entity = new StringEntity(toJson(obj).toString(), StandardCharsets.UTF_8);
        put.setEntity(entity);
        consume(start(interceptor, put, httpStatus));
    }

    private void delete(DRFRequestInterceptor interceptor, String uri, int httpStatus)
            throws ServiceExceptions, IOException {
        HttpDelete delete = absDelete(uri);
        consume(start(interceptor, delete, httpStatus));
    }

    private String toString(HttpResponse dataResponse) throws IOException {
        if (dataResponse == null) {
            return null;
        }
        return IOUtils.toString(dataResponse.getEntity().getContent(), StandardCharsets.UTF_8);
    }

    /**
     * Method used for retrieving data from server.
     * 
     * @param uri : Your base url ex: http://endpoint.io/api/v1/inventory 
     * @param queryParams : A hashmap object, The hashmap will be converted into parameter form
     * ex: /client?id=716772711&form=123123123
     * 
     * @param entityClass : Your POJO Object 
     * @return A POJO object and response code
     * @throws ServiceExceptions : an extended exception class that delivers usefull error log when
     * Exception was triggered
     * @throws IOException
     */
    public <T> T retrieve(String uri, Map<String, String> queryParams, Class<T> entityClass)
            throws ServiceExceptions, IOException {
        return retrieve(null, uri, queryParams, entityClass, 200);
    }

    /**
     * Method used for retrieving data (multiple)
     * @param uri : Your base url ex: http://endpoint.io/api/v1/inventory 
     * @param queryParams : A hashmap object, The hashmap will be converted into parameter form
     * ex: /client?id=716772711&form=123123123
     * @param type : Your list of POJO class 
     * @return : A list of POJO class 
     * @throws ServiceExceptions : an extended exception class that delivers usefull error log when
     * Exception was triggered
     * @throws IOException
     */
    public <T> List<T> retrieve(String uri, Map<String, String> queryParams, TypeReference<List<T>> type)
            throws ServiceExceptions, IOException {
        return retrieve(null, uri, queryParams, type, 200);
    }

    /**
     * Method used for sending data. 
     * @param uri :     Your base url ex: http://endpoint.io/api/v1/inventory 
     * @param object :  Your POJO class, it will automaticaly serialized to JSON format.
     * @return  : A response from a server.
     * @throws ServiceExceptions
     * @throws IOException
     */
    public Header send(String uri, Object object) throws ServiceExceptions, IOException {
        return send(null, uri, object, 201);
    }

    /**
     * Method used for requesting delete
     * @param uri Your base url ex: http://endpoint.io/api/v1/inventory
     * @throws ServiceExceptions an extended exception class that delivers usefull error log when
     * Exception was triggered
     * 
     * @throws IOException
     */
    public void delete(String uri) throws ServiceExceptions, IOException {
        delete(null, uri, 200);
    }

    /**
     * A method that is used for updating a list of data
     * @param uri   : Your base url ex: http://endpoint.io/api/v1/inventory
     * @param data  : Your <List> of POJO class, it will automaticaly serialized to JSON format.
     * @throws ServiceExceptions : an extended exception class that delivers usefull error log when
     * Exception was triggered
     * @throws IOException
     */
    public void update(String uri, List<?> data) throws ServiceExceptions, IOException {
        update(null, uri, data, 200);
    }

    /**
     * A method that is used for updating data
     * @param uri   : Your base url ex: http://endpoint.io/api/v1/inventory
     * @param data  : Your of POJO class, it will automaticaly serialized to JSON format.
     * @throws ServiceExceptions : an extended exception class that delivers usefull error log when
     * Exception was triggered
     * @throws IOException
     */
    public void update(String uri, Object obj) throws ServiceExceptions, IOException {
    update(null, uri, obj, 200);
    }
}