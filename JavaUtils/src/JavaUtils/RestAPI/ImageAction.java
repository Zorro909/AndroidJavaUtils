package JavaUtils.RestAPI;

import java.awt.Image;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import javax.imageio.ImageIO;

import JavaUtils.Parser.ParseObject;
import JavaUtils.UtilHelpers.Cleanable;

public class ImageAction implements Action {

    private static ArrayList<String> uuidnames = new ArrayList<String>();
    private File img;
    private Cleanable<byte[]> idata = new Cleanable<byte[]>(null, 1000*60*10);
    boolean currReading = false;
    
    public ImageAction(Image im){
        UUID uid = UUID.randomUUID();
        while(uuidnames.contains(uid.toString())){
            uid = UUID.randomUUID();
        }
        try {
            //img = File.createTempFile(uid.toString(), ".jpg");
            img = new File(uid.toString() + ".jpg");
            ImageIO.write((RenderedImage) im, "jpg", img);
            new Thread(new Runnable(){

                @Override
                public void run() {
                    readImage();
                }
                
            }).start();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Override
    public ParseObject executeRequest(HashMap<String, String> conf, HashMap<String, String> vars) {
        ParseObject po = new ParseObject("img");
        byte[] s = readImage();
        try {
            po.add("raw", new String(s,"UTF-8"));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        po.add("content_type", "image/jpeg; charset=utf-8");
        return po;
    }

    private byte[] readImage() {
        if(idata.getObject()!=null)return idata.getObject();
        if(currReading){
            System.out.println("Waiting for Reading: " + img.getName());
            while(currReading){
                try {
                    Thread.sleep(200L);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            System.out.println("Finished Waiting: " + img.getName());
            if(idata.getObject()!=null)return idata.getObject();
        }
        System.out.println("Reading File: " + img.getName());
        currReading = true;
        byte[] b = new byte[(int) img.length()];
        try {
            FileInputStream fis = new FileInputStream(img);
            fis.read(b);
            fis.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("Read Image: " + img.getName());
        if(idata.getObject()==null)idata = new Cleanable<byte[]>(b, 1000*60*10);
        currReading = false;
        return b;
    }

    @Override
    public boolean isRaw() {
        return true;
    }

}
