package com.gogirl.gogirl_service.controller.ueditor;

import com.gogirl.gogirl_service.utils.ueditor.ActionEnter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yinyong on 2018/8/23.
 */
@Controller
public class UeditorController {
	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/")
	public String index(){
		return "ueditor";
	}

	@Autowired
	private ActionEnter actionEnter;

	@ResponseBody
	@RequestMapping(method={RequestMethod.GET,RequestMethod.POST},value="/ueditor/exec")
	public String exe(HttpServletRequest request){
		return actionEnter.exec(request);
	}

}
