package hku.cs.controller;

import cn.hutool.cache.impl.FIFOCache;
import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hku.cs.common.lang.Result;
import hku.cs.entity.*;
import hku.cs.service.DatasetService;
import hku.cs.service.ModelService;
import hku.cs.service.TaskService;
import hku.cs.service.UserService;
import hku.cs.util.JsonFormatTool;
import hku.cs.util.PostUtil;
import hku.cs.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.spring.web.json.Json;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.GenericDeclaration;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1.0/task")
@CrossOrigin
public class TaskController {
    @Autowired
    TaskService taskService;
    @Autowired
    UserService userService;
    @Autowired
    DatasetService datasetService;
    @Autowired
    ModelService modelService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    RedisTemplate redisTemplate;
    private final static SimpleDateFormat yyMMdd = new SimpleDateFormat("yyMMdd");

    @DeleteMapping("/del/{task_id}")
    public Result del(@PathVariable Long task_id) {
        taskService.removeById(task_id);
        return Result.succ(task_id);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Task task) {
        System.out.println("model:" + task.getModelId());
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String fiveNumber = timeMillis.substring(timeMillis.length() - 8);
        String date = yyMMdd.format(new Date());
        task.setTaskId(Long.parseLong(date + fiveNumber));
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userId = user.getId();
        task.setUserId(userId);
        task.setStatus(0);
        if (task.getDatasetIdTest() != null) {
            task.setTestPath(datasetService.getById(task.getDatasetIdTest()).getPath());
        }
        if (task.getDatasetIdTrain() != null && task.getTaskType() == 0) {
            task.setTrainPath(datasetService.getById(task.getDatasetIdTrain()).getPath());
        }
        System.out.println("AFTER" + task.toString());
        taskService.save(task);
        return Result.succ(task);
    }

    @GetMapping("/detail")
    public Result getDetail(@RequestParam Long task_id, HttpServletRequest request) throws IOException {
        Task task = taskService.getById(task_id);
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long user_id = user.getId();

        System.out.println(task.toString());

        long nowSecond = 0;
        long endSecond = 0;
        try {
            nowSecond = task.getStartTime().toEpochSecond(ZoneOffset.ofHours(0));
            endSecond = task.getEndTime().toEpochSecond(ZoneOffset.ofHours(0));
        } catch (Exception e) {
            System.out.println("Exception:" + "Unfinished...! End time NOT set!");
//            e.printStackTrace();
//            task.setStartTime(LocalDateTime.MIN);
//            task.setEndTime(LocalDateTime.now());
            endSecond = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(0));
        }
        long absSeconds = Math.abs(nowSecond - endSecond);

        System.out.println(nowSecond + "||" + endSecond);
        long s = absSeconds % 60;
        long m = absSeconds / 60 % 60;
        long h = absSeconds / 60 / 60 % 24;
//        long d = absSeconds / 60 / 60 / 24;

        TaskDetail taskDetail = new TaskDetail();
        String duration = h + "H" + m + "M" + s + "S";

