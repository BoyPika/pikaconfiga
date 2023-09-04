package net.boypika.pikaconfiga.config;

import net.boypika.pikaconfiga.util.ISO_8859_1;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
            this.storea(new FileOutputStream(this.propertiesFile));
        } catch (Exception var2) {
            this.generate();
        }

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
    public void storea(OutputStream out)
            throws IOException
    {
        store0(new BufferedWriter(new OutputStreamWriter(out, ISO_8859_1.INSTANCE)),
                true);
    }

    private void store0(BufferedWriter bw, boolean escUnicode)
            throws IOException {
        bw.newLine();
        synchronized (this) {
            for (Map.Entry<Object, Object> e : entrySet()) {
                String key = (String)e.getKey();
                String val = (String)e.getValue();
                key = saveConvert(key, true, escUnicode);
                val = saveConvert(val, false, escUnicode);
                bw.write(key + "=" + val);
                bw.newLine();
            }
        }
        bw.flush();
    }
    private String saveConvert(String theString,
                               boolean escapeSpace,
                               boolean escapeUnicode) {
        int len = theString.length();
        int bufLen = len * 2;
        if (bufLen < 0) {
            bufLen = Integer.MAX_VALUE;
        }
        StringBuilder outBuffer = new StringBuilder(bufLen);
        HexFormat hex = HexFormat.of().withUpperCase();
        for(int x=0; x<len; x++) {
            char aChar = theString.charAt(x);
            if ((aChar > 61) && (aChar < 127)) {
                if (aChar == '\\') {
                    outBuffer.append('\\'); outBuffer.append('\\');
                    continue;
                }
                outBuffer.append(aChar);
                continue;
            }
            switch(aChar) {
                case ' ':
                    if (x == 0 || escapeSpace)
                        outBuffer.append('\\');
                    outBuffer.append(' ');
                    break;
                case '\t':outBuffer.append('\\'); outBuffer.append('t');
                    break;
                case '\n':outBuffer.append('\\'); outBuffer.append('n');
                    break;
                case '\r':outBuffer.append('\\'); outBuffer.append('r');
                    break;
                case '\f':outBuffer.append('\\'); outBuffer.append('f');
                    break;
                case '=':
                case ':':
                case '#':
                case '!':
                    outBuffer.append('\\'); outBuffer.append(aChar);
                    break;
                default:
                    if (((aChar < 0x0020) || (aChar > 0x007e)) & escapeUnicode ) {
                        outBuffer.append("\\u");
                        outBuffer.append(hex.toHexDigits(aChar));
                    } else {
                        outBuffer.append(aChar);
                    }
            }
        }
        return outBuffer.toString();
    }
    private transient volatile ConcurrentHashMap<Object, Object> map;
    public Set<Map.Entry<Object, Object>> entrySet() {
        return Collections.synchronizedSet(new EntrySet(map.entrySet()));
    }
    private static class EntrySet implements Set<Map.Entry<Object, Object>> {
        private Set<Map.Entry<Object,Object>> entrySet;

        private EntrySet(Set<Map.Entry<Object, Object>> entrySet) {
            this.entrySet = entrySet;
        }

        @Override public int size() { return entrySet.size(); }
        @Override public boolean isEmpty() { return entrySet.isEmpty(); }
        @Override public boolean contains(Object o) { return entrySet.contains(o); }
        @Override public Object[] toArray() { return entrySet.toArray(); }
        @Override public <T> T[] toArray(T[] a) { return entrySet.toArray(a); }
        @Override public void clear() { entrySet.clear(); }
        @Override public boolean remove(Object o) { return entrySet.remove(o); }

        @Override
        public boolean add(Map.Entry<Object, Object> e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(Collection<? extends Map.Entry<Object, Object>> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsAll(Collection<?> c) {
            return entrySet.containsAll(c);
        }

        @Override
        public boolean equals(Object o) {
            return o == this || entrySet.equals(o);
        }

        @Override
        public int hashCode() {
            return entrySet.hashCode();
        }

        @Override
        public String toString() {
            return entrySet.toString();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            return entrySet.removeAll(c);
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            return entrySet.retainAll(c);
        }

        @Override
        public Iterator<Map.Entry<Object, Object>> iterator() {
            return entrySet.iterator();
        }
    }
}

