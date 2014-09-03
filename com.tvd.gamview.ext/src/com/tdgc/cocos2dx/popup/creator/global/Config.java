package com.tdgc.cocos2dx.popup.creator.global;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;

import com.tdgc.cocos2dx.popup.creator.constants.Constants;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.model.Image;

public class Config {
	
	private Config() {
		mDefaultNormalPrefix 			= "m";
		mDefaultSupers 					= new HashMap<String, String>();
		mDefaultBackgroundOnSupers 		= new HashMap<String, String>();
		mDefaultMenuOnSupers 			= new HashMap<String, String>();
		mDefaultTemplateNames 			= new HashMap<String, String>();
		mDefaultBackgroundImages 		= new HashMap<String, Image>();
		mDefaultScreenSize				= new HashMap<String, String>();
	}

	public static Config getInstance() {
		if(sInstance == null) {
			synchronized (Config.class) {
				if(sInstance == null) {
					sInstance = new Config();
				}
			}
		}
		
		return sInstance;
	}
	
	public void loadConfigs() {
		IFile file = mProject.getFile("src/com/config/default.properties");
		FileUtils fileUtils = new FileUtils();
		String fileContent = fileUtils.readFromFile(file);
		mDefaultTemplateNames.putAll(fileUtils.fetchDefaultKeyValues(
				"Template name", fileContent));
		mDefaultSupers.putAll(fileUtils.fetchDefaultKeyValues(
				"Super name", fileContent));
		mDefaultBackgroundOnSupers.putAll(fileUtils.fetchDefaultKeyValues(
				"Background on super", fileContent));
		mDefaultMenuOnSupers.putAll(fileUtils.fetchDefaultKeyValues(
				"Menu on super", fileContent));
		mDefaultNormalPrefix = fileUtils.fetchDefaultValue(
				"normal_prefix", "Prefix and Suffix", fileContent);
		mDefaultViewSuffix = fileUtils.fetchDefaultValue(
				"view_suffix", "Prefix and Suffix", fileContent);
		mDefaultScreenSize.putAll(fileUtils.fetchDefaultKeyValues(
				"Screen size", fileContent));
		
		mDefaultBackgroundImages.putAll(fileUtils.fetchDefaultBackgroundImages(
				"Background image on super", fileContent));
		System.out.println("Config just have loaded");
	}
	
	public void reloadConfigs() {
		loadConfigs();
		System.out.println("Config just have reloaded");
	}
	
	public void setProject(IProject project) {
		if(mProject != project) {
			this.mProject = project;
			this.loadConfigs();
		}
	}
	
	public IProject getProject() {
		return mProject;
	}
	
	public String getDefautSuper(String pViewType) {
		return mDefaultSupers.get(pViewType);
	}
	
	public String getDefaultBackgroundOnSupers(String pViewType) {
		return mDefaultBackgroundOnSupers.get(pViewType);
	}
	
	public String getProjectName() {
		return mProjectName;
	}

	public void setProjectName(String mProjectName) {
		this.mProjectName = mProjectName;
	}

	public String getDefaultNormalPrefix() {
		return mDefaultNormalPrefix;
	}

	public void setDefaultNormalPrefix(String mDefaultNormalPrefix) {
		this.mDefaultNormalPrefix = mDefaultNormalPrefix;
	}

	public Map<String, String> getDefaultSupers() {
		return mDefaultSupers;
	}

	public void setDefaultSupers(Map<String, String> mDefaultSupers) {
		this.mDefaultSupers = mDefaultSupers;
	}

	public String getDefinePath() {
		return mDefinePath;
	}

	public void setDefinePath(String mDefinePath) {
		this.mDefinePath = mDefinePath;
	}

	public String getParametersPath() {
		return mParametersPath;
	}

	public void setParametersPath(String mParametersPath) {
		this.mParametersPath = mParametersPath;
	}

	public String getImagesInputPath() {
		return mImagesInputPath;
	}

	public void setImagesInputPath(String mImagesInputPath) {
		this.mImagesInputPath = mImagesInputPath;
	}

	public String getImagesPath() {
		return mImagesPath;
	}

	public void setImagesPath(String mImagesPath) {
		this.mImagesPath = mImagesPath;
	}

	public String getXibContainerPath() {
		return mXibContainerPath;
	}

	public void setXibContainerPath(String mXibContainerPath) {
		this.mXibContainerPath = mXibContainerPath;
	}

	public String getClassPath() {
		return mClassPath;
	}

	public void setClassPath(String mClassPath) {
		this.mClassPath = mClassPath;
	}

	public String getScreenContainerPath() {
		return mScreenContainerPath;
	}

	public void setScreenContainerPath(String mScreenContainerPath) {
		this.mScreenContainerPath = mScreenContainerPath;
	}

	public void setAndroidContainerPath(String path) {
		this.mAndroidContainerPath = path;
	}
	
	public String getAndroidContainerPath() {
		return this.mAndroidContainerPath;
	}
	
	public int getProgrammingType() {
		return Constants.NORMAL;
	}
	
	public String getDefaultMenuOnSuper(String superType) {
		return mDefaultMenuOnSupers.get(superType);
	}
	
	public String getDefaultTemplateName(String superType) {
		return mDefaultTemplateNames.get(superType);
	}
	
	public Image getDefaultBackgroundImage(String type) {
		return mDefaultBackgroundImages.get(type);
	}
	
	public String getDefaultScreenSizeString(String device) {
		return mDefaultScreenSize.get(device);
	}
	
	public String getDefaultViewSuffix() {
		return mDefaultViewSuffix;
	}
	
	private String mProjectName;
	private String mDefaultNormalPrefix;
	private String mDefaultViewSuffix;
	
	private Map<String, String> mDefaultSupers;
	private Map<String, String> mDefaultBackgroundOnSupers;
	private Map<String, String> mDefaultMenuOnSupers;
	private Map<String, String> mDefaultTemplateNames;
	private Map<String, Image>  mDefaultBackgroundImages;
	private Map<String, String> mDefaultScreenSize;
	
	private String mDefinePath;
	private String mParametersPath;
	private String mImagesInputPath;
	private String mImagesPath;
	private String mXibContainerPath;
	private String mClassPath;
	private String mScreenContainerPath;
	private String mAndroidContainerPath;
	
	private IProject mProject;
	
	private static Config sInstance;
	
}
