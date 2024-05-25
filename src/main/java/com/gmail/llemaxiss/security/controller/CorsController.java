package com.gmail.llemaxiss.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/cors")
public class CorsController {
	@GetMapping()
	public String index() {
		return "cors.html";
	}
	
	/**
	 * Методы могут быть вызваны даже если cors запрещает!
	 * Так как браузер может блокировать выполнение как ДО запроса так и ПОСЛЕ его выполнения
	 *
	 * т.е. БРАЗУЕР решает что делать с запросом, и потому spring-security-cors
	 * НЕ ЯВЛЯЕТСЯ способом защиты конкертной конечной точки
	 *
	 * (
	 *  Для простых запросов браузер отправляет исходный запрос непосредственно на сервер.
	 *  Браузер отклоняет ответ, если  сервер не разрешает  источник.
	 *  В некоторых случаях браузер отправляет предварительный-запрос, чтобы проверить, принимает ли сервер источник.
	 *  Если предварительный-запрос успешен, браузер отправля ет исходный запрос
	 * )
	 */
	
	@ResponseBody
	@PostMapping("/error")
	public String helloCorsWithError() {
		System.out.println("Hello cors with error has been called !!!");
		return "Hello cors world!";
	}
	
	@ResponseBody
	/**
	 * Указываются урлы С КОТОРЫХ можно обратиться
	 */
	@CrossOrigin("http://localhost:8080")
	@PostMapping("/annotation")
	public String helloCorsWithCrossAnnotation() {
		System.out.println("Hello cors with @CrossAnnotation has been called !!!");
		return "Hello cors with @CrossAnnotation world!";
	}
	
	@ResponseBody
	@PostMapping("/config")
	public String helloCorsWithConfig() {
		System.out.println("Hello cors with config has been called !!!");
		return "Hello cors with config world!";
	}
}
