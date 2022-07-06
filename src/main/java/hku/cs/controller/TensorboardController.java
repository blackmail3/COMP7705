package hku.cs.controller;

import hku.cs.common.lang.Result;
import hku.cs.entity.Task;
import hku.cs.entity.User;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
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
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

@RestController
@RequestMapping("/api/v1.0/tensorboard")
@CrossOrigin
public class TensorboardController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping("/open/{task_id}")
    public Result CheckPort(@PathVariable Long task_id) {
        Socket skt;
        String host = "localhost";
        for (int i = 6006; i <= 6016; i++) {
            try {
                System.out.println("Check port...");
//                skt = new Socket(host, i);
                HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/tensorboard");
                if (isPortUsing("http://127.0.0.1", i)){
                    continue;
                }
                //Set request time
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1800).setConnectTimeout(1800).build();
                post.setConfig(requestConfig);
                try {
                    User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                    Long userid = user.getId();
                    String body = "{\"port\":\""
                            + i + "\","
                            + "\"user_dir\":\""
//                            + "/var/doc/usr" + userid + "/task/"
//                            + task_id + "\","
                            + "/root/TextCLS\","
                            + "\"task_id\":\""
                            + task_id
                            + "\"}";
                    post.setEntity(new StringEntity(body));
                    post.setHeader("Content-type", "application/json");
                    System.out.println(body);
                    System.out.println(post.getURI());
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                    return Result.succ(EntityUtils.toString(entity, "UTF-8"));
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

    @GetMapping("/stop/{task_id}")
    public Result Pause(@PathVariable Long task_id) {
        HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/stop_train");
        //Set request time
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1800).setConnectTimeout(1800).build();
        post.setConfig(requestConfig);
        try {
            User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Long userid = user.getId();
            String body = "{\"config_path\":\""
                    + "\","
                    + "\"user_dir\":\""
                    + "/var/doc/usr" + userid + "/task/"
                    + task_id + "\","
                    + "\"task_id\":\""
                    + task_id
                    + "\"}";
            System.out.println(body);
            post.setEntity(new StringEntity(body));
            post.setHeader("Content-type", "application/json");
            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            System.out.println(EntityUtils.toString(entity, "UTF-8"));
            // FIXME: 2022/7/6 task status...
            Task t = taskService.getByTaskId(task_id);
            t.setStatus(3);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.succ(task_id);
    }

    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
//        InetAddress theAddress = InetAddress.getByName(host);
        try{
            Socket socket = new Socket(host, port);
            flag = true;
        } catch (IOException e) {
        }
        return flag;
    }
}
