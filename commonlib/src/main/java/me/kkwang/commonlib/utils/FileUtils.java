package me.kkwang.commonlib.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by kw on 2016/2/18.
 */
public class FileUtils {
    private static final String TAG = FileUtils.class.getSimpleName();

    public static void writeFile(InputStream in, File file) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        try {
            FileOutputStream out = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 128];
            int len = -1;
            while ((len = in.read(buffer)) != -1)
                out.write(buffer, 0, len);
            out.flush();
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readAssetsFile(String file, Context context) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = context.getResources().getAssets().open(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    public static String readFileToString(File file) {
        StringBuffer sb = new StringBuffer();
        try {
            InputStream is = new FileInputStream(file);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String readLine = null;
            while ((readLine = reader.readLine()) != null) {
                sb.append(readLine);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        String content = sb.toString();
        Logger.d(TAG, String.format("read file's content = %s", content.length() >= 150 ? content.substring(0, 150) : content));
        return sb.toString();
    }

    public static byte[] readFileToBytes(File file) {
        try {
            return readStreamToBytes(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readStreamToBytes(InputStream in) throws Exception {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 8];
        int length = -1;
        while ((length = in.read(buffer)) != -1) {
            out.write(buffer, 0, length);
        }
        out.flush();
        byte[] result = out.toByteArray();
        in.close();
        out.close();
        return result;
    }

    public static boolean writeFile(File file, String content) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(content.getBytes());
            out.flush();

            Logger.d(TAG, String.format("write file's content = %s", content.length() >= 150 ? content.substring(0, 150) : content));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
        }

        return true;
    }

    public static boolean writeFile(File file, byte[] bytes) {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        FileOutputStream out = null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        try {
            out = new FileOutputStream(file);

            byte[] buffer = new byte[1024 * 8];
            int length = -1;

            while ((length = in.read(buffer)) != -1) {
                out.write(buffer, 0, length);
            }
            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (out != null)
                try {
                    out.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
        }

        return true;
    }

    public static void copyFile(File sourceFile, File targetFile) throws Exception {
        FileInputStream in = new FileInputStream(sourceFile);
        byte[] buffer = new byte[128 * 1024];
        int len = -1;
        FileOutputStream out = new FileOutputStream(targetFile);
        while ((len = in.read(buffer)) != -1)
            out.write(buffer, 0, len);
        out.flush();
        out.close();
        in.close();
    }

	public static void copyFile(File sourceFile, FileOutputStream targetFileStream) throws Exception {
		FileInputStream in = new FileInputStream(sourceFile);
		byte[] buffer = new byte[128 * 1024];
		int len = -1;
		while ((len = in.read(buffer)) != -1)
			targetFileStream.write(buffer, 0, len);
		targetFileStream.flush();
		targetFileStream.close();
		in.close();
	}

    @SuppressWarnings("unchecked")
    public static <T> T readObject(File file, Class<T> clazz) throws Exception {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(file));

            return (T) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (in != null)
                in.close();
        }

        return null;
    }

    public static void writeObject(File file, Serializable object) throws Exception {
        if (!file.getParentFile().exists())
            file.getParentFile().mkdirs();

        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));

            out.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    private final static String PATTERN = "yyyyMMddHHmmss";

	public static String getRealFilePath( final Context context, final Uri uri ) {
		if ( null == uri ) return null;
		final String scheme = uri.getScheme();
		String data = null;
		if ( scheme == null )
			data = uri.getPath();
		else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
			data = uri.getPath();
		} else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
			Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
			if ( null != cursor ) {
				if ( cursor.moveToFirst() ) {
					int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
					if ( index > -1 ) {
						data = cursor.getString( index );
					}
				}
				cursor.close();
			}
		}
		return data;
	}

    public static String getFileDirTempFile(Context context, String folderName){

        String filename = new Date().toString();
        return getFileDirTempFile(context,folderName);
    }

    public static String getFileDirFile(Context context, String folderName, String fileName){

        File file =  new  File(context.getFilesDir(), folderName);
        if(!file.exists()){
           file.mkdirs();
        }
        File file2 = new File(file.getPath(),fileName);
        if(file2.exists()){
            file2.delete();
        }
        try{
            boolean bFlag = file2.createNewFile();
            if(bFlag){
                return file2.getAbsolutePath();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return "";
    }


    public static File createTmpFile(Context context, String filePath) {

        String timeStamp = new SimpleDateFormat(PATTERN, Locale.CHINA).format(new Date());

        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() + filePath);

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!dir.exists()) {
                dir.mkdirs();
            }
            return new File(dir, timeStamp + ".jpg");
        } else {
            File cacheDir = context.getCacheDir();
            return new File(cacheDir, timeStamp + ".jpg");
        }

    }


    public static void createFile(String filePath) {
        String externalStorageState = Environment.getExternalStorageState();

        File dir = new File(Environment.getExternalStorageDirectory() + filePath);
        File cropFile = new File(Environment.getExternalStorageDirectory() + filePath + "/crop");

        if (externalStorageState.equals(Environment.MEDIA_MOUNTED)) {
            if (!cropFile.exists()) {
                cropFile.mkdirs();
            }

            File file = new File(dir, ".nomedia");
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

}
