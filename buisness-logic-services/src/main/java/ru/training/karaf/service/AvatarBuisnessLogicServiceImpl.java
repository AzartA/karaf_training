package ru.training.karaf.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import ru.training.karaf.model.AvatarDO;
import ru.training.karaf.model.UserDO;
import ru.training.karaf.repo.UserRepo;

public class AvatarBuisnessLogicServiceImpl implements AvatarBuisnessLogicService {
    
    private UserRepo userRepo;

    public void setUserRepo(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    
    @Override
    public Optional<byte[]> getAvatar(String libCard) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            if (user.getAvatar() == null) {
                return Optional.empty();
            }
            return Optional.of(user.getAvatar().getPicture());
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return Optional.empty();
        }
    }
    
    @Override
    public boolean uploadAvatar(String libCard, Attachment avatar) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            InputStream payload = avatar.getObject(InputStream.class);
            byte[] picture = new byte[payload.available()];
            payload.read(picture);
            if (user.getAvatar() != null) {
                user.getAvatar().setPicture(picture);
            } else {
                user.setAvatar(new AvatarDO(picture));
            }
            userRepo.updateUser(user);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return false;
        } catch (IOException ex) {
            System.err.println("Cannot upload an avatar");
            return false;
        }
    }
    
    @Override
    public boolean deleteAvatar(String libCard) {
        try {
            UserDO user = (UserDO)userRepo.getUser(libCard).get();
            if (user.getAvatar() == null) {
                return false;
            }
            Long avatarId = user.getAvatar().getId();
            user.setAvatar(null);
            userRepo.updateUser(user);
            userRepo.deleteUserAvatar(avatarId);
            return true;
        } catch (NoSuchElementException e) {
            System.err.println("User not found");
            return false;
        }
    }
}
