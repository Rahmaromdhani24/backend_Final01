package rahma.backend.gestionPDEK.Configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.annotation.JsonInclude;

@Configuration
public class JacksonConfiguration {

    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        ObjectMapper objectMapper = new ObjectMapper();

        // Configurer l'ObjectMapper pour ignorer les propriétés inconnues lors de la désérialisation
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // Configurer l'ObjectMapper pour inclure uniquement les propriétés non-null lors de la sérialisation
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Configurer l'ObjectMapper pour ne pas échouer si un bean est vide
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        // Enregistrer le module JavaTime pour gérer les dates/heure
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS); // Désactiver l'écriture des dates en timestamps

        // Créer et retourner le convertisseur HTTP avec l'ObjectMapper configuré
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);

        return converter;
    }
}
