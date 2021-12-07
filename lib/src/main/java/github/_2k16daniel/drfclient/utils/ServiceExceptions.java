package github._2k16daniel.drfclient.utils;
import org.apache.http.HttpResponse;

public class ServiceExceptions extends Exception {
    private final HttpResponse myresponse;

    public HttpResponse response(){
        return myresponse;
    }

    public ServiceExceptions(String message, HttpResponse response){
        super(message);
        myresponse = response;

    }

    public ServiceExceptions(Exception exception , HttpResponse response){
        super(exception);
        myresponse = response;
    }

}
