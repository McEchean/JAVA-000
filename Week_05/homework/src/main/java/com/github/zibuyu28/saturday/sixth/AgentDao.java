package com.github.zibuyu28.saturday.sixth;


import java.sql.Date;

public class AgentDao {
    private int id;

    private Date timeCreate;

    private Date timeUpdate;

    private Date timeDelete;

    private String uuid;

    private int state;

    private String message;

    private String fullData;

    private String machineID;

    private String resourceType;

    private String addr;


    public AgentDao() {
    }

    public AgentDao(int id, Date timeCreate, Date timeUpdate, Date timeDelete, String uuid, int state, String message, String fullData, String machineID, String resourceType, String addr) {
        this.id = id;
        this.timeCreate = timeCreate;
        this.timeUpdate = timeUpdate;
        this.timeDelete = timeDelete;
        this.uuid = uuid;
        this.state = state;
        this.message = message;
        this.fullData = fullData;
        this.machineID = machineID;
        this.resourceType = resourceType;
        this.addr = addr;
    }

    @Override
    public String toString() {
        return "AgentDao{" +
                "id=" + id +
                ", timeCreate=" + timeCreate +
                ", timeUpdate=" + timeUpdate +
                ", timeDelete=" + timeDelete +
                ", uuid='" + uuid + '\'' +
                ", state=" + state +
                ", message='" + message + '\'' +
                ", fullData='" + fullData + '\'' +
                ", machineID='" + machineID + '\'' +
                ", resourceType='" + resourceType + '\'' +
                ", addr='" + addr + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getTimeCreate() {
        return timeCreate;
    }

    public void setTimeCreate(Date timeCreate) {
        this.timeCreate = timeCreate;
    }

    public Date getTimeUpdate() {
        return timeUpdate;
    }

    public void setTimeUpdate(Date timeUpdate) {
        this.timeUpdate = timeUpdate;
    }

    public Date getTimeDelete() {
        return timeDelete;
    }

    public void setTimeDelete(Date timeDelete) {
        this.timeDelete = timeDelete;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFullData() {
        return fullData;
    }

    public void setFullData(String fullData) {
        this.fullData = fullData;
    }

    public String getMachineID() {
        return machineID;
    }

    public void setMachineID(String machineID) {
        this.machineID = machineID;
    }

    public String getResourceType() {
        return resourceType;
    }

    public void setResourceType(String resourceType) {
        this.resourceType = resourceType;
    }

    public String getAddr() {
        return addr;
    }

    public void setAddr(String addr) {
        this.addr = addr;
    }
}
