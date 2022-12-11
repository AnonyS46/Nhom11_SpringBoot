package com.haui.main.Controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.haui.main.Dao.CategoryDao;
import com.haui.main.Dao.ManufactureDao;
import com.haui.main.Dao.ProductCateDao;
import com.haui.main.Dao.ProductColorDao;
import com.haui.main.Dao.ProductDao;
import com.haui.main.Dao.ProductSizeDao;
import com.haui.main.Dao.UserDao;
import com.haui.main.Entity.Category;
import com.haui.main.Entity.Product;
import com.haui.main.Model.ShowCategory;
import com.haui.main.Model.ShowSelect;
import com.haui.main.Service.SessionService;
import com.haui.main.Service.ShoppingCartServiceImpl;

@Controller
public class ShopController {
	
	@Autowired
	SessionService sessionService;
	
	@Autowired
	CategoryDao categoryDao;
	
	@Autowired
	ProductCateDao productcateDao;
	
	@Autowired
	ProductSizeDao productsizeDao;
	
	@Autowired
	ProductColorDao productcolorDao;

	@Autowired
	UserDao dao;
	
	@Autowired
	ProductDao productDao;
	
	@Autowired
	ManufactureDao manuDao;
	
	
	@GetMapping("/shop/category/{id}")
	public String index(@PathVariable("id") int id, Model model, @RequestParam("p") Optional<Integer> p) {
		sessionService.set("cateId", id);
		Pageable pageable = PageRequest.of(p.orElse(0), 9);
		
		Page<Product> list = productDao.fillShopDESC(id, pageable);
		model.addAttribute("shop", list);
		return "user/shop";
	}
	
	@ModelAttribute("nameCate")
	public String nameCate(@PathVariable("id") int id) {
		Category entity = categoryDao.getById(id);
		return entity.getName();
	}
	
	@ModelAttribute("procate")
	public List<ShowCategory> procate(Model model) {
		List<ShowCategory> list = productcateDao.getSelectCategory();
		return list;
	}
	
	@ModelAttribute("color")
	public List<ShowSelect> color(@PathVariable("id") int id) {
		List<ShowSelect> list = productcolorDao.getSelectColor(id);
		return list;
	}
	
	@ModelAttribute("size")
	public List<ShowSelect> size(@PathVariable("id") int id) {
		List<ShowSelect> list = productsizeDao.getSelectSize(id);
		return list;
	}
	
	
}
