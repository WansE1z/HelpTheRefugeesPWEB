package com.example.pwebproject.controller;

import com.example.pwebproject.model.Picture;
import com.example.pwebproject.service.ImageService;
import com.example.pwebproject.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class ImageController {

    private final PostService postService;
    private final ImageService imageService;


    @GetMapping("post/getImage/{id}")
    public void downloadImage(@PathVariable Long id, HttpServletResponse response) throws IOException {
        Optional<Picture> picture = imageService.findById(id);
        if (picture.isPresent()) {
            byte[] byteArray = new byte[picture.get().getPicture().length];
            int i = 0;
            for (Byte wrappedByte : picture.get().getPicture()) {
                byteArray[i++] = wrappedByte;
            }
            response.setContentType("image/jpeg");
            InputStream is = new ByteArrayInputStream(byteArray);
            try {
                IOUtils.copy(is, response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @RequestMapping("/post/{postId}/delete/{pictureId}")
    public String deleteById(@PathVariable("postId") Long postId, @PathVariable("pictureId") Long pictureId){
        imageService.deleteById(pictureId);
        return "redirect:/post/editPictures/" + postId;
    }
}
