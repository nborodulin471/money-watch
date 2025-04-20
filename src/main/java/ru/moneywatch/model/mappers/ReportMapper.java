package ru.moneywatch.model.mappers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.moneywatch.model.StatusReport;
import ru.moneywatch.model.dtos.ReportDto;
import ru.moneywatch.model.entities.ReportEntity;
import ru.moneywatch.model.entities.ReportType;
import ru.moneywatch.repository.UserRepository;
import ru.moneywatch.service.auth.AuthenticationFacade;

/**
 * Маппер для отчетов.
 */
@Component
@RequiredArgsConstructor
public class ReportMapper {

    private final AuthenticationFacade authenticationFacade;
    private final UserRepository userRepository;

    public ReportDto toDto(ReportEntity entity) {
        if (entity == null) {
            return null;
        }

        return new ReportDto(
                entity.getId(),
                entity.getLink(),
                entity.getUser().getUsername(),
                entity.getDateCreated(),
                entity.getStatus().name(),
                entity.getType().name()
        );
    }

    public ReportEntity toEntity(ReportDto dto) {
        if (dto == null) {
            return null;
        }

        var user = (UserDetails) authenticationFacade.getAuthentication().getPrincipal();

        ReportEntity entity = new ReportEntity();
        entity.setId(dto.id());
        entity.setLink(dto.link());
        entity.setUser(userRepository.findByUsername(user.getUsername()));
        entity.setDateCreated(dto.dateCreated());
        entity.setStatus(dto.status() != null ? StatusReport.valueOf(dto.status()) : null);
        entity.setType(dto.type() != null ? ReportType.valueOf(dto.type()) : null);

        return entity;
    }

}
