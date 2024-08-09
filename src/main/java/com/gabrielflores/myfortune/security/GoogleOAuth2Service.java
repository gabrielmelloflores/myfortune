// package com.gabrielflores.myfortune.security;

// import com.gabrielflores.myfortune.exception.InvalidTokenException;
// import com.gabrielflores.myfortune.oauth2.GoogleOAuth2Credential;
// import com.gabrielflores.myfortune.oauth2.GoogleOAuth2UserInfo;
// import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
// import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
// import com.google.api.client.http.javanet.NetHttpTransport;
// import com.google.api.client.json.gson.GsonFactory;
// import java.util.Arrays;
// import java.util.HashMap;
// import java.util.Map;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// /**
//  *
//  * @author Douglas Pasqualin <douglas.pasqualin@gmail.com>
//  */
// @Slf4j
// @Service
// public class GoogleOAuth2Service {

//     private final GoogleIdTokenVerifier googleTokenVerifier;

//     public GoogleOAuth2Service(@Value("${oauth2.client.registration.google.client-id}") final String[] clientIds) {
//         this.googleTokenVerifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
//                 .setAudience(Arrays.asList(clientIds))
//                 .build();
//     }

//     public GoogleOAuth2UserInfo getGoogleUserFromToken(final GoogleOAuth2Credential googleOAuth2Credential) {
//         log.info("Autenticando aplicacao: {}", googleOAuth2Credential.getClientId());
//         GoogleIdToken idTokenObj;
//         try {
//             idTokenObj = googleTokenVerifier.verify(googleOAuth2Credential.getCredential());
//         } catch (Exception ex) {
//             log.error("Erro validando token", ex);
//             throw new InvalidTokenException("Erro validando token Google", ex);
//         }
//         if (idTokenObj == null) {
//             //Possui formato JWT correto mas não é um token Google Válido
//             log.error("Token invalido");
//             throw new InvalidTokenException("Token Google Inválido");
//         }
//         GoogleIdToken.Payload payload = idTokenObj.getPayload();
//         /*Não é a melhor forma de fazer, mas quis manter compatibilidade
//         com o código anterior onde toda a autenticação era feita no backend... */
//         final Map<String, Object> attributes = new HashMap<>(4);
//         attributes.put("sub", payload.get("sub"));
//         attributes.put("name", payload.get("name"));
//         attributes.put("email", payload.get("email"));
//         attributes.put("picture", payload.get("picture"));
//         log.info("Token valido: {}", attributes);
//         return new GoogleOAuth2UserInfo(attributes);
//     }
// }
