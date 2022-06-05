package hku.cs.controller;

import hku.cs.service.UserService;
import hku.cs.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
public class FileController
{
    @Autowired
    UserService userService;

    @Autowired
    RedisUtil redisUtil;

    @PostMapping("/upload")
    public String upload(MultipartFile file, HttpServletRequest req)
    {
        // String realPath = req.getSession().getServletContext().getRealPath("/uploadFile/");
//        String format = sdf.format(new Date());
        // File folder = new File(realPath + format);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        String format = user_id+"";
        File folder=new File("D:/dataset/"+format+"/"+"dataset");
        if (!folder.isDirectory())
        {
            if (!folder.mkdirs())
            {
                return "Folder creation failed";
            }
        }
        String oldName = file.getOriginalFilename();
        assert oldName != null;
        String newName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try
        {
            file.transferTo(new File(folder, newName));
//            redisUtil.set("datasetPath_"+user_id,newName);
            return req.getScheme() + "://" + req.getServerName() + ":" + req.getServerPort() + "/files/" + format +"/"+ newName;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "Upload failed";
    }
}