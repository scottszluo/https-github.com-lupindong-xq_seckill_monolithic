package net.lovexq.seckill.kernel;

import com.alibaba.fastjson.JSON;
import net.lovexq.seckill.common.utils.ProtoStuffUtil;
import net.lovexq.seckill.common.utils.constants.AppConstants;
import net.lovexq.seckill.kernel.model.EstateItemModel;
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
        EstateItemModel estateItemModel = estateItemRepository.findByHouseId("GZ0002346888");

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemModel.jsonx");
        String data = JSON.toJSONString(estateItemModel);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + estateItemModel);

        File fileB = new File("D:\\estateItemModel.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        EstateItemModel estateItemModelB = JSON.parseObject(dataB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelB);

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2Json() throws IOException {

        // 370
        EstateItemModel estateItemModel = estateItemRepository.findByHouseId("GZ0002460857");

        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItemModel.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        EstateItemModel estateItemModelB = JSON.parseObject(dataB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelB);

        List<EstateItemModel> list = new ArrayList<>();
        list.add(estateItemModel);
        list.add(estateItemModelB);

        File file = new File("D:\\estateItemModel.jsonx");
        String data = JSON.toJSONString(list);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + list.size());

        File fileBx = new File("D:\\estateItemModel.jsonx");
        String dataBx = FileUtils.readFileToString(fileBx, AppConstants.CHARSET_UTF8);
        List<EstateItemModel> estateItemModelBxList = JSON.parseArray(dataBx, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelBxList.size());

        long end = System.currentTimeMillis();
        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWRJsonList() throws IOException {

        // 254
        List<EstateItemModel> estateItemModelList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemModelList.jsonx");
        String data = JSON.toJSONString(estateItemModelList);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + estateItemModelList.size());

        File fileB = new File("D:\\estateItemModelList.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        List<EstateItemModel> estateItemModelBList = JSON.parseArray(dataB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelBList.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2JsonList() throws IOException {

        // 331
        List<EstateItemModel> estateItemModelList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItemModelList.jsonx");
        String dataB = FileUtils.readFileToString(fileB, AppConstants.CHARSET_UTF8);
        List<EstateItemModel> estateItemModelBList = JSON.parseArray(dataB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelBList.size());

        List<EstateItemModel> list = new ArrayList<>();
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);
        list.addAll(estateItemModelList);
        list.addAll(estateItemModelBList);

        File file = new File("D:\\estateItemModelList.jsonx");
        String data = JSON.toJSONString(list);
        FileUtils.write(file, data, AppConstants.CHARSET_UTF8);
        System.out.println("写入的数据为：" + list.size());

        File fileBx = new File("D:\\estateItemModelList.jsonx");
        String dataBx = FileUtils.readFileToString(fileBx, AppConstants.CHARSET_UTF8);
        List<EstateItemModel> estateItemModelBxList = JSON.parseArray(dataBx, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelBxList.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }

    // Protobuf

    public long testWRProtobuf() throws IOException {

        // 77
        EstateItemModel estateItemModel = estateItemRepository.findByHouseId("GZ0002346888");

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemModel.bytex");
        byte[] dataArray = ProtoStuffUtil.serialize(estateItemModel);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + estateItemModel);


        File fileB = new File("D:\\estateItemModel.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        EstateItemModel estateItemModelB = ProtoStuffUtil.deserialize(dataArrayB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelB);

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2Protobuf() throws IOException {

        // 84
        EstateItemModel estateItemModel = estateItemRepository.findByHouseId("GZ0002460857");

        long begin = System.currentTimeMillis();

        File fileBx = new File("D:\\estateItemModel.bytex");
        byte[] dataArrayBx = FileUtils.readFileToByteArray(fileBx);
        EstateItemModel estateItemModelBx = ProtoStuffUtil.deserialize(dataArrayBx, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelBx);

        List<EstateItemModel> list = new ArrayList<>();
        list.add(estateItemModel);
        list.add(estateItemModelBx);

        File file = new File("D:\\estateItemModel.bytex");
        byte[] dataArray = ProtoStuffUtil.serializeList(list);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + list.size());

        File fileB = new File("D:\\estateItemModel.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItemModel> estateItemModelListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelListB.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWRProtobufList() throws IOException {
        // 138

        List<EstateItemModel> estateItemModelList = estateItemRepository.findAll();

        long begin = System.currentTimeMillis();

        File file = new File("D:\\estateItemModelList.bytex");
        byte[] dataArray = ProtoStuffUtil.serializeList(estateItemModelList);
        FileUtils.writeByteArrayToFile(file, dataArray);
        System.out.println("写入的数据为：" + estateItemModelList.size());

        File fileB = new File("D:\\estateItemModelList.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItemModel> estateItemModelListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelListB.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }


    public long testWR2ProtobufList() throws IOException {

        List<EstateItemModel> estateItemModelList2 = estateItemRepository.findAll();

        // 236
        long begin = System.currentTimeMillis();

        File fileB = new File("D:\\estateItemList.bytex");
        byte[] dataArrayB = FileUtils.readFileToByteArray(fileB);
        List<EstateItemModel> estateItemModelListB = ProtoStuffUtil.deserializeList(dataArrayB, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelListB.size());

        List<EstateItemModel> list = new ArrayList<>();
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);
        list.addAll(estateItemModelListB);
        list.addAll(estateItemModelList2);

        File fileC = new File("D:\\estateItemList.bytex");
        byte[] dataArrayC = ProtoStuffUtil.serializeList(list);
        FileUtils.writeByteArrayToFile(fileC, dataArrayC);
        System.out.println("写入的数据为：" + estateItemModelListB.size());

        File fileD = new File("D:\\estateItemList.bytex");
        byte[] dataArrayD = FileUtils.readFileToByteArray(fileD);
        List<EstateItemModel> estateItemModelListD = ProtoStuffUtil.deserializeList(dataArrayD, EstateItemModel.class);
        System.out.println("读取的数据为：" + estateItemModelListD.size());

        long end = System.currentTimeMillis();

        long time = end - begin;
        System.out.println(time);

        return time;
    }

}