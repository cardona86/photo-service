package com.synchrony.photo_service.models.user;

import com.synchrony.photo_service.models.images.Image;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

public class UserWithImages implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UserProfile userProfile;
    private List<Image> images;

    public UserWithImages() {
    }

    public UserWithImages(UserProfile userProfile, List<Image> images) {
        this.userProfile = userProfile;
        this.images = images;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    @Override
    public String toString() {
        return "UserWithImages{" +
            "userProfile=" + userProfile +
            ", images=" + images +
            '}';
    }
}
