package net.boypika.pikaconfiga.config;

import java.io.*;
import java.util.*;

public class Config {
    private Properties properties = new Properties();
    private File propertiesFile;

    public Config(File file) {
        this.propertiesFile = file;
        if (file.exists()) {
            try {
                this.properties.load(new FileInputStream(file));
            } catch (Exception var3) {
                this.generate();
            }
        } else {
            this.generate();
        }

    }

    public void generate() {
        this.save();
    }

    public void save() {
        try {
            this.properties.store(new FileOutputStream(this.propertiesFile), "Generated By BoyPika Config");
        } catch (Exception var2) {
            this.generate();
        }

    }
    public void addComment(String comment) throws IOException {
        writeComments(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.propertiesFile))), comment);
    }

    public String getString(String string, String string2) {
        if (!this.properties.containsKey(string)) {
            this.properties.setProperty(string, string2);
            this.save();
        }

        return this.properties.getProperty(string, string2);
    }

    public int setInteger(String string, int i) {
        try {
            return Integer.parseInt(this.getString(string, "" + i));
        } catch (Exception var4) {
            this.properties.setProperty(string, "" + i);
            return i;
        }
    }
    public int getInteger(String string){
        return Integer.parseInt(this.getString(string, ""));
    }

    public boolean getBoolean(String string, boolean bl) {
        try {
            return Boolean.parseBoolean(this.getString(string, "" + bl));
        } catch (Exception var4) {
            this.properties.setProperty(string, "" + bl);
            return bl;
        }
    }

    public void setBoolean(String string, boolean bl) {
        this.properties.setProperty(string, "" + bl);
        this.save();
    }
    private static void writeComments(BufferedWriter bw, String comments)
            throws IOException {
        HexFormat hex = HexFormat.of().withUpperCase();
        bw.write("#");
        int len = comments.length();
        int current = 0;
        int last = 0;
        while (current < len) {
            char c = comments.charAt(current);
            if (c > '\u00ff' || c == '\n' || c == '\r') {
                if (last != current)
                    bw.write(comments.substring(last, current));
                if (c > '\u00ff') {
                    bw.write("\\u");
                    bw.write(hex.toHexDigits(c));
                } else {
                    bw.newLine();
                    if (c == '\r' &&
                            current != len - 1 &&
                            comments.charAt(current + 1) == '\n') {
                        current++;
                    }
                    if (current == len - 1 ||
                            (comments.charAt(current + 1) != '#' &&
                                    comments.charAt(current + 1) != '!'))
                        bw.write("#");
                }
                last = current + 1;
            }
            current++;
        }
        if (last != current)
            bw.write(comments.substring(last, current));
        bw.newLine();
    }
}

