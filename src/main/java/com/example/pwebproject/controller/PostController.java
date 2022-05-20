package com.example.pwebproject.controller;

import com.example.pwebproject.email.DefaultEmailService;
import com.example.pwebproject.model.Post;
import com.example.pwebproject.model.User;
import com.example.pwebproject.model.UserReservation;
import com.example.pwebproject.repository.UserReservationRepository;
import com.example.pwebproject.service.EmailService;
import com.example.pwebproject.service.ImageService;
import com.example.pwebproject.service.PostService;
import com.example.pwebproject.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class PostController {

    private final PostService postService;
    private final ImageService imageService;
    private final UserService userService;
    private final UserReservationRepository userReservationRepository;
    private final EmailService emailService;


    @GetMapping("/post/{postId}")
    public String showById(@PathVariable("postId") Long postId, Model model){
        model.addAttribute("post", postService.findById(postId));
        return "singlePost";
    }

    @GetMapping({"/post/list", "/"})
    public ModelAndView postsList(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(3);
        ModelAndView modelAndView = new ModelAndView("listPosts");
        Page<Post> postPage = postService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("postPage", postPage);
        return modelAndView;
    }

    @GetMapping("/post/new")
    public String newPost(Model model) {
        model.addAttribute("post", new Post());

        return "createPost";
    }

    @GetMapping("/post/edit/{postId}")
    public ModelAndView editPost(@PathVariable("postId") Long postId){
        User authenticatedUser = userService.getAuthenticatedUser();
        Post post = postService.findById(postId);
        if(!authenticatedUser.equals(post.getUser())) {
            return postsList(Optional.of(1), Optional.of(3));
        }
        ModelAndView modelAndView = new ModelAndView("editPost");
        modelAndView.addObject("post", postService.findById(postId));
        return modelAndView;
    }

    @GetMapping("/post/editPictures/{postId}")
    public ModelAndView editPictures(@PathVariable("postId") Long postId){
        User authenticatedUser = userService.getAuthenticatedUser();
        Post post = postService.findById(postId);
        if(!authenticatedUser.equals(post.getUser())) {
            return postsList(Optional.of(1), Optional.of(3));
        }
        ModelAndView modelAndView = new ModelAndView("editPicturesPost");
        modelAndView.addObject("post", postService.findById(postId));
        return modelAndView;
    }


    @PostMapping("/post")
    public String save(@Valid @ModelAttribute Post post,
                       BindingResult bindingResult,
                       @RequestParam("images") MultipartFile[] images) throws IOException {
        if (bindingResult.hasErrors()){
            return "createPost";
        }
        post.setDate(LocalDateTime.now());
        post.setUser(userService.getAuthenticatedUser());
        post.setNoAvailablePlaces(post.getNoTotal());
        Post savedPost = postService.save(post);
        imageService.saveImageFile(savedPost.getPostId(), images);
        return "redirect:/post/list" ;
    }

    @PostMapping("/post/edit")
    public String updatePost(@ModelAttribute @Valid Post post,
                             BindingResult bindingResult,
                             @RequestParam("images") MultipartFile[] images) throws IOException {

        if (bindingResult.hasErrors()) {
            return "editPost";
        }
        Post dbPost = postService.findById(post.getPostId());
        post.setUser(dbPost.getUser());
        post.getPictures().addAll(dbPost.getPictures());
        post.setDate(LocalDateTime.now());
        post.setNoAvailablePlaces(dbPost.getNoAvailablePlaces());
        post.setNoTotal(dbPost.getNoTotal());
        Post savedPost = postService.save(post);
        imageService.saveImageFile(savedPost.getPostId(), images);
        return "redirect:/post/" + post.getPostId();
    }

    @RequestMapping("/post/delete/{postId}")
    public String deleteById(@PathVariable("postId") Long postId){
        postService.deleteById(postId);
        return "redirect:/post/list";
    }

    @GetMapping("/post/makereservation/{postId}")
    public String makeRes(@PathVariable("postId") Long postId, Model model) {
        model.addAttribute("userReservation", new UserReservation());
        model.addAttribute("post", postService.findById(postId));
        model.addAttribute("error", null);
        return "makeReservation";
    }

    @PostMapping("/post/makereservation/{postId}")
    public String postRes(@Valid @ModelAttribute UserReservation userReservation, BindingResult bindingResult,
                          @PathVariable("postId") Long postId, Model model) {

        Post post = postService.findById(postId);
        long number = post.getNoAvailablePlaces() - userReservation.getTotalRes();

        if (bindingResult.hasErrors()){
            model.addAttribute("post", post);
            model.addAttribute("error", null);
            return "makeReservation";
        }
        if (number < 0) {
            model.addAttribute("post", post);
            model.addAttribute("error", "Maximum people: " + post.getNoAvailablePlaces());
            return "makeReservation";
        }

        emailService.sendEmail(userReservation, post, userService);
        userReservation.setUser(userService.getAuthenticatedUser());
        userReservation.setPost(post);
        userReservationRepository.save(userReservation);

        post.setNoAvailablePlaces(number);
        postService.save(post);
        return "redirect:/post/list";
    }

}