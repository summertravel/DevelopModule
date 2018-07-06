package com.summer.developmodule.bean;

import com.summer.developmodule.greendaodemo.db.CovertAdapter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Project {
    @Id
    public Long id;
    @Convert(converter = CovertAdapter.class, columnType = String.class)
    public List<User> userList;
    @Generated(hash = 627037597)
    public Project(Long id, List<User> userList) {
        this.id = id;
        this.userList = userList;
    }
    @Generated(hash = 1767516619)
    public Project() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public List<User> getUserList() {
        return this.userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

}
