package hku.cs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import hku.cs.entity.JsonBean;
import hku.cs.util.JsonFormatTool;
import hku.cs.util.PostUtil;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

@RestController
@RequestMapping("/api/v1.0")
@CrossOrigin
@ApiOperation(value = "config_setting", notes = "API for save configs.json file and send post message to algorithm API.")
public class ConfigJsonController {
    @PostMapping("/config")
    public String config(JsonBean jsonBean) throws JsonProcessingException {
        /*
        private String model_name_or_path = "bert-base-uncased";
        private String ln_type = "post";
        private String cls_type = "fc";
        private String pooler_type = "cls";
        private String activation = "gelu";
        private String train_file = "";
        private String valid_file = "";
        private String test_file = "";
        private int max_seq_length = 256;
        private boolean pad_to_max_length = false;
        private String output_dir = "";
        private boolean overwrite_output_dir = true;
        private int num_train_epochs = 10;
        private int per_device_train_batch_size = 128;
        private double learning_rate = 5e-5;
        private String evaluation_strategy = "epoch";
        private boolean load_best_model_at_end = true;
        private boolean do_train = true;
        private boolean do_eval = true;
        private boolean fp16 = true;
        private String report_to = "wandb";
        private String run_name = "";   //Task ID
         */
//        JsonBean jsonBean = new JsonBean();
        // Converting the Java object into a JSON string
        // Creating Object of ObjectMapper define in Jackson API
        jsonBean.setOutput_dir("/"+jsonBean.getRun_name());
        ObjectMapper Obj = new ObjectMapper();
        String jsonStr = Obj.writeValueAsString(jsonBean);
        // Displaying Java object into a JSON string
        System.out.println(jsonStr);
        String filename = "configs";
        createJsonFile(jsonStr,"ModelConfig",filename);
        PostUtil util = new PostUtil("config.json");
        return util.sendPost();
    }
    /**
     * json file generator
     */
    public static boolean createJsonFile(String jsonString, String filePath, String fileName) {
        boolean flag = true;

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
            flag = false;
            e.printStackTrace();
        }

        return flag;
    }

}
