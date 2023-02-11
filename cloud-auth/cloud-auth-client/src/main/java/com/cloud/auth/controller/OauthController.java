package com.cloud.auth.controller;

import com.cloud.common.util.http.OkHttpUtils;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

/**
 * @author liulei
 */
@RestController
@RequestMapping()
public class OauthController {

    // 摘要算法
    String code_challenge_method = "SHA-256";
    String code_verifier = "xx123";

    String grant_type = "authorization_code";
    String client_id = "client1";

    @GetMapping("/index")
    @ResponseBody
    public String msg(String code) {
        String data = OkHttpUtils.builder().url("http://127.0.0.1:8002/oauth2/token")
                // 有参数的话添加参数，可多个
                .addParam("grant_type", grant_type)
                .addParam("client_id", client_id)
                .addParam("code", code)
                .addParam("code_verifier", code_verifier)
                .addParam("redirect_uri", "http://127.0.0.1:8004/index")
                .post(Boolean.FALSE)
                .sync();
        return data;
    }

    @RequestMapping("/oauth")
    public ModelAndView oauth(RedirectAttributes attr , HttpServletResponse response) throws IOException {
        attr.addAttribute("response_type", "code");
        attr.addAttribute("client_id", "client1");
        attr.addAttribute("redirect_uri", "http://127.0.0.1:8004/index");
        attr.addAttribute("scope", "read");
        attr.addAttribute("code_challenge", getCalc(code_verifier, code_challenge_method));
        attr.addAttribute("code_challenge_method", "S256");
        return new ModelAndView("redirect:http://127.0.0.1:8002/oauth2/authorize");
    }

    /**
     * 明文 + 摘要算法，生成 密文
     *
     * @see
     */
    private static String getCalc(String code_verifier, String code_challenge_method) {
        try {
            byte[] bytes = code_verifier.getBytes(StandardCharsets.US_ASCII);
            MessageDigest md = MessageDigest.getInstance(code_challenge_method);
            byte[] digest = md.digest(bytes);
            String code_challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(digest);
            return code_challenge;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}