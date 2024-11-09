package org.mineblock.motd;

import com.google.gson.annotations.SerializedName;

import java.util.Collections;
import java.util.List;

public class Config {
    @SerializedName("motd")
    public Motd motd;
    public static class Motd {
        @SerializedName("enable")
        public boolean enable = true;
        @SerializedName("line1")
        public String line1 = "&6&lMine&f&lBlock&r &a纯净生存服务器 &c[1.17.1]";
        @SerializedName("line2")
        public String line2 = "&a&l高版本生存社区服务器&r &7| &6&l欢迎新玩家！";
    }

    @SerializedName("sample")
    public Sample sample;
    public static class Sample {
        @SerializedName("enable")
        public boolean enable = false;
        @SerializedName("list")
        public List<String> list = Collections.singletonList("在线: %onlineplayers%/%maxplayers%");
    }

    @SerializedName("protocol")
    public Protocol protocol;
    public static class Protocol {
        @SerializedName("enable")
        public boolean enable = false;
        @SerializedName("name")
        public String name = "MineBlock";
        @SerializedName("minimum")
        public int minimum = 0;
    }

    @SerializedName("maintenance")
    public Maintenance maintenance;
    public static class Maintenance {
        @SerializedName("enable")
        public boolean enable = false;
        @SerializedName("protocol")
        public String protocol = "&c维护模式";
        @SerializedName("sample")
        public String sample = "&c服务器正在进行维护！";
        @SerializedName("version")
        public int version = 1;
    }
}
