package com.socialone.provider.data.signin;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.socialone.provider.data.signin.SignInConfiguration;
import com.socialone.provider.data.signin.SignInConfiguration.SignInConfigurationBuilder;

@Embeddable
public class SignInConfigurationEntity extends SignInConfigurationBuilder {

    /**
     * Generated 14/09/2012
     */
    private static final long serialVersionUID = -6938812457409502911L;
    
    @Column(name = "SIGN_IN_URL")
    private String signInURL;
    @Column(name = "SIGN_UP_URL")
    private String signUpURL;
    @Column(name = "POST_SIGN_IN_URL")
    private String postSignInURL;
    
    public SignInConfigurationEntity(){
    }
    
    public SignInConfigurationEntity(SignInConfiguration signInConfiguration) {
        super(signInConfiguration);
    }
    
    @Override
    public SignInConfigurationBuilder setSignInURL(String newSignInURL) {
        this.signInURL = newSignInURL;
        return this;
    }

    @Override
    public SignInConfigurationBuilder setSignUpURL(String newSignUpURL) {
        this.signUpURL = newSignUpURL;
        return this;
    }

    @Override
    public SignInConfigurationBuilder setPostSignInURL(String newPostSignInURL) {
        this.postSignInURL = newPostSignInURL;
        return this;
    }

    @Override
    public String getSignInURL() {
        return signInURL;
    }

    @Override
    public String getSignUpURL() {
        return signUpURL;
    }

    @Override
    public String getPostSignInURL() {
        return postSignInURL;
    }

}
