package gildongmu.trip.service;


import gildongmu.trip.client.S3Client;
import gildongmu.trip.domain.Image.repository.ImageRepository;
import gildongmu.trip.domain.Image.entity.Image;
import gildongmu.trip.domain.post.entity.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;
    private final S3Client s3Client;

    @Transactional
    public List<Image> saveImages(List<String> imageUrls, Post post) {
        List<Image> updatedImages = new ArrayList<>();
        for (String url : imageUrls) {
            Image image = Image.builder()
                .url(url)
                .post(post)
                .build();

            updatedImages.add(image);
            imageRepository.save(image);
        }

        return updatedImages;
    }

    public List<Image> findAllByPostId(Long postId) {
        return imageRepository.findAllByPostId(postId).orElse(null);
    }

    public List<Long> findAllId(Long boardId){
        return imageRepository.findAllId(boardId);
    }

    @Transactional
    public void deleteAllImagesFromS3(List<Image> images) {
        for (Image image : images) {
            s3Client.delete(image.getUrl());
        }
    }

    @Transactional
    public void deleteAllImagesFromDB(List<Long> postId) {
        imageRepository.deleteAllById(postId);
    }



}
