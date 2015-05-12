package com.valhala.Assets;

import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

public class AssetUtils {
	
	/**
	 * Check the File if exits in the Asset Folder
	 * @param context
	 * @param filename
	 * @return
	 */
	public static boolean isExists(Context context, String filename){
		try {
			AssetManager asset = context.getAssets();
			InputStream input = asset.open(filename);;
			
			return (input != null) ? true : false;
		} catch (Exception e) {
			Log.e("[AssetUtils]", "Error: "+ e.getMessage());
		}
		return false;
	}
}
