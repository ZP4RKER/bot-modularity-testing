package com.zp4rker.bots.modularitytest;

import org.slf4j.LoggerFactory;

/**
 * @author ZP4RKER
 */
public abstract class Module {

    private String name, version;

    public Module(String name, String version) {
        this.name = name;
        this.version = version;

        LoggerFactory.getLogger(name).info("Loading module...");
        this.onLoad();
        LoggerFactory.getLogger(name).info("Successfully loaded v" + version);
    }

    public abstract void onLoad();

}
