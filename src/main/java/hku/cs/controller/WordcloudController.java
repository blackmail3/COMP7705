package hku.cs.controller;

import cn.hutool.core.map.MapUtil;
import com.alibaba.fastjson.JSONArray;
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

import java.io.*;
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

    // TODO: 2022/7/7  local test...
    @GetMapping("/gen/{dataset_id}")
    public Result getImg(@PathVariable Long dataset_id) {
        Dataset dataset = datasetService.getById(dataset_id);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();

        String runningFile = "/var/doc/process_input.py ";
        String input_type = ""; // single or dual input
        if (dataset.getType().equals("single")) {
            input_type = "single ";
        } else
            input_type = "dual ";
        String orgData = dataset.getPath() + " ";
        String wordCount = 50 + " ";
        String savePath = "/var/doc/usr" + user_id + "/dataset" + dataset_id;
        File file = new File(savePath);
        if (!file.exists())
            file.mkdir();

        String command = "python3 " + runningFile + input_type + orgData + savePath + wordCount;

        try {
            Process proc = Runtime.getRuntime().exec(command);
            // test for the connection
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Result.succ(savePath);
    }

    @GetMapping("/getjson/{dataset_id}")
    public Result getjson(@PathVariable Long dataset_id){
        Dataset dataset = datasetService.getById(dataset_id);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();

        // TODO: 2022/7/13 Path
        String fileName = "/var/doc/usr" + user_id + "/dataset" + dataset_id;
        File jsonFile = new File(fileName);
        String jsonData = getStr(jsonFile);

        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        JSONArray err = parse.getJSONArray("error_message");
        System.out.println(err);
        JSONArray wc_res = parse.getJSONArray("wc_result");
        JSONArray pie_res = parse.getJSONArray("pie_result");
        JSONArray violin_res = parse.getJSONArray("violin_result");
        System.out.println(pie_res);
        return Result.succ(
                MapUtil.builder()
                        .put("error_message", err)
                        .put("wc_result", wc_res)
                        .put("pie_result", pie_res)
                        .put("violin_result", violin_res)
                        .build()
        );
    }

    public String getStr(File jsonFile) {
        String jsonStr = "";
        try {
            FileReader fileReader = new FileReader(jsonFile);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer sb = new StringBuffer();
            while ((ch = reader.read()) != -1) {
                sb.append((char) ch);
            }
            fileReader.close();
            reader.close();
            jsonStr = sb.toString();
            return jsonStr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
