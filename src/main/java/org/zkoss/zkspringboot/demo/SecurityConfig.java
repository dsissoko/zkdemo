package org.zkoss.zkspringboot.demo;

import org.pac4j.core.client.Clients;
import org.pac4j.core.client.direct.AnonymousClient;
import org.pac4j.core.config.Config;
import org.pac4j.oidc.client.OidcClient;
import org.pac4j.oidc.config.OidcConfiguration;
import org.pac4j.springframework.annotation.AnnotationConfig;
import org.pac4j.springframework.component.ComponentConfig;
import org.pac4j.springframework.web.SecurityInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Import({ ComponentConfig.class, AnnotationConfig.class })
@ComponentScan(basePackages = "org.pac4j.springframework.web")
public class SecurityConfig implements WebMvcConfigurer {

	@Value("${pac4j.logout.defaultUrl:#{null}}")
	private String locaLogoutlDefaultUrl;

	@Value("${pac4j.centralLogout.defaultUrl:#{null}}")
	private String centralLogoutDefaultUrl;

	@Value("${pac4j.centralLogout.logoutUrlPattern:#{null}}")
	private String centralLogoutUrlPattern;

	@Value("${pac4j.logout.destroySession:#{null}}")
	private String destroySession;

	@Value("${pac4j.callBackUrl:#{null}}")
	private String callBackUrl;

	@Value("${oidc.discoveryUri:#{null}}")
	private String oidcDicoveryURI;

	@Value("${oidc.clientId:#{null}}")
	private String oidcClientId;

	@Value("${oidc.clientSecret:#{null}}")
	private String oidcClientSecret;

	@Bean
	public Config pac4jClientsConfig() {
		// OKTA
		final OidcConfiguration oktaOidcConfiguration = new OidcConfiguration();
		oktaOidcConfiguration.setClientId(oidcClientId);
		oktaOidcConfiguration.setSecret(oidcClientSecret);
		oktaOidcConfiguration.setDiscoveryURI(oidcDicoveryURI);
		oktaOidcConfiguration.setLogoutUrl(centralLogoutDefaultUrl);
		final OidcClient oktaOidcClient = new OidcClient(oktaOidcConfiguration);

		final Clients clients = new Clients(callBackUrl, oktaOidcClient, new AnonymousClient());
		return new Config(clients);
	}
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(buildInterceptor("OidcClient")).addPathPatterns("/secure/*");
		registry.addInterceptor(buildInterceptor("AnonymousClient")).addPathPatterns("/*")
				.excludePathPatterns("/secure/*");
	}

	private SecurityInterceptor buildInterceptor(final String client) {
		return SecurityInterceptor.build(pac4jClientsConfig(), client);
	}

}
