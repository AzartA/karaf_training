package ru.training.karaf.rest.auth;

import java.util.Collections;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.osgi.framework.FrameworkUtil;
import org.osgi.util.tracker.ServiceTracker;
import ru.training.karaf.rest.dto.UserDTO;
import ru.training.karaf.view.UserView;

public class Authentication extends AuthorizingRealm {
    private ServiceTracker<UserView, UserView> tracker;

    public Authentication() {
        tracker = new ServiceTracker<>(FrameworkUtil.getBundle(Authentication.class).getBundleContext(),UserView.class,null);
        tracker.open();
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setStringPermissions(Collections.singleton("*"));
        return authorizationInfo;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String login = (String) authenticationToken.getPrincipal();
        return tracker.getService().getByLogin(login).map(user -> {
            SimplePrincipalCollection principals = new SimplePrincipalCollection();
            principals.add(user.getLogin(), getName());
            principals.add(user, getName());
            return new SimpleAuthenticationInfo(principals, user.getPassword());
        }).orElseThrow(UnknownAccountException::new);
    }
}
