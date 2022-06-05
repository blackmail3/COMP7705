package hku.cs.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class PostUtil {
    private String path;

    public PostUtil(String path) {
        this.path = path;
    }

    public String sendPost(){
        String result="";
//        String path = "args.json";
        HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/train");
        //Set request time
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(18000000).setConnectTimeout(18000000).build();
        post.setConfig(requestConfig);
        try {
            String body = "{\"config_path\":\""
                    + this.path
                    + "\"}";
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-type", "application/json");
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity, "UTF-8");
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return result;
    }
}
