package com.haohai.platform.mapmodel.model;

/**
 * Created by geyang on 2020/11/23.
 */

public class MapModel {
    private String id;
    private String name;
    private MapPosition position;
    private String resourceType;

    public MapModel() {
    }

    public MapModel(String id, String name, MapPosition position, String resourceType) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.resourceType = resourceType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MapPosition getPosition() {
        return position;
    }

    public void setPosition(MapPosition position) {
        this.position = position;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }
}
