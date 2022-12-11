package com.haui.main.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.haui.main.Dao.CategoryDao;
import com.haui.main.Dao.FillProCateDao;
import com.haui.main.Dao.FillProSizeDao;
import com.haui.main.Dao.ProductCateDao;
import com.haui.main.Dao.ProductDao;
import com.haui.main.Dao.ProductSizeDao;
import com.haui.main.Dao.SizeDao;
import com.haui.main.Entity.Category;
import com.haui.main.Entity.Product;
import com.haui.main.Entity.ProductCate;
import com.haui.main.Entity.ProductSize;
import com.haui.main.Entity.Size;
import com.haui.main.Model.FillProCate;
import com.haui.main.Model.FillProSize;
import com.haui.main.Model.ProductCateModel;
import com.haui.main.Model.ProductSizeModel;
import com.haui.main.Service.SessionService;

@Controller
public class ProductSizeController {
	
	@Autowired
	ProductSizeDao dao;

	@Autowired
	SizeDao sizeDao;

	@Autowired
	ProductDao productDao;

	@Autowired
	FillProSizeDao prosizeDao;

	@Autowired
	SessionService session;

	@RequestMapping("/admin/productsizeForm/form")
	public String index(Model model, @RequestParam("keyword8") Optional<String> name,
			@RequestParam("p") Optional<Integer> p2) {
		// Load select category
		Map<Integer, String> map = new HashMap<Integer, String>();
		List<Size> list = sizeDao.findAll();
		for (Size c : list) {
			map.put(c.getId(), c.getName());
		}
		ProductSizeModel entity = new ProductSizeModel();
		model.addAttribute("productsize", entity);
		model.addAttribute("size", map);

		// Load select product
		List<Product> list2 = productDao.findSize(list.get(0).getId());
		// List<Product> list = productDao.findProduct();
		Map<Integer, String> map2 = new HashMap<Integer, String>();
		for (Product p : list2) {
			map2.put(p.getId(), p.getName());
		}
		model.addAttribute("product", map2);

		// find by name
		String findName;
		if (session.get("keyword8") == null) {
			findName = name.orElse("");
		} else {
			findName = name.orElse(session.get("keyword8"));
		}

		session.set("keyword8", findName);

		Pageable pageable = PageRequest.of(p2.orElse(0), 5);

		Page<FillProSize> page = prosizeDao.fillToTable("%" + findName + "%", pageable);
		model.addAttribute("prosizeItem", page);

		return "manager/productsizeForm";
	}

	@RequestMapping("/admin/productsizeForm/change/{id}")
	public String change(Model model, @PathVariable("id") String id,
			@ModelAttribute("productsize") ProductSizeModel entity, @RequestParam("keyword8") Optional<String> name,
			@RequestParam("p") Optional<Integer> p2) {

		Map<Integer, String> map2 = new HashMap<Integer, String>();
		List<Size> list2 = sizeDao.findAll();
		for (Size c : list2) {
			map2.put(c.getId(), c.getName());
		}
		model.addAttribute("size", map2);

		List<Product> list = productDao.findSize(Integer.parseInt(id));
		// List<Product> list = productDao.findProduct();
		Map<Integer, String> map = new HashMap<Integer, String>();
		for (Product p : list) {
			map.put(p.getId(), p.getName());
		}
		// model.addAttribute("product", map);
		model.addAttribute("product", map);

		entity.setSizeId(id);

		// find by name
		String findName;
		if (session.get("keyword8") == null) {
			findName = name.orElse("");
		} else {
			findName = name.orElse(session.get("keyword8"));
		}

		session.set("keyword8", findName);

		Pageable pageable = PageRequest.of(p2.orElse(0), 5);

		Page<FillProSize> page = prosizeDao.fillToTable("%" + findName + "%", pageable);
		model.addAttribute("prosizeItem", page);

		return "manager/productsizeForm";
	}

	@PostMapping("/admin/productsizeForm/save")
	public String save(Model model, @ModelAttribute("productsize") ProductSizeModel entity) {
		Product product = productDao.getById(Integer.parseInt(entity.getProductId()));
		Size size = sizeDao.getById(Integer.parseInt(entity.getSizeId()));
		ProductSize productsize = new ProductSize(product, size);
		dao.save(productsize);
		return "redirect:/admin/productsizeForm/form";
	}
	
	@RequestMapping("/admin/productsizeForm/form/delete/{id}")
	public String delete(Model model, @PathVariable("id") int id) {
		ProductSize entity = dao.getById(id);
		dao.delete(entity);
		return "redirect:/admin/productsizeForm/form";
	}
	
}
