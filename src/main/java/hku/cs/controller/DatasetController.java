package hku.cs.controller;

import hku.cs.common.lang.Result;
import hku.cs.entity.Dataset;
import hku.cs.entity.User;
import hku.cs.service.DatasetService;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1.0/dataset")
@CrossOrigin
public class DatasetController {
    @Autowired
    UserService userService;
    @Autowired
    DatasetService datasetService;

    private final static SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");

    @PostMapping("/add_new_dataset")
    public Result add(@RequestBody Dataset dataSet){
        User user = userService.getByUsername((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Long userid = user.getId();
        dataSet.setUserid(userid);
        String timeMillis = String.valueOf(System.currentTimeMillis());
        String fiveNumber = timeMillis.substring(timeMillis.length() - 6);
        String date = yyyyMMdd.format(new Date());

//        dataSet.setDatasetId(Long.parseLong(date+fiveNumber));
        dataSet.setDatasetGroupId(Long.parseLong(date+fiveNumber));
        datasetService.save(dataSet);

        System.out.println(dataSet.getDatasetGroupId());
        return Result.succ(dataSet);
    }

    @GetMapping("/list")
    public Result list(){
        List<Dataset> list = datasetService.getByuserId();
        return Result.succ(list);
    }
}
