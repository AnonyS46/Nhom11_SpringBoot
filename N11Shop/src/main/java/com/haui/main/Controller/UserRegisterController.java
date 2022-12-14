package com.haui.main.Controller;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.haui.main.Dao.RoleDao;
import com.haui.main.Dao.UserDao;
import com.haui.main.Dao.UserRoleDao;
import com.haui.main.Entity.Role;
import com.haui.main.Entity.User;
import com.haui.main.Entity.UserRole;
import com.haui.main.Model.MailInfo;
import com.haui.main.Model.UserLogin;
import com.haui.main.Model.UserRegister;
import com.haui.main.Service.MailerServiceImpl;
import com.haui.main.Service.SessionService;
import com.haui.main.Validator.UserLoginValidator;
import com.haui.main.Validator.UserRegisterValidator;

@Controller
public class UserRegisterController {
	@Autowired
	private UserDao dao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private UserRoleDao userRoleDao;

	@Autowired
	MailerServiceImpl service;

	@Autowired
	UserRegisterValidator userRegisterValidator;

	@Autowired
	SessionService sessionService;
	
	@Autowired
    private PasswordEncoder passwordEncoder;

	// private static int code = (int) Math.floor(((Math.random() * 899999) +
	// 100000));

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		Object target = binder.getTarget();
		if (target == null) {
			return;
		}
		if (target.getClass() == UserRegister.class) {
			binder.setValidator(userRegisterValidator);
		}
	}

	@GetMapping("/register")
	public String index(Model model) {
		UserRegister entity = new UserRegister();
		System.out.println("this is code: " + sessionService.get("code"));
		model.addAttribute("userRegister", entity);
		return "user/register";
	}

	@PostMapping("/register/{check}")
	public String save(Model model, @ModelAttribute("userRegister") @Validated UserRegister entity,
			BindingResult result, @PathVariable("check") String check) {
		// System.out.println(check);
		
		if (check.equals("mailSender")) {
			// System.out.println(code);
			// System.out.println(entity.getEmail());
			int code = (int) Math.floor(((Math.random() * 899999) + 100000));
			sessionService.set("code", code);
			service.queue(entity.getEmail(), "X??c nh???n Email!", "Code x??c nh???n c???a b???n l??: " + code);
		}
		
		if (result.hasErrors()) {
			//return "user/register";
		} else {
			// System.out.println("this is code: "+code);
			if (sessionService.get("code") == null) {
				result.rejectValue("code", "NotDuplicate.userRegister.code");
			} else {
				if (entity.getCode().equals(sessionService.get("code").toString()) == false) {
					//System.out.println("your code is " + entity.getCode());
					//System.out.println("this is code: " + sessionService.get("code"));
					result.rejectValue("code", "NotDuplicate.userRegister.code");
				} else {
					System.out.println("????ng");
					String password = passwordEncoder.encode(entity.getPassword());
					User user = new User(entity.getEmail(), password, entity.getFullname());
					dao.save(user);
					Role role = roleDao.getOne(2);
					UserRole userRole = new UserRole(user, role);
					userRoleDao.save(userRole);
					return "redirect:/login";
				}
			}
		}
			
		return "user/register";
	}

//	@PostMapping("/sendMail")
//	public void send(Model model, @ModelAttribute("userRegister") UserRegister entity) {
//		code = (int) Math.floor(((Math.random() * 899999) + 100000));
//		MailInfo info = new MailInfo(entity.getEmail(), "X??c nh???n Email!", "Code x??c nh???n c???a b???n l??: "+code);
//		service = new MailerServiceImpl();
//		try {
//			service.send(info);
//		} catch (MessagingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		System.out.println(code);
//		
//	}
}
