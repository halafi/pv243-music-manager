package cz.muni.fi.pv243.musicmanager.utils;

import java.util.UUID;

/**
 * Class for generating UUID's and its string representation.
 * */
public class UUIDStringGenerator {
	
	public static String generateCommentId(){
		return UUID.randomUUID().toString();
	}
	
	public static String generateSongId(){
		return UUID.randomUUID().toString();
	}
	
	public static String generateIntepretId(){
		return UUID.randomUUID().toString();
	}
	
}
