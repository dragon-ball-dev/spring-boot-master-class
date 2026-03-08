package com.springmasterclass.study.config;

import com.springmasterclass.study.dto.response.UserRp;
import com.springmasterclass.study.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT);
        modelMapper.typeMap(User.class, UserRp.class).addMappings(mapper -> {
            mapper.map(User::getName, UserRp::setName);
        });

        return modelMapper;
    }
}
