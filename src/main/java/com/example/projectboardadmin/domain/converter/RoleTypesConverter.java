package com.example.projectboardadmin.domain.converter;

import com.example.projectboardadmin.domain.constant.RoleType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

//@Converter(autoApply = true) : Entity 에 설정 없이 사용 가능하게 한다.
@Converter
public class RoleTypesConverter implements AttributeConverter<Set<RoleType>, String> {

    private static final String DELIMITER = ",";

    @Override
    public String convertToDatabaseColumn(Set<RoleType> attribute) {
        return attribute.stream()
                .map(RoleType::name)
                .sorted()
                .collect(Collectors.joining(DELIMITER))
                ;
    }

    @Override
    public Set<RoleType> convertToEntityAttribute(String dbData) {
        return Arrays.stream(dbData.split(DELIMITER))
                .map(RoleType::valueOf)
                .collect(Collectors.toSet())
                ;
    }
}
