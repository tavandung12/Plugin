package com.tvd.gamview.ext.support;
 
import java.net.URI;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Assert;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;

import com.tvd.gamview.ext.Activator;
import com.tvd.gamview.ext.constants.Key;
import com.tvd.gamview.ext.file.FileActions;
 
 
public class CustomProjectSupport {
    /**
     * For this marvelous project we need to:
     * - create the default Eclipse project
     * - add the custom project nature
     * - create the folder structure
     *
     * @param projectName
     * @param location
     * @param natureId
     * @return
     */
    public static IProject createProject(String projectName, URI location) {
        Assert.isNotNull(projectName);
        Assert.isTrue(projectName.trim().length() > 0);
 
        IProject project = createBaseProject(projectName, location);
        try {
            String[] paths = { "src", "src/com/template",
            		"resources/xml/xsd", "src/com/config"}; //$NON-NLS-1$ //$NON-NLS-2$
            addToProjectStructure(project, paths);
        } catch (CoreException e) {
            e.printStackTrace();
            project = null;
        }
 
        return project;
    }
 
    /**
     * Just do the basics: create a basic project.
     *
     * @param location
     * @param projectName
     */
    private static IProject createBaseProject(String projectName, URI location) {
        // it is acceptable to use the ResourcesPlugin class
        IProject newProject = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
 
        if (!newProject.exists()) {
            URI projectLocation = location;
            IProjectDescription desc = newProject.getWorkspace()
            		.newProjectDescription(newProject.getName());
            if (location != null && ResourcesPlugin.getWorkspace()
            		.getRoot().getLocationURI().equals(location)) {
                projectLocation = null;
            }
 
            desc.setLocationURI(projectLocation);
            try {
                newProject.create(desc, null);
                if (!newProject.isOpen()) {
                    newProject.open(null);
                }
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
 
        return newProject;
    }
    
    public static void createFolders(IJavaProject pNewProject, String pPaths[]) {
    	try {
			addToProjectStructure(pNewProject.getProject(), pPaths);
			copyFiles(pNewProject.getProject(), pPaths);
		} catch (CoreException e) {
			e.printStackTrace();
		}
    }
    
    public static void copyFiles(IProject pNewProject, String pPaths[]) {
    	for(String path : pPaths) {
    		String sourcePath = new String(Activator.getDefault()
    				.getPreferenceStore().getString(Key.TEMPLATE_ROOT) + "/" + path);
    		FileActions.copyFiles(pNewProject, sourcePath, path);

    	}
    	
    }
    
    public static void copySourceCodeFiles(String pDesPath) {
    	FileActions.copySourceCodeFiles(pDesPath);
    }
    
    public static void writeFile(IJavaProject pProject, String pFolder, 
			String pFile, String pContent) {
    	FileActions.writeToFile(pProject.getProject(), 
    			pFolder, pFile, pContent);
    }
 
    private static void createFolder(IFolder folder) throws CoreException {
        IContainer parent = folder.getParent();
        if (parent instanceof IFolder) {
            createFolder((IFolder) parent);
        }
        if (!folder.exists()) {
            folder.create(false, true, null);
        }
    }
 
    /**
     * Create a folder structure with a parent root, overlay, and a few child
     * folders.
     *
     * @param newProject
     * @param paths
     * @throws CoreException
     */
    private static void addToProjectStructure(IProject newProject, String[] paths) 
    		throws CoreException {
        for (String path : paths) {
            IFolder etcFolders = newProject.getFolder(path);
            createFolder(etcFolders);
        }
    }
}