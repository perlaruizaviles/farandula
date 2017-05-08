package com.nearsoft.farandula.utilities;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created by pruiz on 5/7/17.
 */
public class NestedMapsHelper {

    public static Object getValueOf(Object stringObjectMap, String keyName) {

        return getValueOf(stringObjectMap, keyName, Object.class);
    }

    public static  <T> T getValueOf(Object stringObjectMap, String keyName, Class<T> type) {
        int indexSeparator = keyName.indexOf(".");
        if (indexSeparator == -1) {
            Map<String, Object> theMap = (Map<String, Object>) stringObjectMap;
            if ( type.getName().equals("java.lang.String") ){
                return (T) theMap.get(keyName).toString();
            }else {
                return type.cast(theMap.get(keyName));
            }
        } else {
            String currentKey = keyName.substring(0, indexSeparator);
            String newKeyPath = keyName.substring(indexSeparator + 1, keyName.length());
            Object currentMap = ((Map<String, Object>) stringObjectMap).get(currentKey);
            return getValueOf(currentMap, newKeyPath, type);
        }

    }
}
