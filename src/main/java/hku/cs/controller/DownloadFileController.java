package hku.cs.controller;

import cn.hutool.core.lang.copier.SrcToDestCopier;
import hku.cs.common.lang.Result;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

// TODO: 2022/7/12 click download button and download the log file
@RestController
//@RequestMapping("/api/v1.0/download")
@CrossOrigin
public class DownloadFileController {
    @Autowired
    UserService userService;
    @Autowired
    TaskService taskService;

    @GetMapping("/download/{task_id}")
    @ResponseBody
    public Result download(HttpServletRequest request, HttpServletResponse response, @RequestParam Long task_id) {
//        String filePath = "D:\\0\\HKU\\proj\\var\\doc\\usr1\\model_config\\model_config_15.json";
//        String fileName = "model_config_15.json";
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        String filePath = "/var/doc/usr" + user_id + "/task/" + task_id + "/log";
        String fileName = user_id + "_" + task_id + "_" + "log";
        try (
                InputStream is = this.getClass().getResourceAsStream(filePath);
                OutputStream os = response.getOutputStream();
        ) {
            byte[] bytes = StreamUtils.copyToByteArray(is);
            response.reset();
            response.setContentType("application/octet-stream");
            response.addHeader("Content-Disposition", "attachment;filename=" + new String(fileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            response.addHeader("Content-Length", "" + bytes.length);
            os.write(bytes);
            os.flush();
            return Result.succ("success");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succ("Download file error...");
        }
    }

}
