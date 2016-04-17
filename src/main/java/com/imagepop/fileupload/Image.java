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

    protected Image() {
    }

    public Image(User user) {
        this.user = user;
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
}
