/**
 * 
 */
package com.ideamoment.saml.sp.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Component;

import com.ideamoment.saml.shiro.token.IdeaSamlToken;


/**
 * @author Chinakite
 *
 */
@Component
public class IdeaSamlSpRealm extends AuthorizingRealm {

    public IdeaSamlSpRealm() {
        setName("ideaSamlSpRealm");
    }
    
    /* (non-Javadoc)
     * @see org.apache.shiro.realm.AuthorizingRealm#doGetAuthorizationInfo(org.apache.shiro.subject.PrincipalCollection)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.AuthenticatingRealm#doGetAuthenticationInfo(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
            throws AuthenticationException {
        IdeaSamlToken samlToken = (IdeaSamlToken)token;
        if(samlToken.getUser() != null && samlToken.getPrincipal() != null) {
            return new SimpleAuthenticationInfo(samlToken.getPrincipal(), samlToken.getCredentials(), getName());
        }else{
            throw new UnknownAccountException();
        }
    }

    /* (non-Javadoc)
     * @see org.apache.shiro.realm.AuthenticatingRealm#supports(org.apache.shiro.authc.AuthenticationToken)
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        if(token instanceof IdeaSamlToken) {
            return true;
        }else{
            return false;
        }
    }

    
}
