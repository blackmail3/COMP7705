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

    public Result getImg(@PathVariable Long dataset_id) {
        Dataset dataset = datasetService.getById(dataset_id);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();

        String runningFile = "/root/back-end/pyFile/process_input.py ";
        String input_type = ""; // single or dual input
        if (dataset.getType() == 0) {
            input_type = "single ";
        } else
            input_type = "dual ";
        String orgData = dataset.getPath() + " ";
        String wordCount = "50";
        String savePath = "/var/doc/usr" + user_id + "/jsonfile" + dataset_id;
        File file = new File(savePath);
        if (!file.exists())
            file.mkdir();
        savePath += "/word_result.json ";
        System.out.println(orgData);
        String command = "python3 " + runningFile + input_type + orgData + savePath + wordCount;

        // table presentation
        String runningFile_table = "/root/back-end/pyFile/csv_reader.py ";
        String filepath_table = dataset.getPath() + " ";
        String result_savepath_table = "/var/doc/usr" + user_id + "/jsonfile" + dataset_id + "/table_result.json ";
        String max_columns_table = "10";
        String command_table = "python3 " + runningFile_table + filepath_table + result_savepath_table + max_columns_table;

        System.out.println("command:\t" + command);
        System.out.println("command_table:\t" + command_table);

        try {
            Process proc = Runtime.getRuntime().exec(command);
            Process proc_table = Runtime.getRuntime().exec(command_table);
            // test for the connection
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            BufferedReader in_table = new BufferedReader(new InputStreamReader(proc_table.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            while ((line = in_table.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
            in_table.close();
            proc_table.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return Result.succ(
                MapUtil.builder()
                        .put("words", savePath)
                        .put("table", result_savepath_table)
                        .map()
        );
    }

    // FIXME: 2022/7/19 generate
    @GetMapping("/getjson/{dataset_id}")
    public Result getjson(@PathVariable Long dataset_id) {
        Dataset dataset = datasetService.getById(dataset_id);
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();

        String fileName = "/var/doc/usr" + user_id + "/jsonfile" + dataset_id + "/word_result.json";
        File jsonFile = new File(fileName);
        String jsonData = getStr(jsonFile);

        if (!jsonFile.exists()){
            try{
                getImg(dataset_id);
            }catch(Exception e){
                System.out.println("Exception:");
                e.printStackTrace();
                return Result.succ(
                        MapUtil.builder()
                                .put("error_message", "Invalid Dataset, please check!")
                                .put("wc_result", new JSONArray())
                                .put("pie_result", new JSONArray())
                                .put("violin_result", new JSONArray())
                                .put("table_result", new JSONArray())
                                .map()
                );
            }
        }

        JSONObject parse = (JSONObject) JSONObject.parse(jsonData);
        if (parse == null) {
            return Result.succ(
                    MapUtil.builder()
                            .put("error_message", "Data NOT Found")
                            .put("wc_result", new JSONArray())
                            .put("pie_result", new JSONArray())
                            .put("violin_result", new JSONArray())
                            .put("table_result", new JSONArray())
                            .map()
            );
        }
        JSONArray err = parse.getJSONArray("error_message");
        System.out.println(err);
        if (err == null)
            err = new JSONArray();
        JSONArray wc_res = parse.getJSONArray("wc_result");
        JSONArray pie_res = parse.getJSONArray("pie_result");
        JSONArray violin_res = parse.getJSONArray("violin_result");
        if (wc_res == null)
            wc_res = new JSONArray();
        if (pie_res == null)
            pie_res = new JSONArray();
        if (violin_res == null)
            violin_res = new JSONArray();
        System.out.println(pie_res);

        //table
        String fileName_table = "/var/doc/usr" + user_id + "/jsonfile" + dataset_id + "/table_result.json";
        File jsonFile_table = new File(fileName_table);
        String jsonData_table = getStr(jsonFile_table);

        JSONObject parse_table = (JSONObject) JSONObject.parse(jsonData_table);

        System.out.println(pie_res);
        return Result.succ(
                MapUtil.builder()
                        .put("error_message", err)
                        .put("wc_result", wc_res)
                        .put("pie_result", pie_res)
                        .put("violin_result", violin_res)
                        .put("table_result", parse_table)
                        .map()
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
