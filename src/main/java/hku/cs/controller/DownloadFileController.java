package hku.cs.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import hku.cs.common.lang.Result;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

// TODO: 2022/7/12 click download button and download the log file
@RestController
@RequestMapping("/api/v1.0/download")
@CrossOrigin
public class DownloadFileController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping("/log/{task_id}")
    @ResponseBody
    public Result download(HttpServletRequest request, HttpServletResponse response, @PathVariable Long task_id) {
//        String filePath = "D:\\0\\HKU\\proj\\var\\doc\\usr1\\model_config\\model_config_15.json";
//        String fileName = "model_config_15.json";
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        String filePath = "/var/doc/usr" + user_id + "/task/" + task_id + "/log";
        if (!new File(filePath).exists()){
            filePath = "/var/doc/usr1/task/1/log";
        }
        String fileName = user_id + "_" + task_id + "_" + "log";
        try (
                InputStream is = this.getClass().getResourceAsStream(filePath);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] bytes = StreamUtils.copyToByteArray(is);
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.addHeader("Content-Length", "" + bytes.length);
            os.write(bytes);
            os.flush();
            return Result.succ("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succ("Download file error...");
        }
    }

//    @GetMapping("/heatmap/{task_id}")
    @ResponseBody
    public Result heatmap(@PathVariable Long task_id) throws FileNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        File file = new File("/var/doc/usr" + user_id + "/task/" + task_id + "/heatmap.png");
        byte[] fileByte = IoUtil.readBytes(new FileInputStream(file));
        String encode = "data:image/png;base64," + Base64Encoder.encode(fileByte);
        return Result.succ(encode);
    }

}
