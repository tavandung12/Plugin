package com.tdgc.cocos2dx.popup.creator.model;

import com.tdgc.cocos2dx.popup.creator.constants.Attribute;
import com.tdgc.cocos2dx.popup.creator.constants.ModelType;
import com.tdgc.cocos2dx.popup.creator.constants.Tag;
import com.tdgc.cocos2dx.popup.creator.file.FileUtils;
import com.tdgc.cocos2dx.popup.creator.global.Config;
import com.tdgc.cocos2dx.popup.creator.model.basic.CommonObject;
import com.tdgc.cocos2dx.popup.creator.model.basic.Point;
import com.tdgc.cocos2dx.popup.creator.model.basic.Size;
import com.tdgc.cocos2dx.popup.creator.utils.StringUtils;

public class Menu extends CommonObject {
	public Menu() {
		super();
		this.mSuffix = "Menu";
		this.mDeclareObjectName = "CCMenu";
		this.mType = ModelType.MENU;
		this.mXmlTagName = Tag.MENU;
		this.mIsUnlocated = true;
		this.mAnchorPoint = new Point(0, 0);
		this.mPosition = new Point(0, 0);
	}
	@Override
	public String declare() {
		StringBuilder builder = new StringBuilder();
		builder.append("CCMenu* " + mName + "; ");
		
		return builder.toString();
	}

	@Override
	public String implement(boolean pInfunction) {
		
		if(pInfunction && isBackground()) {
			return "";
		}
		
		StringBuilder builder = new StringBuilder("\n");
		
		String templateName = "CCMenu";
		if(pInfunction) {
			templateName += " in function";
			mName = getInfunctionName();
		} 
		
//		String template = new FileUtils().fetchTemplate(templateName, 
//				"src/com/template/new_menu.template");
		String template = new FileUtils().fetchTemplate(templateName, 
				"src/com/template/new_menu.template", getProject());
		
		String parentName = Config.getInstance()
				.getDefaultParentForProperties(mParent.getType());
		if(mParent != null) {
			parentName = mParent.getName();
		}
		template = template.replace("{var_name}", mName)
			.replace("{tab}", "\t")
			.replace("{parent_name}", parentName);
		builder.append(template);
		
		return builder.toString();
	}
	
	@Override
	public Point getLocationInView() {
		return mParent.getLocationInView();
	}
	
	@Override
	public Size getSize() {
		return mParent.getSize();
	}
	
	@Override
	public CommonObject clone() {
		Menu menu = new Menu();
		this.setAllPropertiesForObject(menu);
		return menu;
	}
	
	@Override
	public String toXML() {
		String tab = StringUtils.tab(mTabCount);
		StringBuilder builder = new StringBuilder(tab);
		builder.append("<" + mXmlTagName + " " + Attribute.VISIBLE + "=\"true\" ")
			.append(Attribute.COMMENT + "=\"\">");
		builder.append("\n" + tab + "\t")
			.append("<" + Tag.POSITION_NAME + " " + Attribute.VALUE 
					+ "=\"" + mXmlPositionName + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.ANCHORPOINT + " " + Attribute.VALUE 
				+ "=\"" + mAnchorPointString + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.POSITION + " " + Attribute.VALUE 
				+ "=\"" + mPosition + "\" />");
		builder.append("\n" + tab + "\t")
		.append("<" + Tag.Z_INDEX + " " + Attribute.VALUE 
				+ "=\"" + mZIndex + "\" />");
		
		builder.append("\n")
			.append(super.toXML())
			.append(tab)
			.append("</" + mXmlTagName + ">");
		
		builder.append("\n");
		
		return builder.toString();
	}
}
