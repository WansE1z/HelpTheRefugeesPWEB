package com.example.pwebproject.controller;

import com.example.pwebproject.model.User;
import com.example.pwebproject.service.RoleService;
import com.example.pwebproject.service.SecurityService;
import com.example.pwebproject.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private RoleService roleService;


    @GetMapping("/registration")
    public String registration(Model model) {
        if (securityService.isAuthenticated()) {
            return "redirect:/";
        }

        model.addAttribute("userForm", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@Valid @ModelAttribute("userForm") User userForm, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        userService.save(userForm);

        securityService.autoLogin(userForm.getUsername(), userForm.getPassword());

        return "redirect:/";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        if(securityService.isAuthenticated()) {
            return "redirect:/";
        }
        return "login";
    }

    @GetMapping("/user/{username}")
    public ModelAndView getUserByUsername(@PathVariable("username") String username){
        ModelAndView modelAndView = new ModelAndView("user-profile");
        modelAndView.addObject("user", userService.findByUsername(username));
        return modelAndView;
    }

    @PreAuthorize("#username == authentication.principal.username or hasRole('ROLE_ADMIN')")
    @GetMapping("/user/edit/{username}")
    public ModelAndView editUser(@PathVariable("username") String username){
        ModelAndView modelAndView = new ModelAndView("edit-user-profile");
        modelAndView.addObject("user", userService.findByUsername(username));
        return modelAndView;
    }

    @PostMapping("/user/edit")
    public String editUser(@ModelAttribute @Valid User user, BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            return "edit-user-profile";
        }
        User dbUser = userService.findById(user.getUserId());
        user.setRoles(dbUser.getRoles());
        userService.saveWithoutHash(user);
        return "redirect:/user/" + user.getUsername();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/user/list")
    public ModelAndView usersList(@RequestParam("page") Optional<Integer> page,
                                  @RequestParam("size") Optional<Integer> size){
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(2);
        ModelAndView modelAndView = new ModelAndView("users");
        Page<User> userPage = userService.findPaginated(PageRequest.of(currentPage - 1, pageSize));
        modelAndView.addObject("userPage", userPage);
        return modelAndView;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @RequestMapping("/user/delete/{userId}")
    public String deleteById(@PathVariable("userId") Long userId,
                             @RequestParam("page") Optional<Integer> page,
                             @RequestParam("size") Optional<Integer> size){
        userService.deleteById(userId);
        if (size.isPresent() && page.isPresent()) {
            return "redirect:/user/list?size=" + size.get() + "&page=" + page.get();
        }

        return "redirect:/user/list";

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("user/editRoles/{username}")
    public String editUserRoles(@PathVariable("username") String username, Model model) {
        User user = userService.findByUsername(username);

        model.addAttribute("user", user);
        model.addAttribute("listRoles", roleService.findAllRoles());
        return "update-roles";
    }


    @PostMapping("user/save")
    public String saveUser(User user) {

        User currentUser = userService.findById(user.getUserId());
        currentUser.setRoles(user.getRoles());
        userService.saveWithoutHash(currentUser);

        return "redirect:/user/list";
    }
}
