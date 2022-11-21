package com.apsy.project01;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/*@Controller ������̼��� �ִ� Ŭ������ �������� �������� ��û(request)�� �޾Ƶ��̴� ��Ʈ�ѷ���� �����ؼ� ���� ��(Java bean) ���� ����ؼ� ����.
Ctrl + Shift + O�� import��Ŵ.*/
@Controller
public class project01 {
	//���񽺸� ȣ���ϱ� ���� BookService�� ���������� ����
	@Autowired
	BookService bookService;	
	/* @RequestMapping ������̼��� �������� ��û�� ����Ǵ� �ڹ� �޼ҵ带 ���� */
	/* value �Ӽ��� �������� �ּ�(URI)�� ���� */
	/* method �Ӽ��� http ��û �޼ҵ带 �ǹ� */
	/* �Ϲ����� �� ������ ���߿��� GET �޼ҵ�� �����͸� �������� �ʴ� ��쿡, POST �޼ҵ�� �����Ͱ� ����� ��� ��� */
	/* å ���� ȭ���� �� �������� ȭ���� ������ �� �������� ������ �Ͼ�� �����Ƿ� GET �޼ҵ带 ����Ѵ�. */
	/*
	 * ModelAndView�� ��Ʈ�ѷ��� ��ȯ�� �����͸� ����ϴ� ��(Model)�� (And) ȭ���� ����ϴ� ��(View)�� ��θ� ���ĳ���
	 * ��ü
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create() {
		/* ModelAndView�� �����ڿ� ���ڿ� Ÿ��(String type) �Ķ���Ͱ� �ԷµǸ� ���� ��ζ�� ���� */
	    return new ModelAndView("book/create");
	}	
	/* create �޼ҵ�� ���������� /create �ּҰ� GET ������� �ԷµǾ��� �� book/create ����� �並 �����ش�. */
	
	//�������� ����(�Է�)�� �Ͼ�Ƿ� http �޼ҵ�� POST ������� ó��	
	//@RequestParam ������̼��� ���� HTTP �Ķ���͸� map ������ �ڵ����� ���ε�
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createPost(@RequestParam Map<String, Object> map) {
	    ModelAndView mav = new ModelAndView();

	    String bookId = this.bookService.create(map);
	    //������ �Է��� ������ ��� �ٽ� �����͸� �Է¹޾ƾ� �ϹǷ� ���� ȭ������ �����̷�Ʈ
	    if (bookId == null) {
	        mav.setViewName("redirect:/create");
	    }else {
	    //������ �Է��� �����ϸ� �� �������� �̵�
	        mav.setViewName("redirect:/detail?bookId=" + bookId);	
	    }		

	    return mav;
	}	

    @RequestMapping(value = "/detail", method = RequestMethod.GET)
    public ModelAndView detail(@RequestParam Map<String, Object> map) {
        Map<String, Object> detailMap = this.bookService.detail(map);
	    
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", detailMap);
        String bookId = map.get("bookId").toString();
        mav.addObject("bookId", bookId);
        mav.setViewName("/book/detail");
        return mav;
    }	

	/*
	 å ���� ȭ���� å �Է� ȭ�� + å �� ȭ���̴�. å �Է� ȭ���� ȭ�� ������ �״�� �������� �����ͺ��̽��� ����� �����͸� �׷��ָ� �ȴ�. ���� å �����ʹ� �� ȭ�� ���񽺿��� ������ ����, ��� å �Է� ȭ���� ����
	 */
    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView update(@RequestParam Map<String, Object> map) {
        Map<String, Object> detailMap = this.bookService.detail(map);
	    
        ModelAndView mav = new ModelAndView();
        mav.addObject("data", detailMap);
        mav.setViewName("/book/update");
        return mav;
    }
    
	@RequestMapping(value = "update", method = RequestMethod.POST)
	public ModelAndView updatePost(@RequestParam Map<String, Object> map) {
	    ModelAndView mav = new ModelAndView();

	    boolean isUpdateSuccess = this.bookService.edit(map);		
	    if (isUpdateSuccess) {
	        String bookId = map.get("bookId").toString();
	        mav.setViewName("redirect:/detail?bookId=" + bookId);
	    }else {
	        mav = this.update(map);
	    }		

	    return mav;
	}    
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public ModelAndView deletePost(@RequestParam Map<String, Object> map) {
        ModelAndView mav = new ModelAndView();

        boolean isDeleteSuccess = this.bookService.remove(map);
        if (isDeleteSuccess) {
        //������ ���������� �� ������ �����Ƿ� ������� �����̷�Ʈ	
            mav.setViewName("redirect:/list");
        } else {
        //������ ���������� �ٽ� �� �����ڷ� �̵�
            String bookId = map.get("bookId").toString();
            mav.setViewName("redirect:/detail?bookId=" + bookId);
        }

        return mav;
    }	
}