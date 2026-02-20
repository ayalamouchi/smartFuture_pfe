package tn.smartfuture.domain.ports.in;

import tn.smartfuture.application.dto.response.OtpSentResponse;

public interface RegisterUserUseCase {
    OtpSentResponse registerLearner(Object request);
    OtpSentResponse registerTrainer(Object request);
    OtpSentResponse registerCompany(Object request);
}