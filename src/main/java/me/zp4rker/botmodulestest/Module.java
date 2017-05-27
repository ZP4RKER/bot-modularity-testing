package me.zp4rker.botmodulestest;

/**
 * @author ZP4RKER
 */
public abstract class Module {

    public final String name;

    public final String version;

    public Module(String name, String version) {
        this.name = name;
        this.version = version;
    }

}
