package com.by.lizhiyoupin.app.common.utils.storage.sd;

import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.by.lizhiyoupin.app.common.log.LZLog;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import androidx.annotation.Nullable;

/**
 * 实现 {@link SharedPreferences} 的 sd 存储
 */
class StoragePreferences implements SharedPreferences {
    public static final String TAG = StoragePreferences.class.getSimpleName();

    private StorageEditor editor;
    private DocumentBuilderFactory documentBuilderFactory;
    private File file;

    public StoragePreferences(String filePath) {
        file = new File(filePath);
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
    }

    @Override
    public Map<String, ?> getAll() {
        return null;
    }

    @Nullable
    @Override
    public String getString(String key, @Nullable String defValue) {
        String value = getValue(key);
        return value == null ? defValue : value;
    }

    @Nullable
    @Override
    public Set<String> getStringSet(String key, @Nullable Set<String> defValues) {
        return null;
    }

    @Override
    public int getInt(String key, int defValue) {
        String value = getValue(key);
        return value == null ? defValue : Integer.parseInt(value);
    }

    @Override
    public long getLong(String key, long defValue) {
        String value = getValue(key);
        return value == null ? defValue : Long.parseLong(value);
    }

    @Override
    public float getFloat(String key, float defValue) {
        String value = getValue(key);
        return value == null ? defValue : Float.parseFloat(value);
    }

    @Override
    public boolean getBoolean(String key, boolean defValue) {
        String value = getValue(key);
        return value == null ? defValue : Boolean.parseBoolean(value);
    }

    @Override
    public boolean contains(String key) {
        String value = getValue(key);
        return value != null;
    }

    private String getValue(String key) {
        if (TextUtils.isEmpty(key) || file == null || !file.exists()) {
            return null;
        }
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(fis);
            Element element = document.getDocumentElement();
            String tagName = element.getTagName();
            if (!TextUtils.equals("map", tagName)) {
               LZLog.w(TAG, "xml 格式错误：根节点不是 map");
                return null;
            }
            Node node = Doms.getElementByNameAttribute(element, key);
            if (node != null && node.hasAttributes()) {
                NamedNodeMap attributes = node.getAttributes();
                Node valueNode = attributes.getNamedItem("value");
                return valueNode == null ? node.getTextContent() : valueNode.getNodeValue();
            } else {
                return null;
            }
        } catch (Exception e) {
            LZLog.e(TAG, e.getMessage(), e);
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    LZLog.e(TAG, e.getMessage(), e);
                }
            }
        }
        return null;
    }

    @Override
    public Editor edit() {
        if (this.editor == null) {
            this.editor = new StorageEditor();
        }
        return this.editor;
    }

    @Override
    public void registerOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    @Override
    public void unregisterOnSharedPreferenceChangeListener(OnSharedPreferenceChangeListener listener) {
    }

    class StorageEditor implements Editor {
        private Map<String, Object> values = new HashMap<>();
        private Set<String> remove = new HashSet<>();

        @Override
        public Editor putString(String key, @Nullable String value) {
            this.values.put(key, value);
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor putStringSet(String key, @Nullable Set<String> values) {
            return this;
        }

        @Override
        public Editor putInt(String key, int value) {
            this.values.put(key, value);
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor putLong(String key, long value) {
            this.values.put(key, value);
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor putFloat(String key, float value) {
            this.values.put(key, value);
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor putBoolean(String key, boolean value) {
            this.values.put(key, value);
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor remove(String key) {
            this.remove.remove(key);
            return this;
        }

        @Override
        public Editor clear() {
            return this;
        }

        @Override
        public boolean commit() {
            FileOutputStream str = createFileOutputStream(file);
            if (str == null) {
                return false;
            }
            try {
                XmlUtils.writeMapXml(this.values, str);
                return true;
            } catch (XmlPullParserException e) {
                LZLog.e(TAG, e.getMessage(), e);
            } catch (IOException e) {
                LZLog.e(TAG, e.getMessage(), e);
            }finally {
                try {
                    str.close();
                } catch (IOException e) {
                    LZLog.e(TAG, e.getMessage(), e);
                }
            }
            return false;
        }

        private FileOutputStream createFileOutputStream(File file) {
            FileOutputStream str = null;
            try {
                str = new FileOutputStream(file);
            } catch (FileNotFoundException e) {
                File parent = file.getParentFile();
                if (!parent.mkdir()) {
                    Log.e(TAG, "Couldn't create directory for SharedPreferences file " + file);
                    return null;
                }
//                FileUtils.setPermissions(
//                        file.getPath(),
//                        FileUtils.S_IRWXU|FileUtils.S_IRWXG|FileUtils.S_IXOTH,
//                        -1, -1);
                try {
                    str = new FileOutputStream(file);
                } catch (FileNotFoundException e2) {
                    Log.e(TAG, "Couldn't create SharedPreferences file " + file, e2);
                }
            }
            return str;
        }


        @Override
        public void apply() {
            commit();
        }
    }
}
