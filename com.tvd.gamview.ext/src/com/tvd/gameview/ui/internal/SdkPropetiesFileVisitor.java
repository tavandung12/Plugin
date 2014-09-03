package com.tvd.gameview.ui.internal;

import java.io.InputStream;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceProxy;
import org.eclipse.core.resources.IResourceProxyVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Status;

import com.tvd.gamview.ext.Activator;
import com.tvd.gamview.ext.utils.MessageUtils;
import com.tvd.gamview.ext.utils.ProjectUtils;


public class SdkPropetiesFileVisitor implements IResourceProxyVisitor,
	IResourceDeltaVisitor {	

	@Override
	public boolean visit(IResourceDelta delta) throws CoreException {
//		System.out.println("SdkPropetiesFileVisitor::visit::delta.getKind = " + delta.getKind());
		boolean deleted = (IResourceDelta.REMOVED & delta.getKind()) != 0;
		boolean changed = (IResourceDelta.CHANGED & delta.getKind()) != 0;
		IResource resource = delta.getResource();
		String name = resource.getName();
		if(name.endsWith(".properties")) {
			if(deleted) {
				System.out.println("file " + name + " has deleted");
			} else if(changed) {
				processPropertiesResource(resource);
			}
		} 
		return true;
	}
	
	@Override
	public boolean visit(IResourceProxy proxy) throws CoreException {
//		System.out.println("SdkPropetiesFileVisitor::visit::proxy");
		String name = proxy.getName();
		if(name != null && name.endsWith(""))
			processPropertiesResource(proxy.requestResource());
		return true;
	}
	
	private void processPropertiesResource(IResource resource) 
			throws CoreException {
		if(resource instanceof IFile && resource.exists()) {
			try {
				IFile file = (IFile)resource;
				String name = file.getName();
				IProject project = resource.getProject();
				if(name.equals("global.properties")) {
					processGlobalPropertiesResource(project);
				}
			} catch (CoreException e) {
				throw new CoreException(new Status(Status.ERROR,
						Activator.PLUGIN_ID, "Failed to generate resource", e));
			}
		}
	}
	
	private void processGlobalPropertiesResource(IProject project) 
			throws CoreException {
		InputStream inputStream = 
				project.getFile("src/com/properties/global.properties")
				.getContents();
		String deviceStr = MessageUtils.getString(inputStream, "devices");
		
		//if have no device, do nothing
		if(deviceStr == null || deviceStr.equals("")) {
			return;
		}
		
		//get divces
		String devices[] = (deviceStr.contains(",")) 
				? deviceStr.split(",") : new String[] {deviceStr};
				
		//create new devices if have any device added
		createNewDevices(project, devices);
		
	}
	
	private void createNewDevices(IProject project, String devices[]) 
			throws CoreException {
		for(int i = 0 ; i < devices.length ; i++) {
			String device = devices[i].trim();
			IFolder xml = project.getFolder("resources/xml/" + device);
			if(!xml.exists()) {
				xml.create(true, true, null);
			}
			IFolder xib = project.getFolder("resources/xib/" + device);
			if(!xib.exists()) {
				xib.create(true, true, null);
			}
			IFolder screen = project.getFolder("resources/screen/" + device);
			if(!screen.exists()) {
				screen.create(true, true, null);
			}
			IFolder android = project.getFolder("resources/android/" + device);
			if(!android.exists()) {
				android.create(true, true, null);
			}
		}
		//deleted device if have any device deleted
		deleteDevices(project, devices);
		
	}
	
	private void deleteDevices(IProject project, String devices[]) 
			throws CoreException {
		
		List<String> deviceFolderNames = ProjectUtils.getDeviceFolderNames(project);
		
		for(int i = 0 ; i < deviceFolderNames.size() ; i++) {
			String savedDevice = deviceFolderNames.get(i);
			boolean deleted = true;
			for(int k = 0 ; k < devices.length ; k++) {
				if(savedDevice.equals(devices[k].trim())) {
					deleted = false;
				}
			}
			if(deleted) {
				IFolder xml = project.getFolder("resources/xml/" + savedDevice);
				if(xml.exists()) {
					xml.delete(true, null);
				}
				IFolder xib = project.getFolder("resources/xib/" + savedDevice);
				if(xib.exists()) {
					xib.delete(true, null);
				}
				IFolder screen = project.getFolder("resources/screen/" + savedDevice);
				if(screen.exists()) {
					screen.delete(true, null);
				}
				IFolder android = project.getFolder("resources/android/" + savedDevice);
				if(android.exists()) {
					android.delete(true, null);
				}
			}
		}
	}
}