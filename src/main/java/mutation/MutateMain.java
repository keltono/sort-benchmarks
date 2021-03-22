package mutation;

import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class MutateMain {
    static int cvArg = Opcodes.ASM9;

    public static void main(String[] args) throws IOException {
        if(args.length < 2) {
            System.out.println("Run directory and at least one target directory required!");
            return;
        }
        URL[] urlList = stringsToUrls(args);
        List<String> runFiles = new ArrayList<>();
        String directory = urlList[0].toString().split("[:\\]]")[1];
        File urlFile = new File(directory);
        File[] urlFiles = urlFile.listFiles();
        if(urlFile.isDirectory() && urlFiles != null) {
            for (File f : urlFiles) {
                String fName = f.getName();
                runFiles.add(directory + fName);
            }
        }
        Counter counter = new Counter(Arrays.copyOfRange(urlList, 1, urlList.length));
        Map<String, Map<Mutator, Long>> instanceMap = counter.getMutationInstances();
        Map<Mutator, Long> totalMap = new HashMap<>();
        long sum = 0;
        /*for(Mutator m : Mutator.values()) {
            totalMap.put(m, 0L);
        }*/
        for(int c = 0; c < 32; c++) {
            totalMap.put(Mutator.values()[c], 0L);
        }
        for(String s : instanceMap.keySet()) {
            /*for(Mutator m : instanceMap.get(s).keySet()) {
                totalMap.put(m, instanceMap.get(s).get(m) + totalMap.get(m));
            }*/
            for(int c = 0; c < 32; c++) {
                totalMap.put(Mutator.values()[c], instanceMap.get(s).get(Mutator.values()[c]) + totalMap.get(Mutator.values()[c]));
                sum += instanceMap.get(s).get(Mutator.values()[c]);
            }
        }
        System.out.println(totalMap);
        System.out.println(sum);
        for(String s : instanceMap.keySet()) {
            long exceptions = 0;
            for(Mutator m : instanceMap.get(s).keySet()) {
                if(instanceMap.get(s).get(m) > 0) {
                    System.out.println(m);
                }
                for(int c = 0; c < instanceMap.get(s).get(m); c++) {
                    SeparatedMutationClassLoader smcl = new SeparatedMutationClassLoader(new File(s).toURI().toURL(), m, c, s);
                    try {
                        System.out.println(runFiles);
                        for(String f : runFiles) {
                            Method[] methods = smcl.findClass(s).getMethods();
                            for(Method md : methods) {
                                md.invoke(null, new Object[]{new int[]{2, 1, 0}});
                            }
                        }
                    } catch(Exception e) {
                        e.printStackTrace();
                        exceptions++;
                    }
                }
                System.out.println("Tested " + instanceMap.get(s).get(m) + " of type " + m);
            }
            System.out.println("Caught " + exceptions + " exceptions in class " + s);
        }
    }

    public static URL[] stringsToUrls(String[] paths) throws MalformedURLException {
        URL[] urls = new URL[paths.length];
        for (int i = 0; i < paths.length; i++) {
            urls[i] = new File(paths[i]).toURI().toURL();
        }
        return urls;
    }

    public static byte[] getBytes(String path) throws IOException {
        FileInputStream fis = new FileInputStream(path);
        List<Byte> fileBytes = new ArrayList<>();
        while (fis.available() > 0) {
            fileBytes.add((byte) fis.read());
        }
        fis.close();
        byte[] fB = new byte[fileBytes.size()];
        for (int c = 0; c < fB.length; c++) {
            fB[c] = fileBytes.get(c);
        }
        return fB;
    }

    //TODO use type "getArgumentsAndReturnSizes" to not have to
    /*public static int argsInDescriptor(String desc) {
        String args = desc.split("[()]")[1];
        int toReturn = 0;
        boolean inObjName = false;
        for(char c : args.toCharArray()) {
            if(!inObjName && c == 'L') {
                inObjName = true;
                toReturn++;
            } else if(!inObjName) {
                toReturn++;
            } else if(c == ';') {
                inObjName = false;
            }
        }
        System.out.println("Descriptor " + desc + " has " + toReturn + " args");
        return toReturn;
    }*/
}
