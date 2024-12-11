package extensions;

import org.frontier.crypto.AESEncryptor;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.InvocationInterceptor;
import org.junit.jupiter.api.extension.ReflectiveInvocationContext;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

public class CryptoExtension implements InvocationInterceptor {
    public final InputStream inputStream;
    public final AESEncryptor encryptor;

    public CryptoExtension() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(256);
        SecretKey secretKey = keyGenerator.generateKey();

        encryptor = new AESEncryptor(secretKey);

        this.inputStream = Objects.requireNonNull(
                getClass().getClassLoader().getResourceAsStream("test.png"));
    }

    @Override
    public void interceptTestMethod(
            Invocation<Void> invocation,
            ReflectiveInvocationContext<Method> invocationContext,
            ExtensionContext extensionContext) throws Throwable {
        invocation.proceed();
    }
}
