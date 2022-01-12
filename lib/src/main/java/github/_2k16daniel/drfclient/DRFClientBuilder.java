package github._2k16daniel.drfclient;



import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.lang.reflect.Constructor;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
public class DRFClientBuilder {

    public HttpClient client;
    public ObjectMapper mapper;
    public DRFRequestInterceptor intercept;
    Class<? extends DRFClient> DRFClass;
    
    DRFClientBuilder(){ /* empty constructor */ }

    public DRFClientBuilder putInterceptor(DRFRequestInterceptor interceptor) {
        this.intercept = interceptor;
        return this;
        }

    public DRFClient builder(){

        if (DRFClass == null){
            DRFClass = DRFClient.class;
        }
        if (mapper == null){
            mapper = new ObjectMapper();
            mapper.setSerializationInclusion(Include.NON_NULL);
        }
        if (client == null){
            client = HttpClientBuilder.create().useSystemProperties().build();
        }
        return buildRestCLient(this, DRFClass);
    }

    private DRFClient buildRestCLient(DRFClientBuilder builder, Class DRFClientCLass){
        try{
            Constructor constructThis = DRFClientCLass.getDeclaredConstructor(DRFClientBuilder.class);
            constructThis.setAccessible(true);
            return (DRFClient) constructThis.newInstance(builder);
        }
        catch (Exception error){
            throw new RuntimeException(error);
        }
    }
}
