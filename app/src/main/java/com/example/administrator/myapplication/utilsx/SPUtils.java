package com.example.administrator.myapplication.utilsx;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.ArrayList;

public class SPUtils {
	private static final String SP_NAME = "sp_name";
	private static SharedPreferences sp;

	/**
	 * ??????
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}

		return sp.getBoolean(key, defValue);
	}

	/**
	 * ??????
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}
	
	/**
	 * ?????
	 * 
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putString(Context context, String key, String value){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putString(key, value).commit();
	}
	
	/**
	 * ????
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static String getString(Context context, String key, String defValue){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getString(key, defValue);
	}
	/**
	 * ??int
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putInt(Context context, String key, int value){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}
	
	/**
	 * ?int
	 * 
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static int getInt(Context context, String key, int defValue){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getInt(key, defValue);
	}

	/**
	 * ??int
	 *
	 * @param context
	 * @param key
	 * @param value
	 */
	public static void putLong(Context context, String key, long value){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putLong(key, value).commit();
	}

	/**
	 * ?int
	 *
	 * @param context
	 * @param key
	 * @param defValue
	 * @return
	 */
	public static long getLong(Context context, String key, long defValue){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		return sp.getLong(key, defValue);
	}
	
//	//cun
//	public static boolean putArrayList(Context context, ArrayList<String> value) {  
//		if (sp == null) {
//			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
//		}  
//			sp.edit().putInt("Status_size",value.size()); /*sKey is an array*/   
//		  
//		  for(int i=0;i<value.size();i++) {  
//			  sp.edit().remove("Status_" + i);  
//			  sp.edit().putString("Status_" + i, value.get(i));	
//		  }  
//		  return sp.edit().commit();	   
//		}
//	//qu
//	public static ArrayList<String> getArrayList(Context context, ArrayList<String> old) {	
//		old.clear();  
//		  int size = sp.getInt("Status_size", 0);	
//		  
//		  for(int i=0;i<size;i++) {  
//			  old.add(sp.getString("Status_" + i, null));	
//		  
//		  }
//		  return old;
//		}
	
//	public static String SceneList2String(List SceneList)
//            throws IOException {
//      // ??????ByteArrayOutputStream?????????????????????????
//      ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//      // ?????????????????ObjectOutputStream
//      ObjectOutputStream objectOutputStream = new ObjectOutputStream(
//              byteArrayOutputStream);
//      // writeObject ?????????????????????????????????? readObject ????????????
//      objectOutputStream.writeObject(SceneList);
//      // ?????Base64.encode?????????????Base64????????String??
//      String SceneListString = new String(Base64.encode(
//              byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
//      // ???objectOutputStream
//      objectOutputStream.close();
//      return SceneListString;
//	}
//	
//	@SuppressWarnings("unchecked")
//	  public static List String2SceneList(String SceneListString)
//	          throws StreamCorruptedException, IOException,
//	          ClassNotFoundException {
//	      byte[] mobileBytes = Base64.decode(SceneListString.getBytes(),
//	              Base64.DEFAULT);
//	      ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
//	              mobileBytes);
//	      ObjectInputStream objectInputStream = new ObjectInputStream(
//	              byteArrayInputStream);
//	      List SceneList = (List) objectInputStream
//	              .readObject();
//	      objectInputStream.close();
//	      return SceneList;
//	  }
	
	
	
	
	public static String list2String(ArrayList<String> list)
            throws IOException {
        // ??????ByteArrayOutputStream?????????????????????????
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // ?????????????????ObjectOutputStream
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(
                byteArrayOutputStream);
        // writeObject ?????????????????????????????????? readObject ????????????
        objectOutputStream.writeObject(list);
        // ?????Base64.encode?????????????Base64????????String??
        String listString = new String(Base64.encode(
                byteArrayOutputStream.toByteArray(), Base64.DEFAULT));
        // ???objectOutputStream
        objectOutputStream.close();
        return listString;
    }

    @SuppressWarnings("unchecked")
    public static ArrayList<String> string2List(String listString)
            throws StreamCorruptedException, IOException,
            ClassNotFoundException {
        byte[] mobileBytes = Base64.decode(listString.getBytes(),
                Base64.DEFAULT);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(
                mobileBytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(
                byteArrayInputStream);
        ArrayList<String> list = (ArrayList<String>) objectInputStream
                .readObject();
        objectInputStream.close();
        return list;
    }
	 
	
	public static void putList(Context context, String key, ArrayList value){
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		
		try {
		    String liststr = list2String(value);
		    sp.edit().putString(key, liststr).commit();
		} catch (IOException e) {
		    e.printStackTrace();
		}
	}
	
	
	public static ArrayList getList(Context context, String key, ArrayList defValue){
		ArrayList list = null;
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		String liststr = sp.getString(key, "");
		try {
			   list = string2List(liststr);
			} catch (StreamCorruptedException e) {
			   e.printStackTrace();
			} catch (IOException e) {
			   e.printStackTrace();
			} catch (ClassNotFoundException e) {
			   e.printStackTrace();
			}
		return list;
	}

	/**
	 * ???sp
	 * @param context
	 */
	public static void dismiss(Context context) {
		if (sp != null) {
			sp.edit().clear().commit();
		}

	}




}
