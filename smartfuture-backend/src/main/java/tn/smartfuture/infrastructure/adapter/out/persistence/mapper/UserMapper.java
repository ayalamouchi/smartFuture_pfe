package tn.smartfuture.infrastructure.adapter.out.persistence.mapper;

import org.springframework.stereotype.Component;
import tn.smartfuture.domain.model.*;
import tn.smartfuture.infrastructure.adapter.out.persistence.entity.*;

@Component
public class UserMapper {

    public UserEntity toEntity(User user) {
        if (user instanceof Learner learner) {
            return toLearnerEntity(learner);
        } else if (user instanceof Trainer trainer) {
            return toTrainerEntity(trainer);
        } else if (user instanceof Company company) {
            return toCompanyEntity(company);
        }
        throw new IllegalArgumentException("Unknown user type");
    }

    public User toDomain(UserEntity entity) {
        if (entity instanceof LearnerEntity learnerEntity) {
            return toLearnerDomain(learnerEntity);
        } else if (entity instanceof TrainerEntity trainerEntity) {
            return toTrainerDomain(trainerEntity);
        } else if (entity instanceof CompanyEntity companyEntity) {
            return toCompanyDomain(companyEntity);
        }
        throw new IllegalArgumentException("Unknown entity type");
    }

    private LearnerEntity toLearnerEntity(Learner learner) {
        LearnerEntity entity = new LearnerEntity();
        entity.setId(learner.getId());
        entity.setEmail(learner.getEmail());
        entity.setPhoneNumber(learner.getPhoneNumber());
        entity.setPasswordHash(learner.getPasswordHash());
        entity.setRole(learner.getRole());
        entity.setStatus(learner.getStatus());
        entity.setEmailVerified(learner.getEmailVerified());
        entity.setCreatedAt(learner.getCreatedAt());
        entity.setLastLogin(learner.getLastLogin());
        entity.setFirstName(learner.getFirstName());
        entity.setLastName(learner.getLastName());
        entity.setCompany(learner.getCompany());
        entity.setVille(learner.getVille());
        entity.setLevel(learner.getLevel());
        entity.setProfileCompletionRate(learner.getProfileCompletionRate());
        return entity;
    }

    private Learner toLearnerDomain(LearnerEntity entity) {
        Learner learner = new Learner();
        learner.setId(entity.getId());
        learner.setEmail(entity.getEmail());
        learner.setPhoneNumber(entity.getPhoneNumber());
        learner.setPasswordHash(entity.getPasswordHash());
        learner.setRole(entity.getRole());
        learner.setStatus(entity.getStatus());
        learner.setEmailVerified(entity.getEmailVerified());
        learner.setCreatedAt(entity.getCreatedAt());
        learner.setLastLogin(entity.getLastLogin());
        learner.setFirstName(entity.getFirstName());
        learner.setLastName(entity.getLastName());
        learner.setCompany(entity.getCompany());
        learner.setVille(entity.getVille());
        learner.setLevel(entity.getLevel());
        learner.setProfileCompletionRate(entity.getProfileCompletionRate());
        return learner;
    }

    private TrainerEntity toTrainerEntity(Trainer trainer) {
        TrainerEntity entity = new TrainerEntity();
        entity.setId(trainer.getId());
        entity.setEmail(trainer.getEmail());
        entity.setPhoneNumber(trainer.getPhoneNumber());
        entity.setPasswordHash(trainer.getPasswordHash());
        entity.setRole(trainer.getRole());
        entity.setStatus(trainer.getStatus());
        entity.setEmailVerified(trainer.getEmailVerified());
        entity.setCreatedAt(trainer.getCreatedAt());
        entity.setLastLogin(trainer.getLastLogin());
        entity.setFirstName(trainer.getFirstName());
        entity.setLastName(trainer.getLastName());
        entity.setBio(trainer.getBio());
        entity.setCvFileUrl(trainer.getCvFileUrl());
        entity.setCvCNFCPPFileUrl(trainer.getCvCNFCPPFileUrl());
        entity.setAverageRating(trainer.getAverageRating());
        entity.setTotalSessionsAnimated(trainer.getTotalSessionsAnimated());
        entity.setIsValidated(trainer.getIsValidated());
        return entity;
    }

    private Trainer toTrainerDomain(TrainerEntity entity) {
        Trainer trainer = new Trainer();
        trainer.setId(entity.getId());
        trainer.setEmail(entity.getEmail());
        trainer.setPhoneNumber(entity.getPhoneNumber());
        trainer.setPasswordHash(entity.getPasswordHash());
        trainer.setRole(entity.getRole());
        trainer.setStatus(entity.getStatus());
        trainer.setEmailVerified(entity.getEmailVerified());
        trainer.setCreatedAt(entity.getCreatedAt());
        trainer.setLastLogin(entity.getLastLogin());
        trainer.setFirstName(entity.getFirstName());
        trainer.setLastName(entity.getLastName());
        trainer.setBio(entity.getBio());
        trainer.setCvFileUrl(entity.getCvFileUrl());
        trainer.setCvCNFCPPFileUrl(entity.getCvCNFCPPFileUrl());
        trainer.setAverageRating(entity.getAverageRating());
        trainer.setTotalSessionsAnimated(entity.getTotalSessionsAnimated());
        trainer.setIsValidated(entity.getIsValidated());
        return trainer;
    }

    private CompanyEntity toCompanyEntity(Company company) {
        CompanyEntity entity = new CompanyEntity();
        entity.setId(company.getId());
        entity.setEmail(company.getEmail());
        entity.setPhoneNumber(company.getPhoneNumber());
        entity.setPasswordHash(company.getPasswordHash());
        entity.setRole(company.getRole());
        entity.setStatus(company.getStatus());
        entity.setEmailVerified(company.getEmailVerified());
        entity.setCreatedAt(company.getCreatedAt());
        entity.setLastLogin(company.getLastLogin());
        entity.setCompanyName(company.getCompanyName());
        entity.setContactName(company.getContactName());
        entity.setAddress(company.getAddress());
        entity.setAccountType(company.getAccountType());
        entity.setBudgetAllocated(company.getBudgetAllocated());
        entity.setBudgetConsumed(company.getBudgetConsumed());
        return entity;
    }

    private Company toCompanyDomain(CompanyEntity entity) {
        Company company = new Company();
        company.setId(entity.getId());
        company.setEmail(entity.getEmail());
        company.setPhoneNumber(entity.getPhoneNumber());
        company.setPasswordHash(entity.getPasswordHash());
        company.setRole(entity.getRole());
        company.setStatus(entity.getStatus());
        company.setEmailVerified(entity.getEmailVerified());
        company.setCreatedAt(entity.getCreatedAt());
        company.setLastLogin(entity.getLastLogin());
        company.setCompanyName(entity.getCompanyName());
        company.setContactName(entity.getContactName());
        company.setAddress(entity.getAddress());
        company.setAccountType(entity.getAccountType());
        company.setBudgetAllocated(entity.getBudgetAllocated());
        company.setBudgetConsumed(entity.getBudgetConsumed());
        return company;
    }
}