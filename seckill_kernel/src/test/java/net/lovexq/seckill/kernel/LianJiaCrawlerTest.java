package net.lovexq.seckill.kernel;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.kernel.model.EstateItem;
import net.lovexq.seckill.kernel.repository.EstateItemRepository;
import org.apache.commons.io.FileUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LuPindong on 2017-4-19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class LianJiaCrawlerTest {

    @Autowired
    private EstateItemRepository estateItemRepository;

    @Test
    public void testAll() throws Exception {
        System.out.println("testWRJson()");
        testWRJson();//
        System.out.println("testWR2Json()");
        testWR2Json();//
        System.out.println("testWRJsonList()");
        testWRJsonList();//144,136,129,133,130,121,309,347
        System.out.println("testWR2JsonList()");
        testWR2JsonList();//722,704,886,1451,1415,2612,2504,2604

        System.out.println("testWRProtobuf()");
        testWRProtobuf();//
        System.out.println("testWR2Protobuf()");
        testWR2Protobuf();//
        System.out.println("testWRProtobufList()");
        testWRProtobufList();//63,51,60,52,49,51,50,64
        System.out.println("testWR2ProtobufList()");
        testWR2ProtobufList();//289,169,232,335,335,603,460,495
    }

    // Json
    public long testWRJson() throws IOException {

        // 136
        EstateItem estateItem = estateItemRepository.findByHouseId("GZ0002346888");

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItem.jsonx");
        String data = JSON.toJSONString(estateItem);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + estateItem);

        File fileB = new File("D:\\estateItem.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        EstateItem estateItemB = JSON.parseObject(dataB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemB);

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2Json() throws IOException {

        // 370
        EstateItem estateItem = estateItemRepository.findByHouseId("GZ0002460857");

        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItem.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        EstateItem estateItemB = JSON.parseObject(dataB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemB);

        List<EstateItem> list = new ArrayList<>();
        list.add(estateItem);
        list.add(estateItemB);

        File file = new File("D:\\estateItem.jsonx");
        String data = JSON.toJSONString(list);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + list.size());

        File fileBx = new File("D:\\estateItem.jsonx");
        String dataBx = FileUtils.readFileToString(fileBx, AppConstants.CHARSET_UTF8);
        List<EstateItem> estateItemBxList = JSON.parseArray(dataBx, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemBxList.size());

        long end = System.currentTimeMillis();
        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWRJsonList() throws IOException {

        // 254
        List<EstateItem> estateItemList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemList.jsonx");
        String data = JSON.toJSONString(estateItemList);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + estateItemList.size());

        File fileB = new File("D:\\estateItemList.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        List<EstateItem> estateItemBList = JSON.parseArray(dataB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemBList.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2JsonList() throws IOException {

        // 331
        List<EstateItem> estateItemList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItemList.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        List<EstateItem> estateItemBList = JSON.parseArray(dataB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemBList.size());

        List<EstateItem> list = new ArrayList<>();
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);
        list.addAll(estateItemList);
        list.addAll(estateItemBList);

        File file = new File("D:\\estateItemList.jsonx");
        String data = JSON.toJSONString(list);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + list.size());

        File fileBx = new File("D:\\estateItemList.jsonx");
        String dataBx = FileUtils.readFileToString(fileBx, AppConstants.CHARSET_UTF8);
        List<EstateItem> estateItemBxList = JSON.parseArray(dataBx, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemBxList.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }

    // Protobuf

    public long testWRProtobuf() throws IOException {

        // 77
        EstateItem estateItem = estateItemRepository.findByHouseId("GZ0002346888");

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItem.bytex");
        byte[] dataArray = ProtoStuffUtil.serialize(estateItem);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + estateItem);


        File fileB = new File("D:\\estateItem.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        EstateItem estateItemB = ProtoStuffUtil.deserialize(dataArrayB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemB);

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2Protobuf() throws IOException {

        // 84
        EstateItem estateItem = estateItemRepository.findByHouseId("GZ0002460857");

        long begin = System.currentTimeMillis();

        File fileBx = new File("D:\\estateItem.bytex");
        byte[] dataArrayBx = FileUtils.readFileToByteArray(fileBx);
        EstateItem estateItemBx = ProtoStuffUtil.deserialize(dataArrayBx, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemBx);

        List<EstateItem> list = new ArrayList<>();
        list.add(estateItem);
        list.add(estateItemBx);

        File file = new File("D:\\estateItem.bytex");
        byte[] dataArray = ProtoStuffUtil.serializeList(list);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + list.size());

        File fileB = new File("D:\\estateItem.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItem> estateItemListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemListB.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWRProtobufList() throws IOException {
        // 138

        List<EstateItem> estateItemList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemList.bytex");
        byte[] dataArray = ProtoStuffUtil.serializeList(estateItemList);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + estateItemList.size());

        File fileB = new File("D:\\estateItemList.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItem> estateItemListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemListB.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2ProtobufList() throws IOException {

        List<EstateItem> estateItemList2 = estateItemRepository.findAll();

        // 236
        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItemList.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItem> estateItemListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemListB.size());

        List<EstateItem> list = new ArrayList<>();
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);
        list.addAll(estateItemListB);
        list.addAll(estateItemList2);

        File fileC = new File("D:\\estateItemList.bytex");
        byte[] dataArrayC = ProtoStuffUtil.serializeList(list);
        FileUtils.writeByteArrayToFile(fileC, dataArrayC);
        System.out.println("写入的数据为：" + estateItemListB.size());

        File fileD = new File("D:\\estateItemList.bytex");
        byte[] dataArrayD = FileUtils.readFileToByteArray(fileD);
        List<EstateItem> estateItemListD = ProtoStuffUtil.deserializeList(dataArrayD, EstateItem.class);
        System.out.println("读取的数据为：" + estateItemListD.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }

}