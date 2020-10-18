import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;

public class HelloClassLoader extends ClassLoader{
    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        byte[] classFile = getClassFile();
        if(classFile == null) {
            throw new ClassNotFoundException();
        }
        //将错的byte改为正确
        for (int i = 0; i < classFile.length; i++) {
            classFile[i] = (byte)(255 - (int)classFile[i]);
        }
        return defineClass(name,classFile, 0, classFile.length);
    }

    private byte[] getClassFile() {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream bos = null;
        try {
            fileInputStream = new FileInputStream("/Users/wujiajia/IdeaProjects/leetcode/src/main/java/com/whf/study/gk/Hello.xlass");
            bos = new ByteArrayOutputStream();
            byte[] buffer = new byte[4096];
            int readn = 0;
            while ((readn = fileInputStream.read(buffer)) != -1) {
                bos.write(buffer, 0, readn);
            }
            return bos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    public static void main(String[] args) {
        HelloClassLoader hcl = new HelloClassLoader();
        try {
            Class<?> helloClass = hcl.findClass("Hello");
            Method helloMethod = helloClass.getMethod("hello");
            Object o = helloClass.newInstance();
            helloMethod.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
