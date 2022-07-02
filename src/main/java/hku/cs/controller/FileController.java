package hku.cs.controller;

import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController {
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/dataset_upload")
    public String upload(@RequestBody MultipartFile file, HttpServletRequest req) {
        // String realPath = req.getSession().getServletContext().getRealPath("/uploadFile/");
//        String format = sdf.format(new Date());
        // File folder = new File(realPath + format);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();

        // Linux
        File folder = new File("/var/doc/usr" + user_id + "/dataset");
        //Win
//        File folder=new File("D:/FileDir/"+format+"/"+"dataset");

        if (!folder.isDirectory()) {
            if (!folder.mkdirs()) {
                return "Folder creation failed";
            }
        }
        String oldName = file.getOriginalFilename();
        assert oldName != null;
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            file.transferTo(new File(folder, newName));
            //redis--cache
            redisUtil.set("datasetPath_" + user_id, folder + "/" + newName);
//            return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/files/" + format +"/"+ newName;
            return folder + "/" + newName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "Upload failed";
    }
}