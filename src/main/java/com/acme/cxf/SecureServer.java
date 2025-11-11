package com.acme.cxf;

import com.acme.cxf.impl.HelloServiceImpl;
import com.acme.cxf.security.UTPasswordCallback;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxws.JaxWsServerFactoryBean;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import java.util.HashMap;
import java.util.Map;

public class SecureServer {
    public static void main(String[] args) {

        // 1. Configurer l'intercepteur WSS4J (IN)
        Map<String,Object> inProps = new HashMap<>();

        // Action : On attend un UsernameToken
        inProps.put("action", "UsernameToken");

        // Type de mot de passe : Texte clair
        inProps.put("passwordType", "PasswordText");

        // Callback pour valider le couple user/pass
        // Ici, on "hardcode" l'utilisateur 'student' avec le mot de passe 'secret123'
        inProps.put("passwordCallbackRef", new UTPasswordCallback(Map.of("student","secret123")));

        WSS4JInInterceptor wssIn = new WSS4JInInterceptor(inProps);

        // 2. Publier le service (comme avant)
        JaxWsServerFactoryBean factory = new JaxWsServerFactoryBean();
        factory.setServiceClass(HelloServiceImpl.class);
        factory.setAddress("http://localhost:8080/services/hello-secure");

        Server server = factory.create();

        // 3. Attacher l'intercepteur de sécurité au point d'accès (endpoint)
        server.getEndpoint().getInInterceptors().add(wssIn);

        System.out.println("Serveur (SÉCURISÉ) démarré.");
        System.out.println("Secure WSDL: http://localhost:8080/services/hello-secure?wsdl");
    }
}