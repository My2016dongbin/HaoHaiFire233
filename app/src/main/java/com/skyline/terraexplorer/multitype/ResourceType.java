package com.skyline.terraexplorer.multitype;

import java.util.List;

/**
 * Created by geyang on 2020/1/19.
 */
public class ResourceType {
    public String resourceTypeName;
    public List<Resource> resourceList;
    public boolean isOpen;

    public ResourceType(String resourceTypeName, List<Resource> resourceList) {
        this.resourceTypeName = resourceTypeName;
        this.resourceList = resourceList;
        this.isOpen = true;
    }

    public String getResourceTypeName() {
        return resourceTypeName;
    }

    public void setResourceTypeName(String resourceTypeName) {
        this.resourceTypeName = resourceTypeName;
    }

    public List<Resource> getResourceList() {
        return resourceList;
    }

    public void setResourceList(List<Resource> resourceList) {
        this.resourceList = resourceList;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }
}