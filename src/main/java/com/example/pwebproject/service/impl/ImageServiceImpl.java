package com.example.pwebproject.service.impl;

import com.example.pwebproject.exception.NotFoundException;
import com.example.pwebproject.model.Post;
import com.example.pwebproject.model.Picture;
import com.example.pwebproject.repository.PostRepository;
import com.example.pwebproject.repository.PictureRepository;
import com.example.pwebproject.service.ImageService;
import com.example.pwebproject.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ImageServiceImpl implements ImageService {

    private final PostRepository postRepository;
    private final PictureRepository pictureRepository;


    @Override
    @Transactional
    public void saveImageFile(Long postId, MultipartFile[] images) throws IOException {

        Optional<Post> post = postRepository.findById(postId);

        if(post.isPresent()) {
            List<Picture> pictures = post.get().getPictures();

            if(pictures == null || pictures.size() == 0) {
                pictures = new ArrayList<>();
            }

            for (MultipartFile file: images) {
                Byte[] byteObjects = new Byte[file.getBytes().length];
                int i = 0; for (byte b : file.getBytes()){
                    byteObjects[i++] = b; }

                Picture picture = new Picture();
                picture.setPicture(byteObjects);
                picture.setPost(post.get());
                Picture savedPicture = pictureRepository.save(picture);
                pictures.add(savedPicture);
                post.get().setPictures(pictures);
                postRepository.save(post.get());
            }

        } else {
            throw new NotFoundException("Post with postId " + postId + " not found");
        }

    }

    @Override
    public Optional<Picture> findById(Long id) {
        return pictureRepository.findById(id);
    }

    @Override
    public void deleteById(Long pictureId) {
        Optional<Picture> picture = pictureRepository.findById(pictureId);
        if(!picture.isPresent()) {
            throw new NotFoundException("Picture not found!");
        }
        pictureRepository.deleteById(pictureId);
    }
}
