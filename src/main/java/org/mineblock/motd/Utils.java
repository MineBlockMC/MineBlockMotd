package org.mineblock.motd;

import net.kyori.adventure.text.minimessage.MiniMessage;

public class Utils {
    public static String makeCenter(String line){
        int size = (45 - MiniMessage.miniMessage().stripTags(line).length()) / 2;
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i++){
            sb.append(" ");
        }
        sb.append(line);
        return sb.toString();
    }
}
