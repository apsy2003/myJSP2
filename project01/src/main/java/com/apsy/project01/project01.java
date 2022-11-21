package com.apsy.project01;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

/*@Controller 어노테이션이 있는 클래스는 스프링이 브라우저의 요청(request)을 받아들이는 컨트롤러라고 인지해서 자자 빈(Java bean) 으로 등록해서 관리.
Ctrl + Shift + O로 import시킴.*/
@Controller
public class project01 {
	//서비스를 호출하기 위해 BookService를 의존성으로 주입
	@Autowired
	BookService bookService;	
	/* @RequestMapping 어노테이션은 브라우저의 요청에 실행되는 자바 메소드를 지정 */
	/* value 속성은 브라우저의 주소(URI)를 지정 */
	/* method 속성은 http 요청 메소드를 의미 */
	/* 일반적인 웹 페이지 개발에서 GET 메소드는 데이터를 변경하지 않는 경우에, POST 메소드는 데이터가 변경될 경우 사용 */
	/* 책 생성 화면은 웹 브라우저에 화면을 보여줄 뿐 데이터의 변경이 일어나지 않으므로 GET 메소드를 사용한다. */
	/*
	 * ModelAndView는 컨트롤러가 반환할 데이터를 담당하는 모델(Model)과 (And) 화면을 담당하는 뷰(View)의 경로를 합쳐놓은
	 * 객체
	 */
	@RequestMapping(value="/create", method = RequestMethod.GET)
	public ModelAndView create() {
		/* ModelAndView의 생성자에 문자열 타입(String type) 파라미터가 입력되면 뷰의 경로라고 간주 */
	    return new ModelAndView("book/create");
	}	
	/* create 메소드는 브라우저에서 /create 주소가 GET 방식으로 입력되었을 때 book/create 경로의 뷰를 보여준다. */
	
	//데이터의 변경(입력)이 일어나므로 http 메소드는 POST 방식으로 처리	
	//@RequestParam 어노테이션을 통해 HTTP 파라미터를 map 변수에 자동으로 바인딩
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ModelAndView createPost(@RequestParam Map<String, Object> map) {
	    ModelAndView mav = new ModelAndView();

	    String bookId = this.bookService.create(map);
	    //데이터 입력이 실패일 경우 다시 데이터를 입력받아야 하므로 생성 화면으로 리다이렉트
	    if (bookId == null) {
	        mav.setViewName("redirect:/create");
	    }else {
	    //데이터 입력이 성공하면 상세 페이지로 이동
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
	 책 수정 화면은 책 입력 화면 + 책 상세 화면이다. 책 입력 화면의 화면 형식을 그대로 따라가지만 데이터베이스에 저장된 데이터만 그려주면 된다. 따라서 책 데이터는 상세 화면 서비스에서 가지고 오고, 뷰는 책 입력 화면을 복사
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
        //삭제가 성공했으면 상세 페이자 없으므로 목록으로 리다이렉트	
            mav.setViewName("redirect:/list");
        } else {
        //삭제가 실패했으면 다시 상세 페이자로 이동
            String bookId = map.get("bookId").toString();
            mav.setViewName("redirect:/detail?bookId=" + bookId);
        }

        return mav;
    }	
}