        taskDetail.setTrainingTime(duration);
        //...
        String path = "";
        JSONObject parse = new JSONObject();
        switch (task.getTaskType()) {
            // train train_result.json eval_result.json log
            case 0:
                boolean flag = false;
                for (int i = 6006; i <= 6016; i++) {
                    if (redisUtil.get("port" + i + "_" + task_id) != null) {
                        parse.put("tensorboard_port", "http://60.205.197.119:" + i);
                        flag = true;
                        break;
                    }
                }
                if (!flag)
                    parse.put("tensorboard_port", "http://60.205.197.119:" + CheckPort(task_id));
                return Result.succ(
                        MapUtil.builder()
                                .put("start_time", task.getStartTime())
                                .put("end_time", task.getStartTime())
                                .put("duration", duration)
                                .put("detail_train", parse)
//                                .put()
                                .map()
                );
            // predict predict.csv
            case 1:
                path = "/var/doc/usr" + user_id + "/task/" + task_id + "/predict.csv";
                if (!new File("/var/doc/usr" + user_id + "/task/" + task_id).exists()) {
                    new File("/var/doc/usr" + user_id + "/task/" + task_id).mkdir();
                }
                File predict_json = new File(path);
                if (!predict_json.exists()) {
                    path = "/root/TextCLS/data/sample_output/prediction_output/predict.csv";
//                    path = "predict.csv";
                    predict_json = new File(path);
                }
                // table presentation ------------------------------------------
                String runningFile_table = "/root/back-end/pyFile/csv_reader.py ";
                String filepath_table = predict_json.getPath() + " ";
                String result_savepath_table = "/var/doc/usr" + user_id + "/task/" + task_id + "/table_result.json ";
//                if (!new File("/var/doc/usr" + user_id + "/task/" + task_id + "/table_result.json").exists()) {
//
//                }
                String max_columns_table = "10";
                String command_table = "python3 " + runningFile_table + filepath_table + result_savepath_table + max_columns_table;

                System.out.println("command_table:\t" + command_table);
                try {
                    Process proc_table = Runtime.getRuntime().exec(command_table);
                    // test for the connection
                    BufferedReader in_table = new BufferedReader(new InputStreamReader(proc_table.getInputStream()));
                    String line = null;
                    while ((line = in_table.readLine()) != null) {
                        System.out.println(line);
                    }
                    in_table.close();
                    proc_table.waitFor();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }

                String fileName_table = "/var/doc/usr" + user_id + "/task/" + task_id + "/table_result.json";
                File jsonFile_table = new File(fileName_table);

                JSONObject parse_table;

                if (!new File(fileName_table).exists()) {
                    String path_table = "/var/doc/usr1/task/1/predict.csv";
                    String tmp = getStr(new File(path_table));
                    return Result.succ("Parse csv to json failed!");
                } else {
                    String jsonData_table = getStr(jsonFile_table);
                    parse_table = (JSONObject) JSONObject.parse(jsonData_table);
                }
                // ---------------------------------------------------------------
                return Result.succ(
                        MapUtil.builder()
                                .put("start_time", task.getStartTime())
                                .put("end_time", task.getStartTime())
                                .put("duration", duration)
                                .put("detail_predict", parse_table)
                                .map()
                );
            // eval eval_results.json log heatmap.png
            case 2:
                path = "/var/doc/usr" + user_id + "/task/" + task_id + "/eval_results.json";
                File file = new File(path);
                if (!file.exists()) {
                    path = "/root/TextCLS/data/sample_output/eval_output/eval_results.json";
                    // for win test
//                    path = "eval_results.json";
                    file = new File(path);
                }
                String jsonData = getStr(file);
                JSONObject tmp_parse = new JSONObject();
                try {
                    tmp_parse = (JSONObject) JSONObject.parse(jsonData);
                    System.out.println(parse.toJSONString());
                    Object acc = tmp_parse.get("accuracy");
                    Object loss = tmp_parse.get("loss");
                    Object report = tmp_parse.get("report");
                    parse.put("accuracy", acc);
                    parse.put("loss", loss);
                    parse.put("report", report);
                } catch (Exception e) {
                    System.out.println("Exception:");
                    e.printStackTrace();
                    parse = new JSONObject();
                }
                String logpath = "/var/doc/usr" + user_id + "/task/" + task_id + "/log";
                File logfile = new File(logpath);
                if (!logfile.exists()) {
                    logpath = "/root/TextCLS/data/sample_output/prediction_output/log";
//                    logpath = "log";
                    logfile = new File(logpath);
                }

                InetAddress inetAddress = InetAddress.getLocalHost();
                String ip = inetAddress.getHostAddress();
                String heatmapURL = "http://60.205.197.119:8088" + "/getfile" + heatmap(task_id);

                return Result.succ(
                        MapUtil.builder()
                                .put("start_time", task.getStartTime())
                                .put("end_time", task.getStartTime())
                                .put("duration", duration)
                                .put("detail_eval", parse)
//                                .put("log", logfile.getPath())
                                .put("heatmap", heatmapURL)
                                .map()
                );
        }
        return Result.succ(
                MapUtil.builder()
                        .put("start_time", task.getStartTime())
                        .put("end_time", task.getStartTime())
                        .put("duration", duration)
                        .put("detail", new JSONObject().put("err", "task type invalid!"))
                        .map()
        );
    }

    @GetMapping("/get")
    public Result getByNameStatus(@RequestParam @Nullable String name, @RequestParam @Nullable String status) {
        System.out.println("name:\t" + name);
        System.out.println("status:\t" + status);
        if (name == null || name.equals("")) {
            name = "";
        }
        if (status == null || status.equals("")) {
            status = "-1";
        }
        List<Task> list = taskService.getByName(name, Integer.parseInt(status));
        if (list != null) {
            List<Task> res = new ArrayList<>();
            for (int i = list.size() - 1; i >= 0; i--) {
                res.add(list.get(i));
            }
            return Result.succ(res);
//            return Result.succ(list);
        } else
            return Result.succ(new ArrayList<>());
    }

    @GetMapping("/get_train_task")
    public Result getTrainTask() {
        List<Task> list = taskService.getByType(0);
        if (list != null)
            return Result.succ(list);
        else
            return Result.succ(new ArrayList<>());
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

    // FIXME: 2022/7/20 maybe some problem...
    @PutMapping("/start_train")
    @ApiOperation(value = "start_task", notes = "Click 'Start Task' for training.")
    public Result start(@RequestParam String task_id) throws JsonProcessingException {
        //...start training
        //task->model->path
        Task task = taskService.getByTaskId(Long.parseLong(task_id));
        String fullPath = "";
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long user_id = user.getId();
        // FIXME: 2022/7/7
//        if (task.getTaskType().equals("train")) {
        try {
            if (task.getTaskType() == 0) {
//            fullPath
                fullPath = Config2JsonFile_Train(task_id);
            } else { // predict & eval
                fullPath = Config2JsonFile_other(task_id);
            }
//            task.setConfigPath(fullPath);
            PostUtil util = new PostUtil(fullPath, task_id);
            System.out.println("BEFORE SENDING..." + fullPath + " TASK" + task_id);
            String res = util.sendPost(user_id);
            task.setStatus(1);
            task.setStartTime(LocalDateTime.now());
            taskService.updateById(task);
            return Result.succ(res);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.succ(e);
        }
    }


    /**
     * Model + ModelConfig + Task => JSON
     *
     * @param task_id
     * @return
     * @throws JsonProcessingException
     */
//    @GetMapping("/train_task")
    public String Config2JsonFile_Train(String task_id) throws JsonProcessingException {
        TrainJsonBean trainJsonBean = new TrainJsonBean();
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        Task task = taskService.getByTaskId(Long.parseLong(task_id));
        Long dataset_id_train = task.getDatasetIdTrain();
        Long dataset_id_test = task.getDatasetIdTest();
        Long model_id = task.getModelId();
        Model model = modelService.getById(model_id);
        Dataset dataset_train = datasetService.getByDatasetId(dataset_id_train);
        Dataset dataset_test = datasetService.getByDatasetId(dataset_id_test);
//        ModelConfig modelConfig = modelService.getModelConfig(model_id);
        ModelConfig modelConfig = modelService.getModelConfig(model_id);

        // init param
        trainJsonBean.setModel_name_or_path(model.getBasicModel());
        // From model_config
        System.out.println(modelConfig.toString());
        trainJsonBean.setLn_type(modelConfig.getLnType());
        trainJsonBean.setFreeze_layer(modelConfig.getFreeze());
        trainJsonBean.setFreeze("");
        trainJsonBean.setCls_type(modelConfig.getClsType());
        trainJsonBean.setPooler_type(modelConfig.getPoolerType());
        trainJsonBean.setActivation(modelConfig.getActivation());
        System.out.println(modelConfig.toString());

        // From dataset
        System.out.println(dataset_train.toString());
        System.out.println(dataset_test.toString());
        trainJsonBean.setTrain_file(dataset_train.getPath());
        trainJsonBean.setValid_file(dataset_test.getPath());
        trainJsonBean.setTest_file(dataset_test.getPath());
        System.out.println(task.toString());
        // column
        trainJsonBean.setSrc_column1(dataset_train.getInput1());
        trainJsonBean.setTgt_column(dataset_train.getLabel());
        if (dataset_train.getType() == 1) {
            trainJsonBean.setSrc_column2(dataset_train.getInput2());
        }

        // From task
        trainJsonBean.setMax_seq_length(task.getMaxLength());
        trainJsonBean.setPad_to_max_length(false);
        trainJsonBean.setOutput_dir("/var/doc/usr" + user_id + "/task/" + task_id);
        trainJsonBean.setOverwrite_output_dir(true);
        trainJsonBean.setNum_train_epochs(task.getEpoch());
        trainJsonBean.setPer_device_train_batch_size(task.getBatchSize());
        trainJsonBean.setPer_device_eval_batch_size(task.getBatchSize());
        trainJsonBean.setLearning_rate(BigDecimal.valueOf(Double.parseDouble(task.getLearningRate())));
        // fix value
        trainJsonBean.setEvaluation_strategy("epoch");
        trainJsonBean.setLoad_best_model_at_end(true);
        trainJsonBean.setDo_train(true);
        trainJsonBean.setDo_eval(true);
        trainJsonBean.setFp16(true);
        trainJsonBean.setReport_to("tensorboard");
        trainJsonBean.setLogging_dir("/var/doc/usr" + user_id + "/task/" + task_id + "/tensorboard");
        // Converting the Java object into a JSON string
        // Creating Object of ObjectMapper define in Jackson API
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(trainJsonBean);
        // Displaying Java object into a JSON string
        System.out.println("json:" + jsonStr);
        String filename = "model_config_" + task_id;
        String fullPath = createJsonFile(jsonStr, "/var/doc/usr" + user_id + "/model_config", filename);
        return fullPath;
    }

    //    @GetMapping("/other")
    // TODO: 2022/7/7 local test
    public String Config2JsonFile_other(@RequestParam String task_id) throws JsonProcessingException {
        EvalJsonBean evalJsonBean = new EvalJsonBean();
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long user_id = user.getId();
        Task task = taskService.getByTaskId(Long.parseLong(task_id));
        Long model_id = task.getModelId();
        Model model = modelService.getById(model_id);
        Dataset dataset_test = datasetService.getByDatasetId(task.getDatasetIdTest());
        ModelConfig modelConfig = modelService.getModelConfig(model_id);

        System.out.println(model.toString());
        // Task_id == dataset_id_train
//        Task task_train = taskService.getByTaskId(task.getDatasetIdTrain());
        Task task_train = taskService.getByTaskId(task.getDatasetIdTrainTask());
        String output_dir = "/var/doc/usr" + user_id + "/task/" + task_train.getTaskId();
        evalJsonBean.setModel_name_or_path(output_dir);

        // From model_config
        System.out.println(modelConfig.toString());
        evalJsonBean.setLn_type(modelConfig.getLnType());
        evalJsonBean.setFreeze_layer(modelConfig.getFreeze());
        evalJsonBean.setCls_type(modelConfig.getClsType());
        evalJsonBean.setPooler_type(modelConfig.getPoolerType());
        evalJsonBean.setActivation(modelConfig.getActivation());

        // From dataset
        System.out.println(dataset_test.toString());
        evalJsonBean.setTest_file(dataset_test.getPath());
        evalJsonBean.setSrc_column1(dataset_test.getInput1());
        evalJsonBean.setTgt_column(dataset_test.getLabel());

        // From task
        evalJsonBean.setMax_seq_length(task.getMaxLength());
        evalJsonBean.setPad_to_max_length(false);
        evalJsonBean.setOutput_dir("/var/doc/usr" + user_id + "/task/" + task_id);
        evalJsonBean.setOverwrite_output_dir(true);
        evalJsonBean.setPer_device_eval_batch_size(task.getBatchSize());

        // Converting the Java object into a JSON string
        // Creating Object of ObjectMapper define in Jackson API
//        jsonBean.setOutput_dir("/"+jsonBean.getRun_name());
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(evalJsonBean);
        // Displaying Java object into a JSON string
        System.out.println("json:" + jsonStr);
        String filename = "model_config_" + task_id;
        // For win test
        String fullPath = createJsonFile(jsonStr, "/var/doc/usr" + user_id + "/model_config", filename);
        return fullPath;
    }

    /**
     * json file generator
     */
    public static String createJsonFile(String jsonString, String filePath, String fileName) {
//        boolean flag = true;

        String fullPath = filePath + File.separator + fileName + ".json";

        try {
            File file = new File(fullPath);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();

            jsonString = JsonFormatTool.formatJson(jsonString);

            Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            write.write(jsonString);
            write.flush();
            write.close();
        } catch (Exception e) {
//            flag = false;
            e.printStackTrace();
        }

        return fullPath;
    }

    @PutMapping("/stop/{task_id}")
    public Result stop(@PathVariable Long task_id) {
        HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/stop_train");
        //Set request time
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(1800).setConnectTimeout(1800).build();
        post.setConfig(requestConfig);
        try {
            User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            Long userid = user.getId();
            Task t = taskService.getByTaskId(task_id);
            String body = "{\"config_path\":\""
                    + "/var/doc/usr" + user.getId() + "/model_config" + t.getTaskId()
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
            t.setStatus(3);
            taskService.updateById(t);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.succ(task_id);
    }

    public int CheckPort(Long task_id) {
        Socket skt;
        String host = "localhost";
        for (int i = 6006; i <= 6016; i++) {
            try {
                System.out.println("Check port...");
//                skt = new Socket(host, i);
                HttpPost post = new HttpPost("http://127.0.0.1:9000/NLPServer/tensorboard");
//                if (isPortUsing("http://127.0.0.1", i)) {
//                    continue;
//                }
                //set redis cache

                Object o = redisUtil.get("port" + i);
                if (o != null)
                    continue;
                redisTemplate.opsForValue().set("port" + i, i, 600, TimeUnit.SECONDS);
                redisTemplate.opsForValue().set("port" + i + "_" + task_id, i, 600, TimeUnit.SECONDS);
                //Set request time
                RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(900).setConnectTimeout(900).build();
                post.setConfig(requestConfig);
                try {
                    User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
                    Long userid = user.getId();
                    String body;
                    if (new File("/var/doc/usr" + userid + "/task/" + task_id + "/tensorboard").exists()) {
                        body = "{\"port\":\""
                                + i + "\","
                                + "\"user_dir\":\""
                                + "/var/doc/usr" + userid + "/task/" + task_id + "/"
////                            + task_id + "\","
//                            + "/root/TextCLS\","
//                            + "\"task_id\":\""
//                            + task_id
                                + "\"}";
                    } else {
                        body = "{\"port\":\""
                                + i + "\","
                                + "\"user_dir\":\""
//                                + "/var/doc/usr" + userid + "/task/" + task_id + "/tensorboard"
////                            + task_id + "\","
                                + "/root/TextCLS/output\","
                                + "\"task_id\":\""
                                + task_id
                                + "\"}";
                    }
                    System.out.println(body);
                    post.setEntity(new StringEntity(body));
                    post.setHeader("Content-type", "application/json");
                    System.out.println(post.getURI());
                    CloseableHttpClient client = HttpClients.createDefault();
                    CloseableHttpResponse response = client.execute(post);
                    HttpEntity entity = response.getEntity();
                    System.out.println(EntityUtils.toString(entity, "UTF-8"));
                    return 6016;
                } catch (Exception e1) {
                    System.out.println("NOT USE! SOCKET TIME OUT!" + "PORT" + i);
                }
                return i;
            } catch (UnknownError e) {
                System.out.println("Exception occurred" + e);
                break;
            }
        }
        return -1;
    }

    public static boolean isPortUsing(String host, int port) throws UnknownHostException {
        boolean flag = false;
//        InetAddress theAddress = InetAddress.getByName(host);
        try {
            Socket socket = new Socket(host, port);
            flag = true;
        } catch (IOException e) {
        }
        return flag;
    }

    public String heatmap(Long task_id) throws FileNotFoundException {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long user_id = userService.getByUsername(username).getId();
        String path = "/usr" + user_id + "/task/" + task_id + "/heatmap.png";
        File file = new File("/var/doc" + path);
        if (!file.exists()) {
            path = "/usr1/task/1/heatmap.png";
            file = new File("/var/doc" + path);
//            sftp://root@60.205.197.119/var/doc/usr1/task/1/heatmap.png
            // for win test
//            file = new File("D:/0/HKU/proj/heatmap.png");
        }
        byte[] fileByte = IoUtil.readBytes(new FileInputStream(file));
        String encode = "data:image/png;base64," + Base64Encoder.encode(fileByte);
        return path;
    }
}
