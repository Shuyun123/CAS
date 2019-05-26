package net.anumbrella.sso.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import net.anumbrella.sso.utils.CaptchaCodeUtils;
import net.anumbrella.sso.utils.KaptchaCodeUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author anumbrella
 */
@Controller
public class CaptchaController {

    /**
     * 工具类生成captcha验证码路径
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/captcha", produces = "image/png")
    public void captcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        OutputStream out = null;
        try {
            //设置response头信息
            //禁止缓存
            response.setHeader("Cache-Control", "no-cache");
            response.setContentType("image/png");
            //存储验证码到session
            CaptchaCodeUtils.CaptchaCode code = CaptchaCodeUtils.getInstance().getCode();

            //获取验证码code
            String codeTxt = code.getText();
            request.getSession().setAttribute("captcha_code", codeTxt);
            //写文件到客户端
            out = response.getOutputStream();
            byte[] imgs = code.getData();
            out.write(imgs, 0, imgs.length);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 谷歌kaptcha验证码路径
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @GetMapping(value = "/kaptcha", produces = "image/png")
    public void kaptcha(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] captchaChallengeAsJpeg = null;
        DefaultKaptcha captchaProducer = KaptchaCodeUtils.getDefaultKaptcha();
        OutputStream out = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {

            response.setHeader("Cache-Control", "no-store");
            response.setContentType("image/png");

            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            request.getSession().setAttribute("captcha_code", createText);

            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "png", jpegOutputStream);

            //使用response输出流输出图片的byte数组
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();

            out = response.getOutputStream();

            out.write(captchaChallengeAsJpeg);
            out.flush();


        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } finally {
            if (out != null) {
                out.close();
            }
        }
    }

    /**
     * 用于前端ajax校验
     */
    @RequestMapping(value = "/chkCode", method = RequestMethod.POST)
    public void checkCode(String code, HttpServletRequest req, HttpServletResponse resp) {

        //获取session中的验证码
        String storeCode = (String) req.getSession().getAttribute("captcha_code");
        code = code.trim();
        //返回值
        Map<String, Object> map = new HashMap<String, Object>();
        //验证是否对，不管大小写
        if (!StringUtils.isEmpty(storeCode) && code.equalsIgnoreCase(storeCode)) {
            map.put("error", false);
            map.put("msg", "验证成功");
        } else if (StringUtils.isEmpty(code)) {
            map.put("error", true);
            map.put("msg", "验证码不能为空");
        } else {
            map.put("error", true);
            map.put("msg", "验证码错误");
        }
        this.writeJSON(resp, map);
    }

    /**
     * 在SpringMvc中获取到Session
     *
     * @return
     */
    public void writeJSON(HttpServletResponse response, Object object) {
        try {
            //设定编码
            response.setCharacterEncoding("UTF-8");
            //表示是json类型的数据
            response.setContentType("application/json");
            //获取PrintWriter 往浏览器端写数据
            PrintWriter writer = response.getWriter();

            ObjectMapper mapper = new ObjectMapper(); //转换器
            //获取到转化后的JSON 数据
            String json = mapper.writeValueAsString(object);
            //写数据到浏览器
            writer.write(json);
            //刷新，表示全部写完，把缓存数据都刷出去
            writer.flush();

            //关闭writer
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
