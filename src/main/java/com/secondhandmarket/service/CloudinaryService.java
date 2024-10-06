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
import java.util.Map;
import java.util.Set;

@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CloudinaryService {

    CloudinaryConfig cloudinary;
    UserRepository userRepository;

    public String uploadImg(MultipartFile file) throws IOException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found", "user-s-01"));

        var result = cloudinary.cloudinary().uploader()
                .upload(file.getBytes(), ObjectUtils.asMap(
                        "folder", "secondhandmarket/product_owner_"+user.getId()+"/images"
                ));
        return (String) result.get("secure_url");
    }

    public Set<String> uploadMultiImg(List<MultipartFile> files) throws IOException {
        String id = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found", "user-s-01"));

        Set<String> urlList = new HashSet<>();
        for (MultipartFile file : files) {
            var result = cloudinary.cloudinary().uploader()
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
                .orElseThrow(() -> new AppException(HttpStatus.BAD_REQUEST, "User not found", "user-s-01"));

        var result = cloudinary.cloudinary().uploader().upload(vid.getBytes(), ObjectUtils.asMap(
                "resource_type", "video",
                "folder", "secondhandmarket/product_owner_"+user.getId()+"/videos"
        ));
        return (String) result.get("secure_url");
    }

    public Map deleteImg(String url){
        String path = extractPublicIdFromUrl(url);
        if(path == null){
            return Map.of("Error", "Invalid format error");
        }
        try{
            Map<String, Object> result = cloudinary.cloudinary().uploader().destroy(path, ObjectUtils.emptyMap());
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public Map deleteVideo(String url){
        String path = extractPublicIdFromUrl(url);
        if(path == null){
            return Map.of("Error", "Invalid format error");
        }
        try{
            Map<String, Object> result = cloudinary.cloudinary().uploader().destroy(path, ObjectUtils.asMap(
                    "resource_type", "video"
            ));
            return result;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String extractPublicIdFromUrl(String url) {
        // Tìm vị trí bắt đầu của "secondhandmarket"
        int startIndex = url.indexOf("secondhandmarket");

        // Tìm vị trí của dấu chấm (phần mở rộng)
        int endIndex = url.indexOf(".", startIndex);

        // Kiểm tra xem cả hai vị trí có hợp lệ không
        if (startIndex != -1 && endIndex != -1) {
            // Trả về dữ liệu từ "secondhandmarket" đến trước phần mở rộng
            return url.substring(startIndex, endIndex);
        }

        return null; // Trả về null nếu không tìm thấy
    }

}
