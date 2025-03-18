package overcloud.blog.usecase.user.confirm_email;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import overcloud.blog.repository.RefreshTokenRepository;
import overcloud.blog.entity.UserEntity;
import overcloud.blog.repository.UserRepository;
import overcloud.blog.usecase.user.common.UserResMsg;
import overcloud.blog.utils.validation.ObjectsValidator;

import java.util.Optional;

@Service
public class ConfirmEmailServiceImpl implements ConfirmEmailService{
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectsValidator validator;
    private final UserRepository userRepository;
    public static final String AUTHORIZE_FAILED = "authorize.failed";

    public ConfirmEmailServiceImpl(RefreshTokenRepository refreshTokenRepository,
                                   UserRepository userRepository,
                                   ObjectsValidator validator) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.validator = validator;
    }

    @Override
    @Transactional
    public Void confirmEmail(ConfirmEmailRequest confirmToken) {
        if (confirmToken == null || !StringUtils.hasText(confirmToken.getConfirmToken())) {
            throw validator.fail(AUTHORIZE_FAILED);
        }

        Optional<UserEntity> user = refreshTokenRepository.findUserByToken(confirmToken.getConfirmToken());

        if (user.isPresent()) {
            this.userRepository.enableUser(confirmToken.getConfirmToken());
        } else {
            throw validator.fail(AUTHORIZE_FAILED);
        }

        return null;
    }
}
