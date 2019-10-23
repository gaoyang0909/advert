package com.szzt.advert;


import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class MainActivity extends Activity implements View.OnClickListener {
    public static final  String TAG= MainActivity.class.getSimpleName();
    //图片列表
    List<String> imagePathList = new ArrayList<>();
    long preOnClickTime = 0;
    Banner banner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        banner = (Banner) findViewById(R.id.fragment_main_home_banner);
       //设置样式
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR);
        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader(this));
        List<String> imagePathFromSD = getImagePathFromSD();
        if(imagePathFromSD.size()>0){
            //设置图片集合,从/sdcard/某个路径获取
           banner.setImages(imagePathFromSD);
        }else{
            finish();
        }
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.DepthPage);
        //设置自动轮播，默认为true
       banner.isAutoPlay(false);
        //设置轮播时间
        //banner.setDelayTime(5000);

        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        banner.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void onClick(View v) {
        long currenttime = System.currentTimeMillis();
        if(preOnClickTime <= 0){
            preOnClickTime = currenttime;
        }else{
            long diff = currenttime - preOnClickTime;
            if(diff <= 300){
                finish();
            }else{
                preOnClickTime = currenttime;
            }
        }
    }
    private   List<String>  getImagePathFromSD() {

        //判断sdcard是否存在
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //得到Sd卡内image文件夹的路径
            String filePath = Environment.getExternalStorageDirectory().toString() + File.separator + "Music";
           // String  filePath="/storage/emulated/legacy/"+ File.separator+"cpayResource";
            //得到该路径下文件夹下所有的文件
            File fileAll = new File(filePath);
            File[] files = fileAll.listFiles(new FilenameFilter(){
                @Override
                public boolean accept(File dir, String name) {
                    if(name.endsWith(".xml")){
                        return  true;
                    }
                    return false;
                }
            });

            //将所有的文件存入ArrayList中,并过滤所有图片格式的文件
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                    try {
                        //获得DOM解析器的工厂示例
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        //从Dom工厂中获得dom解析器
                        DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
                        //把解析的xml文件读入Dom解析器
                        Document doc = dbBuilder.parse(new FileInputStream(file));
                        //得到文档中名称为advertisement的元素的结点列表
                        NodeList nList = doc.getElementsByTagName("advertisement");
                        //遍历该集合，显示集合中的元素以及子元素的名字
                        for (int a = 0; a < nList.getLength(); a++) {
                            //从advertisement元素开始解析
                            Element element = (Element) nList.item(a);
                            //获取本地图片
                            String path = element.getAttribute("file");
                            //获取URL图片
                            final String url = element.getAttribute("url");
                            File   file1=new File(path);
                            if(file1.exists()){
                                //String replace = path.replace("&", "&amp;");
                                imagePathList.add(path);
                            }
                            if(!TextUtils.isEmpty(url)){
                                //String replace = url.replace("&", "&amp;");
                                imagePathList.add(url);
                            }
                        }
                    } catch (ParserConfigurationException e) {
                        e.printStackTrace();
                        Log.e("SD", "error1:" + e.toString());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.e("SD", "error2:" + e.toString());
                    } catch (SAXException e) {
                        e.printStackTrace();
                        Log.e("SD", "error3:" + e.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("SD", "error4:" + e.toString());
                    }
            }
        } else {
            finish();
            Log.e("SD", "no  have");
        }
        //返回得到的图片列表
        return imagePathList;
  }


}
