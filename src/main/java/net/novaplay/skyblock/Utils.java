package net.novaplay.skyblock;

import cn.nukkit.Server;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

public class Utils {

    public static String printSeconds(int seconds) {
        int m = seconds / 60;
        int s = seconds % 60;
        return ((m < 10 ? "0" : "") + m + ":" + (s < 10 ? "0" : "") + s);
    }

    public static File getIslandPath(String id) {
        return new File(Main.getInstance().getDataFolder() + "/islands/" + id + ".json");
    }

    public static String genIslandId() {
        //UUID.randomUUID();
        return "a" + System.nanoTime() + "-" + new Random().nextInt(9999);
    }

    public static String createPositionString(Position position) {
        return position.getLevel().getName() + ", " + position.getX() + ", " + position.getY() + ", " + position.getZ();
    }

    public static Position parsePosition(String string) {
        try {
            String[] args = string.split(",");
            if (args.length == 4) {
                Level level = Server.getInstance().getLevelByName(args[0]);
                if (level != null) {
                    return new Position(Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]), level);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void addWorld(String id) {
        File from = new File(Server.getInstance().getDataPath() + "/sb_base/SBWorldBackup");
        File to = new File(Server.getInstance().getDataPath() + "/worlds/" + id);

        try {
            FileUtils.copyDirectory(from, to);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteWorld(String id) {
        try {
            File directory = new File(Server.getInstance().getDataPath() + "/worlds/" + id);
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}