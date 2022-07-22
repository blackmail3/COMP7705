package hku.cs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hku.cs.common.lang.Result;
import hku.cs.entity.*;
import hku.cs.service.*;
import hku.cs.util.JsonFormatTool;
import hku.cs.util.PostUtil;
import hku.cs.util.RedisUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.time.LocalDateTime;

//@RestController
//@RequestMapping("/api/v1.0/train")
//@CrossOrigin
public class TrainController {
    @Autowired
    UserService userService;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    ModelConfigService modelConfigService;
    @Autowired
    ModelService modelService;
    @Autowired
    DatasetService datasetService;
    @Autowired
    TaskService taskService;

    @PostMapping("/start")
    @ApiOperation(value = "start_task", notes = "Click 'Start Task' for training.")
    public Result start(@RequestParam String task_id) throws JsonProcessingException {
        //...start training
        //task->model->path
        Task task = taskService.getByTaskId(Long.parseLong(task_id));
        String fullPath = "";
        // FIXME: 2022/7/7
//        if (task.getTaskType().equals("train")) {
        if (task.getTaskType() == 0) {
//            fullPath
            fullPath = Config2JsonFile_Train(task_id);
        } else { // predict & eval
            fullPath = Config2JsonFile_other(task_id);
        }
        PostUtil util = new PostUtil(fullPath, task_id);
        String res = util.sendPost();
        task.setStatus(1);
        task.setStartTime(LocalDateTime.now());
        return Result.succ(res);
    }

    /**
     * Model + ModelConfig + Task => JSON
     *
     * @param task_id
     * @return
     * @throws JsonProcessingException
     */
//    @GetMapping("/train_task")
    // TODO: 2022/7/7 local test
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
        ModelConfig modelConfig = modelService.getModelConfig(model_id);

        trainJsonBean.setModel_name_or_path(model.getModelName());
        trainJsonBean.setLn_type(modelConfig.getLnType());
        trainJsonBean.setFreeze_layer(modelConfig.getFreeze());
        trainJsonBean.setFreeze("");
        trainJsonBean.setCls_type(modelConfig.getClsType());
        trainJsonBean.setPooler_type(modelConfig.getPoolerType());
        trainJsonBean.setActivation(modelConfig.getActivation());
        trainJsonBean.setTrain_file(dataset_train.getPath());
        /// FIXME: 2022/7/7 File...
        trainJsonBean.setValid_file(dataset_test.getPath());
        trainJsonBean.setTest_file(dataset_test.getPath());
        System.out.println(task.toString());
        // FIXME: 2022/7/7 confirm...
        trainJsonBean.setSrc_column1(dataset_train.getInput1());
        trainJsonBean.setTgt_column(dataset_train.getLabel());
        if (dataset_train.getType() == 1) {
            trainJsonBean.setSrc_column2(dataset_train.getInput2());
        }
        trainJsonBean.setMax_seq_length(task.getMaxLength());
        trainJsonBean.setPad_to_max_length(false);
        trainJsonBean.setOutput_dir("/var/doc/usr" + user_id + "/task/" + task_id);
        trainJsonBean.setOverwrite_output_dir(true);
        trainJsonBean.setNum_train_epochs(task.getEpoch());
        trainJsonBean.setPer_device_train_batch_size(task.getBatchSize());
        trainJsonBean.setPer_device_eval_batch_size(task.getBatchSize());
        trainJsonBean.setLearning_rate(task.getLearningRate());
        trainJsonBean.setEvaluation_strategy("epoch");
        trainJsonBean.setLoad_best_model_at_end(true);
        trainJsonBean.setDo_train(true);
        trainJsonBean.setDo_eval(true);
        trainJsonBean.setFp16(true);
        trainJsonBean.setReport_to("tensorboard");
        // Converting the Java object into a JSON string
        // Creating Object of ObjectMapper define in Jackson API
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(trainJsonBean);
        // Displaying Java object into a JSON string
        System.out.println("json:" + jsonStr);
        String filename = "model_config_" + task_id;
        // For win test
        String fullPath = createJsonFile(jsonStr, "/var/doc/usr" + user_id + "/model_config/", filename);
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

        evalJsonBean.setModel_name_or_path(model.getModelName());
        evalJsonBean.setLn_type(modelConfig.getLnType());
        evalJsonBean.setFreeze_layer(modelConfig.getFreeze());
        evalJsonBean.setCls_type(modelConfig.getClsType());
        evalJsonBean.setPooler_type(modelConfig.getPoolerType());
        evalJsonBean.setActivation(modelConfig.getActivation());
        evalJsonBean.setTest_file(dataset_test.getPath());
        evalJsonBean.setSrc_column1(dataset_test.getInput1());
        evalJsonBean.setTgt_column(dataset_test.getLabel());
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
        String fullPath = createJsonFile(jsonStr, "/var/doc/usr" + user_id + "/model_config/", filename);
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

}
