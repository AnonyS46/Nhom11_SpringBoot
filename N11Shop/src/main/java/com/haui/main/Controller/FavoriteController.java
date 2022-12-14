package com.haui.main.Controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.haui.main.Dao.FavoriteDao;
import com.haui.main.Dao.ProductDao;
import com.haui.main.Dao.UserDao;
import com.haui.main.Entity.Favorite;
import com.haui.main.Entity.Product;
import com.haui.main.Entity.User;
import com.haui.main.Service.SessionService;

@Controller
public class FavoriteController {
	@Autowired
	FavoriteDao dao;

	@Autowired
	UserDao userDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	SessionService sessionService;

	@RequestMapping("/shop/favorite/{id}")
	public String index(Model model, @PathVariable("id") int id) {
		User user = sessionService.get("userLogin");
		Product product = productDao.getById(id);
		Date date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String strDate = formatter.format(date);
		Favorite entity = new Favorite(strDate, user, product);
		dao.save(entity);

		int count = dao.countFavorite(user.getId());
		sessionService.set("countFavorite", count);
		return "redirect:/shop/detail/" + id;
	}
	
	@GetMapping("/shop/profile/favorite")
	public String favorite(Model model) {
		User user = sessionService.get("userLogin");
		List<Favorite> list = dao.getFavoriteByEmail(user.getEmail());
		model.addAttribute("favorite", list);
		model.addAttribute("location", 0);
		return "user/favorite";
	}
	
	@GetMapping("/shop/favorite/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id) {		
		Favorite entity = dao.getById(id);
		dao.delete(entity);
		int count = dao.countFavorite(entity.getUser().getId());
		sessionService.set("countFavorite", count);
		return "redirect:/shop/profile/favorite";
	}
}
