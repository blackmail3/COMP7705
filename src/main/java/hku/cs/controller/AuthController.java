package hku.cs.controller;

import cn.hutool.core.lang.UUID;
import cn.hutool.core.map.MapUtil;
import com.google.code.kaptcha.Producer;
import hku.cs.common.lang.Const;
import hku.cs.common.lang.Result;
import hku.cs.entity.User;
import hku.cs.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.Map;

/**
 * Captcha generation class
 * @author xxy
 */
@RestController
@CrossOrigin
public class AuthController extends BaseController{

    @Autowired
    Producer producer;
    @Autowired
    UserService userService;

    @GetMapping("/captcha")
    public Map<Object, Object> captcha() throws IOException {

        String key = UUID.randomUUID().toString();
        String code = producer.createText();

        // for Test
        key = "aaaaa";
        code = "123456";

//        producer.createImage("123");

        System.out.println("key:"+key);
        System.out.println("code:"+code);

        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "jpg", outputStream);

        String str = "data:image/jpeg;base64,";
        System.out.println(str);

        String base64Img = str + Base64.encodeBase64String(outputStream.toByteArray());

        System.out.println("Redis---");
        redisUtil.hset(Const.CAPTCHA_KEY, key, code, 120);
        System.out.println("redis write...");

        return MapUtil.builder()
                .put("token", key)
                .put("captchaImg", base64Img)
                .build();
    }

    /**
     * Interface to get user information
     * @param principal
     * @return
     */
    @GetMapping("/userInfo")
    public Result userInfo(Principal principal) {

        User user = userService.getByUsername(principal.getName());

        return Result.succ(MapUtil.builder()
                .put("id", user.getId())
                .put("username", user.getUsername())
                .put("avatar", user.getAvatar())
                .map()
        );
    }

}