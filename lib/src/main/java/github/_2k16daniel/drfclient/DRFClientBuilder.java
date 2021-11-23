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
    public Class<? extends DRFClient> DRFClass;
    
    DRFClientBuilder(){ /* empty constructor */ }

    public DRFClientBuilder httpclient(HttpClient myclient){
        this.client = myclient;
        return this;
    }

    public DRFClientBuilder objMapper(ObjectMapper mapper){
        this.mapper = mapper;
        return this;
    }

    public DRFClientBuilder requestInterceptor (DRFRequestInterceptor intercept){
        this.intercept = intercept;
        return this;
    }

    public DRFClientBuilder DRFClientCLass(Class<? extends DRFClient> DRFClass){
        this.DRFClass = DRFClass;
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

    private <T extends DRFClient> T buildRestCLient(DRFClientBuilder builder, Class<T> DRFClientCLass){
        try{
            Constructor<T> constructThis = DRFClientCLass.getDeclaredConstructor(DRFClientCLass);
            constructThis.setAccessible(true);
            return constructThis.newInstance(builder);
        }
        catch (Exception error){
            throw new RuntimeException(error);
        }
    }
}
