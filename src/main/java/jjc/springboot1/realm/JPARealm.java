package jjc.springboot1.realm;

import jjc.springboot1.pojo.User;
import jjc.springboot1.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jws.soap.SOAPBinding;

public class JPARealm extends AuthorizingRealm {
    @Autowired
    UserService userService;
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo s = new SimpleAuthorizationInfo();
        return s;
    }

    /**
     * 对传入的token进行认证t
     * @param token 封装了用户名和密码；
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        String userName = token.getPrincipal().toString();
        User user = userService.getByName(userName);
        String passwordDB = user.getPassword();    //获取数据库中的用户密码，是加密后的
        String salt = user.getSalt();   //获取加密盐

        //该对象会自动对密码进行校验。
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(userName, passwordDB,
                ByteSource.Util.bytes(salt), getName());

        return authenticationInfo;
    }
}
