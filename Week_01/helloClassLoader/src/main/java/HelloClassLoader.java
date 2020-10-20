import java.io.IOException;
import java.util.Objects;

public class HelloClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        Class hello = new HelloClassLoader().findClass("Hello");
        hello.getMethod("hello").invoke(hello.newInstance());

        //Jdk11
        //hello.getMethod("hello").invoke(hello.getDeclaredConstructor().newInstance());

        //Result: Hello, classLoader!
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        try {

            byte[] encodeBytes = Objects.requireNonNull(ClassLoader.getSystemResourceAsStream("Hello.xlass")).readAllBytes();
            byte[] decodeBytes = new byte[encodeBytes.length];

            for (int i = 0; i < encodeBytes.length; i++) {
                decodeBytes[i] = (byte) (255 - encodeBytes[i]);
            }

            return defineClass(name, decodeBytes, 0, decodeBytes.length);
        } catch (IOException e) {
            e.printStackTrace();
            return super.findClass(name);
        }

    }
}
