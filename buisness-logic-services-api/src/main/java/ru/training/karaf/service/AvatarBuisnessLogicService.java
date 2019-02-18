package ru.training.karaf.service;

import java.util.Optional;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;

public interface AvatarBuisnessLogicService {
    
    Optional<byte[]> getAvatar(String libCard);
    
    boolean uploadAvatar(String libCard, Attachment avatar);
    
    boolean deleteAvatar(String avatar);
    
}
