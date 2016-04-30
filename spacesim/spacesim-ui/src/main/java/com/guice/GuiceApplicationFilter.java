package com.guice;

import javax.servlet.annotation.WebFilter;

import com.google.inject.servlet.GuiceFilter;

@WebFilter(urlPatterns = "/*")  
public class GuiceApplicationFilter extends GuiceFilter {  
}  