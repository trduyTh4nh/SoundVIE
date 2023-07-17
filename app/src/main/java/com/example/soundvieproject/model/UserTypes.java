package com.example.soundvieproject.model;

import org.bson.types.ObjectId;

import io.realm.RealmObject;

public class UserTypes extends RealmObject {
    private ObjectId id;
    private String typeID;
    private String typeName;
    private String typeDescription;
    public UserTypes(){}

    public UserTypes(String typeName, String typeDescription, String typeID) {
        id = new ObjectId();
        this.typeName = typeName;
        this.typeDescription = typeDescription;
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getTypeDescription() {
        return typeDescription;
    }

    public void setTypeDescription(String typeDescription) {
        this.typeDescription = typeDescription;
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }
}
