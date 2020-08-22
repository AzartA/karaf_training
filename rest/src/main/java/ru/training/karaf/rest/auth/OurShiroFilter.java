package ru.training.karaf.rest.auth;

import org.apache.shiro.web.env.EnvironmentLoader;
import org.apache.shiro.web.servlet.ShiroFilter;

import javax.servlet.ServletContext;

public class OurShiroFilter extends ShiroFilter {
    Object webEnv;
    @Override
    public void init() throws Exception {
        ServletContext context = getServletContext();
        Object atr = context.getAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY);
        if (webEnv == null && atr != null) {
            webEnv = atr;
        } else if (webEnv != null && atr == null) {
            context.setAttribute(EnvironmentLoader.ENVIRONMENT_ATTRIBUTE_KEY, webEnv);
        }
        super.init();

    }
}
