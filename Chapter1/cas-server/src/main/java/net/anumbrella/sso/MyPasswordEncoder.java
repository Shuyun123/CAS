package net.anumbrella.sso;

import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author anumbrella
 */
public class MyPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence charSequence) {
        // charSequence为输入的用户密码
        return charSequence.toString();
    }

    @Override
    public boolean matches(CharSequence charSequence, String str) {
        // 当encode方法返回不为null时，matches方法才会调用，charSequence为encode返回的字符串
        // str字符串为数据库中密码字段返回的值
        String encodeStr = charSequence.toString() + "aa";
        if (encodeStr.equals(str)) {
            return true;
        }
        return false;
    }
}
