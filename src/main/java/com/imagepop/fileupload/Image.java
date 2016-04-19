package com.imagepop.fileupload;

import com.imagepop.domain.User;

import javax.persistence.*;

/**
 * Created by matt on 4/16/16.
 */
@Entity
public class Image {

    public enum Status {
        NONE, UPLOADED, POPPED
    }

    @Id
    @GeneratedValue
    private long id;

    @OneToOne
    private User user;

    @Column
    private Status status;

    @Column
    private String name;

    @Column
    private int size;

    protected Image() {
    }

    public Image(User user, String name, int size) {
        this.user = user;
        this.size = size;
        this.status = Status.NONE;
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Image[id=%d, user=%s]", id, user.toString());
    }

    public Long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

}
