package com.keith;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.*;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ATest {


    @Test
    public void test() {
        BigDecimal bd = new BigDecimal("12.141");
        long l  = bd.setScale( 0, BigDecimal.ROUND_UP ).longValue(); // 向上取整
        BigDecimal b=new BigDecimal(45.45);
        int a = b.intValue();
        System.out.println(a);
        System.out.println(l);
    }

    /**
     * 获取30天记录
     */
   /* @Test
    public void test() {
        Map<String,Object> map=new HashMap<>();
        SimpleDateFormat sj = new SimpleDateFormat("yyyy-MM-dd");
        Date date=new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        for (int i=1;i<=30;i++){
            calendar.add(calendar.DATE, -1);
            //System.out.println("前天：" + sj.format(calendar.getTime()));
            map.put( sj.format(calendar.getTime()),"0.0");
        }

        List<ShopCouponsRecordResultType> list=shopCouponsRecordDao.selectBySumMoney("10000");
        for (ShopCouponsRecordResultType resultType:list){
            map.put(resultType.getDay(),resultType.getSumMoney());
        }
        TreeMap<String,Object> treeMap=new TreeMap(map);
        System.out.println(treeMap);

    }*/

    /**
     * 生成二维码的测试类
     * @throws Exception
     */
    //@Test
  /* public void qrCodeTest() throws Exception {

        // 存放在二维码中的内容
        String text = "10000";
        // 嵌入二维码的图片路径
        String imgPath = "D:/img/1.jpg";
        // 生成的二维码的路径及名称
        String destPath = "D:/img/1.jpg";
        Long fileName = System.currentTimeMillis();
        String save = "zkzb/user/" + DateUtil.Date2Str(new Date(), "yyyyMMdd") + "/" + fileName + ".jpg";
        //生成二维码
        QRCodeUtil.encode(text, null, save, true);
        // 解析二维码
        String str = QRCodeUtil.decode(destPath);
        // 打印出解析出的内容
        System.out.println(str);
        File file = new File(save);
        //FileInputStream is = new FileInputStream(file);
        byte[] bytes1 = getBytesFromFile(file);
        //ByteArrayOutputStream bos = new ByteArrayOutputStream(fis);
        //BASE64Decoder decoder = new BASE64Decoder();
        //Long fileName = System.currentTimeMillis();
        //String save = "zkzb/user/" + DateUtil.Date2Str(new Date(), "yyyyMMdd") + "/" + fileName + ".jpg";
        //byte[] bytes1 = decoder.decodeBuffer("");
        ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
        S3Utils.getInstance().putObjectContent(bais, save, bytes1.length);

        String url= "https://zhikelive.oss-cn-hangzhou.aliyuncs.com" + "/" +save;
        System.out.println(url);

   }*/

    // 返回一个byte数组
    public static byte[] getBytesFromFile(File file) throws IOException {
        InputStream is = new FileInputStream(file);
        // 获取文件大小
        long length = file.length();
        if (length > Integer.MAX_VALUE) {
        // 文件太大，无法读取
            throw new IOException("File is to large "+file.getName());
        }
        // 创建一个数据来保存文件数据
        byte[] bytes = new byte[(int)length];
        // 读取数据到byte数组中
        int offset = 0;
        int numRead = 0;
        while (offset < bytes.length
                && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
            offset += numRead;
        }
        // 确保所有数据均被读取
        if (offset < bytes.length) {
            throw new IOException("Could not completely read file "+file.getName());
        }
        // Close the input stream and return bytes
        is.close();
        return bytes;
    }



}
