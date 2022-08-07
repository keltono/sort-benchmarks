package phosphor;

import java.io.File;

import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;
import org.junit.runner.Request;
import org.junit.runner.Runner;

public class PhosphorTestDriver {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: /path/to/phosphor-instrumented-jdk/ " + PhosphorTestDriver.class + " TEST_CLASS TEST_METHOD");
            System.exit(1);
        }
        String testClassName = args[0];
        String testMethodName = args[1];

        try {
            Class<?> testClass =
                    java.lang.Class.forName(testClassName, true, ClassLoader.getSystemClassLoader());
            Request testRequest = Request.method(testClass, testMethodName);
            Runner testRunner = testRequest.getRunner();
            JUnitCore junit = new JUnitCore();

            junit.addListener(new TextListener(System.out));

            junit.run(testRunner);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }

    }
}
