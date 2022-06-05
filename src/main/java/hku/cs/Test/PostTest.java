package hku.cs.Test;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * @author xxy
 * @version 1.0
 * Test HttpPost for NLPServer connect
 */
public class PostTest {
    public static void main(String[] args) {
        sendPost();
    }

    public static void sendPost(){
        String path = "args.json";
        HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/train");
        //Set request time
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(80000).setConnectTimeout(80000).build();
        post.setConfig(requestConfig);
        try {
            String body = "{\"config_path\":\""
                    + path
                    + "\"}";
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-type", "application/json");
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "UTF-8");

            System.out.println(result);
        } catch (Exception e1) {
            e1.printStackTrace();

        }
    }
}
