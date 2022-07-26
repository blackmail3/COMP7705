package hku.cs.util;

import hku.cs.entity.User;
import hku.cs.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

public class PostUtil {
    private String path;
    private String tid;

    @Autowired
    UserService userService;

    public PostUtil(String path, String tid) {
        this.path = path;
        this.tid = tid;
    }

    //tbc: post err
    public String sendPost(Long user_id){
//        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        Long user_id = user.getId();
        String result="";
//        String path = "args.json";
        HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/train");
        //Set request time
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(18000).setConnectTimeout(18000).build();
        post.setConfig(requestConfig);
        try {
            String body = "{\"config_path\":\""
                    + this.path + "\","
                    + "\"task_id\":\""
                    + tid
                    + "\","
                    + "\"user_dir\":\""
                    + "/var/doc/usr" + user_id + "/task/" + tid
                    + "\"" +
                    "}";
//            String body = "{\"config_path\":\""
//                    + "/root/TextCLS/config/args.json" + "\","
//                    + "\"task_id\":\""
//                    + tid
//                    + "\"}";
            System.out.println(body);
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
