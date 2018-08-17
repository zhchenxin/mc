package top.zhchenxin.mc.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 模型详细数据
 */
public abstract class DetailResource<T extends BaseEntity> implements Resource{
    private T entity;

    @Override
    public Map<String, Object> toMap() {
        Field[] fields= this.entity.getClass().getDeclaredFields();
        Map<String, Object> infoMap = new HashMap<>();
        for (Field field : fields) {
            String fieldName = field.getName();
            infoMap.put(fieldName, getFieldValueByName(fieldName, this.entity));
        }
        return infoMap;
    }

    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter);
            return method.invoke(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public T getEntity() {
        return entity;
    }

    public void setEntity(T entity) {
        this.entity = entity;
    }
}
