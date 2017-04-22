package net.lovexq.seckill.common.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.runtime.RuntimeSchema;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * 序列化和反序列化工具类
 *
 * @author LuPindong
 * @time 2017-04-22 13:23
 */
public class ProtoStuffUtil {

    /**
     * 序列化对象
     *
     * @param obj
     * @param <T>
     * @return
     */
    public static <T> byte[] serialize(T obj) {
        if (obj == null) {
            throw new RuntimeException("序列化对象(" + obj + ")为空!");
        }
        RuntimeSchema<T> schema = RuntimeSchema.createFrom((Class<T>) obj.getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protoStuff = null;
        try {
            protoStuff = ProtostuffIOUtil.toByteArray(obj, schema, buffer);
        } catch (Exception e) {
            throw new RuntimeException("序列化(" + obj.getClass() + ")对象(" + obj + ")发生异常!", e);
        } finally {
            buffer.clear();
        }
        return protoStuff;
    }

    /**
     * 反序列对象
     *
     * @param dataArray
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> T deserialize(byte[] dataArray, Class<T> targetClass) {
        if (dataArray == null || dataArray.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }
        T instance = null;
        try {
            instance = targetClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("反序列化过程中依据类型创建对象失败!", e);
        }
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(targetClass);
        ProtostuffIOUtil.mergeFrom(dataArray, instance, schema);
        return instance;
    }

    /**
     * 序列化列表对象
     *
     * @param objList
     * @param <T>
     * @return
     */
    public static <T> byte[] serializeList(List<T> objList) {
        if (objList == null || objList.isEmpty()) {
            throw new RuntimeException("序列化对象列表(" + objList + ")参数异常!");
        }
        RuntimeSchema<T> schema = RuntimeSchema.createFrom((Class<T>) objList.get(0).getClass());
        LinkedBuffer buffer = LinkedBuffer.allocate(1024 * 1024);
        byte[] protoStuff = null;
        ByteArrayOutputStream bos = null;
        try {
            bos = new ByteArrayOutputStream();
            ProtostuffIOUtil.writeListTo(bos, objList, schema, buffer);
            protoStuff = bos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("序列化对象列表(" + objList + ")发生异常!", e);
        } finally {
            buffer.clear();
            try {
                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return protoStuff;
    }

    /**
     * 反序列花列表对象
     *
     * @param dataArray
     * @param targetClass
     * @param <T>
     * @return
     */
    public static <T> List<T> deserializeList(byte[] dataArray, Class<T> targetClass) {
        if (dataArray == null || dataArray.length == 0) {
            throw new RuntimeException("反序列化对象发生异常,byte序列为空!");
        }
        RuntimeSchema<T> schema = RuntimeSchema.createFrom(targetClass);
        List<T> result = null;
        try {
            result = ProtostuffIOUtil.parseListFrom(new ByteArrayInputStream(dataArray), schema);
        } catch (IOException e) {
            throw new RuntimeException("反序列化对象列表发生IO异常!", e);
        }
        return result;
    }
}
