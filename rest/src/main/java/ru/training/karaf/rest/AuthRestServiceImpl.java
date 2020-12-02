package ru.training.karaf.rest;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import ru.training.karaf.rest.dto.AuthDTO;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.UserView;
import ru.training.karaf.view.ViewFacade;

public class AuthRestServiceImpl implements AuthRestService {
    private ViewFacade viewFacade;

    public void setViewFacade(ViewFacade viewFacade) {
        this.viewFacade = viewFacade;
    }

    @Override
    public UserDTO setAuth(AuthDTO auth) {
        UsernamePasswordToken token = new UsernamePasswordToken(auth.getLogin(), auth.getPassword());
        token.setRememberMe(true);
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.login(token);
        return viewFacade.getView(UserView.class).getByLogin((String) currentUser.getPrincipal()).map(UserDTO::new).get();
    }
}
