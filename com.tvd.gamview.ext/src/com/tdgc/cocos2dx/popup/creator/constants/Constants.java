package com.tdgc.cocos2dx.popup.creator.constants;

public interface Constants {
	public static final String DONT_DELETE_THIS_LINE 	= "//don't modify or delete this line";
	public static final String IMAGEVIEW_TAG_MARK 		= "<!--{imageview_tag}-->";
	public static final String IMAGE_TAG_MARK			= "<!--{image_tag}-->";
	public static final String POSITION_DECLARING_CMM	= "positions declaring";
	public static final String POSITION_IMPLEMENTING_CMM	= "positions initializing";
	public static final String IMAGEIDS_DECLARING_CMM	= "image ids declaring";
	public static final String IMAGEIDS_IMPLEMENTING_CMM	= "image ids initializing";
	public static final String XIB						= "Xib";
	public static final String SCREEN					= "Screen";
	public static final String ANDROID					= "Android";
	
	public static final int PLUGIN						= 1001;
	public static final int NORMAL						= 1002;
	
	public static interface ViewType {
		public static final int VIEW = 1001;
		public static final int CELL = 1002;
	}
}