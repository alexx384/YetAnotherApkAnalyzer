package property;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class SourcePropertiesLoader {
    private static final String METHOD_PROPERTIES_RESOURCE = "/method.properties";
    private static final String IMPORT_PROPERTIES_RESOURCE = "/import.properties";
    private static final String CONSTRUCTOR_PROPERTIES_RESOURCE = "/constructor.properties";

    /**
     * Loads properties from {@code METHOD_PROPERTIES_RESOURCE} and return count of loaded properties
     * Example property:
     * String=toUpperCase,charAt,toLowerCase
     *
     * @param classMethodMap map which contains [type name : method name : index] mapping
     * @param methodNameSet set of possible method names
     * @return count of loaded method names or 0
     */
    public static int loadMethodProperties(Map<String, Map<String, Integer>> classMethodMap,
                                           Set<String> methodNameSet) {
        if (classMethodMap == null || methodNameSet == null) {
            return 0;
        }
        int propertyCounter = 0;
        try (InputStream stream = SourcePropertiesLoader.class.getResourceAsStream(METHOD_PROPERTIES_RESOURCE)) {

            Properties prop = new Properties();
            prop.load(stream);
            for (Map.Entry<Object, Object> element : prop.entrySet()) {
                String value = (String) element.getValue();
                String[] methodNames = value.split(",");
                HashMap<String, Integer> methodNameMap = new HashMap<>(methodNames.length);
                for (String methodName : methodNames) {
                    methodNameMap.put(methodName, propertyCounter++);
                    methodNameSet.add(methodName);
                }
                String className = (String) element.getKey();
                classMethodMap.put(className, methodNameMap);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return propertyCounter;
    }

    /**
     * Loads properties from {@code IMPORT_PROPERTIES_RESOURCE} and return count of loaded properties
     * Example property:
     * Properties=java.io.File,java.io.DataInputStream,java.io.DataOutputStream
     *
     * @param importMap map which contains [type name : index] mapping
     * @return count of loaded method names or 0
     */
    public static int loadImportProperties(Map<String, Integer> importMap) {
        return loadSimpleProperties(importMap, IMPORT_PROPERTIES_RESOURCE);
    }

    /**
     * Loads properties from {@code CONSTRUCTOR_PROPERTIES_RESOURCE} and return count of loaded properties
     * Example property:
     * Properties=String,StringBuilder,StringBuffer
     *
     * @param constructorMap map which contains [type name : index] mapping
     * @return count of loaded method names or 0
     */
    public static int loadConstructorProperties(Map<String, Integer> constructorMap) {
        return loadSimpleProperties(constructorMap, CONSTRUCTOR_PROPERTIES_RESOURCE);
    }

    private static int loadSimpleProperties(Map<String, Integer> map, String resourceFileName) {
        if (map == null) {
            return 0;
        }
        int propertyCounter = 0;
        try (InputStream stream = SourcePropertiesLoader.class.getResourceAsStream(resourceFileName)) {

            Properties prop = new Properties();
            prop.load(stream);
            String[] properties = prop.getProperty("Properties").split(",");
            for (String methodName : properties) {
                map.put(methodName, propertyCounter++);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return propertyCounter;
    }
}
