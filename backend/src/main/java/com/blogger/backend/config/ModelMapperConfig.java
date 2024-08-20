package com.blogger.backend.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Bu sınıf, ModelMapper'ı yapılandırmak için kullanılır
// ModelMapper, nesneler arasında veri aktarımını kolaylaştıran bir kütüphanedir
// DTO'lar ve entity'ler arasında veri aktarımı yaparken kullanılır
@Configuration
public class ModelMapperConfig {

    @Bean
    @Qualifier("modelMapperForResponse")
    public ModelMapper modelMapperForResponse() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.LOOSE);
        return modelMapper;
    }

    @Bean
    @Qualifier("modelMapperForRequest")
    public ModelMapper modelMapperForRequest() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setAmbiguityIgnored(true)
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}

