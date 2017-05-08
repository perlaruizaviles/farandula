package com.nearsoft.farandula.utilities;

import java.util.Map;

/**
 * Created by pruiz on 5/7/17.
 */
public class MapsNestedHelper {

    public static Object getValueOf(Object stringObjectMap, String keyName) {

        return getValueOf(stringObjectMap, keyName, Object.class);
    }

    public static  <T> T getValueOf(Object stringObjectMap, String keyName, Class<T> type) {
        int indexSeparator = keyName.indexOf(".");
        if (indexSeparator == -1) {
            Map<String, Object> theMap = (Map<String, Object>) stringObjectMap;

            return type.cast(theMap.get(keyName));
        } else {
            String currentKey = keyName.substring(0, indexSeparator);
            String newKeyPath = keyName.substring(indexSeparator + 1, keyName.length());
            Object currentMap = ((Map<String, Object>) stringObjectMap).get(currentKey);
            return getValueOf(currentMap, newKeyPath, type);
        }

    }
}
