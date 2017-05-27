package me.zp4rker.botmodulestest;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.hooks.AnnotatedEventManager;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

/**
 * @author ZP4RKER
 */
public class Main {

    public static void main(String[] args) {
        try {
            String token = args[0];

            JDA jda = new JDABuilder(AccountType.BOT)
                    .setToken(token)
                    .setEventManager(new AnnotatedEventManager())
                    .buildBlocking();

            File modulesDir = new File(getDirectory().getPath() + "/modules");
            if (!modulesDir.exists()) {
                modulesDir.mkdir();
            }

            URLClassLoader cl;

            File[] modules = modulesDir.listFiles((dir, name) -> name.endsWith(".jar"));

            for (File file : modules) {
                cl = new URLClassLoader(new URL[]{new URL("file://" + file.getPath())}, Main.class.getClassLoader());
                List<Class> classes = new ArrayList<>();
                for (String className : getClasseNames(file.getPath())) {
                    classes.add(cl.loadClass(className));
                }
                for (Class c : classes) {
                    if (Module.class.isAssignableFrom(c)) {
                        Method m = c.getDeclaredMethod("onLoad", JDA.class);
                        m.invoke(c, jda);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static File getDirectory() {
        return new File(Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()).getParentFile();
    }

    public static List<String> getClasseNames(String jarPath) {
        List<String> classes = new ArrayList<>();

        try {
            JarInputStream jarFile = new JarInputStream(new FileInputStream(jarPath));
            JarEntry jarEntry;

            while (true) {
                jarEntry = jarFile.getNextJarEntry();
                if (jarEntry == null) {
                    break;
                }
                if (jarEntry.getName().endsWith(".class")) {
                    classes.add(jarEntry.getName().replaceAll("/", "\\.").replace(".class", ""));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return classes;
    }

}
