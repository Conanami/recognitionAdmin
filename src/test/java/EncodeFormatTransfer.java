import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class EncodeFormatTransfer
{
    private Logger log = LoggerFactory.getLogger(this.getClass());

    public static String DefaultSrcEncodeFormat = "GBK";
    public static String DefaultDestEncodeFormat = "UTF-8";
    public static String UnsupportedEncodingExceptionError = "编码格式错误！";
    public static String FileNotFoundExceptionError = "文件不存在！";
    public static String IOExceptionError = "文件读写错误！";
    public static String IsUtf8File = "文件是UTF-8编码格式！";
    public static String IsNotUtf8File = "文件不是UTF-8编码格式！";

    public static String readFile(String path,String encodeFormat)
    {
        if((encodeFormat==null || encodeFormat.equals("")))
        {
            if(isUTF8File(path))
            {
                encodeFormat = DefaultDestEncodeFormat;
            }
            else
            {
                encodeFormat = DefaultSrcEncodeFormat;
            }
        }

        try
        {
            String context = "";
            InputStreamReader isr;
            isr = new InputStreamReader(new FileInputStream(path),encodeFormat);
            BufferedReader br = new BufferedReader(isr);
            String line;

            while((line = br.readLine()) != null)
            {
                context += line + "\r\n";
                System.out.println(line);
            }

            br.close();

            return context;
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            System.out.println(UnsupportedEncodingExceptionError);
            e.printStackTrace();
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            System.out.println(FileNotFoundExceptionError);
            e.printStackTrace();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            System.out.println(IOExceptionError);
            e.printStackTrace();
        };

        return "";
    }

    public static boolean isUTF8File(String path)
    {
        return false;
//        try
//        {
//            File file = new File(path);
//            CharsetPrinter detector = new CharsetPrinter();
//            String charset = detector.guessEncoding(file);
//
//            if(charset.equalsIgnoreCase(DefaultDestEncodeFormat))
//            {
//                System.out.println(IsUtf8File);
//                return true;
//            }
//        }
//        catch (FileNotFoundException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            System.out.println(FileNotFoundExceptionError);
//        }
//        catch (IOException e)
//        {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//            System.out.println(IOExceptionError);
//        }
//
//        System.out.println(IsNotUtf8File);
//        return false;
    }

    public static String transfer(String context,String encodeFormat)
    {
        if(encodeFormat==null || encodeFormat.equals(""))
            encodeFormat = DefaultDestEncodeFormat;

        try
        {
            byte[] content = context.getBytes();

            String result = new String(content,encodeFormat);
            return result;
        }
        catch (UnsupportedEncodingException e)
        {
            // TODO Auto-generated catch block
            System.out.println(UnsupportedEncodingExceptionError);
            e.printStackTrace();
        }

        return "";
    }

    public static void writeFile(String context,String path,String destEncode)
    {
        File file = new File(path);
        if(file.exists())
            file.delete();
        BufferedWriter  writer;

        try
        {
            FileOutputStream fos = new FileOutputStream(path,true);
            writer = new BufferedWriter(new OutputStreamWriter(fos, destEncode));
            writer.append(context);
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println(IOExceptionError);
            e.printStackTrace();
        }
    }

    public static void writeFile(String context,String path)
    {
        File file = new File(path);
        if(file.exists())
            file.delete();
        Writer  writer;

        try
        {
            writer = new FileWriter(file, true);
            writer.append(context);
            writer.close();
        }
        catch (IOException e)
        {
            System.out.println(IOExceptionError);
            e.printStackTrace();
        }
    }

    public static void transfer(String srcPath,String destPath,String srcEncode,String destEncode)
    {
        if(destPath==null || destPath.equals(""))
            destPath = srcPath;

        String context = readFile(srcPath,srcEncode);

        context = transfer(context,destEncode);
        writeFile(context,destPath,destEncode);
    }

    public static void transfer(String srcPath,String destPath,String destEncode)
    {
        if(true != isUTF8File(srcPath))
        {
            transfer(srcPath,destPath,DefaultSrcEncodeFormat,destEncode);
        }
    }

    public List<File> listFiles(File file){
        List<File> list = new ArrayList<>();
        if (file.isDirectory()){
            for(File fl:file.listFiles()){
                list.addAll(listFiles(fl));
            }
        }else {
            list.add(file);
        }
        return list;
    }

    @Test
    public void test(){
        File filedir = new File("C:\\Users\\boshu\\Downloads\\11公开接口\\9录音\\PhoneFilter\\src");
        List<File> list = new ArrayList<>();
        list.addAll(listFiles(filedir));
        for (File fl : list){
            String filepath = fl.getAbsolutePath();
            if (filepath.endsWith(".java")){
                log.info("fl:"+filepath);
                transfer(filepath,filepath,"UTF-8");
            }
        }
    }

    public static void main(String args[])
    {
        String path1 = args[0];

        transfer(path1,path1,"UTF-8");
    }
}
