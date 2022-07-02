package hku.cs.controller;

import hku.cs.common.lang.Result;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.Socket;

@RestController
@RequestMapping("/api/v1.0/tensorboard")
@CrossOrigin
public class TensorboardController {
    @Autowired
    UserService userService;

    @GetMapping("/ports/{task_id}")
    public Result CheckPort(@PathVariable Long task_id) {
        Socket skt;
        String host = "localhost";
        for (int i = 6006; i <= 6016; i++) {
            try {
                System.out.println("Check port...");
                skt = new Socket(host, i);
                HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/train");
                //Set request time
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(18000).setConnectTimeout(18000).build();
                post.setConfig(requestConfig);
                try {
                    User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                    Long userid = user.getId();
                    String body = "{\"port\":\""
                            + i + "\","
                            + "\"path\":\"/var/doc/usr" + userid + "/task/"
                            + task_id
                            + "\"}";
                    System.out.println(body);
                    post.setEntity(new StringEntity(body));
                    post.setHeader("Content-type", "application/json");
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

                return Result.succ(i);
            } catch (UnknownError e) {
                System.out.println("Exception occurred" + e);
                break;
            } catch (IOException e) {
            }
        }
        return Result.succ("No port available...");
    }
}
