package com.muzhiwan.tools;

import java.net.URLDecoder;
import java.security.MessageDigest;

public class MuzhiwanSDKUtils {
	
	public static final String SIGN = "534b9fcbc1aad";
	
	public static String sign(String appkey,
			String orderid,
			String productName,
			String productDesc,
			String productID,
			String money,
			String uid,
			String extern)throws Throwable{
		
		StringBuilder builder = new StringBuilder();
		builder.append(appkey);
		builder.append(orderid);
		builder.append(URLDecoder.decode(productName, "UTF-8"));
		builder.append(URLDecoder.decode(productDesc, "UTF-8"));
		builder.append(URLDecoder.decode(productID, "UTF-8"));
		builder.append(money);
		builder.append(uid);
		builder.append(extern);
		builder.append(SIGN);
		
		
		String content = builder.toString();
		System.out.println(content);
		return getMd5(content, "UTF-8");
		
	}
	
	public static String getMd5( String value, String charset )
	{
		try
		{
			MessageDigest md5 = MessageDigest.getInstance( "MD5" );
			md5.update( value.getBytes( charset ) );
			return toHexString( md5.digest() );
		}
		catch( Throwable e )
		{
			e.printStackTrace();
		}
		return "";
	}

	private static final char	HEX_DIGITS[]	= { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString( byte[] b )
	{
		StringBuilder sb = new StringBuilder( b.length * 2 );
		for( int i = 0; i < b.length; i++ )
		{
			sb.append( HEX_DIGITS[ ( b[ i ] & 0xf0 ) >>> 4 ] );
			sb.append( HEX_DIGITS[ b[ i ] & 0x0f ] );
		}
		return sb.toString();
	}
}
