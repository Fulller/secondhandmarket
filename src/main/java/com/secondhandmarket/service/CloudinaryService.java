package com.secondhandmarket.service;

import com.cloudinary.utils.ObjectUtils;
import com.secondhandmarket.config.CloudinaryConfig;
import com.secondhandmarket.exception.AppException;
import com.secondhandmarket.model.User;
import com.secondhandmarket.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CloudinaryService {

    CloudinaryConfig cloudinaryConfig;
    UserRepository userRepository;


    public Set<String> uploadMultiImg(List<MultipartFile> files) throws IOException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));

        Set<String> urlList = new HashSet<>();
        for (MultipartFile file : files) {
            var result = cloudinaryConfig.cloudinary().uploader()
                    .upload(file.getBytes(), ObjectUtils.asMap(
                            "folder", "secondhandmarket/product_owner_"+user.getId()+"/images"
                    ));
            urlList.add((String) result.get("secure_url"));;
        }
        return urlList;
    }
    public String uploadMultiVideos(MultipartFile vid) throws IOException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found"));

        var result = cloudinaryConfig.cloudinary().uploader().upload(vid.getBytes(), ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "secondhandmarket/product_owner_"+user.getId()+"/videos"
        ));
        return (String) result.get("secure_url");
    }
}
