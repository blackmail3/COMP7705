package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONObject;
import hku.cs.common.lang.Result;
import hku.cs.entity.Dataset;
import hku.cs.entity.Task;
import hku.cs.service.DatasetService;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

@RestController
@RequestMapping("/api/v1.0/wordcloud")
@CrossOrigin
public class WordcloudController {
    @Autowired
    DatasetService datasetService;
    @Autowired
    UserService userService;

    @GetMapping("/{dataset_id}/{wordcount}")
    public Result getImg(@PathVariable Long dataset_id, @PathVariable int wordcount) {
        Dataset dataset = datasetService.getById(dataset_id);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        //single input command
//        String runningFile = "/Users/yuanren/Desktop/FinalProj/tool/pycode/single_input.py ";
//        String orgData = "/Users/yuanren/Desktop/FinalProj/tool/SST-2/train.csv ";
//        String wc_savePath = "/Users/yuanren/Desktop/FinalProj/tool/result/result_sc_single.json ";
//        String wordCount = "200 ";
//        String pie_savePath = "/Users/yuanren/Desktop/FinalProj/tool/result/result_pie_single.json ";
//        String command = "python3 " + runningFile + orgData + wc_savePath + wordCount + pie_savePath;
        String runningFile;
        if (dataset.getType().equals("single")) {
            runningFile = "/var/doc/single_input.py ";
        }
        else
            runningFile = "/var/doc/dual_input.py ";
        //dual input command
        String orgData = dataset.getPath() + " ";
        String wordCount = wordcount + " ";
        String path = "/var/doc/dataset/" + user_id + "/" + dataset_id;
        File file = new File(path);
        if (!file.exists())
            file.mkdir();
        String wc_savePath = path + "/result_sc.json ";
        String pie_savePath = path + "/result_pie.json ";
        String command = "python3 " + runningFile + orgData + wc_savePath + wordCount + pie_savePath;

        if (!new File(wc_savePath).exists() || !new File(pie_savePath).exists()) {
            try {
                Process proc = Runtime.getRuntime().exec(command);
                // test for the connection
//            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//            String line = null;
//            while((line = in.readLine()) != null){
//                System.out.println(line);
//            }
//            in.close();
                proc.waitFor();
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        JSONObject json_wc = null;
        JSONObject json_pie = null;
        try (
                InputStream is_wc = Thread.currentThread().getContextClassLoader().getResourceAsStream(wc_savePath);
                InputStream is_pie = Thread.currentThread().getContextClassLoader().getResourceAsStream(pie_savePath);
        ) {
            json_wc = JSONObject.parseObject(IOUtils.toString(is_wc, "utf-8"));
            json_pie = JSONObject.parseObject(IOUtils.toString(is_pie, "utf-8"));
        } catch (Exception e) {
            System.out.println(wc_savePath + " read file error!" + e);
            System.out.println(pie_savePath + " read file error!" + e);
        }
        return Result.succ(
                MapUtil.builder()
                        .put("wc", json_wc)
                        .put("pie", json_pie)
                        .build()
        );
    }
}
